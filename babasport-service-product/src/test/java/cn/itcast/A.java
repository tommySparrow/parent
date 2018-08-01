package cn.itcast;

public class A {
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		// 读xml的配置文件
		String className = "cn.itcast.B";

		Haha haha = (Haha) Class.forName(className).newInstance();

		haha.xixi();
	}
}
