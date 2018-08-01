package cn.itcast.core.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.Brand;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.BrandService;
import cn.itcast.core.service.SolrService;
import cn.itcast.core.tools.Encoding;
import cn.itcast.core.tools.PageHelper.Page;

/**
 * 首页控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class IndexAction {

	@Autowired
	private SolrService solrService;

	@Autowired
	private BrandService brandService;

	// 显示首页
	@RequestMapping(value = "/")
	public String showIndex() {
		System.out.println("显示首页");
		return "index";
	}

	// 首页搜索商品
	@RequestMapping(value = "/search")
	public String search(String keyword, String sort, Model model,
			Integer pageNum, Integer pageSize, Long brandId, Float pa, Float pb)
			throws SolrServerException {
		System.out.println("开始搜索");
		keyword = Encoding.encodeGetRequest(keyword);
		System.out.println("keyword:" + keyword);
		System.out.println("sort:" + sort);

		// 从redis查询品牌，并给页面显示
		List<Brand> brands = brandService.findAllFromRedis();

		model.addAttribute("brands", brands);

		// 根据keyword去solr索引库中查询商品信息并丢给页面显示
		Page<SuperPojo> pageSuperPojos = solrService.findProductByKeyWord(
				keyword, sort, pageNum, pageSize, brandId, pa, pb);

		model.addAttribute("pageSuperPojos", pageSuperPojos);
		// 将反转前的sort加入到sort2
		model.addAttribute("sort2", sort);

		// 对sort进行反转
		if (sort != null && sort.equals("price asc")) {
			sort = "price desc";
		} else {
			sort = "price asc";
		}

		model.addAttribute("sort", sort);

		// 做已选条件的map
		Map<String, String> treeMap = new TreeMap<String, String>();

		// 根据brandId获得品牌名称
		for (Brand brand : brands) {

			if (brand.getId().equals(brandId)) {
				treeMap.put("品牌", brand.getName());
				break;
			}
		}

		if (pa != null && pb != null) {
			// 价格
			treeMap.put("价格", pa + "-" + pb);
		}

		// 回显数据给页面
		model.addAttribute("keyword", keyword);
		model.addAttribute("brandId", brandId);
		model.addAttribute("pa", pa);
		model.addAttribute("pb", pb);

		model.addAttribute("map", treeMap);

		return "search";
	}

}
