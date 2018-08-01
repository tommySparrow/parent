package cn.itcast.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 自定义拦截器
 * 
 * @author Administrator
 *
 */
public class MyInterceptor implements HandlerInterceptor {

	@Autowired
	private SessionService sessionService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 判断用户是否登录.....
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));

		if (username == null) {
			// 用户没有登录，回到登录页面
			response.sendRedirect(
					"http://localhost:8081/login.aspx?returnUrl=http://localhost:8082/cart");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
