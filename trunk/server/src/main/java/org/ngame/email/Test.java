/**
 * 邮件测试
 */
package org.ngame.email;

import java.io.File;
import java.util.Arrays;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Administrator
 */
public class Test
{

	private AnnotationConfigApplicationContext springContext;

	public Test()
	{
		springContext = new AnnotationConfigApplicationContext();
		springContext.scan("org.ngame");
		springContext.refresh();
		//springContext = new AnnotationConfigApplicationContext(Spring.class);
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = springContext.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames)
		{
			System.out.println(beanName);
		}
	}

	public static void main(String... arg)
	{
		Test t = new Test();
		//System.out.println(test());
		t.email();
	}

	public void email()
	{
		Email e = springContext.getBean(Email.class);
		String html = "孔先生和妻子吵的不可开交，他儿子孔融说“能过过，不能过就赶紧离了吧，好吗？”这就是著名的孔融让离的故事";
		// while (true)
		{
			e.send(new String[]
			{
				"345627010@qq.com"
			}, new String[]
			{
				"345627010@qq.com"
			}, new String[]
			{
				"345627010@qq.com"
			}, "好东西", html, new File("E:\\迅雷下载/《西游记》.txt")
			//			,new File("E:\\迅雷下载/the_cpp_standard_library_2nd_edition.pdf") 
			//			,new File("E:\\迅雷下载/C++ Templates - The Complete Guide.pdf")
			);
			System.out.println("发送完毕。。。");
		}
	}

	public static int test()
	{
		int x = 1;
		try
		{
			return x;
		} finally
		{
			++x;
		}
	}
}
