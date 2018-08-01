package cn.itcast.core.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.core.service.SolrService;

public class MyMessageListener implements MessageListener {

	@Autowired
	private SolrService solrService;

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage amessage = (ActiveMQTextMessage) message;
		try {
			// 从mq里面监听到消息并取出（*）
			String ids = amessage.getText();
			System.out.println("solr_productIds:" + ids);

			// 并根据ids查询相关的商品信息，并添加到solr索引库
			solrService.addProduct(ids);

		} catch (JMSException | SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
