package com.ccm.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public class MoreLanguageUtil {

	
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> rebuildI18nMapList(String moreLanguageJson){
		
		JSONArray arry = JSONArray.parseArray(moreLanguageJson);
		Iterator<Object> iter = arry.iterator();
		List<Map<String, String>> i18nMapList = new ArrayList<Map<String,String>>();
		
		while (iter.hasNext()) {

			Map<String,String> map = (Map<String, String>) iter.next();
			
			//是否需要存在新的语言
			boolean flag = true;
			for (Map<String, String> i18nMap : i18nMapList) {
				if(i18nMap.get("codeNo").equals(map.get("codeNo"))){
					i18nMap.putAll(map);
					flag = false;
					break;
				}
			}
			
			if(flag){
				i18nMapList.add(map);
			}
		}
		return i18nMapList;
	}
	
}
