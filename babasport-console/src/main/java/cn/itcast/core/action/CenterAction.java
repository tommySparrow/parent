package cn.itcast.core.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.core.pojo.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * 管理中心控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class CenterAction {

	// 通用的转发
	@RequestMapping(value = "/console/{pageName}.do")
	public String consoleShow(@PathVariable("pageName") String pageName) {
		System.out.println("通用：" + pageName);
		return pageName;
	}

	// 框架frame
	@RequestMapping(value = "/console/frame/{pageName}.do")
	public String consoleFrameShow(@PathVariable("pageName") String pageName) {
		System.out.println("通用Frame：" + pageName);
		return "frame/" + pageName;
	}

	
}
