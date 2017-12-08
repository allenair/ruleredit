package com.zxtech.ruleredit.util;

import java.util.HashMap;

import com.zxtech.ruleredit.bean.LogicUnitBean;
import com.zxtech.ruleredit.bean.ScriptBean;

public class CacheTools {
	public static ScriptBean script;
	public static HashMap<String, LogicUnitBean> logicMap;
	static {
		init();
	}
	public static void init() {
		script = new ScriptBean();
		logicMap = new HashMap<>();
	}
	
	public static void loadCache() {
		loadCache(FileTools.FILE_NAME);
	}
	
	public static void loadCache(String fileName) {
		FileTools.FILE_NAME = fileName;
		script = FileTools.loadFromFile(fileName);
		loadLogicMap();
	}
	
	private static void loadLogicMap() {
		if(script!=null) {
			logicMap = new HashMap<>();
			for (LogicUnitBean bean : script.getLogic_unit()) {
				logicMap.put(bean.getLg_name(), bean);
			}
		}
	}
	
	public static void saveToFile() {
		FileTools.saveToFile(FileTools.FILE_NAME, script);
	}
}
