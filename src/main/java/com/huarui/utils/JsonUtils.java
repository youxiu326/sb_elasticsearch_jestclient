package com.huarui.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
	
	private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
	private static JsonFactory jsonfactory = new JsonFactory(); 
	private static ObjectMapper mapper = new ObjectMapper(jsonfactory);
	
	public static <T> T parseObject(String json,Class<T> clzz) {
		//设置JSON时间格式    
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		mapper.setDateFormat(myDateFormat);
		try {
			return mapper.readValue(json, clzz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * json转map
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String,Object> parseMap(String json){
		TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {}; 
		try {
			return mapper.readValue(json, typeRef);
		} catch (JsonParseException e) {
			log.error("字符串转json出错!"+json, e);
		} catch (JsonMappingException e) {
			log.error("json映射map出错!"+json, e);
		} catch (IOException e) {
			log.error("json转map流错误!"+json, e);
		}
		return null;
	}
	
	
	public static <T> List<T> parseList(String json,Class<?> clazz){
		TypeFactory t = TypeFactory.defaultInstance(); 
		try {
			List<T> list = mapper.readValue(json,t.constructCollectionType(ArrayList.class,clazz));
			return list;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 对像转json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String parseString(Object obj){
		String result = null;
		try {
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
			mapper.setDateFormat(myDateFormat);
			result = mapper.writeValueAsString(obj);
		} catch (JsonParseException e) {
			log.error("字符串转json出错!", e);
		} catch (JsonMappingException e) {
			log.error("json映射map出错!", e);
		} catch (IOException e) {
			log.error("json转map流错误!", e);
		}
		return result;
	}
}
