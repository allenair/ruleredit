package com.zxtech.ruleredit.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxtech.ruleredit.bean.ScriptBean;

public class FileTools {
	public static String FILE_NAME="";
	
	public static void saveToFile(String fileName, ScriptBean bean) {
		String json = new Gson().toJson(bean);
		try {
			PrintWriter fw = new PrintWriter(FileTools.FILE_NAME);
			fw.println(json);
			fw.close();
		} catch (FileNotFoundException e) {
			System.err.println("Write File ERROR!!");
		}
	}
	
	public static ScriptBean loadFromFile(String fileName) {
		ScriptBean bean=null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String tmp;
			StringBuilder json = new StringBuilder();
			while((tmp=br.readLine())!=null){
				json.append(tmp);
			}
			br.close();
			bean = new Gson().fromJson(json.toString(), new TypeToken<ScriptBean>(){}.getType());
			
			bean.init();
		}catch(Exception e) {
			System.err.println("Read File ERROR!!");
		}
		return bean;
	}
}
