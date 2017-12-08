package com.zxtech.ruleredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.zxtech.ruleredit.util.CacheTools;
import com.zxtech.ruleredit.util.FileTools;

@SpringBootApplication
public class RulereditApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext con = SpringApplication.run(RulereditApplication.class, args);
		init(con);
	}
	
	private static void init(ConfigurableApplicationContext con) {
		FileTools.FILE_NAME=con.getEnvironment().getProperty("app.default.filename");
		CacheTools.loadCache();
	}
}
