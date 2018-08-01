package cn.itcast.core.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.pojo.Color;
import cn.itcast.core.pojo.SuperPojo;
import cn.itcast.core.service.ProductService;
import cn.itcast.core.service.StaticPageService;
import freemarker.template.TemplateException;

public class MyMessageListener implements MessageListener {
	@Autowired
	private ProductService productService;

	@Autowired
	private StaticPageService staticPageService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;
		String ids;
		try {
			ids = amessage.getText();
			System.out.println("cms ids:" + ids);

			// 根据ids查询出各个商品的复合信息（skus=superpojo）

			String[] split = ids.split(",");
			for (String id : split) {

				// 根据productId获得该商品的相关信息，丢给页面显示
				SuperPojo superPojo = productService.findById(Long.parseLong(id));

				// 利用hashset去除颜色的重复
				Set<Color> colors = new HashSet<Color>();
				
				List<SuperPojo> skus = (List<SuperPojo>) superPojo.get("skus");
				
				for (SuperPojo sku : skus) {
					Color color = new Color();
					
					Long colorId = (Long) sku.get("color_id");
					String colorName = (String) sku.get("colorName");
					
					color.setId(colorId);
					color.setName(colorName);
					
					colors.add(color);

				}
			
				superPojo.setProperty("colors", colors);
				
				Map<String, Object> rootMap = new HashMap<String, Object>();
				rootMap.put("superPojo", superPojo);
				
				try {
					//执行该商品的静态化功能
					staticPageService.staticProductPage(rootMap, id);
					
				} catch (IOException | TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
