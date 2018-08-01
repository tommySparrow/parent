package cn.itcast.core.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.qpid.proton.engine.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.service.BuyerService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.Encryption;
import cn.itcast.core.tools.SessionTool;

/**
 * 登录管理控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginAction {

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SessionService sessionService;

	// 显示登录页面
	@RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
	public String showLogin(Model model) {
		System.out.println("显示登录页面");
		return "login";
	}

	// 执行登录
	@RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
	public String doLogin(Model model, String username, String password,
			String returnUrl, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("执行登录");
		System.out.println(username);
		System.out.println(password);

		// 如果用户直接打开登录页面，则登录后返回首页
		if (returnUrl == null || returnUrl.length() < 1) {
			returnUrl = "http://localhost:8082/";
		}

		if (username != null) {
			if (password != null) {
				// 检查用户名是否正确
				Buyer buyer = buyerService.findByUsername(username);
				if (buyer != null) {
					// 开始判断密码
					if (buyer.getPassword()
							.equals(Encryption.encrypt(password))) {

						// 用户正确，密码也正确，开始登录
						// session.setAtrr(...);
						// 获得maosid 配合用户名存入redis
						String maosid = SessionTool.getSessionID(request,
								response);
						sessionService.addUsernameToRedis(maosid,
								buyer.getUsername());

						System.out.println("验证成功，开始登录");

						return "redirect:" + returnUrl;

					} else {
						// 密码错误
						model.addAttribute("error", "密码错误");

					}
				} else {
					// 用户名不存在
					model.addAttribute("error", "用户名不存在");
				}

			} else {
				// 密码不能为空
				model.addAttribute("error", "密码不能为空");
			}
		} else {
			// 用户不能为空
			model.addAttribute("error", "用户不能为空");
		}

		return "login";
	}

	// 验证登录
	@RequestMapping(value = "/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("验证登录!!!");
		System.out.println("callback:" + callback);
		
		//jQuery3854844

		// 获得浏览器传来的maosid
		String maosid = SessionTool.getSessionID(request, response);

		// 根据maosid去redis找对应的用户名
		String username = sessionService.getUsernameForRedis(maosid);

		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(username);
		mappingJacksonValue.setJsonpFunction(callback);
		
		//jQuery3854844("fbb2016");

		return mappingJacksonValue;
	}
}
