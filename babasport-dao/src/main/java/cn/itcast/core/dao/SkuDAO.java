package cn.itcast.core.dao;

import java.util.List;

import com.github.abel533.mapper.Mapper;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

public interface SkuDAO extends Mapper<Sku> {

	/**
	 * 根据商品id查询出该商品旗下的所有库存，并带上颜色名称
	 * 
	 * @param productId
	 * @return
	 */
	public List<SuperPojo> findByProductIdAndColor(Long productId);

	/**
	 * 根据库存id查询某商品的库存，并且将颜色名称，商品名称等通过对颜色表、商品表连接查询的方式也带出来
	 * @param skuId
	 * @return
	 */
	public SuperPojo findSKuAndColorAndProductBySkuId(Long skuId);
}
