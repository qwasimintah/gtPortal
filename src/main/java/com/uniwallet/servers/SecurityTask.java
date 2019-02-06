package com.uniwallet.servers;
/**
 * *******************************************************************************
 * 						##  SECURITY TASK   Class  ##
 *  
 * This Class   handle all security routines to ensure no breach on the system
 * transaction 
 * @date     2016/01/29
 * @access   Public 
 * @category Server  
 * @version  v_1_0
 * @author  Etio@ 
 * 
 * *******************************************************************************
 */


/**
 * @Dependencies
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uniwallet.api.Version_1_0;
import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Mail;
import com.uniwallet.utilities.Texts;
import com.uniwallet.utilities.Parsor;


public class SecurityTask implements Runnable {
	/**
	*################################################################################################
	*									 | API SECURITY CLASS | 
	*################################################################################################
	*
	*/
	private String apiSessionID= Texts.SERVER_SECURITY_SESSION_ID; 
    private BufferedReader in ;	
    private long cycle = 60000;
    private Mail mail ;
	
	/**
	 * ******************************************
	 * Overloaded Constructor
	 * @param [String[] ] - args
	 * @throw IOException
	 * @return [void]
	 * ******************************************
	 * 
	 */
	public SecurityTask(long _cycle){

	 String module_name = "constructor";
	 try {
		this.cycle = _cycle;
		mail = new Mail(apiSessionID);

		} catch (Exception e) {
			Logger.console(apiSessionID,"IO error: "+e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
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
	public void handleRequest(){
		String module_name = "handleRequest";	

		try
		{	
			//response object to be written
			 while(true){
				// Test DB connectivity before proceeding
				 SQLDBLayer.reuse_or_open_db_connectivity( );
				 if(!SQLDBLayer.is_db_connectivity_exist( )){
					 Logger.console(apiSessionID,"Server could not connect to DB ... will try again after cycle  ",Texts.FLAG_SYS_ERROR);
					 Thread.sleep(cycle); 
					 continue;
				 }
				 
				 Logger.console(apiSessionID,"In Security Task ",Texts.FLAG_INFO_LOG); 
				 //#check for all decline 
				 Thread.sleep(cycle);
			 }
			
		}catch(Exception e){
			Logger.console(apiSessionID,e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
			try {
				Thread.sleep(cycle);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 handleRequest();
		}
	}
	
	

	
	/**
	 * ******************************************
	 * Process  ___ Level Security  
	 * @param  [void] 
	 * @return [void]
	 * ******************************************
	 * 
	 */
	public boolean process_level_security() {
		
		String module_name = "process_____level_security";
		try{
		Logger.console(apiSessionID,"Processing  level security ... ",Texts.FLAG_INFO_LOG);

		Logger.console(apiSessionID,"Processing   level security completed ... ",Texts.FLAG_INFO_LOG);
		}catch(Exception e){
			Logger.console(apiSessionID,e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
		}
		return true;
	
	}
	
	// Transaction Level security 
	// Engine Level Security
	// Log Level Security 
	// backup level security 
	// suspicious access Level Security 
	// look into management server 
	
	

	
	
	
	
	

	/**
	 * ******************************************
	 * Run Thread 
	 * @param  [void] 
	 * @return [void]
	 * ******************************************
	 * 
	 */
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		handleRequest();
		return;
	}
	

	
	
	/**
	*################################################################################################
	*									 | END SECURITY TASK CLASS | 
	*################################################################################################
	*
	*/
}
