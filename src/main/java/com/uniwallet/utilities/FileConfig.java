/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uniwallet.utilities;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author DJAN DENNIS MINTAH
 */
public class FileConfig {
	
	private String sessionID  = null  ; 
	/**
	 * **************************************************************
	 * Default constructor 
	 * @param  void 
	 * @return 
	 * **************************************************************
	 * 
	 */
	public FileConfig(String session_id){
		this.sessionID = session_id; 
	}
	
	
    private static String className="FileConfig";
    private static File_io file_io = new File_io(null); 

    
    
    public  static String getField(String attribute ){
		String moduleName = "getField";
		attribute = attribute.trim();
		String result = null;
		try{
		JSONObject setting_content = new JSONObject(file_io.getData(Texts.DIR_SERVICES_SETTINGS));
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
    
    
    public static void insertField(JSONObject value, String name){
        //making sure config is not empty
        init();
        String moduleName="insertField";
              
        try {
            
            JSONObject data = new JSONObject(file_io.getData(Texts.DIR_SERVICES_SETTINGS));
        
            data.put(name,value);
            file_io.writeData(Texts.DIR_SERVICES_SETTINGS, data.toString());
                        
        } catch (Exception e) {
            Logger.error(null, " Setting content is corrupted. Reason:  "+e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);

        }
        
    
    
    }
    
    public static void init() {
        
        String moduleName="init";
        
        try {
            JSONObject setting_content;
            try {
                 setting_content= new JSONObject(file_io.getData(Texts.DIR_SERVICES_SETTINGS));

            } catch (Exception e) {
                setting_content=null;
            }
              
              if(setting_content==null){
              
              JSONObject template=new JSONObject();
              file_io.writeData(Texts.DIR_SERVICES_SETTINGS, template.toString());
              }

            
        } catch (Exception e) {
            Logger.error(null, " Could not initialize  "+e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);

            
        }

    
    }
    
    
    public static JSONObject addServiceDetails(JSONObject input, JSONObject constant) {
        
        Iterator x = input.keys();

		while (x.hasNext()){
			
			String key = (String) x.next();
			constant.put(key, input.optString(key));
		
		}
		
		return  constant;
		
	}
		
		
	public static JSONObject addOtherDetails(JSONObject input, JSONObject constant) {
		
		Iterator x = input.keys();
		
		while (x.hasNext()) {
		            
			String key = (String) x.next();
            
			if(!key.equalsIgnoreCase("serviceDetails")) {
            
				constant.put(key, input.optString(key));
            }
		}
		
		return  constant;
		
	}
    
	
	 public static void deleteField(String name){
        
		 //making sure config is not empty
        String moduleName="deleteField";
              
        try {
            
            JSONObject data = new JSONObject(file_io.getData(Texts.DIR_SERVICES_SETTINGS));
            data.remove(name);
                            
            file_io.writeData(Texts.DIR_SERVICES_SETTINGS, data.toString());
                        
        } catch (Exception e) {
            Logger.error(null, " Setting content is corrupted. Reason:  "+e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);

        }
        
         
    }
	
	
	 public static HashMap <String, String> mapJSONtoHashmap (JSONObject details) {
		 
		 HashMap<String, String> map= new HashMap<String, String>();
		 Iterator x = details.keys();
			
			while (x.hasNext()) {
				        
				String key = (String) x.next();
				map.put(key, details.optString(key));
				
			}
		
		 return map;
	 }
	 
    
}
