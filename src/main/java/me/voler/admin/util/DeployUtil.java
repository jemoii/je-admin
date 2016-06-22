package me.voler.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DeployUtil {
	private static final Logger Log = Logger.getLogger(DeployUtil.class);

	public Properties getResources(String propName) {
		Properties prop = new Properties();
		try {
			InputStream inputStream = buildInputStream(propName);
			prop.load(inputStream);
		} catch (IOException e) {
			Log.error(String.format("加载配置文件错误，%s", e.getMessage()));
			return new Properties();
		}

		return prop;
	}

	/**
	 * 将配置文件保存在机器上，由环境变量{@code JE_PROP_HOME}指定配置文件根目录
	 * 
	 * @param propName
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream buildInputStream(String propName) throws FileNotFoundException {
		String env = System.getenv("JE_PROP_HOME");
		if (env == null) {
			return DeployUtil.class.getClassLoader().getResourceAsStream(buildPath(propName));
		}
		return new FileInputStream(env + File.separator + "jeadmin" + File.separator + propName);
	}

	/**
	 * 依据{@link #PLATFORM PLATFORM}确定配置文件路径，可以覆盖该方法自定义确定路径的方法
	 * 
	 * @param propName
	 * @return 配置文件路径
	 */
	protected String buildPath(String propName) {
		String platform = PLATFORM.get(System.getProperty("os.name"));
		if (StringUtils.isEmpty(platform)) {
			throw new RuntimeException("可能需要覆盖buildPath");
		}
		return platform + "/" + propName;
	}

	/** 使用{@code System.getProperty("os.name")}区分本地与线上机器 */
	private static HashMap<String, String> PLATFORM = new HashMap<String, String>();

	static {
		PLATFORM.put("Windows 8.1", "local");
		PLATFORM.put("Linux", "prod");
	}

}
