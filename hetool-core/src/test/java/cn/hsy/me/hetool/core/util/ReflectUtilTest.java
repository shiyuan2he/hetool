package cn.hsy.me.hetool.core.util;

import cn.hsy.me.hetool.core.lang.test.bean.ExamInfoDict;
import cn.hsy.me.hetool.core.lang.Filter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类单元测试
 * 
 * @author heshiyuan
 *
 */
public class ReflectUtilTest {

	@Test
	public void getMethodsTest() {
		Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
		Assert.assertEquals(22, methods.length);
		
		//过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, new Filter<Method>() {
			
			@Override
			public boolean accept(Method t) {
				return Integer.class.equals(t.getReturnType());
			}
		});
		
		Assert.assertEquals(4, methods.length);
		final Method method = methods[0];
		Assert.assertNotNull(method);
		
		//null过滤器测试
		methods = ReflectUtil.getMethods(ExamInfoDict.class, null);
		
		Assert.assertEquals(22, methods.length);
		final Method method2 = methods[0];
		Assert.assertNotNull(method2);
	}

	@Test
	public void getMethodTest() {
		Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);
		
		method = ReflectUtil.getMethod(ExamInfoDict.class, "getId", Integer.class);
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}
	
	@Test
	public void getMethodIgnoreCaseTest() {
		Method method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);
		
		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "GetId");
		Assert.assertEquals("getId", method.getName());
		Assert.assertEquals(0, method.getParameterTypes().length);
		
		method = ReflectUtil.getMethodIgnoreCase(ExamInfoDict.class, "setanswerIs", Integer.class);
		Assert.assertEquals("setAnswerIs", method.getName());
		Assert.assertEquals(1, method.getParameterTypes().length);
	}

	@Test
	public void getFieldTest() {
		// 能够获取到父类字段
		Field privateField = ReflectUtil.getField(ClassUtilTest.TestSubClass.class, "privateField");
		Assert.assertNotNull(privateField);
	}
	
	@Test
	public void setFieldTest() {
		TestClass testClass = new TestClass();
		ReflectUtil.setFieldValue(testClass, "a", "111");
		Assert.assertEquals(111, testClass.getA());
	}

	@Test
	public void invokeTest() {
		TestClass testClass = new TestClass();
		ReflectUtil.invoke(testClass, "setA", 10);
		Assert.assertEquals(10, testClass.getA());
	}

	static class TestClass {
		private int a;

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}
	}
}
