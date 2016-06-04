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
		// InputStream inputStream =
		// DeployUtil.class.getClassLoader().getResourceAsStream(buildPath(propName));
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

	private InputStream buildInputStream(String propName) throws FileNotFoundException {
		String propPath = System.getenv("JE_PROP_HOME") + File.separator + "jeadmin";
		return new FileInputStream(propPath + File.separator + propName);
	}

	/**
	 * 默认情况下，依据{@link #PLATFORM PLATFORM}确定配置文件路径，可以覆盖该方法自定义确定路径的方法
	 * 
	 * @param propName
	 * @return 配置文件路径
	 */
	@Deprecated
	protected String buildPath(String propName) {
		String platform = PLATFORM.get(System.getProperty("os.name"));
		if (StringUtils.isEmpty(platform)) {
			throw new RuntimeException("可能需要覆盖buildPath");
		}
		return platform + "/" + propName;
	}

	/** 使用{@code System.getProperty("os.name")}区分本地与线上机器 */
	@Deprecated
	private static HashMap<String, String> PLATFORM = new HashMap<String, String>();

	static {
		PLATFORM.put("Windows 8.1", "local");
		PLATFORM.put("Linux", "prod");
	}

}
