package com.uniwallet.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.uniwallet.interfaces.IModel;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.File_io;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Texts;

public class LocalDB extends IModel{
	private static File_io file_io = new File_io(null); 
	private static String className = "LocalDB";
	
	
	/**
	 * 
	 * @return
	 */
	public  static JSONObject getSetting(){
		String moduleName = "getSetting";
		try{
		JSONObject setting_content = new JSONObject(file_io.getData(Texts.DIR_SETTINGS));
		return setting_content;
		}catch(Exception e){
			Logger.error(null, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
			return new JSONObject();
		}
	}
	
	public  static String getSetting(String attribute ){
		String moduleName = "getSetting";
		attribute = attribute.trim();
		String result = null;
		try{
		JSONObject setting_content = new JSONObject(file_io.getData(Texts.DIR_SETTINGS));
		if(!setting_content.isNull(attribute));
			result = String.valueOf( setting_content.get(attribute));
			
		return result;
		}catch(JSONException e){
			Logger.error(null, " Setting content is corrupted. Reason:  "+e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
		}catch(Exception e){
			Logger.error(null, " Unable to load  Setting content. Reason :"+e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
			
		}
		return result;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public  static JSONObject updateSetting(){
		return null;
	}
	
	
	public  static JSONObject getLogSize(){
		return null;
	}
	
	public  static JSONObject updateChecksum(){
		return null;
	}
	
	public  static JSONObject getChecksum(){
		return null;
	}
	
	public  static JSONObject getSystemInfo(){
		return null;
	}
	
	public  static JSONObject getErrors(){
		return null;
	}
	
	public  static JSONObject getEvents(){
		return null;
	}
	
	public  static JSONObject getSMS(){
		return null;
	}
	
	public  static JSONObject getMail(){
		return null;
	}
	
	public  static JSONObject getAccess(){
		return null;
	}
	
	public  static JSONObject getAllSession(){
		return null;
	}
}
