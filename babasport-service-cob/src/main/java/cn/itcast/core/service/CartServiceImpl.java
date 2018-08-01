package cn.itcast.core.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.dao.SkuDAO;
import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.pojo.SuperPojo;
import redis.clients.jedis.Jedis;

/**
 * 购物车服务实现类
 * 
 * @author Administrator
 *
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

	@Autowired
	private SkuDAO skuDAO;

	@Autowired
	private Jedis jedis;

	@Override
	public Cart fillItemsSkus(Cart cart) {

		if (cart == null) {
			return null;
		}

		// 从购物车对象中取出购买项集合
		List<Item> items = cart.getItems();
		for (Item item : items) {
			Long skuId = item.getSkuId();
			// 根据skuId去查询库存的复合信息
			SuperPojo sku = skuDAO.findSKuAndColorAndProductBySkuId(skuId);

			// 将sku添加到item中
			item.setSku(sku);
		}
		return cart;
	}

	@Override
	public Cart getCartFormRedis(String username)
			throws JsonParseException, JsonMappingException, IOException {
		String cartJson = jedis.get("cart:" + username);

		if (cartJson == null) {
			return null;
		}

		// 将cart的json字符串转成cart对象
		ObjectMapper om = new ObjectMapper();
		Cart cart = om.readValue(cartJson, Cart.class);

		return cart;
	}

	@Override
	public void addCartToRedis(String username, Cart cart)
			throws JsonProcessingException {
		// TODO Auto-generated method stub

		if (cart == null) {
			return;
		}

		// 将cart对象变成json字符串
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		String cartJson = om.writeValueAsString(cart);

		jedis.set("cart:" + username, cartJson);
	}

}
