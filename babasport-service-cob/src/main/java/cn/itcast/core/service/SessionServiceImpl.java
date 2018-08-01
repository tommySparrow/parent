package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

/**
 * Session服务类实现类
 * 
 * 模拟官方session功能，并将session数据存放到redis中
 * 
 * @author Administrator
 *
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {

	@Autowired
	private Jedis jedis;

	@Override
	public void addUsernameToRedis(String key, String value) {
		// 因为考虑到以后可能还有验证码，所以在key后面加个username再保存到redis中
		jedis.set(key + ":username", value);

		// 设置失效时间
		jedis.expire(key + ":username", 1800);
	}

	@Override
	public String getUsernameForRedis(String key) {

		String username = jedis.get(key + ":username");

		if (username != null) {
			// 给该用户续命
			// 失效时间应该从最后一次访问开始算起，所以让value有值时，则延长续命时间
			// 设置失效时间
			jedis.expire(key + ":username", 1800);

			return username;
		}
		return null;
	}

}
