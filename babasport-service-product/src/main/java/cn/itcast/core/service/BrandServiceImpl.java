package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.container.page.PageHandler;

import cn.itcast.core.dao.BrandDAO;
import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 品牌管理实现类
 * 
 * @author Administrator
 *
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDAO brandDAO;

	@Autowired
	private Jedis jedis;

	@Override
	public Page<Brand> findByExample(Brand brand, Integer pageNum,
			Integer pageSize) {
		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		brandDAO.findByExample(brand);
		Page endPage = PageHelper.endPage();// 结束分页
		return endPage;
	}

	@Override
	public Brand findById(Long brandId) {
		Brand brand = brandDAO.findById(brandId);
		return brand;
	}

	@Override
	public void updateById(Brand brand) {
		// TODO Auto-generated method stub
		brandDAO.updateById(brand);

		// 同步修改信息到redis中
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
	}

	@Override
	public void deleteByIds(String ids) {
		brandDAO.deleteByIds(ids);
	}

	@Override
	public List<Brand> findAllFromRedis() {

		Map<String, String> hgetAll = jedis.hgetAll("brand");

		// 将查询的结果放入到品牌对象集合中
		List<Brand> brands = new ArrayList<Brand>();

		for (Entry<String, String> entry : hgetAll.entrySet()) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}

		return brands;
	}

}
