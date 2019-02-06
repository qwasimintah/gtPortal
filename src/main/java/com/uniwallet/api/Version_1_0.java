package com.uniwallet.api;
/**
 * *******************************************************************************
 * 						    ## API VERSION_1_0 CLASS   ##
 *  
 * This Class handle all services under the version 1.0 
 * @date     2016/08/10
 * @access   Public 
 * @category API  Class  
 * @version  v_1_0
 * @author   Etio@ edited by Tunde
 * 
 * *******************************************************************************
 */


/**
 * @Dependencies
 */
import org.json.JSONArray;
import org.json.JSONObject;
import com.uniwallet.controllers.User;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.FileConfig;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Texts;


public class Version_1_0  {
	/**
	*################################################################################################
	*									 | API VERSION_1_0 CLASS | 
	*################################################################################################
	*/
	
	
	/**
	 * ******************************************
	 * handle Method
	 * @param [String] - apiSessionID
	 * @param [String] - api_user_ip
	 * @param [String] - apiKey
	 * @param [String] - module
	 * @param [String] - group
	 * @param ]JSONObject] - details
	 * @return [JSONObject] 
	 * ******************************************
	 * 
	 */
	public static JSONObject handle(String apiSessionID,String api_user_ip,String apiKey ,String module, String group, JSONObject details){
		try{
		//# Check if API Class is implemented 
		if(!is_group_module_exist(group)){
			return Auxiliary.reply(Texts.CODE_ERROR, "Unkwown group  [ "+group+" ] provided. Kindly contact admin.");
		}
		
		//# Call the appropriate module
		if( group.equalsIgnoreCase("User") )
			return call_User(apiSessionID,api_user_ip,apiKey, module ,  details);
		else 
			return Auxiliary.reply(Texts.CODE_ERROR, "group  [ "+group+" ] provided is currently inactive.Kindly contact admin. ");
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), null, null);
	           return Auxiliary.reply(Texts.DB_ERROR , Texts.SERVICE_UNAVAILABLE);
	     }
	}
	

	/**
	 * ******************************************
	 * Is Group Module Exist
	 * @param [String] - group_module_str
	 * @return [boolean]
	 * ******************************************
	 * 
	 */
	private static  boolean is_group_module_exist(String group_module_str) {
		return Auxiliary.isClass("com.uniwallet.controllers."+Auxiliary.capitalize(group_module_str.trim()) );
	}
		

	
	/**
	 * ******************************************
	 * Call User
	 * @param [String] - apiSessionID
	 * @param [String] - api_user_ip
	 * @param [String] - apiKey
	 * @param [String] - module
	 * @param [JSONObject] - details
	 * @return [boolean]
	 * ******************************************
	 * 
	 */
	private static JSONObject  call_User(String apiSessionID,String api_user_ip, String apiKey, String module , JSONObject details) {
		
		User user = new  User(apiSessionID,api_user_ip,apiKey);
		
		if(module.equalsIgnoreCase("login"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject().put("name", "Test"));
			
                       return user.login(details);
		
                else if(module.equalsIgnoreCase("signup"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.signup(details);
                
                else if(module.equalsIgnoreCase("get_users"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.get_users(details);
                
                else if(module.equalsIgnoreCase("update_user"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.update_user(details);
                
                else if(module.equalsIgnoreCase("remove_user"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.remove_user(details);
                
               
                
                else if (module.equalsIgnoreCase("get_events"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                        return user.get_events(details);
                
                else if(module.equalsIgnoreCase("create_event"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.create_event(details);
                
                else if(module.equalsIgnoreCase("update_event"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.update_event(details);
                
                else if(module.equalsIgnoreCase("delete_event"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.delete_event(details);
                
                else if(module.equalsIgnoreCase("search_ad"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.search_ad(details);
                
                else if(module.equalsIgnoreCase("post_ad"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                    return user.post_ad(details);
                
                 else if (module.equalsIgnoreCase("get_ads"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                        return user.get_ads(details);
                 
                 else if (module.equalsIgnoreCase("delete_ad"))
			//return new JSONObject().put("code", "00").put("msg", "Success").put("data", new JSONObject());
		
                        return user.delete_ad(details);
                
	
		else 
			return Auxiliary.reply(Texts.CODE_ERROR, "Unknown or Inactive Module  [ "+module+" ]  provided. Kindly contact admin.");

	}
	

   /** 
	*################################################################################################
	*									 | END API VERSION_1_0 CLASS | 
	*################################################################################################
	*/
}
