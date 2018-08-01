package cn.itcast.core.action;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.Product;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 商品管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class ProductAction {

	@Autowired
	private ProductService productService;

	// 产品
	@RequestMapping(value = "/console/product/{pageName}.do")
	public String consoleProductShow(
			@PathVariable("pageName") String pageName) {
		System.out.println("通用Product：" + pageName);
		return "product/" + pageName;
	}

	// 显示商品列表(特殊)
	@RequestMapping(value = "/console/product/list.do")
	public String consoleProductShowList(Integer pageNum, Integer pageSize,
			String name, Model model) {
		System.out.println("显示商品列表");
		System.out.println("pageNum:" + pageNum);
		System.out.println("pageSize:" + pageSize);

		Product product = new Product();
		name = Encoding.encodeGetRequest(name);
		product.setName(name);

		Page<Product> pageProducts = productService.findByExample(product,
				pageNum, pageSize);

		System.out.println("psize:" + pageProducts.getResult().size());

		model.addAttribute("pageProducts", pageProducts);

		// 设置数据的回显
		model.addAttribute("name", name);

		return "product/list";
	}

	// 显示商品的添加页面
	@RequestMapping(value = "/console/product/showAdd.do")
	public String consoleProductShowAdd(Model model) {
		System.out.println("显示商品的添加页面");

		// 查询数据库中的所有可用颜色
		List<Color> colors = productService.findEnableColors();

		model.addAttribute("colors", colors);

		return "product/add";
	}

	// 执行商品的添加
	@RequestMapping(value = "/console/product/doAdd.do")
	public String consoleProductDoAdd(Product product) {
		System.out.println("执行商品的添加");
		System.out.println(product);

		// 将product添加到商品表中，并在库存表中也添加信息
		productService.add(product);

		return "redirect:/console/product/list.do";
	}

	// 执行某些商品的上下架
	@RequestMapping(value = "/console/product/isShow.do")
	public String consoleProductDoAdd(String ids,Integer flag) throws SolrServerException, IOException {
		System.out.println("执行商品的上下架");
		System.out.println("flag:"+flag);
		
		Product product = new Product();
		product.setIsShow(flag);
		
		//根据ids和flag执行商品上下架
		productService.update(ids, product);
		
		return "redirect:/console/product/list.do";
	}

}
