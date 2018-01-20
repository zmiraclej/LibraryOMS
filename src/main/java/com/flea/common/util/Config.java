package com.flea.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>基础配置类，该类实现了初始化读取系统根目录下的config.properties配置文件和读取</p>
 * <p>该配置文件的方法；</p>
 * @author yuanye
 *
 */
public class Config {
	private static Logger logger = Logger.getLogger(Config.class.getName());
	private static Properties properties;
	private static InputStream is;
	
	static{
		 try {
			String path = Config.class.getResource("/").toURI().getPath();
			//后去配置文件
			is = new FileInputStream(path + "config.properties");
			properties = new Properties();
			//加载配置文件到properties
			properties.load(is);
		} catch (Exception e) {
			logger.error("系统配置文件”config.properties“未找到。");
			e.printStackTrace();
		}
	}
	
	/*
	 * 防止实例化
	 */
	private Config(){}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
	
	//TODO 在这里实现一些系统公用的操作函数，如去空格等基本的转换操作。
	
}
