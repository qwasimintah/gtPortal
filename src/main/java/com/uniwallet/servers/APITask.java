package com.uniwallet.servers;
/**
 * *******************************************************************************
 * 						##  API TASK   Class  ##
 *  
 * This Class   handle all new incoming socket request received 
 * @date     2016/01/29
 * @access   Public 
 * @category Main Server  
 * @version  v_1_0
 * @author   Chrystish- modified by   Etio@ 
 * 
 * *******************************************************************************
 */


/**
 * @Dependencies
 */


import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uniwallet.utilities.Security;
import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.api.Version_1_0;
import com.uniwallet.interfaces.ITask;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Parsor;
import com.uniwallet.utilities.Texts;


public class APITask extends ITask   {
	/**
	*################################################################################################
	*									 | API TASK CLASS | 
	*################################################################################################
	*
	*/
	private String apiSessionID="0000"; 
	private HttpServletRequest api_client ; 


	/**
	 * ******************************************
	 * Overloaded Constructor
	 * @param [String[] ] - args
	 * @throw IOException
	 * @return [void]
	 * ******************************************
	 * 
	 */
	public APITask(HttpServletRequest client ){
		String apiSessionID = Auxiliary.generateSessionID();
		this.apiSessionID = apiSessionID;
		this.api_client = client;
		String module_name = "constructor";
		
		 try {
				
			
			
			//response object to be written
		} catch (Exception e) {
			Logger.console(apiSessionID,"Exception: "+e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
			//System.exit(1000);
		}

	}
	
	
	
	/**
	 * ******************************************
	 * handleRequest
	 * Handle all incoming socket request 
	 * @param  [Socket] - scon
	 * @return [void]
	 * ******************************************
	 * 
	 */
	public String handleRequest(String dataload){
		String module_name = "handleRequest";	

		try
		{	
			String dataload_cpy = dataload.length() >1000 ? dataload.substring(0,950) :dataload;
			 Logger.access(apiSessionID,"request", new JSONArray().put(this.api_client.getRemoteAddr()).put(dataload_cpy));
				
			
			 JSONObject request = null;
			 
           		//# Convert to JSON  
			 	request= new JSONObject(dataload);
			 	
			 
			 	
			 	/*JSONObject request = new JSONObject(request_encoded);
			 	*/
			 	
				//# Analyze the request
				JSONArray mand_params = new JSONArray();
				mand_params = mand_params.put("apiKey"); 
				mand_params = mand_params.put("apiVersion");
				mand_params = mand_params.put("module"); 
				mand_params = mand_params.put("group");
				mand_params = mand_params.put("details"); 
				
				JSONObject mand_pars_res = Parsor.analyseInput(mand_params, request, true);
				
				if(mand_pars_res.getString("code") != Texts.CODE_SUCCESS){
				
					JSONObject response = new JSONObject();
					
					response = Auxiliary.reply(Texts.CODE_ERROR,mand_pars_res.getString("msg"));
					
					return Auxiliary.renderSocket( apiSessionID,response.toString());
					 
				}
				
			    //# Get Mandatory Parameters  
				String apiKey      = request.getString("apiKey").trim();
				String apiVersion  =request.getString("apiVersion").trim(); //V1_0
				String module      = request.getString("module").trim();
				String group       = request.getString("group").trim();
				JSONObject details = request.getJSONObject("details");
				
				//# Validate version 
				if(!Parsor.parseApiVersion(apiVersion)){
					
					JSONObject response = new JSONObject();
					
					response = Auxiliary.reply(Texts.CODE_ERROR, "Invalid Version Provided. kinly submit the API version in the format V1_0");
					
					return Auxiliary.renderSocket( apiSessionID,response.toString());				
					
				}
				
				String ApiCLass =  Auxiliary.capitalize( getapiVersionModule(apiVersion.trim()));
				
				//# Check if API Class is implemented 
				if(!is_version_module_exist(ApiCLass)){
					JSONObject response = new JSONObject();
					response = Auxiliary.reply(Texts.CODE_ERROR, "Version "+ApiCLass+ " Provided is not activated on Engine. Kindly contact admin.");
					return Auxiliary.renderSocket( apiSessionID,response.toString());
					
				}
				
				//# Check if DB connection is set 	
				if( !SQLDBLayer.is_db_connectivity_exist() ){
					SQLDBLayer.reuse_or_open_db_connectivity( );
					if( !SQLDBLayer.is_db_connectivity_exist() ){
						JSONObject response = new JSONObject(); 
						response = Auxiliary.reply(Texts.CODE_ERROR,Texts.SERVICE_UNAVAILABLE);
						return Auxiliary.renderSocket( apiSessionID,response.toString());
						
					}
				}
					
				//# Reflexion
				JSONObject response = new JSONObject(); 
				if(ApiCLass.equalsIgnoreCase("Version_1_0")){
					response =  Version_1_0.handle(this.apiSessionID,this.api_client.getRemoteAddr(),apiKey ,module, group, details);
					
				} else{
					response = Auxiliary.reply(Texts.CODE_ERROR,"Invalid API Version Module.");
				}		
				
				//# Reply to socket user 
				if( response != null){
					return Auxiliary.renderSocket( apiSessionID,response.toString());
					
				}
				else{
					String syst_err = Auxiliary.reply(Texts.CODE_ERROR,Texts.SERVICE_UNAVAILABLE).toString();
					return Auxiliary.renderSocket( apiSessionID,syst_err);
					
				}
				
		} catch (JSONException e) { 
			//# Handle  JSON Exception
			String emsg = e.getMessage().replace("JSONObject[", "");
			emsg = emsg.replace("]", "");
			JSONObject response = new JSONObject();
			response = Auxiliary.reply(Texts.CODE_FAILURE,emsg, new JSONObject() );
			Logger.console(apiSessionID,e.getMessage()+" "+e.getCause(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
			return Auxiliary.renderSocket( apiSessionID,response.toString());
		}catch(Exception e){
			//# Handle  General  Exception
			String syst_err = Auxiliary.reply(Texts.CODE_ERROR,Texts.SERVICE_UNAVAILABLE).toString();
			Logger.console(apiSessionID,e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
			return Auxiliary.renderSocket( apiSessionID ,syst_err);
		}
		
	
	  
	}
	
	
	
	/**
	 * ******************************************
	 * Is Version Module Exist
	 * @param  [String] version
	 * @return [boolean]
	 * ******************************************
	 * 
	 */
	private static  boolean is_version_module_exist(String version_str) {
		return Auxiliary.isClass("com.uniwallet.api."+version_str);
	}
	
	

	
	/**
	 * ******************************************
	 * get API Version Module
	 * @param  [String] version
	 * @return [boolean]
	 * ******************************************
	 * 
	 */
	private static  String getapiVersionModule(String version_str) {
		String real_api_vs_class = "Version"+version_str.toUpperCase().replace("V","_");
		return real_api_vs_class;
	}
	
	

	
	
	/**
	*################################################################################################
	*									 | END API TASK CLASS | 
	*################################################################################################
	*
	*/
}