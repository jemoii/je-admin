package me.voler.admin.util.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface WhereClause {

	/**
	 * 构建where子句时，首先将field按priority值升序排列（priority值越小，优先级越高），
	 * 使用第一个不为null的field构建where子句。同一个类中， priority值不应重复。
	 * 
	 * @return 构建where子句时的优先级
	 */
	int priority() default 0;

}
