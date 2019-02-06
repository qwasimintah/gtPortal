package com.uniwallet.utilities;

/**
 * *******************************************************************************
 * 						##  Logger  Class  ##
 *  
 * This Class is used to Log all the various all data inputed or output
 * from the system. ie sms, mail , access, event, error ... 
 * 
 * @date     2016/01/26
 * @access   Public 
 * @category Input/Output Stream 
 * @version  v_1_0
 * @author   Etienne ( Etio@ )
 * 
 * *******************************************************************************
 */

/**
 * @Dependencies
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;


public class Logger {
	/**
	 * ################################################################################################
	 * 										| LOGGER CLASS  |
	 * ################################################################################################
	 * 
	 * 
	 */

	private static String rootFolder =  (new File(new File(System.getProperty("user.dir")).getParent())).getParent();;

	/**
	 * **************************************************************
	 * Default constructor 
	 * @param  void 
	 * @return 
	 * **************************************************************
	 * 
	 */
	public Logger(){}
	
	
	/**
	 * **************************************************************
	 * Log System access in the access log : access/yymm/dd.ns  
	 * @param String - sessionID
	 * @param JSONArray - accessDetails
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static void access(String sessionID, String type, JSONArray accessDetails  ){
		
		console(sessionID,"Writing in Access log... ",Texts.FLAG_SYS_EVENT);
		
		//# Set up File Path 		
		DateFormat df     = new SimpleDateFormat("yyMM");
		DateFormat fn     = new SimpleDateFormat("dd");
		String yearMonth  = df.format(new Date()); 
		String fileName   = fn.format(new Date())+".ns";
		String logDir     = rootFolder+File.separator+"logs";
		String dirName    = logDir+File.separator+"access"+File.separator+yearMonth;
		
		String path = dirName+File.separator+fileName;
		File file   = new File(path);
		file.getParentFile().mkdirs();	
		
		//# Inflate to log params with time stamps and reference point 
		JSONArray fields = new JSONArray();
		fields.put(generateTimeStamp());
		fields.put(sessionID);
		fields.put(type);
		fields.put(accessDetails.join(" | "));
				
		//# Stringify all the parameters in the JSON array 
		String tolog =  fields.join(" | ");
		tolog = Auxiliary.removeWiteSpace(tolog);
	
		
		//# Log into the file and 
		if(file.exists()){
		
			//# Append to the file
			try {
				tolog = String.format("%s%n", tolog);
				Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
			}catch (IOException e) {
					console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and write  data
			try {
					tolog = String.format("%s%n", tolog);
					file.createNewFile();
					Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
				} catch (IOException e) {
							console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}
					
	}
	
	

	/**
	 * **************************************************************
	 * Log system errors in the access log : errors/errors.ns  
	 * @param sessionID
	 * @param tolog
	 * @param logType
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static void error(String sessionID,  String error_details_str , String line , String moduleName , String className){
		
		console(sessionID,"Writing in Error log... ",Texts.FLAG_SYS_EVENT);
		
		//# Set up File path 
		String fileName   = "errors.ns";
		String logDir     = rootFolder+File.separator+"logs";
		String dirName    =  logDir+File.separator+"errors";
		
		String path = dirName+File.separator+fileName;
		File file = new File(path);
		
		//# Inflate to log params with time stamps and reference point 
		JSONArray fields = new JSONArray();
		fields.put(generateTimeStamp());
		fields.put(sessionID);
		fields.put( error_details_str);
		fields.put(line);
		fields.put(moduleName);
		fields.put(className);
		
		//# Stringify all the parameters in the JSON array 
		String tolog = fields.join(" | ");
		
		
		//# Log into the file and 
		if(file.exists()){
			//# Append to the file
			try {
				tolog = String.format("%s%n", tolog);
				Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
				
			} catch (IOException e) {
				console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and add data
			try {
				tolog = String.format("%s%n", tolog);
				file.createNewFile();
				Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
			} catch (IOException e) {
				console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}
		
		
	}
	
	

	/**
	 * **************************************************************
	 * Log system event in the event log : events/events.ns  
	 * @param sessionID
	 * @param tolog
	 * @param logType
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static void event(String sessionID,  String event_details_str){
		
		//console(sessionID,"Writing in Error log... ",Texts.FLAG_SYS_EVENT);
		
		//# Set up File path 
		String fileName   = "activities.ns";
		String logDir     = rootFolder+File.separator+"logs";
		String dirName    =  logDir+File.separator+"events";
		
		String path = dirName+File.separator+fileName;
		File file = new File(path);
		
		//# Inflate to log params with time stamps and reference point 
		JSONArray fields = new JSONArray();
		fields.put(generateTimeStamp());
		fields.put(sessionID);
		fields.put( event_details_str);

		//# Stringify all the parameters in the JSON array 
		String tolog = fields.join(" | ");
		
		
		//# Log into the file and 
		if(file.exists()){
			//# Append to the file
			try {
				tolog = String.format("%s%n", tolog);
				Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
			} catch (IOException e) {
				//console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and add data
			try {
				tolog = String.format("%s%n", tolog);
				file.createNewFile();
				Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
			} catch (IOException e) {
				//console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}
		
		
	}

	/**
	 * **************************************************************
	 * Log  sms Notification in the sms log : via/sms/yymm/dd.ns  
	 * @param String - sessionID
	 * @param JSONArray - accessDetails
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static  void  sms(String sessionID,  JSONArray accessDetails  ){
		
		console(sessionID,"Writing in SMS Notification log... ",Texts.FLAG_SYS_EVENT);
		
		//# Set up File Path 		
		DateFormat df     = new SimpleDateFormat("yyMM");
		DateFormat fn     = new SimpleDateFormat("dd");
		String yearMonth  = df.format(new Date()); 
		String fileName   = fn.format(new Date())+".ns";
		String logDir     = rootFolder+File.separator+"logs";
		String dirName    = logDir+File.separator+"via"+File.separator+"sms"+File.separator+yearMonth;
		
		String path = dirName+File.separator+fileName;
		File file   = new File(path);
		file.getParentFile().mkdirs();	
		
		//# Inflate to log params with time stamps and reference point 
		JSONArray fields = new JSONArray();
		fields.put(generateTimeStamp());
		fields.put(sessionID);
		fields.put(accessDetails.join(" | "));
				
		//# Stringify all the parameters in the JSON array 
		String tolog = fields.join(" | ");
		tolog = Auxiliary.removeWiteSpace(tolog);
				
		//# Log into the file and 
		if(file.exists()){
		
			//# Append to the file
			try {			
				tolog = String.format("%s%n", tolog);
				Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
			}catch (IOException e) {
				console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and write  data
			try {
				tolog = String.format("%s%n", tolog);
					file.createNewFile();
					Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
				} catch (IOException e) {
					console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}				
	}
	
	
	
	/**
	 * **************************************************************
	 * Log  mail Notification in the mail log : via/mail/yymm/dd.ns 
	 * @param String - sessionID
	 * @param JSONArray - accessDetails
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static void mail(String sessionID,  JSONArray accessDetails  ){
		
		console(sessionID,"Writing in Mail Notification  log... ",Texts.FLAG_SYS_EVENT);
		
		//# Set up File Path 		
		DateFormat df     = new SimpleDateFormat("yyMM");
		DateFormat fn     = new SimpleDateFormat("dd");
		String yearMonth  = df.format(new Date()); 
		String fileName   = fn.format(new Date())+".ns";
		String logDir     = rootFolder+File.separator+"logs";
		String dirName    = logDir+File.separator+"via"+File.separator+"mail"+File.separator+yearMonth;
		
		String path = dirName+File.separator+fileName;
		File file   = new File(path);
		file.getParentFile().mkdirs();	
		
		//# Inflate to log params with time stamps and reference point 
		JSONArray fields = new JSONArray();
		fields.put(generateTimeStamp());
		fields.put(sessionID);
		fields.put(accessDetails.join(" | "));
				
		//# Stringify all the parameters in the JSON array 
		String tolog = fields.join(" | ");
		tolog = Auxiliary.removeWiteSpace(tolog);

				
		//# Log into the file and 
		if(file.exists()){
		
			//# Append to the file
			try {			
				tolog = String.format("%s%n", tolog);
				Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
			}catch (IOException e) {
				console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and write  data
			try {
					tolog = String.format("%s%n", tolog);
					file.createNewFile();
					Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
				} catch (IOException e) {
					console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}
					
	}
	
	
	

	/**
	 * **************************************************************
	 * Log  smartdog Notification in  log : via/smartdog/yymm/dd.ns 
	 * @param String - sessionID
	 * @param JSONArray - accessDetails
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static  void smartdog(final String sessionID,  final JSONArray accessDetails  ){
		
		console(sessionID,"Writing in Smartdog Notification  log... ",Texts.FLAG_SYS_EVENT);
		final String moduleName = "console";
		final String className = "Logger";
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

			try {
				//# Set up File Path 		
				DateFormat df     = new SimpleDateFormat("yyMM");
				DateFormat fn     = new SimpleDateFormat("dd");
				String yearMonth  = df.format(new Date()); 
				String fileName   = fn.format(new Date())+".ns";
				String logDir     = rootFolder+File.separator+"logs";
				String dirName    = logDir+File.separator+"via"+File.separator+"smartdog"+File.separator+yearMonth;
				
				String path = dirName+File.separator+fileName;
				File file   = new File(path);
				file.getParentFile().mkdirs();	
				
				//# Inflate to log params with time stamps and reference point 
				JSONArray fields = new JSONArray();
				fields.put(generateTimeStamp());
				fields.put(sessionID);
				fields.put(accessDetails.join(" | "));
						
				//# Stringify all the parameters in the JSON array 
				String tolog = fields.join(" | ");
				tolog = Auxiliary.removeWiteSpace(tolog);
						
				//# Log into the file and 
				if(file.exists()){
				
					//# Append to the file
					try {			
						tolog = String.format("%s%n", tolog);
						Files.write(Paths.get(path), tolog.getBytes(), StandardOpenOption.APPEND,StandardOpenOption.WRITE);
					}catch (IOException e) {
							console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
					}
				}else{
					//# Create new file and write  data
					try {
						tolog = String.format("%s%n", tolog);
							file.createNewFile();
							Files.write(Paths.get(path),tolog.getBytes() , StandardOpenOption.CREATE);
						} catch (IOException e) {
									console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
					}
				}	

				} catch (Exception e) {
					Logger.error(sessionID, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
				}
			}

		});
		t.start();
			
	}
	
	
	
	/**
	 * **************************************************************
	 * Log event, error , warning , info ... unto the console 
	 * @param sessionID
	 * @param tolog
	 * @param logType
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public static void console(final String sessionID, final String tolog,  final String logType  ){
		final String moduleName = "console";
		final String className = "Logger";
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

			try {
				String logType_cp =logType ;
				if(logType_cp == null)
					logType_cp = Texts.FLAG_INFO_LOG;
				String tolog_str = "[ "+generateTimeStamp()+" ]-[ "+sessionID+" ]-"+"[ "+logType_cp+" ] :: "+tolog;
				System.out.println(tolog_str);
				System.out.println();
				event( sessionID, tolog_str);
				} catch (Exception e) {
					Logger.error(sessionID, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
				}
			}

		});
		t.start();
		
	}
	
	

	/**
	 * **************************************************************
	 * Method to generate a time stamp 
	 * @param  void 
	 * @return String - 12 character string based on yyyy/mm/dd hh:mm:ss 
	 * **************************************************************
	 * 
	 */
	public static String generateTimeStamp(){
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return  df.format(new Date());
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * ################################################################################################
	 * 										|END LOGGER CLASS  |
	 * ################################################################################################
	 */
}
