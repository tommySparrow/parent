package cn.itcast.core.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 静态化页面服务实现类
 * 
 * @author Administrator
 *
 */
@Service("staticPageService")
public class StaticPageServiceImpl
		implements StaticPageService, ServletContextAware {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	private ServletContext servletContext;

	@Override
	public void staticProductPage(Map<String, Object> rootMap, String id)
			throws IOException, TemplateException {

		// 使用spring的Freemarker配置获得标准的Freemark配置器
		Configuration configuration = freeMarkerConfigurer.getConfiguration();

		// 设置模版文件
		Template template = configuration.getTemplate("product.html");

		// 获得最终静态文件输出的位置
		String path = servletContext
				.getRealPath("/html/product/" + id + ".html");
		
		System.out.println("获得最终静态文件输出的位置:" + path);
		
		File file = new File(path);
		//获得438.html文件的父目录
		File parentFile = file.getParentFile();
		
		// 如果父目录不存在，则进行创建
		if(!parentFile.exists())
		{
			parentFile.mkdirs();
		}

		// 设置最终静态文件输出的位置
		Writer out = new FileWriter(new File(path));
		template.process(rootMap, out);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
