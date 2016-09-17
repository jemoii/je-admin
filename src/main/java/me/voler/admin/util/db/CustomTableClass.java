package me.voler.admin.util.db;

import java.io.Serializable;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class CustomTableClass implements Serializable {

	private static final long serialVersionUID = 6253077753202580L;

	private String tableName;
	private SortedMap<String, Object> columns;
	private String whereClause;
	private String operationType;
	private String sql;

	public CustomTableClass() {
		setColumns(Collections.synchronizedSortedMap(new TreeMap<String, Object>()));
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public SortedMap<String, Object> getColumns() {
		return columns;
	}

	public void setColumns(SortedMap<String, Object> columns) {
		this.columns = columns;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * 
	 * 数据库操作类型
	 *
	 */
	public enum OperationType {
		SELECT(1 << 1), INSERT(1 << 2), UPDATE(1 << 3), DELETE(1 << 4);

		private int mask;

		public final int getMask() {
			return mask;
		}

		OperationType(int mask) {
			this.mask = mask;
		}

		public static boolean isRead(OperationType type) {
			return (type.mask & SELECT.mask) != 0;
		}

		public static boolean isWrite(OperationType type) {
			return (type.mask & SELECT.mask) == 0;
		}

	}

}
