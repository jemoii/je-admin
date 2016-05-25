package me.voler.admin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import me.voler.admin.usercenter.dto.UserInfo;

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
		StringBuffer sql = new StringBuffer(
				"select username, telephone, password, user_level as level, user_status as status, nickname from user_info_v3 ");
		if (StringUtils.isNotEmpty(input.getUsername())) {
			sql.append("where username = ?");
		} else {
			sql.append("where telephone = ?");
		}
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		UserInfo output = new UserInfo();
		try {
			PreparedStatement ps = connection.prepareStatement(sql.toString());
			if (StringUtils.isNotEmpty(input.getUsername())) {
				ps.setString(1, input.getUsername());
			} else {
				ps.setString(1, input.getTelephone());
			}
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return output;
			} else {
				if (result.next()) {
					output.setUsername(result.getString("username"));
					output.setTelephone(result.getString("telephone"));
					output.setPassword(result.getString("password"));
					output.setLevel(result.getInt("level"));
					output.setStatus(result.getInt("status"));
					output.setNickname(result.getString("nickname"));
				} else {
					return output;
				}
			}

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}

		return output;
	}

	public int deleteLoginInfo(UserInfo info) {
		return 0;
	}

	public int insertUserInfo(UserInfo info) {
		String sql = "insert into user_info_v3(username, password, user_level) values(?, ?, ?)";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getUsername());
			ps.setString(2, info.getPassword());
			ps.setInt(3, info.getLevel());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public int updateUserInfo(UserInfo info, Object... columnNames) {
		StringBuffer sqlBuffer = new StringBuffer("update user_info_v3 set ");
		for (int i = 0; i < columnNames.length; i++) {
			if (((String) columnNames[i]).equals("status") || ((String) columnNames[i]).equals("level")) {
				sqlBuffer.append("user_%s = ?");
			} else {
				sqlBuffer.append("%s = ?");
			}
			if (i != columnNames.length - 1) {
				sqlBuffer.append(", ");
			}
		}
		sqlBuffer.append(" where username = ?");
		String sql = String.format(sqlBuffer.toString(), columnNames);
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			int index = 1;
			for (; index <= columnNames.length; index++) {
				ps.setObject(index, ClassUtil.getter(info, (String) columnNames[index - 1]));
			}
			ps.setString(index, info.getUsername());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public int deleteUserInfo(UserInfo info) {
		return 0;
	}

	public ArrayList<UserInfo> selectUserInfoList(int limitLevel) {
		String sql = "select username, telephone, password, user_level as level, user_status as status, nickname from user_info_v3"
				+ " where user_level < ? order by username";
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		ArrayList<UserInfo> infoList = new ArrayList<UserInfo>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, limitLevel);
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return infoList;
			} else {
				while (result.next()) {
					UserInfo info = new UserInfo();
					info.setUsername(result.getString("username"));
					info.setTelephone(result.getString("telephone"));
					info.setLevel(result.getInt("level"));
					info.setStatus(result.getInt("status"));
					info.setNickname(result.getString("nickname"));
					infoList.add(info);
				}
			}

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}

		return infoList;
	}
}
