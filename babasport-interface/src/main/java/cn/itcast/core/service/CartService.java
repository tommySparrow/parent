package cn.itcast.core.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Cart;

/**
 * 购物车服务接口
 * 
 * @author Administrator
 *
 */
public interface CartService {

	/**
	 * 自我填充购物车 根据参数中cart中的item的skuId为条件，查询相关的复合型库存信息，填充到superpojo的sku中
	 * 
	 * @param cart
	 *            填充好的购物车对象
	 * @return
	 */
	public Cart fillItemsSkus(Cart cart);

	/**
	 * 从redis中取得购物车信息
	 * 
	 * @param username
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Cart getCartFormRedis(String username) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * 将购物车信息添加到Redis中
	 * 
	 * @param username
	 * @param cart
	 * @throws JsonProcessingException 
	 */
	public void addCartToRedis(String username, Cart cart) throws JsonProcessingException;

}
