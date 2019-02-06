package com.uniwallet.controllers;



import java.io.File;



import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.File_io;
import com.uniwallet.utilities.HTTPClient;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Security;
import com.uniwallet.utilities.Texts;
import com.uniwallet.utilities.Parsor;
import com.uniwallet.utilities.NumberFormater;
import com.uniwallet.interfaces.IController;
import com.uniwallet.models.SQLDBLayer;


public class User  extends IController{
	
   /** 
 	*################################################################################################
 	*									 	  | USER CLASS | 
 	*################################################################################################
 	*
 	*
 	*/
	
	boolean is_user_authenticated = false;
	private String apiSessionID;
	private File_io  file_io;
	private String secreteKey  = "N$@n0.S3cret#1F8";
	private String portalAPIKey  = "c45ssherbdd77";


	public User(  String sessionID ,String api_user_ip, String apiKey){
		
		//if( authenticate(apiKey,api_user_ip))
			this.is_user_authenticated = true;
		
		 apiSessionID = sessionID; 
		 file_io = new File_io(sessionID);
	}
		
	
	/**
	 * ******************************************
	 * authenticate user on API 
	 * @param [String] - apikey
	 * @param [String] - api_user_ip
	 * @return [Boolean]
	 * ******************************************
	 * 
	 * 
	 */
	public boolean authenticate(String apikey, String api_user_ip)
	{
		String module_name = "authenticate";
		try{	
		//# Call authenticate model 
		JSONObject db_res = SQLDBLayer.authenticate_api_user(apikey, api_user_ip);
		if(db_res.getString("code").equals(Texts.CODE_DB_SUCCESS)){
			//if(db_res.getJSONArray("data").getJSONObject(0).getString("privilege").equals("0") )
                    return true;
		}
		
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		 return false;
	}
	
		
	
	/**
	 * ******************************************
	 * login  
	 * @param  [JSONObject] - phoneNumber, pin
	 * @return [JSObject]
	 * ******************************************
	 * 
	 * 
	 */
	public  JSONObject  login(JSONObject details )
	{
		String module_name = "login";
		try{	
			//#Parameters Validate login Params 
			JSONObject validation_res = validate_params_login(details);
			if(!validation_res.getString("code").equals(Texts.CODE_SUCCESS) )
				return Auxiliary.reply(Texts.CODE_ERROR, validation_res.getString("msg"));
			
			//#Get Paramaters 
			String userId = details.getString("userId");
			String password  = details.getString("password");
			 
			//Pass data to be sent in the json object for encryption
			JSONObject login_params = new JSONObject();
			login_params.put("userId", userId);
			login_params.put("password", password);
		
			//JSONObject user_login = SQLDBLayer.check_user_login(login_params);
			
			JSONObject user_login = SQLDBLayer.check_user_login(login_params);
			

			if( user_login == null ){
			
				Logger.console(apiSessionID, "Null response", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , Texts.SERVICE_UNAVAILABLE ) ;
			
			}
			
			
			
			
			
			if ( user_login.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
                            
                               
			
				Logger.console(apiSessionID, module_name + " has been successfully performed ", user_login.getString("msg"));
				Logger.event(apiSessionID, user_login.getString("msg"));
				return Auxiliary.reply(Texts.CODE_SUCCESS ," Login successful ", user_login.getJSONArray("data").getJSONObject(0) ) ;
			
			}
                        else {
				
				Logger.console(apiSessionID, "Failure", user_login.getString("msg"));
				Logger.event(apiSessionID,  user_login.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,user_login.getString("msg") ) ;
			}
			
		
			// Should return user basic info along with the userID
			//return Auxiliary.reply(Texts.CODE_SUCCESS ,  module_name +" was successfully performed ", new JSONObject().put("userID", "UNIUSER123456789") );
			
		
		} catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR, Texts.SERVICE_UNAVAILABLE);
		
	}
			
	
	/**
	 * ******************************************
	 * signup  user on Anroid application  
	 * @param  [JSONObject] - firstName, 
	 * lastName,phoneNumber,email,secretQuestion
	 * answer,city,region
	 * @return [JSObject]
	 * ******************************************
	 * 
	 */
	public  JSONObject  signup(JSONObject details )
	{
		String module_name = "signup";
		try{	
			//#Parameters Validate module Params 
			JSONObject validation_res = validate_params_signup(details);
			
			if(!validation_res.getString("code").equals(Texts.CODE_SUCCESS) )
				return Auxiliary.reply(Texts.CODE_ERROR, validation_res.getString("msg"));
			
			//#Get Paramaters 
			String name  = details.getString("name");
			String userId = details.getString("userId");
			String phoneNumber  = NumberFormater.format(details.getString("phoneNumber"), "GH");
			String email  = details.getString("email");
			String password= details.getString("password");
                        String date_created=Auxiliary.generateTimeStamp();
			
			
			//Pass data to be sent in the json object for encryption
			JSONObject signup_params = new JSONObject();
			
			signup_params.put("name", name);
			signup_params.put("userId", userId);
			signup_params.put("phoneNumber", phoneNumber);
			signup_params.put("email", email);
			signup_params.put("password", password);
                        signup_params.put("date_created", date_created);

			JSONObject user_signup = SQLDBLayer.register_user(signup_params);
			
			//Get response, interpret and return response to app
			
			if (user_signup == null ) {
				
				Logger.console(apiSessionID, "Null response from Signup method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Signup method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Signup method " ) ;
			}
			
			if (user_signup.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Sign Up successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Sign Up successful ",new JSONArray() ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", user_signup.getString("msg"));
				Logger.event(apiSessionID,  user_signup.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,user_signup.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
	
        
         public  JSONObject  get_users(JSONObject details )
	{
		String module_name = "get_users";
		try{	
			
			
			JSONObject response = SQLDBLayer.list_users();
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Get Users method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Get Users method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Get Users method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Get Users successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Get Users successful ",response ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        
        	/**
	 * ******************************************
	 * update  user on Anroid application  
	 * @param  [JSONObject] - firstName, 
	 * lastName,phoneNumber,email,secretQuestion
	 * answer,city,region
	 * @return [JSObject]
	 * ******************************************
	 * 
	 */
	public  JSONObject  update_user(JSONObject details )
	{
		String module_name = "update_user";
		try{	
			//#Parameters Validate module Params 
			JSONObject validation_res = validate_params_signup(details);
			
			if(!validation_res.getString("code").equals(Texts.CODE_SUCCESS) )
				return Auxiliary.reply(Texts.CODE_ERROR, validation_res.getString("msg"));
			
			//#Get Paramaters 
			String name  = details.getString("name");
			String userId = details.getString("userId");
			String phoneNumber  = NumberFormater.format(details.getString("phoneNumber"), "GH");
			String email  = details.getString("email");
			String password= details.getString("password");
                        
			
			
			//Pass data to be sent in the json object for encryption
			JSONObject signup_params = new JSONObject();
			
			signup_params.put("name", name);
			signup_params.put("phoneNumber", phoneNumber);
			signup_params.put("email", email);
			signup_params.put("password", password);
                        

			JSONObject user_signup = SQLDBLayer.update_user(userId,signup_params);
			
			//Get response, interpret and return response to app
			
			if (user_signup == null ) {
				
				Logger.console(apiSessionID, "Null response from Update user method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Update user method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Update user method " ) ;
			}
			
			if (user_signup.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Updated user successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Updated User successful ",new JSONArray() ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", user_signup.getString("msg"));
				Logger.event(apiSessionID,  user_signup.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,user_signup.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
	
		
	 
	/**
	 * ******************************************
	 * Module to parse 
	 * @param details
	 * @return
	 * ******************************************
	 * 
	 */
	private  JSONObject validate_params_login(JSONObject details){
		
		String module_name = "validate_params_login";
		try{
		//# Validate User 
			if(this.is_user_authenticated == false)
				return Auxiliary.reply(Texts.CODE_ERROR, Texts.SERVICE_UNAUTHORIZED);
				
		//# Mandatory Params 
		JSONArray mand_params = new JSONArray();
		mand_params.put("userId");
		mand_params.put("password");
		JSONObject mand_parse_res = Parsor.analyseInput(mand_params, details, true);
		if(mand_parse_res.getString("code") != Texts.CODE_SUCCESS)
			return Auxiliary.reply(Texts.CODE_ERROR, mand_parse_res.getString("msg"));
		 
		return Auxiliary.reply(Texts.CODE_SUCCESS, "params successfully parsed ");
		
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	           return Auxiliary.reply(Texts.DB_ERROR , Texts.SERVICE_UNAVAILABLE);
	     }
	}
	
	
	
	/**
	 * ******************************************
	 * Module to parse module params
	 * @param details
	 * @return
	 * ******************************************
	 * 
	 */
	private  JSONObject validate_params_signup(JSONObject details){
		
		String module_name = "validate_params_signup";
		try{
		//# Validate User 
			if(this.is_user_authenticated == false)
				return Auxiliary.reply(Texts.CODE_ERROR, Texts.SERVICE_UNAUTHORIZED);
				
		//# Mandatory Params 
		JSONArray mand_params = new JSONArray();
		mand_params.put("name");
		mand_params.put("userId");
		mand_params.put("phoneNumber");
		mand_params.put("email");
		mand_params.put("password");
		
		
		JSONObject mand_parse_res = Parsor.analyseInput(mand_params, details, true);
		
		if(mand_parse_res.getString("code") != Texts.CODE_SUCCESS)
			return Auxiliary.reply(Texts.CODE_ERROR, mand_parse_res.getString("msg"));
		
		//# Parse msisdn
		
		if(!NumberFormater.veriyFormat(NumberFormater.formatNumberGH(details.getString("phoneNumber")),"GH")){
			return Auxiliary.reply(Texts.CODE_ERROR, "Invalid phone Number provided.");
		}
	
		 
		return Auxiliary.reply(Texts.CODE_SUCCESS, "params successfully parsed ");
		
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	           return Auxiliary.reply(Texts.DB_ERROR , Texts.SERVICE_UNAVAILABLE);
	     }
	}

        
        /**
	 * ******************************************
	 * Delete  user on Android application  
	 * @param  [JSONObject] - userId, 
	 * @return [JSObject]
	 * ******************************************
	 * 
	 */
	public  JSONObject  remove_user(JSONObject details )
	{
		String module_name = "remove_user";
		try{	
			
			String userId = details.getString("userId");

			JSONObject remove_params = new JSONObject();
		
			remove_params.put("userId", userId);
			
			JSONObject user_delete = SQLDBLayer.delete_user(remove_params);
			
			//Get response, interpret and return response to app
			
			if (user_delete== null ) {
				
				Logger.console(apiSessionID, "Null response from Delete method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Delete method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Delete method " ) ;
			}
			
			if (user_delete.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Deleted User successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Deleted User successful ",new JSONArray() ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", user_delete.getString("msg"));
				Logger.event(apiSessionID,  user_delete.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,user_delete.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        
        
        public  JSONObject  get_events(JSONObject details )
	{
		String module_name = "get_events";
		try{	
			
			
			JSONObject response = SQLDBLayer.list_events();
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Get Event method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Get Event method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Get Event method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Get Event successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Get Event successful ",response ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
	public  JSONObject  create_event(JSONObject details )
	{
		String module_name = "create_event";
		try{	
			String time= details.getString("time");
                        String venue=details.getString("venue");
                        String des=details.getString("des");
                        String date=details.getString("date");
                        String title=details.getString("title");
                        
                        JSONObject request= new JSONObject();
                        request.put("time", time);
                        request.put("venue", venue);
                        request.put("des", des);
                        request.put("date", date);
                        request.put("title", title);
			request.put("date_created", Auxiliary.generateTimeStamp());
			JSONObject response = SQLDBLayer.post_event(request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Create Event method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Create Event method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Create Event method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Event returned successful ");
                                
                                //HashMap<String, String> send= new HashMap<String,String>();
                                //send.put("data", details.toString());
                                //String resp=HTTPClient.sendPost("http://localhost:8886/events", send, 60000);
                                //System.out.println(resp);
                                //System.out.println("Notification Sent to HttpClient");
                                
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Event returned successful ",response ) ;
                                
                                
                                
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
                public  JSONObject  delete_event(JSONObject details )
                {
                        String module_name = "de;ete_events";
                        try{	

                                String title= details.getString("title");

                                JSONObject request= new JSONObject();
                                request.put("title",title);

                                JSONObject response = SQLDBLayer.delete_events(request);

                                //Get response, interpret and return response to app

                                if (response== null ) {

                                        Logger.console(apiSessionID, "Null response from Delete method ", Texts.FLAG_SYS_ERROR);
                                        Logger.error(apiSessionID, "Null response from Delete method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
                                        return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Delete method " ) ;
                                }

                                if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){

                                        Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
                                        Logger.event(apiSessionID, " Deleted Event successful ");
                                        return Auxiliary.reply(Texts.CODE_SUCCESS , " Deleted Event successful ",new JSONArray() ) ;
                                }

                                else {

                                        Logger.console(apiSessionID, "Failure", response.getString("msg"));
                                        Logger.event(apiSessionID,  response.getString("msg"));
                                        return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
                                }

                                //return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );

                        }catch (Exception e) {
                           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
                           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
                     }

                        return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );

                }
        
        
        public  JSONObject  update_event(JSONObject details )
	{
		String module_name = "update_event";
		try{	
			String time= details.getString("time");
                        String venue=details.getString("venue");
                        String des=details.getString("des");
                        String date=details.getString("date");
                        String title=details.getString("title");
                        
                        JSONObject request= new JSONObject();
                        request.put("time", time);
                        request.put("venue", venue);
                        request.put("des", des);
                        request.put("date", date);
		
			JSONObject response = SQLDBLayer.update_event(title, request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Update Event method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Update Event method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response Update Event from  method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Updated  Event successfully ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Updated Event successfully ",response ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        
        public  JSONObject post_ad(JSONObject details )
	{
		String module_name = "post_ad";
		try{	
			String title= details.getString("title");
                        String price=details.getString("price");
                        String des=details.getString("des");
                        String phone_number=details.getString("phone_number");
                        String image=details.getString("image");
                        String category=details.getString("category");
                        
                        
                        JSONObject request= new JSONObject();
                        request.put("title", title);
                        request.put("price", price);
                        request.put("des", des);
                        request.put("phone_number", phone_number);
                        request.put("image", image);
                        request.put("category", category);
			request.put("date_created", Auxiliary.generateTimeStamp());
			JSONObject response = SQLDBLayer.post_ad(request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Post Ad method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Post Ad method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Post Ad method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, "Ad successfully returned successful ");
                                
                             
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Ad returned successful ",response ) ;
                                
                                
                                
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        
        
        
        public  JSONObject search_ad(JSONObject details )
	{
		String module_name = "search_ad";
		try{	
			String title= details.getString("title");
                        String categories=details.getString("category");
                        
                        
                        JSONObject request= new JSONObject();
                        request.put("title", title);
                        request.put("category", categories);
                        
			JSONObject response = SQLDBLayer.search_add(request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Post Ad method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Post Ad method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Post Ad method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, "Ad successfully returned successful ");
                                
                             
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Ad returned successful ",response.getJSONArray("data") ) ;
                                
                                
                                
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {  
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        
         public  JSONObject  get_ads(JSONObject details )
	{
		String module_name = "get_ads";
		try{	
			
			
			JSONObject response = SQLDBLayer.list_ads();
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Get Ads method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Get Ads method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Get Ads method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Get Ads successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Get Ads successful ",response.getJSONArray("data") ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
         
         
         
         public  JSONObject  delete_ad(JSONObject details )
	{
		String module_name = "delete_ad";
		try{	
			
                        String title= details.getString("title");
                        
                        JSONObject request= new JSONObject();
                        request.put("title",title);
			
			JSONObject response = SQLDBLayer.delete_ads(request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Delete method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Delete method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Delete method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, " Deleted Ad successful ");
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Deleted Ad successful ",new JSONArray() ) ;
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
         
         
         
       public  JSONObject update_ad(JSONObject details )
	{
		String module_name = "update";
		try{	
                        String id=details.getString("id");
			String title= details.getString("title");
                        String price=details.getString("price");
                        String des=details.getString("des");
                        String phone_number=details.getString("phone_number");
                        String image=details.getString("image");
                        String category=details.getString("category");
                        
                        
                        JSONObject request= new JSONObject();
                        request.put("title", title);
                        request.put("price", price);
                        request.put("des", des);
                        request.put("phone_number", phone_number);
                        request.put("image", image);
                        request.put("category", category);
			request.put("date_created", Auxiliary.generateTimeStamp());
			JSONObject response = SQLDBLayer.post_ad(request);
			
			//Get response, interpret and return response to app
			
			if (response== null ) {
				
				Logger.console(apiSessionID, "Null response from Post Ad method ", Texts.FLAG_SYS_ERROR);
				Logger.error(apiSessionID, "Null response from Post Ad method ", Auxiliary.getLineNumber(), module_name, this.getClass().getName());
				return Auxiliary.reply(Texts.CODE_ERROR , "Null response from Post Ad method " ) ;
			}
			
			if (response.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS ) ){
				
				Logger.console(apiSessionID, module_name + " has been successfully performed ", new JSONObject().toString());
				Logger.event(apiSessionID, "Ad successfully returned successful ");
                                
                             
				return Auxiliary.reply(Texts.CODE_SUCCESS , " Ad returned successful ",response ) ;
                                
                                
                                
			}
			
			else {
				
				Logger.console(apiSessionID, "Failure", response.getString("msg"));
				Logger.event(apiSessionID,  response.getString("msg"));
				return Auxiliary.reply(Texts.CODE_FAILURE ,response.getString("msg") ) ;
			}
			
			//return Auxiliary.reply(Texts.CODE_SUCCESS , module_name +" was successfully performed " );
			
		}catch (Exception e) {
	           Logger.console(apiSessionID, e.getMessage(), Texts.FLAG_SYS_ERROR);
	           Logger.error(apiSessionID, e.getMessage(), Auxiliary.getLineNumber(), module_name, this.getClass().getName());
	     }
		
		return Auxiliary.reply(Texts.CODE_ERROR ,Texts.SERVICE_UNAVAILABLE );
		
	}
        
        
        

	
   /**
 	*################################################################################################
 	*									 	  |USER CLASS | 
 	*################################################################################################
 	*/
			
}
