package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Cart;
import cn.itcast.core.pojo.Item;
import cn.itcast.core.service.CartService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 购物车相关控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CartAction {

	@Autowired
	private CartService cartService;

	@Autowired
	private SessionService sessionService;

	// 显示购物车
	@RequestMapping(value = "/cart")
	public String showCart(Model model, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("显示购物车");
		Cart cart1 = null;// cookies
		Cart cart2 = null;// redis
		Cart cart = null;// 合并之后的cart

		// 从cookie中取出cart
		cart1 = this.getCartFormCookies(request);

		// 判断用户是否登录
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		if (username != null) {
			// 从redis中取出cart
			cart2 = cartService.getCartFormRedis(username);
		}

		// 合并两处cart
		cart = this.mergeCart(cart1, cart2);

		if (username != null) {
			// 将cart添加到redis中
			cartService.addCartToRedis(username, cart);

			// 从cookie中删除cart
			this.delCartFormCookies(response);
		}

		System.out.println(cart);

		// 丢给页面前自我填充一下
		cart = cartService.fillItemsSkus(cart);

		model.addAttribute("cart", cart);

		return "cart";
	}

	// 加入购物车
	@RequestMapping(value = "/addCart")
	public String addCart(Long skuId, Integer amount,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("加入购物车");
		System.out.println("skuId" + skuId);
		System.out.println("amount" + amount);

		Cart cart = null;

		// 从cookie中取出cart
		cart = this.getCartFormCookies(request);

		// 判断用户是否登录
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		if (username != null) {
			// 从redis中取出cart
			cart = cartService.getCartFormRedis(username);
		}

		if (cart == null) {
			cart = new Cart();
		}

		// 将相应的库存商品信息，添加到购物车对象中
		Item item = new Item();
		item.setAmount(amount);
		item.setSkuId(skuId);

		cart.addItem(item);

		if (username != null) {
			// 将新cart反存到redis中
			cartService.addCartToRedis(username, cart);
		} else {
			// 将新cart反存到cookie
			this.addCartToCookies(cart, response);
		}

		return "redirect:/cart";
	}

	// 显示结算页面
	// 结算 skuIds表示用户从购物车中选择的商品，本次课程先不做该功能
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(Long[] skuIds, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		// 判断购买数量是否大于库存数量

		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		// 获得redis中的购物车
		Cart cart = cartService.getCartFormRedis(username);

		// 自我填充购物车
		cart = cartService.fillItemsSkus(cart);

		// 判断redis里的购物车不能为空，也不能是空车子
		if (cart != null && cart.getItems().size() > 0) {
			List<Item> items = cart.getItems();

			Boolean flag = true;// 全部商品都有货

			for (Item item : items) {

				// 购买数量大于库存数量
				if (item.getAmount() > Integer
						.parseInt(item.getSku().get("stock").toString())) {
					item.setAisHave(false);
					flag = false;
				}

			}

			// 至少有一件商品无货
			if (!flag) {
				model.addAttribute("cart", cart);
				return "cart";
			}
		} else {
			//cart是空，或者是空车子
			// 回到空的购物车页面
			return "redirect:/cart";
		}

		return "order";
	}

	/**
	 * 从cookie中取出cart
	 * 
	 * @param request
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Cart getCartFormCookies(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart")) {
					String cartJson = cookie.getValue();

					// 将cartJson转成cart对象
					ObjectMapper om = new ObjectMapper();
					Cart cart = om.readValue(cartJson, Cart.class);
					return cart;
				}
			}
		}

		return null;
	}

	/**
	 * 将新cart反存到cookie
	 * 
	 * @param cart
	 * @param response
	 * @throws JsonProcessingException
	 */
	public void addCartToCookies(Cart cart, HttpServletResponse response)
			throws JsonProcessingException {

		// 将cart对象转成jsoncart
		ObjectMapper om = new ObjectMapper();
		String cartJson = om.writeValueAsString(cart);

		Cookie cookie = new Cookie("cart", cartJson);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setPath("/");

		response.addCookie(cookie);
	}

	/**
	 * 从cookie中删除cart
	 * 
	 * @param response
	 */
	public void delCartFormCookies(HttpServletResponse response) {

		Cookie cookie = new Cookie("cart", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * 合并两次cart
	 * 
	 * @param cart1
	 * 
	 * @param cart2
	 * @return
	 */
	public Cart mergeCart(Cart cart1, Cart cart2) {
		if (cart1 == null) {
			return cart2;
		} else if (cart2 == null) {
			return cart1;
		} else {
			// 合并cart1，cart2
			List<Item> items = cart1.getItems();
			for (Item item : items) {
				cart2.addItem(item);
			}
		}
		return cart2;
	}

}
