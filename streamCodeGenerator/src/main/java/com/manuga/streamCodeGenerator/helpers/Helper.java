package com.manuga.streamCodeGenerator.helpers;

import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseSettings;
import com.manuga.streamCodeGenerator.helpers.databases.dbModels.DatabaseTypeEnum;


/**
 * 
 */

/**
 * @author Suncica
 * 
 */
public class Helper { 
     
	//public static String GetTableFieldsListJoined(List<Ta>)
	public static DatabaseSettings zoharSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDataBaseType(DatabaseTypeEnum.postgres);
		settings.setLogin("ezop");
		settings.setPass("amr6vkur");
		settings.setHost("zohar.dnsalias.org");
		settings.setPort(5432);
		settings.setDataBase("bioinf");
		
		return settings;
	}

	public static DatabaseSettings fSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDataBaseType(DatabaseTypeEnum.postgres);
		settings.setLogin("ezop");
		settings.setPass("amr6vkur");
		settings.setHost("127.0.0.1");
		settings.setPort(5432);
		settings.setDataBase("svastara");
		return settings;
	}

	public static DatabaseSettings homeSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDataBaseType(DatabaseTypeEnum.postgres);
		settings.setLogin("test");
		settings.setPass("test");
		settings.setHost("127.0.0.1");
		settings.setPort(5432);
		settings.setDataBase("test");
		
		return settings;
	}
	public static DatabaseSettings vmSettingsMysql() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDataBaseType(DatabaseTypeEnum.mysql);
		settings.setLogin("rosada");
		settings.setPass("rosada");
		settings.setHost("192.168.0.211");
		settings.setPort(3306);
		settings.setDataBase("rosada");

		return settings;
	}


 
}
