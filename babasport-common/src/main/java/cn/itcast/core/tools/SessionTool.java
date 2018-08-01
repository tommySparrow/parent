package cn.itcast.core.tools;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义session工具类 主要是分配maosessionid
 * 
 * @author Administrator
 *
 */
public class SessionTool {

	/**
	 * 获得自定义sessionID，并在初次访问时，分配maosessionid，并将maosessionid写入到浏览器的cookie中
	 * 
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request,
			HttpServletResponse response) {

		// 从cookie中取出maosid
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("maosessionid")) {
					return cookie.getValue();
				}
			}
		}

		// cookie并没有maosid，给它创建一个maosid
		String maosessionid = UUID.randomUUID().toString().replaceAll("-", "");

		// 将maosessionid写入到cookie中
		Cookie cookie = new Cookie("maosessionid", maosessionid);

		// 会话级cookie,关闭浏览器失效
		cookie.setMaxAge(-1);

		// 设置二级跨域，由于本次课程中都是localhost，所有无需设置，但是项目正式上限后需要设置该项
		// cookie.setDomain(".babasport.com");

		// 设置路径 如果不设置，端口后面的目录名称（xxx:8080/login）会对cookie存储照成影响
		cookie.setPath("/");

		response.addCookie(cookie);

		return maosessionid;
	}

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
