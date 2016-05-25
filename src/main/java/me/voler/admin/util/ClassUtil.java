package me.voler.admin.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtil {

	public static Object getter(Object obj, String fieldName) {
		Method[] methods = obj.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get") && methods[i].getParameterTypes().length == 0) {
				String original = methodName.substring("get".length());
				String custom = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				if (original.equals(custom)) {

					try {
						return methods[i].invoke(obj);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}
		return null;
	}

}
