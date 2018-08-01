package cn.itcast.core.dao;

import java.util.List;

import cn.itcast.core.pojo.Brand;

/**
 * 品牌DAO
 * @author Administrator
 *
 */
public interface BrandDAO {
	
	/**
	 * 根据案例条件查询出品牌
	 * @param brand 条件封装的品牌对象   如果是null表示查询所有
	 * @return
	 */
	public List<Brand> findByExample(Brand brand);
	
	/**
	 * 根据品牌id查询该品牌信息
	 * @param brandId
	 * @return
	 */
	public Brand findById(Long brandId);
	

	/**
	 * 根据品牌id修改品牌对象
	 * @param brand  id作为修改的条件   其它属性修改后的值（新值）
	 */
	public void updateById(Brand brand);
	
	/**
	 * 批量删除品牌
	 * 
	 * @param ids
	 */
	public void deleteByIds(String ids);

}
