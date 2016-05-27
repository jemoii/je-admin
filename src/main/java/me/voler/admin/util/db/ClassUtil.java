package me.voler.admin.util.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import me.voler.admin.util.db.CustomTableClass.OperationType;

public class ClassUtil {

	/**
	 * 
	 * @param clazz
	 *            用于取数据库对应的表名
	 * @param whereClause
	 *            自定义where子句
	 * @return
	 */
	public <T> String buildSelectSQL(Class<T> clazz, String whereClause) {
		String clazzName = camel2Underline(clazz.getSimpleName());
		Table tableName = clazz.getAnnotation(Table.class);
		// 使用@Table注解
		if (tableName != null) {
			if (StringUtils.isBlank(tableName.value())) {
				throw new IllegalArgumentException(String.format("illegal table name \"%s\"", tableName.value()));
			} else {
				clazzName = tableName.value();
			}
		}
		StringBuilder builder = new StringBuilder("SELECT * FROM ");
		builder.append(clazzName).append(" ").append(whereClause);
		return builder.toString();
	}

	public <T> ArrayList<T> readFromDB(Class<T> clazz, ResultSet result) throws SQLException {
		ArrayList<T> instanceList = new ArrayList<T>();

		Constructor<T> defaultConstructor = null;

		try {
			//
			defaultConstructor = clazz.getConstructor();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		T instance = null;
		try {
			while (result.next()) {
				//
				instance = defaultConstructor.newInstance();

				for (Field field : clazz.getDeclaredFields()) {
					field.setAccessible(true);
					if (field.getModifiers() == Modifier.PRIVATE
							|| field.getModifiers() == (Modifier.PRIVATE + Modifier.TRANSIENT)) {
						String fieldName = field.getName();
						Column columnName = field.getAnnotation(Column.class);
						// 使用@Column注解
						if (columnName != null) {
							fieldName = columnName.value();
						}
						//
						field.set(instance, result.getObject(fieldName));
					}
				}
				instanceList.add(instance);
			}

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return instanceList;
	}

	public CustomTableClass writeToDB(Object obj, OperationType operationType) {
		// 判断操作类型
		if (!OperationType.isWrite(operationType)) {
			throw new IllegalArgumentException(String.format("\"%s\" is not write operation", operationType.name()));
		}

		CustomTableClass tableClazz = buildClass(obj, operationType);

		if (tableClazz.getOperationType().equals("INSERT")) {
			StringBuilder builder = new StringBuilder("INSERT INTO ");
			builder.append(tableClazz.getTableName());
			StringBuilder columnNamesBuilder = new StringBuilder();
			StringBuilder columnValuesBuilder = new StringBuilder();
			boolean allNull = true;
			for (Iterator<Entry<String, Object>> columns = tableClazz.getColumns().entrySet().iterator(); columns
					.hasNext();) {
				Entry<String, Object> column = columns.next();
				if (column.getValue() != null) {
					columnNamesBuilder.append(column.getKey()).append(", ");
					columnValuesBuilder.append("?, ");
					allNull = false;
				}
			}
			if (allNull) {
				throw new IllegalArgumentException("all fields are null when INSERT");
			} else {
				columnNamesBuilder = new StringBuilder(StringUtils.removeEnd(columnNamesBuilder.toString(), ", "));
				columnValuesBuilder = new StringBuilder(StringUtils.removeEnd(columnValuesBuilder.toString(), ", "));
				builder.append("(").append(columnNamesBuilder).append(") VALUES(").append(columnValuesBuilder)
						.append(")");
			}
			tableClazz.setSql(builder.toString());
		} else if (tableClazz.getOperationType().equals("UPDATE")) {
			StringBuilder builder = new StringBuilder("UPDATE ");
			builder.append(tableClazz.getTableName());
			StringBuilder columnNamesBuilder = new StringBuilder();
			boolean allNull = true;
			for (Iterator<Entry<String, Object>> columns = tableClazz.getColumns().entrySet().iterator(); columns
					.hasNext();) {
				Entry<String, Object> column = columns.next();
				if (column.getValue() != null && !column.getKey().equals(tableClazz.getWhereClause())) {
					columnNamesBuilder.append(column.getKey()).append(" = ?, ");
					allNull = false;
				}
			}
			if (allNull) {
				throw new IllegalArgumentException("all fields are null when UPDATE");
			} else {
				builder.append(" SET ").append(StringUtils.removeEnd(columnNamesBuilder.toString(), ", "));
				builder.append(" WHERE ").append(tableClazz.getWhereClause()).append(" = ?");
			}
			tableClazz.setSql(builder.toString());
		} else {
			StringBuilder builder = new StringBuilder("DELETE * ");
			builder.append("FROM ").append(tableClazz.getTableName());
			builder.append(" WHERE ").append(tableClazz.getWhereClause()).append(" = ?");
			tableClazz.setSql(builder.toString());
		}

		return tableClazz;

	}

	public CustomTableClass buildClass(Object obj, OperationType operationType) {
		CustomTableClass tableClazz = new CustomTableClass();
		Class<? extends Object> objClazz = obj.getClass();
		// 表名
		Table tableName = objClazz.getAnnotation(Table.class);
		// 使用@Table注解
		if (tableName != null) {
			if (StringUtils.isBlank(tableName.value())) {
				throw new IllegalArgumentException(String.format("illegal table name \"%s\"", tableName.value()));
			} else {
				tableClazz.setTableName(tableName.value());
			}
		} else {
			String objClazzName = objClazz.getSimpleName();
			tableClazz.setTableName(camel2Underline(objClazzName));
		}
		TreeMap<Integer, String> priority2FieldName = new TreeMap<Integer, String>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getModifiers() == Modifier.PRIVATE
					|| field.getModifiers() == (Modifier.PRIVATE + Modifier.TRANSIENT)) {
				String fieldName = field.getName();
				Column columnName = field.getAnnotation(Column.class);
				// 使用@Column注解
				if (columnName != null) {
					fieldName = columnName.value();
				}
				try {
					tableClazz.getColumns().put(fieldName, field.get(obj));
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
				WhereClause whereClause = field.getAnnotation(WhereClause.class);
				// 使用@WhereClause注解
				if (whereClause != null && tableClazz.getColumns().get(fieldName) != null) {
					// 同一类中priority值不应重复
					if (priority2FieldName.get(whereClause.priority()) != null) {
						throw new IllegalArgumentException("more than one field have the same priority value");
					} else {
						priority2FieldName.put(whereClause.priority(), fieldName);
					}
				}
			}
		}
		// 更新、删除操作时where子句不可缺省
		if (!operationType.equals(OperationType.INSERT) && priority2FieldName.isEmpty()) {
			throw new IllegalArgumentException("the where clause can not be empty when UPDATE and DELETE");
		}
		if (!priority2FieldName.isEmpty()) {
			tableClazz.setWhereClause(priority2FieldName.firstEntry().getValue());
		}
		// 操作类型
		tableClazz.setOperationType(operationType.name());
		return tableClazz;
	}

	public String camel2Underline(String simpleName) {
		if (StringUtils.isBlank(simpleName)) {
			throw new IllegalArgumentException(String.format("illegal class name \"%s\"", simpleName));
		}

		Matcher firstLetterToUppercase = Pattern.compile("([A-Z][a-z])").matcher(simpleName);
		String semiName = simpleName;
		while (firstLetterToUppercase.find()) {
			String group = firstLetterToUppercase.group();
			semiName = semiName.replaceFirst(group, "_" + group.toLowerCase());
		}

		Matcher sequenceToUppercase = Pattern.compile("([A-Z]+)").matcher(semiName);
		String name = semiName;
		while (sequenceToUppercase.find()) {
			String group = sequenceToUppercase.group();
			name = name.replaceFirst(group, "_" + group.toLowerCase());
		}
		return name.substring(1);
	}

}
