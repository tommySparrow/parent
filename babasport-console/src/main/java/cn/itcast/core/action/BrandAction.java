package cn.itcast.core.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

@Controller
public class BrandAction {

	@Autowired
	private BrandService brandService;

	// 品牌通用
	@RequestMapping(value = "/console/brand/{pageName}.do")
	public String consoleBrandShow(@PathVariable("pageName") String pageName) {
		System.out.println("通用Brand：" + pageName);
		return "brand/" + pageName;
	}

	// 显示品牌列表(特殊)
	@RequestMapping(value = "/console/brand/list.do")
	public String consoleBrandShowList(Integer pageNum, Integer pageSize,
			String name, Integer isDisplay,Model model)
			throws InterruptedException {
		System.out.println("显示品牌列表");
		System.out.println("pageNum:" + pageNum);
		System.out.println("pageSize:" + pageSize);

	

		// 将查询条件封装成brand对象
		name = Encoding.encodeGetRequest(name);
		System.out.println("name:" + name);
		System.out.println("isDisplay:" + isDisplay);
		Brand brand = new Brand();
		brand.setIsDisplay(isDisplay);
		brand.setName(name);

		// 查询数据库中所有品牌
		Page<Brand> pageBrands = brandService.findByExample(brand, pageNum,pageSize);


		System.out.println("brands size:" + pageBrands.getResult().size());

		// 将查询的结果丢给页面显示
		model.addAttribute("pageBrands", pageBrands);

		// 将查询的条件进行回显
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);

	
		return "brand/list";
	}

	// 显示品牌修改
	@RequestMapping(value = "/console/brand/showEdit.do")
	public String consoleBrandShowEdit(Model model, Long brandId) {
		System.out.println("brandId:" + brandId);
		System.out.println("显示品牌修改");
		// 根据用户选择的品牌去查询该品牌信息，并丢给页面显示
		Brand brand = brandService.findById(brandId);
		System.out.println(brand);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}

	// 执行品牌修改
	@RequestMapping(value = "/console/brand/doEdit.do")
	public String consoleBrandDoEdit(Brand brand) {
		System.out.println("执行品牌修改");
		System.out.println(brand);

		// 根据品牌id修改品牌
		brandService.updateById(brand);

		return "redirect:/console/brand/list.do";
	}

	// 执行品牌批量删除
	@RequestMapping(value = "/console/brand/doDelete.do")
	public String consoleBrandDelete(String ids)
			throws UnsupportedEncodingException {
		System.out.println("执行品牌批量删除");
		System.out.println("ids:" + ids);

		// 从数据库中删除这些品牌
		// brandService.deleteByIds(ids);

		String name = "莲";

		return "redirect:/console/brand/list.do?name="
				+ URLEncoder.encode(name, "UTF-8");
	}

}
