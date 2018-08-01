package cn.itcast.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Sku;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;

/**
 * 商品相关页面控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	// 显示单个商品页面
	@RequestMapping(value = "/product/detail")
	public String showSingleProduct(Model model, Long productId) {
		System.out.println("显示单个商品页面");
		System.out.println("productId" + productId);

		// 根据productId获得该商品的相关信息，丢给页面显示
		SuperPojo superPojo = productService.findById(productId);

		// 利用hashmap去除颜色的重复
		Map<Long, String> colors = new HashMap<Long, String>();

		List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");

		for (SuperPojo sku : skus) {

			Long colorId = (Long) sku.get("color_id");
			String colorName = (String) sku.get("colorName");
			colors.put(colorId, colorName);

		}
		superPojo.setProperty("colors", colors);

		model.addAttribute("superPojo", superPojo);

		return "product";
	}

}
