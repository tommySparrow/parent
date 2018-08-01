package cn.itcast.core.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.itcast.core.pojo.Order;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.SessionTool;

/**
 * 订单控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class OrderAction {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SessionService sessionService;

	// 提交订单
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order, HttpServletRequest request,
			HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		// 添加订单表一条记录 订单详情表添加N条记录
		String username = sessionService.getUsernameForRedis(
				SessionTool.getSessionID(request, response));
		orderService.addOrderAndDetail(order, username);

		return "success";
	}

}
