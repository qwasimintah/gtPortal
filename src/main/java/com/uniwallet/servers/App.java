/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Eti@
 */
package com.uniwallet.servers;

import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;

import com.uniwallet.models.LocalDB;
import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Texts;



public class App {
	
	private static boolean is_started = false;
	private static String serverSessionID =Texts.SERVER_SESSION_ID;;
	private static String className = "App";

    public static void main(String[] args) throws Exception {
    	
        //# Inform that Uniwallet Engine in being started 
        if(is_started == false){
    		 	
    		//# Initialize all configurations  setting  for  Uniwallet
    		boolean init_res = init_modules();
    		if(!init_res){
    			System.out.println(":::Execption occured while loading server module.");
    			System.out.println(":::Shutting down server ...");
    			System.exit(52003);
    		}
    		Logger.console(serverSessionID,"-- GTPORTAL Engine  started-- " ,Texts.FLAG_INFO_LOG);
    		 is_started = true;
        }else{
        	
        	System.out.println(":::Execption Server Already started .");
			System.out.println(":::Shutting down server ...");
			System.exit(52004);
        }
    	
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        
        

        Server jettyServer = new Server(Texts.PORT_ENGINE);
        jettyServer.setHandler(context);
        
        
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        //# Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                EntryPoint.class.getCanonicalName() );
        
        try {
            
        
            Logger.console(serverSessionID,"-- GTPORTAL Engine starting-- " ,Texts.FLAG_INFO_LOG);
    		Logger.console(serverSessionID,"-- GTPORTAL Engine Services starting-- " ,Texts.FLAG_INFO_LOG);
	   
    		jettyServer.start();
            jettyServer.join();

        } finally {
            jettyServer.destroy();
        }
    }
    
    
    
    /**
	 * 
	 * @return
	 */
	private static boolean load_settings_config(){
		String module_name = "load_settings_config";
		Logger.console(serverSessionID," Loading server config ",Texts.FLAG_INFO_LOG);
		List<String> all_static_vars =  Auxiliary.getVariableValue(com.uniwallet.utilities.Texts.class);
		
		try {
			JSONObject db_res = LocalDB.getSetting();
			for(int i = 0 ; i < db_res.names().length() ; i++){
				String field = db_res.names().getString(i).toUpperCase().trim();
				if(  all_static_vars.contains(field)){
						String key = String.valueOf(db_res.names().get(i)) ;
						String key_as_in_Texts = key.trim().toUpperCase();
						String value = String.valueOf(db_res.get(key));
						//# Perform reflection on all the static variable in the class Texts 
							Auxiliary.setVariableValue(Texts.class,key_as_in_Texts,value);
						

				}
			}
		all_static_vars =  Auxiliary.getVariableValue(com.uniwallet.utilities.Texts.class);	
			
		}catch (Exception e) {
			Logger.console(serverSessionID,e.getMessage()+" "+e.getCause(),Texts.FLAG_SYS_ERROR);
			Logger.error(serverSessionID,  e.getMessage(),  Auxiliary.getLineNumber(), module_name , className );
			return false;
		}	

		 return true;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private static boolean init_modules(){
		 // Save server process id in settings
		load_settings_config();
		 if( !init_db()){
			 Logger.console(serverSessionID,"Database connection could not be established " ,Texts.FLAG_WARNING_LOG);
			 return false;
		 }
		

		 
		  /*
		 if( !init_alerting_module()){
			 Logger.console(serverSessionID,"Alerting Server could not start " ,Texts.FLAG_WARNING_LOG);
			 return false;
	 	 }
		 
		 if( !init_security_module()){
			 Logger.console(serverSessionID,"Security Server could not start " ,Texts.FLAG_WARNING_LOG);
			 return false;
		 }
	 	 */
		 return true;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private static boolean init_db(){
		Logger.console(serverSessionID,"--Database connection starting-- " ,Texts.FLAG_INFO_LOG);
		try{
			if(SQLDBLayer.get_connection() == null)
				return false;
		}catch(Exception e){
			Logger.console(serverSessionID,e.getMessage() ,Texts.FLAG_INFO_LOG);
			return false;
		}
		return true ;
	}
	
	

	
	
	/**
	 * 
	 * @return
	 */
	private static boolean init_alerting_module(){
		Logger.console(serverSessionID,"--Alerting Server starting-- " ,Texts.FLAG_INFO_LOG);
		final String moduleName = "init_alerting_module";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			try {
				long cycle = Texts.SERVER_ALERTING_SYS_CYCLE >1 ? (Texts.SERVER_ALERTING_SYS_CYCLE * 1000)*60 : 60*1000;
				AlertTask task = new AlertTask(cycle);
			} catch (Exception e) {
					Logger.error(Texts.SERVER_SESSION_ID, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
				}
			}

		});
		t.start();
		long pid = t.getId();
		// TODO save pid in settings 
		return true ;
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	private static boolean init_security_module(){
		Logger.console(serverSessionID,"--Security Server starting-- " ,Texts.FLAG_INFO_LOG);
		final String moduleName = "init_security_module";

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
			try {
				long cycle = Texts.SERVER_SECURITY_SYS_CYCLE >1 ? (Texts.SERVER_SECURITY_SYS_CYCLE * 1000)*60 : 60*1000;
				SecurityTask task = new SecurityTask(cycle);
			} catch (Exception e) {
					Logger.error(Texts.SERVER_SESSION_ID, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
				}
			}

		});
		t.start();
		long pid = t.getId();
		// TODO save pid in settings 
		return true ;
	}
	
}
