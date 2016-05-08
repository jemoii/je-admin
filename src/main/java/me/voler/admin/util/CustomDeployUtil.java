package me.voler.admin.util;

public class CustomDeployUtil extends DeployUtil {

	@Override
	public String buildPath(String propName) {
		return propName;
	}

}
