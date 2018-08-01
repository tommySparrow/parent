package cn.itcast;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试solr
 * 
 * @author Administrator
 *
 */
/**
 * Junit + Spring
 * 
 * @author Administrator 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestSolr {

	@Autowired
	private SolrServer solrServer;

	/**
	 * 创建索引（传统方式）
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void createIndex1() throws SolrServerException, IOException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		// SolrServer solrServer = new HttpSolrServer(
		// "http://192.168.56.101:8080/solr/collection1");
		//
		// // 创建文档对象
		// SolrInputDocument document = new SolrInputDocument();
		//
		// // 添加字段到文档对象中
		// document.addField("id", 1);
		// document.addField("name_ik", "范冰冰最喜欢的哈哈哈哈");
		//
		// // 添加文档到solr服务器对象
		// solrServer.add(document);
		// solrServer.commit();// 提交
	}

	/**
	 * 创建索引（使用Spring方式）
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void createIndex2() throws SolrServerException, IOException {
		// 创建文档对象
		SolrInputDocument document = new SolrInputDocument();

		// 添加字段到文档对象中
		document.addField("id", "2");
		document.addField("name_ik", "范冰冰最喜欢的22222222");

		// 添加文档到solr服务器对象
		solrServer.add(document);
		solrServer.commit();// 提交
	}

	public static void main(String[] args) {

		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		final SolrServer solrServer2 = new HttpSolrServer(
				"http://192.168.56.101:8080/solr/collection1");

		// 第一个线程
		new Thread() {
			public void run() {
				// 创建文档对象
				SolrInputDocument document = new SolrInputDocument();

				// 添加字段到文档对象中
				document.addField("id", "205");
				document.addField("name_ik", "范冰冰最喜欢的22222222");
				try {

					solrServer2.add(document);
					Thread.sleep(5000);
					solrServer2.rollback();

				} catch (SolrServerException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				System.out.println("第一个线程完毕");
			};
		}.start();

		// 第2个线程
		new Thread() {
			public void run() {

				// 创建文档对象
				SolrInputDocument document = new SolrInputDocument();

				// 添加字段到文档对象中
				document.addField("id", "206");
				document.addField("name_ik", "范冰冰最喜欢的22222222");
				try {

					solrServer2.add(document);
					Thread.sleep(1000);
					solrServer2.commit();

				} catch (SolrServerException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				System.out.println("第2个线程完毕");
			};
		}.start();

	}

}
