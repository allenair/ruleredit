package com.zxtech.ruleredit.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zxtech.ruleredit.bean.LogicUnitBean;
import com.zxtech.ruleredit.bean.ScriptBean;
import com.zxtech.ruleredit.util.CacheTools;
import com.zxtech.ruleredit.util.FileTools;

@RestController
public class MyController {
	@Autowired
    private Environment env;
	
	@RequestMapping(value="/testjson.do")
	public Map<String, Object> echoJson() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "aa");
		map.put("num", 12);
		return map;
	}
	
	@RequestMapping(value="/teststr.do")
	public String echoStr() {
		return "hello";
	}
	
	@RequestMapping(value="/loadbean.do", method= {RequestMethod.GET})
	public ScriptBean loadBeanFromFile(String fileName) {
		if(StringUtils.isEmpty(fileName)) {
			fileName = env.getProperty("app.default.filename");
		}
		CacheTools.loadCache(fileName);
		
		return CacheTools.script;
	}
	
	@RequestMapping(value="/getcachebean.do", method= {RequestMethod.GET})
	public ScriptBean getBeanFromCache() {
		if(CacheTools.script==null) {
			CacheTools.loadCache();
		}
		return CacheTools.script;
	}
	
	@RequestMapping(value="/newbean.do", method= {RequestMethod.GET})
	public String newBean(String fileName) {
		if(StringUtils.isEmpty(fileName)) {
			fileName = FileTools.FILE_NAME;
		}else {
			FileTools.FILE_NAME = fileName;
		}
		CacheTools.init();
		
		return "ok";
	}
	
	@RequestMapping(value="/saveinput.do", method= {RequestMethod.POST})
	public String saveInput(String name, String description) {
		Map<String, Object> inputMap = CacheTools.script.getInit_parameter();
		Map<String, String> descriptionMap = CacheTools.script.getState_description();
		
		if(inputMap.get(name)!=null || descriptionMap.get(name)!=null) {
			return "repeat";
			
		}else {
			inputMap.put(name, "");
			descriptionMap.put(name, description);
			CacheTools.saveToFile();
		}
		
		return "ok";
	}
	
	@RequestMapping(value="/saveoutput.do", method= {RequestMethod.POST})
	public String saveOutput(String name, String description) {
		Map<String, Integer> outputMap = CacheTools.script.getState();
		Map<String, String> descriptionMap = CacheTools.script.getState_description();
		
		if(outputMap.get(name)!=null || descriptionMap.get(name)!=null) {
			return "repeat";
			
		}else {
			outputMap.put(name, 0);
			descriptionMap.put(name, description);
			CacheTools.saveToFile();
		}
		
		return "ok";
	}
	
	@RequestMapping(value="/getLogic.do", method= {RequestMethod.GET})
	public LogicUnitBean getLogicBeanByName(String lgName) {
		return CacheTools.logicMap.get(lgName);
	}
	
	@RequestMapping(value="/getLogicList.do", method= {RequestMethod.GET})
	public List<String> getLogicNameList() {
		return new ArrayList<>(CacheTools.logicMap.keySet());
	}
	
	@RequestMapping(value="/saveLogic.do", method= {RequestMethod.POST})
	public String saveLogicBean(String lgName, Map<String, List<Map<String, String>>> valueMap) {
		LogicUnitBean bean = new LogicUnitBean();
		bean.setLg_name(lgName);
		bean.setCondition(new HashMap<>());
		bean.setDep_logic(new ArrayList<>());
		bean.setLogic(new HashMap<>());
		bean.setValue_map(valueMap);
		
		CacheTools.logicMap.put(lgName, bean);
		CacheTools.script.getLogic_unit().add(bean);
		CacheTools.saveToFile();
		
		return "ok";
	}
	
	@RequestMapping(value="/delete.do", method= {RequestMethod.GET})
	public String delete(String name, int type) {
		if(type==1) {
			CacheTools.script.getInit_parameter().remove(name);
			CacheTools.script.getState_description().remove(name);
		}
		if(type==2) {
			CacheTools.script.getState().remove(name);
			CacheTools.script.getState_description().remove(name);
		}
		if(type==3) {
			CacheTools.logicMap.remove(name);
			int index=-1;
			for(int i=0; i<CacheTools.script.getLogic_unit().size(); i++) {
				LogicUnitBean bean = CacheTools.script.getLogic_unit().get(i);
				if(bean.getLg_name().equals("name")) {
					index=i;
					break;
				}
			}
			CacheTools.script.getLogic_unit().remove(index);
		}
		
		CacheTools.saveToFile();
		return "ok";
	}
	
}