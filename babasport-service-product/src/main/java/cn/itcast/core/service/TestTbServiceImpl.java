package cn.itcast.core.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.dao.TestTbDAO;
import cn.itcast.core.pojo.TestTb;

/**
 * 测试类服务实现类
 * @author Administrator
 *
 */
@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService{
	
	@Autowired
	private TestTbDAO testTbDAO;

	@Override
	public void add(TestTb testTb) {
		
		testTbDAO.add(testTb);
		
		TestTb testTb2 = new TestTb();
		testTb2.setName("景甜6");
		testTb2.setBirthday(new Date());
		
		//int i = 5/0;
		
		testTbDAO.add(testTb2);
		
	}
}
