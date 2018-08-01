package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * solr服务接口
 * 
 * @author Administrator
 *
 */
public interface SolrService {

	/**
	 * 根据关键字查询商品信息（变异）
	 * 
	 * @param keyword
	 * @return
	 * @throws SolrServerException
	 */
	public Page<SuperPojo> findProductByKeyWord(String keyword, String sort,
			Integer pageNum, Integer pageSize,Long brandId,Float pa,Float pb) throws SolrServerException;

	/**
	 * 根据ids查询相关的商品信息，并添加到solr索引库
	 * @param ids
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void addProduct(String ids) throws SolrServerException, IOException;
	
}
