package com.uniwallet.servers;
/**
 * *******************************************************************************
 * 						##  ALERT TASK   Class  ##
 *  
 * This Class handles all the Alert tasks
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
import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Mail;
import com.uniwallet.utilities.Texts;


public class AlertTask implements Runnable {
	/**
	*################################################################################################
	*									 | API ALERT CLASS | 
	*################################################################################################
	*
	*/
	private String apiSessionID= Texts.SERVER_ALERT_SESSION_ID; 
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
	public AlertTask(long _cycle){

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
				 
				 SQLDBLayer.reuse_or_open_db_connectivity( );
				 if(!SQLDBLayer.is_db_connectivity_exist( )){
					 Logger.console(apiSessionID,"Server could not connect to DB ... will try again after cycle  ",Texts.FLAG_SYS_ERROR);
					 Thread.sleep(cycle); 
					 continue;
				 }
				 Logger.console(apiSessionID,"In Alert Task ",Texts.FLAG_INFO_LOG); 
				 //#check for all decline 
				 Thread.sleep(cycle);
			 }
			
		}catch(Exception e){
			Logger.console(apiSessionID,e.getMessage(),Texts.FLAG_SYS_ERROR);
			Logger.error(apiSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , this.getClass().getName() );
			
		}
	}
	
		
	

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
	*									 | END ALERT TASK CLASS | 
	*################################################################################################
	*
	*/
}
