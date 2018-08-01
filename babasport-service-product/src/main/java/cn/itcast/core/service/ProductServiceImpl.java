package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ColorDAO;
import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 商品服务实现类
 * 
 * @author Administrator
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private ColorDAO colorDAO;

	@Autowired
	private Jedis jedis;

	@Autowired
	private SolrServer solrServer;

	@Autowired
	private SkuService skuService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public Page<Product> findByExample(Product product, Integer pageNum,
			Integer pageSize) {

		if (product.getName() == null) {
			product.setName("");
		}

		// 设置查询条件
		Example example = new Example(Product.class);
		example.createCriteria().andLike("name", "%" + product.getName() + "%");

		// 设置倒序
		example.setOrderByClause("id desc");

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		productDAO.selectByExample(example);
		Page endPage = PageHelper.endPage();// 结束分页

		return endPage;
	}

	@Override
	public List<Color> findEnableColors() {

		Example example = new Example(Color.class);
		example.createCriteria().andNotEqualTo("parentId", 0);

		return colorDAO.selectByExample(example);
	}

	@Override
	public void add(Product product) {

		// 设置默认值
		if (product.getIsShow() == null) {
			product.setIsShow(0);
		}
		if (product.getCreateTime() == null) {
			product.setCreateTime(new Date());
		}

		// 通过redis分配自增长的商品id
		Long incr = jedis.incr("pno");
		product.setId(incr);

		// 添加商品表
		productDAO.insert(product);

		System.out.println("product id:" + product.getId());

		// 添加库存
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");

		// 将商品信息添加到库存表中
		// 遍历不同的颜色和尺码
		// 每一个不同颜色，或者不同尺码，都应该插入库存表中，成为一条数据
		for (String color : colors) {
			for (String size : sizes) {
				Sku sku = new Sku();
				sku.setProductId(product.getId());
				sku.setColorId(Long.parseLong(color));
				sku.setSize(size);

				sku.setMarketPrice(1000.00f);
				sku.setPrice(800.00f);
				sku.setDeliveFee(20f);
				sku.setStock(0);
				sku.setUpperLimit(100);
				sku.setCreateTime(new Date());

				skuDAO.insert(sku);
			}
		}

	}

	@Override
	public void update(final String ids, Product product)
			throws SolrServerException, IOException {

		String[] split = ids.split(",");

		for (String id : split) {
			// 设置商品的id作为修改条件
			product.setId(Long.parseLong(id));

			// 修改该商品的信息
			productDAO.updateByPrimaryKeySelective(product);
		}

		// 商品上架 (==会有问题的)
		if (product.getIsShow() == 1) {

			// 采用消息服务模式
			// 将商品信息添加到solr服务器中（发送消息（ids）到ActiveMQ中）
			
			jmsTemplate.send("productIds", new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {

					TextMessage message = session.createTextMessage(ids);

					return message;
				}
			});
			
			
		}
	}

	@Override
	public SuperPojo findById(Long productId) {

		SuperPojo superPojo = new SuperPojo();

		// product 1条sql语句
		Product product = productDAO.selectByPrimaryKey(productId);

		// skus 1条sql语句
		List<SuperPojo> skus = skuService.findByProductId(productId);

		superPojo.setProperty("product", product);
		superPojo.setProperty("skus", skus);

		return superPojo;
	}

}
