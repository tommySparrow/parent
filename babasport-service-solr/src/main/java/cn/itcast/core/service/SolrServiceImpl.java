package cn.itcast.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.ProductDAO;
import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务实现类
 * 
 * @author Administrator
 *
 */
@Service("solrService")
public class SolrServiceImpl implements SolrService {
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private SolrServer solrServer;

	@Override
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Integer pageNum, Integer pageSize, Long brandId, Float pa, Float pb)
			throws SolrServerException {
		// 根据关键字查询商品信息（变异）
		SolrQuery solrQuery = new SolrQuery("name_ik:" + keyword);

		// 设置品牌过滤
		if (brandId != null) {
			solrQuery.addFilterQuery("brandId:" + brandId);
		}

		// 过滤价格
		if (pa != null && pb != null) {
			if (pb == -1) {
				solrQuery.addFilterQuery("price:[" + pa + " TO *]");
			} else {
				solrQuery.addFilterQuery("price:[" + pa + " TO " + pb + "]");
			}
		}

		// solrQuery.addFilterQuery("price:[100 TO 200]");

		// 设置提取行数
		// solrQuery.setRows(100);

		// 分页
		Page<SuperPojo> page = new Page<SuperPojo>(pageNum, pageSize);

		solrQuery.setStart(page.getStartRow());// 设置起点
		solrQuery.setRows(page.getPageSize());// 设置提取行数

		// 设置排序
		if (sort != null && sort.trim().length() > 5) {
			// solrQuery.setSort("price", ORDER.asc);
			SortClause sortClause = new SortClause(sort.split(" ")[0],
					sort.split(" ")[1]);
			solrQuery.setSort(sortClause);
		}

		// 设置高亮的格式
		solrQuery.setHighlight(true);// 开启高亮
		solrQuery.setHighlightSimplePre("<span style='color:red;'>");// 前缀
		solrQuery.setHighlightSimplePost("</span>");// 后缀
		solrQuery.addHighlightField("name_ik");// 添加高亮的字段

		// 开始查询
		QueryResponse response = solrServer.query(solrQuery);

		// 获得高亮的数据
		// 大map 的key是id ，大map的值是小map 小map的key是高亮字段的名称 小map的值高亮的内容
		Map<String, Map<String, List<String>>> highlighting = response
				.getHighlighting();

		SolrDocumentList results = response.getResults();

		// 获得总记录数
		long numFound = results.getNumFound();
		page.setTotal(numFound);

		List<SuperPojo> al = new ArrayList<SuperPojo>();

		for (SolrDocument document : results) {

			// 创建商品对象(变异)
			SuperPojo superProduct = new SuperPojo();

			// id
			String id = (String) document.getFieldValue("id");
			superProduct.setProperty("id", id);

			// 商品名称
			// String name = (String) document.get("name_ik");

			// 根据id获得小map
			Map<String, List<String>> map = highlighting.get(id);
			String name = map.get("name_ik").get(0);

			superProduct.setProperty("name", name);

			// 图片地址
			String imgUrl = (String) document.get("url");
			superProduct.setProperty("imgUrl", imgUrl);

			// 商品最低价格
			Float price = (Float) document.get("price");
			superProduct.setProperty("price", price);

			// 品牌id
			Long brandId2 = (Long) document.get("brandId");
			superProduct.setProperty("brandId", brandId2);

			al.add(superProduct);

		}

		// 将分页数据放到result
		page.setResult(al);

		return page;
	}

	@Override
	public void addProduct(String ids) throws SolrServerException, IOException {
		
		String[] split = ids.split(",");

		// 将数组转为集合给下面用
		List<Object> al = new ArrayList<Object>();
		for (String id : split) {
			al.add(id);
		}

		// 根据ids查询这些商品的信息
		Example example = new Example(Product.class);
		example.createCriteria().andIn("id", al);
		List<Product> products = productDAO.selectByExample(example);
		for (Product product2 : products) {

			SolrInputDocument document = new SolrInputDocument();

			// id
			document.addField("id", String.valueOf(product2.getId()));
			// name
			document.addField("name_ik", product2.getName());
			// brandId
			document.addField("brandId", product2.getBrandId());

			// 商品首张图片
			String url = product2.getImgUrl().split(",")[0];
			document.addField("url", url);

			// 该商品旗下的所有库存的最低价格
			// SELECT * FROM bbs_sku WHERE product_id = 438 ORDER BY price
			// ASC LIMIT 0,1;
			Example example2 = new Example(Sku.class);

			// =
			example2.createCriteria().andEqualTo("productId", product2.getId());

			// 排序
			example2.setOrderByClause("price asc");

			// limit
			PageHelper.startPage(1, 1);
			skuDAO.selectByExample(example2);
			Page<Sku> endPage = PageHelper.endPage();

			Sku sku = endPage.getResult().get(0);

			document.addField("price", sku.getPrice());

			solrServer.add(document);

		}
		solrServer.commit();

	}

}
