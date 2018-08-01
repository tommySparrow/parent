package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品服务接口
 * 
 * @author Administrator
 *
 */
public interface ProductService {

	/**
	 * 根据案例条件查询出商品
	 * 
	 * @param product
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public Page<Product> findByExample(Product product, Integer pageNum,
			Integer pageSize);
	
	/**
	 * 查询所有可用颜色（父id不为0的颜色）
	 * @return
	 */
	public List<Color> findEnableColors();
	
	/**
	 * 添加商品  除了商品，还有库存信息
	 * @param product
	 */
	public void add(Product product);
	
	
	/**
	 * 批量修改商品统一信息
	 * @param ids
	 * @param product
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void update(String ids,Product product) throws SolrServerException, IOException;

	/**
	 * 根据商品id查询单个商品相关信息
	 * @param id
	 * @return
	 */
	public SuperPojo findById(Long productId);
	
}
