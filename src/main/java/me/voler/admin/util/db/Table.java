package me.voler.admin.util.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Table {

	/**
	 * 不使用@Table注解时，默认将驼峰式类名转换成下划线式数据库表名。如“UserInfo”转换成“user_info”。
	 * 
	 * @return 该类对应的数据库中的表名
	 * 
	 * @see me.voler.admin.util.db.ClassUtil#camel2Underline(String)
	 */
	String value();

}
