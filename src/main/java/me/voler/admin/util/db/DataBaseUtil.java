package me.voler.admin.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import me.voler.admin.usercenter.dto.UserInfo;
import me.voler.admin.util.DeployUtil;
import me.voler.admin.util.db.CustomTableClass.OperationType;

/**
 * 工程使用的数据库表，
 * 
 * <pre>
create table login_info_v2(
	id serial primary key,
	user_status varchar(7) not null,
	email varchar(32) not null,
	password varchar(32) not null,
	auth boolean default false
);

create table user_info_v2(
	userId varchar(9) primary key,
	username varchar(9) not null,
    user_status varchar(7) not null,
	email varchar(32) not null,
	telephone varchar(11)
);

create table mail_auth(
	id serial primary key,
	email varchar(32) not null,
	auth_code varchar(15) not null,
	sent_time bigint not null
);
//////////////////////////////////////
create table user_info_v3 (
	id serial,
	username varchar(32) unique,
	telephone varchar(11) unique,
	password varchar(32),
	user_level smallint default 1, 
	user_status smallint default 0, 
	nickname varchar(16),
	check(username is not null or telephone is not null)
);

create table login_info_v3 (
	username varchar(32) not null,
	login_count integer default 0,
	last_logined timestamp
);
 * </pre>
 * 
 * 新建{@code <platform>/jdbc.properties}资源文件保存数据库配置，如：
 * 
 * <pre>
me.sql.driverClassName=
me.sql.url= 
me.sql.username= 
me.sql.password=
 * </pre>
 */
public class DataBaseUtil {

	private static final Logger Log = Logger.getLogger(DataBaseUtil.class);

	private static String driverClassName;
	private static String url;
	private static String username;
	private static String password;

	// 如果自定义了配置文件的名称、格式，需要在这里做相应修改
	static {
		Properties prop = new DeployUtil().getResources("jdbc.properties");
		driverClassName = prop.getProperty("me.sql.driverClassName");
		url = prop.getProperty("me.sql.url");
		username = prop.getProperty("me.sql.username");
		password = prop.getProperty("me.sql.password");
	}

	private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	private Connection getConnection() {
		Connection connection = threadLocal.get();
		if (connection == null) {
			try {
				Class.forName(driverClassName);
				connection = DriverManager.getConnection(url, username, password);
				threadLocal.set(connection);

			} catch (Exception e) {
				Log.error(String.format("连接数据库失败，%s", e.getMessage()));
				return null;
			}
		}
		return connection;
	}

	public UserInfo selectUserInfo(UserInfo input) {
		String whereClause = "where username = ?";
		if (StringUtils.isEmpty(input.getUsername())) {
			whereClause = "where telephone = ?";
		}
		String sql = new ClassUtil().buildSelectSQL(UserInfo.class, whereClause);
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			if (StringUtils.isNotEmpty(input.getUsername())) {
				ps.setString(1, input.getUsername());
			} else {
				ps.setString(1, input.getTelephone());
			}
			ResultSet result = ps.executeQuery();
			ArrayList<UserInfo> infoList = new ClassUtil().readFromDB(UserInfo.class, result);
			if (infoList.isEmpty()) {
				return new UserInfo();
			} else {
				return infoList.get(0);
			}

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}
	}

	public int deleteLoginInfo(UserInfo info) {
		return 0;
	}

	public int insert(Object obj) {
		CustomTableClass tableClazz = new ClassUtil().writeToDB(obj, OperationType.INSERT);
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(tableClazz.getSql());
			int index = 1;
			for (Iterator<Entry<String, Object>> columns = tableClazz.getColumns().entrySet().iterator(); columns
					.hasNext();) {
				Entry<String, Object> column = columns.next();
				if (column.getValue() != null) {
					ps.setObject(index, column.getValue());
					index++;
				}
			}
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", tableClazz.getSql(), e.getMessage()));
			return -1;
		}
	}

	public int update(Object obj) {
		CustomTableClass tableClazz = new ClassUtil().writeToDB(obj, OperationType.UPDATE);
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(tableClazz.getSql());
			int index = 1;
			for (Iterator<Entry<String, Object>> columns = tableClazz.getColumns().entrySet().iterator(); columns
					.hasNext();) {
				Entry<String, Object> column = columns.next();
				if (column.getValue() != null && !column.getKey().equals(tableClazz.getWhereClause())) {
					ps.setObject(index, column.getValue());
					index++;
				}
			}
			ps.setObject(index, tableClazz.getColumns().get(tableClazz.getWhereClause()));
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", tableClazz.getSql(), e.getMessage()));
			return -1;
		}
	}

	public int deleteUserInfo(UserInfo info) {
		return 0;
	}

	public ArrayList<UserInfo> selectUserInfoList(int limitLevel) {

		String sql = new ClassUtil().buildSelectSQL(UserInfo.class, "where user_level < ? order by username");
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, limitLevel);
			ResultSet result = ps.executeQuery();
			return new ClassUtil().readFromDB(UserInfo.class, result);
		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}
	}
}
