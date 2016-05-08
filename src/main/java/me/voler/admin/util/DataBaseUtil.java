package me.voler.admin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import me.voler.admin.usercenter.dto.LoginInfo;
import me.voler.admin.usercenter.dto.MailAuthentication;
import me.voler.admin.usercenter.dto.RegisterInfoIDTO;
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

	public LoginInfo selectLoginInfo(String email) {
		String sql = "select id, user_status as status, email, password, auth from login_info_v2 where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		LoginInfo info = new LoginInfo();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return info;
			} else {
				if (result.next()) {
					info.setId(result.getInt("id"));
					info.setStatus(result.getString("status"));
					info.setEmail(result.getString("email"));
					info.setPassword(result.getString("password"));
					info.setAuth(result.getBoolean("auth"));
				} else {
					return info;
				}
			}

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}

		return info;
	}

	public void insertLoginInfo(RegisterInfoIDTO info, String encryptedPassword) {
		String sql = "insert into login_info_v2(user_status, email, password, auth) values(?, ?, ?, ?)";
		Connection connection = getConnection();
		if (connection == null) {
			return;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getStatus());
			ps.setString(2, info.getEmail());
			ps.setString(3, encryptedPassword);
			ps.setBoolean(4, false);
			ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return;
		}
	}

	public int deleteLoginInfo(UserInfo info) {
		String sql = "delete from login_info_v2 where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getEmail());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public int updateLoginInfo(MailAuthentication auth) {
		String sql = "update login_info_v2 set auth = ? where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setString(2, auth.getEmail());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public int updateLoginInfo(String email, String encryptedPassword) {
		String sql = "update login_info_v2 set password = ? where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, encryptedPassword);
			ps.setString(2, email);
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public void insertMailAuth(String email, String authCode) {
		String sql = "insert into mail_auth(email, auth_code, sent_time) values(?, ?, ?)";
		Connection connection = getConnection();
		if (connection == null) {
			return;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, authCode);
			ps.setLong(3, System.currentTimeMillis());
			ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return;
		}
	}

	public MailAuthentication selectMailAuth(String email) {
		String sql = "select email, auth_code as authCode, sent_time as sentTime from mail_auth where email = ?"
				+ " order by sent_time desc limit 1";
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		MailAuthentication auth = new MailAuthentication();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return auth;
			} else {
				if (result.next()) {
					auth.setEmail(result.getString("email"));
					auth.setAuthCode(result.getString("authCode"));
					auth.setSentTime(result.getLong("sentTime"));
				} else {
					return auth;
				}
			}
		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}
		return auth;
	}

	public void insertUserInfo(UserInfo info) {
		String sql = "insert into user_info_v2(userId, username, user_status, email, telephone) values(?, ?, ?, ?, ?)";
		Connection connection = getConnection();
		if (connection == null) {
			return;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getUserId());
			ps.setString(2, info.getUsername());
			ps.setString(3, info.getStatus());
			ps.setString(4, info.getEmail());
			ps.setString(5, info.getTelephone());
			ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return;
		}
	}

	public UserInfo selectUserInfo(String email) {
		String sql = "select userId, username, user_status as status, email, telephone from user_info_v2 where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		UserInfo info = new UserInfo();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return info;
			} else {
				if (result.next()) {
					info.setUserId(result.getString("userId"));
					info.setUsername(result.getString("username"));
					info.setStatus(result.getString("status"));
					info.setEmail(result.getString("email"));
					info.setTelephone(result.getString("telephone"));
				} else {
					return info;
				}
			}

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return null;
		}

		return info;
	}

	public int updateUserInfo(UserInfo info) {
		String sql = "update user_info_v2 set username = ?, telephone = ? where userId = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getUsername());
			ps.setString(2, info.getTelephone());
			ps.setString(3, info.getUserId());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public int deleteUserInfo(UserInfo info) {
		String sql = "delete from user_info_v2 where email = ?";
		Connection connection = getConnection();
		if (connection == null) {
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, info.getEmail());
			return ps.executeUpdate();

		} catch (SQLException e) {
			Log.error(String.format("操作数据库失败，sql：%s，message：%s", sql, e.getMessage()));
			return -1;
		}
	}

	public ArrayList<UserInfo> selectUserInfoList() {
		String sql = "select userId, username, user_status as status, email, telephone from user_info_v2"
				+ " where user_status != 'admin' order by userId";
		Connection connection = getConnection();
		if (connection == null) {
			return null;
		}

		ArrayList<UserInfo> infoList = new ArrayList<UserInfo>();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			if (result == null) {
				return infoList;
			} else {
				while (result.next()) {
					UserInfo info = new UserInfo();
					info.setUserId(result.getString("userId"));
					info.setUsername(result.getString("username"));
					info.setStatus(result.getString("status"));
					info.setEmail(result.getString("email"));
					info.setTelephone(result.getString("telephone"));
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
