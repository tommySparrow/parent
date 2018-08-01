package cn.itcast;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.pojo.SuperPojo;

public class TestJson {

	/**
	 * 测试json转对象，对象转json
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public void testJson1()
			throws JsonGenerationException, JsonMappingException, IOException {

		// 对象转json
//		Buyer buyer = new Buyer();
//		buyer.setUsername("大毛");
//		buyer.setPassword("123456");
		
		SuperPojo buyer = new SuperPojo();
		buyer.setProperty("username", "damao");
		buyer.setProperty("password", null);

		ObjectMapper om = new ObjectMapper();
		/*
		 * Writer w = new StringWriter(); om.writeValue(w, buyer);
		 * System.out.println(w.toString());
		 */
		// 排除null属性
		om.setSerializationInclusion(Include.NON_NULL);

		String str = om.writeValueAsString(buyer);
		System.out.println(str);
		
//		//json字符串转对象
		Buyer buyer2 = om.readValue(str, Buyer.class);
		System.out.println(buyer2);
		

	}

}
