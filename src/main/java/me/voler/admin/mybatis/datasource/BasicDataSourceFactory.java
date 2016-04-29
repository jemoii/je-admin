package me.voler.admin.mybatis.datasource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

public class BasicDataSourceFactory extends UnpooledDataSourceFactory {

	public BasicDataSourceFactory() {
		this.dataSource = new BasicDataSource();
	}

}
