package cn.itcast.core.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Spring的转换器
 * 
 * @param <S>
 *            转换前的类型
 * @param <T>
 *            转换后的类型
 * 
 * @author Administrator
 *
 */
public class MyConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
	
		if(source!=null)
		{
			String trim = source.trim();
			if(!trim.equals(""))
			{
				return trim;
			}
		}
		return null;
	}

}
