package cn.itcast.core.service;

import cn.itcast.core.pojo.Buyer;

/**
 * 买家服务接口
 * @author Administrator
 *
 */
public interface BuyerService {

	/**
	 * 根据用户名获得用户信息对象
	 * @param username
	 * @return
	 */
	public Buyer findByUsername(String username);
	
}
