package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 库存服务接口
 * 
 * @author Administrator
 *
 */
public interface SkuService {

	/**
	 * 根据商品id查找该商品的库存信息
	 * 
	 * @param producId
	 * @return
	 */
	public List<SuperPojo> findByProductId(Long productId);

	/**
	 * 修改库存对象
	 * 
	 * @param sku
	 *            id是修改条件 其它属性是新值
	 */
	public void update(Sku sku);

}
