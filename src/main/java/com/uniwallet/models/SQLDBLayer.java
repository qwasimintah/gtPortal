package com.uniwallet.models;

/**
 * *******************************************************************************
 * SQLDBLayer Class
 *  
 * This Class is used to interface with all MySQL DB
 * It contains all the used queries performed in the 
 * actual Bank Client Engine
 * 
 * 
 * @date     2014/10/29
 * @access   Public 
 * @category Database Layer 
 * @version  v_1_0
 * @author   Etienne ( Etio@ )
 * 
 * *******************************************************************************
 */

/**
 * @Dependencies
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uniwallet.interfaces.IModel;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.HTTPClient;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Texts;
import com.uniwallet.utilities.Convertor;
import com.uniwallet.utilities.Parsor;



public class SQLDBLayer extends IModel {
	
	//################################################################################################
	//						    		   | SQLDBLayer Class |  
	//################################################################################################
		
	//# Db connection
	private static Connection db_con = null;

	private static String  tbl_user  = "tbl_user";
        private static String  tbl_event  = "tbl_events";

	

	
	//################################################################################################
	//								         | GET  DB CON |  
	//################################################################################################
	
	
	/**
	 * ******************************************
	 * get_connection is used to obtain the 
	 * 	SQL database connection and save it 
	 * 	statically in var db_con
	 * 
	 * @access  private static 
	 * @param   [void]
	 * @return  [Connection] 
	 * ******************************************
	 */
	public static Connection get_connection(){
		try{
		if(db_con == null){
			// Load JDBC driver for MySQL database
			String property = "?zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false"; // Use to avoid 0000-00-00 time stamp Exception
            Class.forName("com.mysql.jdbc.Driver");
            // Establish connection using Driver Manager
			db_con = DriverManager.getConnection(Texts.SQL_DB_STRING+property, Texts.SQL_USERNAME,  Texts.SQL_PASSWORD);
		}
		}catch(Exception ex ){
            Logger.console(Texts.SERVER_SESSION_ID,"Exception in  DB connect . Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
    		Logger.error(Texts.SERVER_SESSION_ID, "Exception in  DB connect . Reason:  " + ex.getMessage(), null, null, null);
		}
		
		return db_con;
	}
	
	
	//################################################################################################
	//									 | SQL  QUERIES  | 
	//################################################################################################
	
	
         /**
     * **********************************************
     * Authenticate API user 
     * @param  [String] - apikey
     * @param  [String] - ip
     * @return [JSONObject]
     * **********************************************
     */
    public static JSONObject  authenticate_api_user(String apikey , String ip){
    	try{
    		return new JSONObject().put("code", "00");
		    
		 }catch(Exception ex){
			Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    	Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray() );
		 }
    }
        
	//###############
	//    LIST
	//###############    
    

    
    /**
     * **********************************************
     * List all existing events 
     *
     * @return [JSONObject]
     * **********************************************
     */
    public static JSONObject  list_events(){
    	try{
    		JSONArray params = new JSONArray();
		    String sqlQuery = " SELECT * FROM  tbl_events ORDER BY date_created DESC LIMIT 15";
		    return  abst_select(sqlQuery,params);
		    
		 }catch(Exception ex){
			Logger.console(Texts.SERVER_SESSION_ID,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    	Logger.error(Texts.SERVER_SESSION_ID, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray() );
		 }
    }
    
   
    
        /**
     * **********************************************
     * List all existing users 
     * @return [JSONObject]
     * **********************************************
     */
    public static JSONObject  list_users(){
    	try{
    		JSONArray params = new JSONArray();
		    String sqlQuery = " SELECT * FROM  tbl_user";
		    return  abst_select(sqlQuery,params);
		    
		 }catch(Exception ex){
			Logger.console(Texts.SERVER_SESSION_ID,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    	Logger.error(Texts.SERVER_SESSION_ID, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray() );
		 }
    }
    
   
    

    

   

    
  
 

	
	//################################################################################################
	//								       ABSTRACT CRUD METHODS 
	//################################################################################################
	
	

    

	/**
	 * **********************************************
	 * OverLoaded Abstract SQL Select   Method 
	 * @param  [String]    - sqlString
	 * @param  [JSONArray] - toBind
	 * @return [JSONObject]
	 * **********************************************
	 */
    public static JSONObject abst_select(String sqlString , JSONArray toBind) {
    	
    	
        // Initialize connection in case does not exist 
      	 if(get_connection() == null)
      		 return reply(Texts.DB_ERROR , "Failed to connect to SQL Database", new JSONArray());
      	 	ResultSet rs = null;
          try {
          	
         	  //Declare connection, statement objects 
              PreparedStatement statement = null;
              
              //initialize prepareStatement instance
              statement = db_con.prepareStatement(sqlString);

    
              //build SQL value statement
              if( toBind != null ){
	              for(int i=0 ; i < toBind.length() ; i++){	
	              	 statement.setString(i+1, toBind.get(i).toString() );
	              }
              }
              

   
              //Select and Fetch data
              rs = statement.executeQuery();
              JSONArray data = null;
              data = Convertor.convertResultSetIntoJSON(rs);
            
              if (data.length() != 0) {
                  
                  return reply(Texts.DB_SUCCESS , "Record successfully Fetched" , data );
              } else {
                  return reply(Texts.DB_SUCCESS , "No Record found" , new JSONArray());
              }
              
              
          } catch (Exception e) {
              Logger.console(null,"Unable to Fetch Records. Reason : " + e.getMessage(), Texts.FLAG_SYS_ERROR);
              Logger.error(null, "Unable to Fetch Records. Reason : " +e.getMessage(), null, null, null);
              return reply(Texts.DB_ERROR , "Exception occured on MySQL DatabaseLayer. Please Check the System log." , new JSONArray());
          }
          finally {
              close(rs);
          }
        
    }
    
   
    /**
     * **********************************************
     * Overloaded Abstract SQL Insert  Method 
     * @param [String] - sqlString
	 * @param  [JSONArray] - toBind
     * @return [JSONObject]
     * **********************************************
     */
    public  static JSONObject abst_insert(String sqlString , JSONArray toBind) {
    	
    	 if(get_connection() == null)
    		 return reply(Texts.DB_ERROR , "Failed to connect to SQL Database", new JSONArray());

        try {
        	
       	 	//declare connection, statement objects 
            PreparedStatement statement = null;
            
            //initialize prepareStatement instance
            statement = db_con.prepareStatement(sqlString);
           
            //build SQL value statement
            if( toBind != null ){

	            for(int i=0 ; i < toBind.length(); i++){	
	            	 statement.setString(i+1,  toBind.get(i).toString());
	            }
            }
            
            //insert and verify action 
            if (statement.executeUpdate() > 0) {
            	return reply(Texts.DB_SUCCESS , "Record successfully inserted" , new JSONArray());
            }

        } catch (SQLException e) {
            Logger.console(null,"Unable to Insert Record. Reason : " + e.getMessage(), Texts.FLAG_SYS_ERROR);
            Logger.error(null, "Unable to Insert Record. Reason : " +e.getMessage(), null, null, null);
            return reply(Texts.DB_ERROR , "Exception occured on MySQL DatabaseLayer. Please Check the System log.", new JSONArray());
        }
        
        return reply(Texts.DB_FAIL , "Failure in inserting records "+sqlString, new JSONArray());
		
    }
    
    
    
    
    
	/**
	 * **********************************************
	 * Overloaded Abstract SQL Update  Method 
	 * @param  [String] - sqlString
	 * @param  [JSONArray] - toBind
	 * @return [JSONObject]
	 * **********************************************
	 */
    public static JSONObject abst_update(String sqlString , JSONArray toBind) {
    	
    // Initialize connection in case does not exist 
   	 if(get_connection() == null)
   		 return reply(Texts.DB_ERROR , "Failed to connect to SQL Database", new JSONArray());

       try {
       	
      	   //Declare connection, statement objects 
           PreparedStatement statement = null;
           
           //initialize prepareStatement instance
           statement = db_con.prepareStatement(sqlString);

           //build SQL value statement
           if( toBind != null ){
	           for(int i=0 ; i < toBind.length() ; i++){	
	           	 statement.setString(i+1,  toBind.get(i).toString());
	           }
           }
           
           //Update and verify action 
           if (statement.executeUpdate() > 0) {
           	return reply(Texts.DB_SUCCESS , "Record successfully Updated" , new JSONArray());
           }

       } catch (SQLException e) {
           Logger.console(null,"Unable to Update Record. Reason : " + e.getMessage(), Texts.FLAG_SYS_ERROR);
           Logger.error(null, "Unable to Update Record. Reason : " +e.getMessage(), null, null, null);
           return reply(Texts.DB_ERROR , "Exception occured on MySQL DatabaseLayer. Please Check the System log.", new  JSONArray());
       }
       
       return reply(Texts.DB_FAIL , "Failure in Updating records. No record(s) matches the criteria. ", new JSONArray());
    }
    


    

	/**
	 * **********************************************
	 * Overloaded Abstract SQL Delete  Method 
	 * @param  [String] - sqlString
	 * @param  [JSONArray]   - toBind
	 * @return [JSONObject]
	 * **********************************************
	 */
    public static JSONObject abst_delete(String sqlString , JSONArray toBind) {
    	
    // Initialize connection in case does not exist 
   	 if(get_connection() == null)
   		 return reply(Texts.DB_ERROR , "Failed to connect to SQL Database", new JSONArray());

       try {
       	
      	   //Declare connection, statement objects 
           PreparedStatement statement = null;
           
           //initialize prepareStatement instance
           statement = db_con.prepareStatement(sqlString);

           //build SQL value statement
           if( toBind != null ){
	           for(int i=0 ; i < toBind.length() ; i++){	
	           	 statement.setString(i+1,  toBind.get(i).toString());
	           }
           }
           
           //Update and verify action 
           if (statement.executeUpdate() > 0) {
           	return reply(Texts.DB_SUCCESS , "Record successfully Deleted" , new JSONArray());
           }

       } catch (SQLException e) {
           Logger.console(null,"Unable to Delete Record. Reason : " + e.getMessage(), Texts.FLAG_SYS_ERROR);
           Logger.error(null, "Unable to Delete Record. Reason : " +e.getMessage(), null, null, null);
           return reply(Texts.DB_ERROR , "Exception occured on MySQL DatabaseLayer. Please Check the System log.", new  JSONArray());
       }
       
       return reply(Texts.DB_FAIL , "Failure in Deleting records. No record(s) matches the criteria ", new JSONArray());
    }
    

    

	
	//################################################################################################
	//								              AUXILIARIES 
	//################################################################################################
	
    /***********************************************
     * Module used to render all output of this class
     * @param  [String]     - code
     * @param  [String]     - msg
     * @param  [JSONArray]  - data
     * @return [JSONObject]
     ***********************************************
     */
    private static JSONObject reply(String code , String msg , JSONArray data)
    {   
    	if(data == null )
    		data = new JSONArray();
    	JSONObject res = new JSONObject();
    	res.put("code", code);
    	res.put("msg", msg);
    	res.put("data",data);
		return res;
    }
    
  /**
   * **********************************************
   * Module use to set placeholders <?> in the 
   * query string 
   * @param  [int]  - count
   * @return [JSONObject]
   * **********************************************
   */
    private static String  set_sql_placeHolder(int count )
    {   
    	String res = "";
    	String delimiter=",";
    	 for( int i = 0 ; i < count  ; i++){
  		   res  = res + "?";
  		   if(i != (count -1) ){
  			   res = res + delimiter;
  		   }
  	   }
		return res;
    }



    /**
     * **********************************************
     * Module to get all values in an associative 
     * array ignoring their keys
     * @param params
     * @param [JSONObject] - params 
     * @param [JSONArray]       - elements
     * @return[JSONArray]
     * **********************************************
     */
    private static JSONArray  get_array_values_toList(JSONObject params, JSONArray elements )
    { 
    	JSONArray res = new JSONArray();
 
    	 for(int i = 0 ; i < elements.length() ; i++  ){
    		 if(!params.isNull( elements.get(i).toString() ))
    			 res.put(params.getString( elements.get(i).toString() ));
    	 }
    	
		return res;
    }
    


    /**
     * **********************************************
     * Module to get all fields in the update change string 
     * @param params
     * @param [String] - changes
     * @return[JSONArray]
     * **********************************************
     */
    private static JSONArray  get_update_fields(JSONObject  changes)
    { 
    	return Auxiliary. getAllKeys_json_array(changes);
    }
    
    
    /**
     * **********************************************
     * Module to get all fields in the update change string 
     * @param params
     * @param [String] - changes
     * @return[JSONArray]
     * **********************************************
     */
    private static String   convert_update_changes_to_str(JSONObject changes)
    { 
    	String str = "";
    	for(int i = 0; i<changes.names().length(); i++){
    		String key = changes.names().getString(i);
    		str += " "+key+" = '"+changes.get(key)+"' ";
    		if(i != (changes.names().length())-1 )
    			str +=",";
    	}

    	return str;
   }
    
    

    /**
     * **********************************************
     * Module to get all fields in the update change string 
     * @param params
     * @param [String] - changes
     * @return[JSONArray]
     * **********************************************
     */
    private static JSONObject  is_field_mutable(JSONArray mutable_fields, JSONArray provided_fields )
    { 
    	JSONObject mutable_fields_cpy = mutable_fields.toJSONObject(mutable_fields);
  
    	for( int i = 0 ; i< provided_fields.length(); i++  ){
    		if( !mutable_fields_cpy.has(provided_fields.getString(i).toString().trim()))
    			return reply(Texts.DB_ERROR,"field "+provided_fields.get(i)+" is not nutable. ", new JSONArray());
    	}

    	return reply(Texts.DB_SUCCESS," All fields provided are mutable. ", new JSONArray());
    }
    
    

    /**
     * **********************************************
     * Module to check that db connectivity exist
     * @param [void] 
     * @return[Boolean]
     * **********************************************
     */
    public  static boolean  is_db_connectivity_exist( )
    { 
    	try {
			return db_con.isValid(20);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		Logger.console(null,"Unable to Insert Record. Reason : " + e.getMessage(), Texts.FLAG_SYS_ERROR);
	       Logger.error(null, "Unable to Insert Record. Reason : " +e.getMessage(), null, null, null);
		}
     	catch( NullPointerException e){
    		// NullAny other Exception 
    		 Logger.error(null, "DB Module does not exist  : " +e.getMessage(), null, null, null);
    	}
    	catch(Exception e){
    		Logger.error(null, " Error in is_db_connectivity_exist " +e.getMessage(), null, null, null);
    	}
    	return false;
    }
    
    /**
     * **********************************************
     * Module to get all fields in the update change string 
     * @param params
     * @param [String] - changes
     * @return[JSONArray]
     * **********************************************
     */
    public static void  reuse_or_open_db_connectivity( )
    { 
    	try{
	    	if( !is_db_connectivity_exist( )){
	    		 db_con = null;
	    		 get_connection();
	    	}
    	}catch(Exception e){
    		Logger.error(null, " Error in is_db_connectivity_exist " +e.getMessage(), null, null, null);
    	}
    	return;
    	
    }

    
    
   /**
    * **********************************************
    * Close the Result Set connection 
    * @param   [ResultSet] - rs
    * @return  [void]
    * **********************************************
    */
   public static void close(ResultSet rs) {
       try {
           if (rs != null) {
               rs.close();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }


   /**
    * **********************************************
    * check_user_login
    * Query done to check if the user exist 
    * @param  [JSONObject] - login_params (phoneNumber-Pin)
    * @return [JSONObject]
    * **********************************************
    */
	public static JSONObject check_user_login(JSONObject login_params) {
		// TODO Auto-generated method stub
		try{
	    	JSONArray params = new JSONArray();
	    	
	    	params.put(login_params.getString("password"));
	    	params.put(login_params.getString("userId"));
	    	
	    	JSONObject res = abst_select(" SELECT * FROM  tbl_user  WHERE password = ? and userId = ? ",params);
	    	
	    	if ( res.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS )) {
	    		
	    		 if(res.getJSONArray("data").length() > 0){
	    		
	    			 return  reply( Texts.DB_SUCCESS,"Login successful.", res.getJSONArray("data") );
	    		 
	    		 } else return  reply( Texts.CODE_FAILURE,"Login not successful. UserId or password not valid", res.getJSONArray("data") );
	    		 
	    	 }
	    			 
	    	}catch(Exception ex){
	    		 Logger.console(null, ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    		 Logger.error(null,ex.getMessage(), null, null, null);
	    		 return  reply( Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.", new JSONArray() );
	    	}
	    	return  reply( Texts.DB_FAIL,"Login not successful. ", new JSONArray() );
	}



	/**
    * ***********************************************************
    * register_user
    * Query done to register user 
    * @param  [JSONObject] - signup_params 
    * (firstName-lastName-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject register_user(JSONObject signup_params) {
		// TODO Auto-generated method stub
		try {
	    	JSONArray params = new JSONArray();
	    	
	    	params.put("name");
	    	params.put("email");
	    	params.put("userId");
	    	params.put("password");
	    	params.put("phoneNumber");
                params.put("date_created");
	    	
	    	JSONObject res = Parsor.analyseInput( params , signup_params, false );
	    	
	    	if( !res.getString("code").equals("00") ){
	    	
	    		return reply("01",res.getString("msg"),new JSONArray());
	    	
	    	}
	    	
	    	String columnName  = params.join(" , ").replace("\"","");
	    	String placeHolder = set_sql_placeHolder(params.length());
	    	
	    	String queryString = "INSERT INTO tbl_user ( "+columnName+") VALUES ( "+placeHolder+" ) ";
	    	
	    	return  abst_insert(queryString ,get_array_values_toList( signup_params, params ));
	    	
	    	}catch(Exception ex){
	    		Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    		Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
	    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
	    	}	
	}

        
                	/**
    * ***********************************************************
    * update_user
    * Query done to register user 
    * @param  [JSONObject] - signup_params 
    * (firstName-lastName-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject update_user(String userId,JSONObject user_params) {
		// TODO Auto-generated method stub
		try {
                JSONArray columns = get_update_fields(user_params);

                    JSONArray mutable_columns =new JSONArray();
                    mutable_columns.put("password");
                    mutable_columns.put("phoneNumber");
                    mutable_columns.put("email");
                    mutable_columns.put("name");
                   

                    if(columns.length() == 0)
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer.No  changes provided ",new JSONArray());

                    JSONObject chk_mutable_res = is_field_mutable(mutable_columns, columns );

                    if(!chk_mutable_res.getString("code" ).equals(Texts.DB_SUCCESS) )
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. [ "+chk_mutable_res.getString("msg")+" ] ",new JSONArray());

                    String str_changes = convert_update_changes_to_str(user_params);
                    JSONArray params = new JSONArray();
                    params.put(userId);
                    return  abst_update(" UPDATE tbl_user SET "+str_changes+"  WHERE userId = ? ",params);

                            }catch(Exception ex){
                                    Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                                    Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                                    return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                            }	
                    }


   
        
        
        
        	/**
    * ***********************************************************
    * post_event
    * Query done to register user 
    * @param  [JSONObject] - signup_params 
    * (firstName-lastName-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject post_event(JSONObject event_params) {
		// TODO Auto-generated method stub
		try {
	    	JSONArray params = new JSONArray();
	    	
	    	params.put("title");
	    	params.put("venue");
	    	params.put("time");
	    	params.put("date");
	    	params.put("des");
                params.put("date_created");
	    	
	    	JSONObject res = Parsor.analyseInput( params ,event_params, false );
	    	
	    	if( !res.getString("code").equals("00") ){
	    	
	    		return reply("01",res.getString("msg"),new JSONArray());
	    	
	    	}
	    	
	    	String columnName  = params.join(" , ").replace("\"","");
	    	String placeHolder = set_sql_placeHolder(params.length());
	    	
	    	String queryString = "INSERT INTO tbl_events ( "+columnName+") VALUES ( "+placeHolder+" ) ";
	    	
	    	return  abst_insert(queryString ,get_array_values_toList( event_params, params ));
	    	
	    	}catch(Exception ex){
	    		Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    		Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
	    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
	    	}	
	}

        
        
        
        
        public static JSONObject delete_user(JSONObject save_params) {
		
		try {
	    	
			JSONArray params = new JSONArray();
	    	
                        params.put("userId");
	    	
                        JSONObject res = Parsor.analyseInput( params , save_params, false );

                        if( !res.getString("code").equals("00") ){

                                return reply("01",res.getString("msg"),new JSONArray());
	    	
                        }
	    	
	    
                        String queryString = " DELETE from tbl_user WHERE userId = ? ";
                        //JSONArray param=new JSONArray().put(save_params.getString("userId"));
                        
                        return  abst_delete(queryString ,get_array_values_toList( save_params, params ));

                    }
                catch(Exception ex){
                            Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                            Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                    }	
	}
        
        
        public static JSONObject delete_events(JSONObject save_params) {
		
		try {
	    	
			JSONArray params = new JSONArray();
	    	
                        params.put("title");
	    	
                        JSONObject res = Parsor.analyseInput( params , save_params, false );

                        if( !res.getString("code").equals("00") ){

                                return reply("01",res.getString("msg"),new JSONArray());
	    	
                        }
	    	
	    	
                        String queryString = "DELETE from tbl_events WHERE title  = ? ";

                        return  abst_delete(queryString ,get_array_values_toList( save_params, params ));

                    }
                catch(Exception ex){
                            Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                            Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                    }	
	}

        
           	/**
    * ***********************************************************
    * update_event
    * Query done to register user 
    * @param  [JSONObject] - signup_params 
    * (firstName-lastName-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject update_event(String title,JSONObject event_params) {
		// TODO Auto-generated method stub
		try {
                JSONArray columns = get_update_fields(event_params);

                    JSONArray mutable_columns =new JSONArray();
                    mutable_columns.put("venue");
                    mutable_columns.put("des");
                    mutable_columns.put("date");
                    mutable_columns.put("time");
                   

                    if(columns.length() == 0)
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer.No  changes provided ",new JSONArray());

                    JSONObject chk_mutable_res = is_field_mutable(mutable_columns, columns );

                    if(!chk_mutable_res.getString("code" ).equals(Texts.DB_SUCCESS) )
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. [ "+chk_mutable_res.getString("msg")+" ] ",new JSONArray());

                    String str_changes = convert_update_changes_to_str(event_params);
                    JSONArray params = new JSONArray();
                    params.put(title);
                    return  abst_update(" UPDATE tbl_events SET "+str_changes+"  WHERE title = ? ",params);

                            }catch(Exception ex){
                                    Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                                    Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                                    return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                            }	
                    }

        
        
        
        /// ADs Feature
        
             	/**
    * ***********************************************************
    * post_ads
    * Query done to register user 
    * @param  [JSONObject] - ad_params 
    * (title-price-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject post_ad(JSONObject ad_params) {
		// TODO Auto-generated method stub
		try {
	    	JSONArray params = new JSONArray();
	    	
	    	params.put("title");
	    	params.put("price");
	    	params.put("des");
	    	params.put("phone_number");
	    	params.put("image");
                params.put("category");
                params.put("date_created");
	    	
	    	JSONObject res = Parsor.analyseInput( params ,ad_params, false );
	    	
	    	if( !res.getString("code").equals("00") ){
	    	
	    		return reply("01",res.getString("msg"),new JSONArray());
	    	
	    	}
	    	
	    	String columnName  = params.join(" , ").replace("\"","");
	    	String placeHolder = set_sql_placeHolder(params.length());
	    	
	    	String queryString = "INSERT INTO tbl_ads ( "+columnName+") VALUES ( "+placeHolder+" ) ";
	    	
	    	return  abst_insert(queryString ,get_array_values_toList( ad_params, params ));
	    	
	    	}catch(Exception ex){
	    		Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    		Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
	    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
	    	}	
	}

        
        
                 	/**
    * ***********************************************************
    * post_ads
    * Query done to register user 
    * @param  [JSONObject] - ad_params 
    * (title-price-phoneNumber-pin-secretQuestion-answer)
    * @return [JSONObject]
    * ***********************************************************
    */
	
	public static JSONObject search_add(JSONObject ad_params) {
		// TODO Auto-generated method stub
		try {
	    	JSONArray params = new JSONArray();
	    	
	    	params.put("title");
	    	params.put("category");
	    
	    	
	    	JSONObject res = Parsor.analyseInput( params ,ad_params, false );
	    	
	    	if( !res.getString("code").equals("00") ){
	    	
	    		return reply("01",res.getString("msg"),new JSONArray());
	    	
	    	}
                
                String category=ad_params.getString("category");
                JSONObject re;
                if(category.equalsIgnoreCase("all")){
                    JSONArray param = new JSONArray();
                    param.put(ad_params.getString("title"));
                    re = abst_select(" SELECT * FROM  tbl_ads  WHERE title = ? LIMIT 10",param);
                    System.out.println("Came to default");
                    System.out.print(re);
                }
                else{
                    JSONArray param = new JSONArray();
                    param.put(ad_params.getString("title"));
                    param.put(ad_params.getString("category"));
                    re = abst_select(" SELECT * FROM  tbl_ads  WHERE title = ? OR category = ? LIMIT 10",param);
                    System.out.print(re);
                }
	    	
                if ( re.getString("code").equalsIgnoreCase(Texts.CODE_SUCCESS )) {
	    		
	    		 if(re.getJSONArray("data").length() > 0){
	    		
	    			 return  reply( Texts.DB_SUCCESS,"Ad Found", re.getJSONArray("data") );
	    		 
	    		 } else return  reply( Texts.CODE_FAILURE,"No Ad", re.getJSONArray("data") );
	    		 
	    	 }
	    	
	    	
	    	
	    	}catch(Exception ex){
	    		Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    		Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
	    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
	    	}	
                
              return  reply( Texts.DB_FAIL,"No Ad Found ", new JSONArray() );
        }
        
        
         
    /**
     * **********************************************
     * List all existing ads 
     *
     * @return [JSONObject]
     * **********************************************
     */
    public static JSONObject  list_ads(){
    	try{
    		JSONArray params = new JSONArray();
		    String sqlQuery = " SELECT * FROM  tbl_ads ORDER BY date_created DESC LIMIT 15";
		    return  abst_select(sqlQuery,params);
		    
		 }catch(Exception ex){
			Logger.console(Texts.SERVER_SESSION_ID,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
	    	Logger.error(Texts.SERVER_SESSION_ID, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
    		return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray() );
		 }
    }
    
    
    
     
        public static JSONObject delete_ads(JSONObject save_params) {
		
		try {
	    	
			JSONArray params = new JSONArray();
	    	
                        params.put("title");
	    	
                        JSONObject res = Parsor.analyseInput( params , save_params, false );

                        if( !res.getString("code").equals("00") ){

                                return reply("01",res.getString("msg"),new JSONArray());
	    	
                        }
	    	
	    	
                        String queryString = "DELETE from tbl_ads WHERE title  = ? ";

                        return  abst_delete(queryString ,get_array_values_toList( save_params, params ));

                    }
                catch(Exception ex){
                            Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                            Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                    }	
	}
        
        
        
        
        
    public static JSONObject update_ad(String id,JSONObject ad_params) {
		// TODO Auto-generated method stub
		try {
                JSONArray columns = get_update_fields(ad_params);

                    JSONArray params =new JSONArray();
                    params.put("id");
                    params.put("title");
                    params.put("price");
                    params.put("des");
                    params.put("phone_number");
                    params.put("image");
                    params.put("category");
                    params.put("date_created");
                   

                    if(columns.length() == 0)
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer.No  changes provided ",new JSONArray());

                    JSONObject chk_mutable_res = is_field_mutable(params, columns );

                    if(!chk_mutable_res.getString("code" ).equals(Texts.DB_SUCCESS) )
                            return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. [ "+chk_mutable_res.getString("msg")+" ] ",new JSONArray());

                    String str_changes = convert_update_changes_to_str(ad_params);
                    JSONArray param = new JSONArray();
                    param.put(id);
                    return  abst_update(" UPDATE tbl_ads SET "+str_changes+"  WHERE id = ? ",param);

                            }catch(Exception ex){
                                    Logger.console(null,"Exception occured in SQL Layer. Reason:  " + ex.getMessage(), Texts.FLAG_ERROR_LOG);
                                    Logger.error(null, "Exception occured in SQL Layer. Reason:  " + ex.getMessage(), null, null, null);
                                    return reply(Texts.DB_ERROR,"Exception occured on MySQL DatabaseLayer. Please Check the System log.",new JSONArray());
                            }	
                    }


    
    

   
	
	
    
	//################################################################################################
	//								      | End SQLDBLayer Class |  
	//################################################################################################
		
}
