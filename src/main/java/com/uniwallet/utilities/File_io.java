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


public class File_io {
	private String sessionID  = null  ; 
	/**
	 * **************************************************************
	 * Default constructor 
	 * @param  void 
	 * @return 
	 * **************************************************************
	 * 
	 */
	public File_io(String session_id){
		this.sessionID = session_id; 
	}
	
	
	/**
	 * **************************************************************
	 * Write content data in filePath 
	 * @param String - filePath
	 * @param String - content 
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public void writeData( String filePath,  String content  ){
		
		//# Create new file and write  data
		try {
			File file   = new File(filePath);
			file.getParentFile().mkdirs();	
			file.createNewFile();
			Files.write(Paths.get(filePath), content.getBytes() , StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (Exception e) {
				console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
		}	
	}
	
	
	
	/**
	 * **************************************************************
	 * Append content data in filePath 
	 * @param String - filePath
	 * @param String - content 
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public void appendData( String filePath,  String content  ){
		try{
		File file   = new File(filePath);
		file.getParentFile().mkdirs();	
	
		//# Log into the file and 
		if(file.exists()){	
			//# Append to the file
			try {			
					Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
					console(sessionID,"Could not read log file: ",Texts.FLAG_ERROR_LOG);
			}
		}else{
			//# Create new file and write  data
			try {
					file.createNewFile();
					Files.write(Paths.get(filePath), content.getBytes() , StandardOpenOption.CREATE);
				} catch (IOException e) {
					console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
			}
		}
		} catch (Exception e) {
			console(sessionID, e.getMessage(),Texts.FLAG_ERROR_LOG);
	}	
	}
	

	
	
	/**
	 * **************************************************************
	 * Get content data in filePath 
	 * @param String - filePath
	 * @param String - content 
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public String getData( String filePath ){
		String content = null;
		try {
			if(new File(filePath).exists())
				content = new String(Files.readAllBytes(Paths.get(filePath)));
			else{
				console(sessionID,"Could not open  file: "+filePath,Texts.FLAG_ERROR_LOG);
			}
		}catch (Exception e) {
				console(sessionID,"Could not read log file: "+e.getMessage(),Texts.FLAG_ERROR_LOG);
		}
		
		return content;
	}
	
	
	
	/**
	 * **************************************************************
	 * Get content data in filePath 
	 * @param String - filePath
	 * @param String - content 
	 * @return void 
	 * **************************************************************
	 * 
	 */
	public JSONArray getDataArray2d( String filePath){
		String content = "";
		try {
			if(new File(filePath).exists())
				content = new String(Files.readAllBytes(Paths.get(filePath)));
			else{
				console(sessionID,"Could not open  file: "+filePath,Texts.FLAG_ERROR_LOG);
			}
		}catch (Exception e) {
				console(sessionID,"Could not read log file: "+e.getMessage(),Texts.FLAG_ERROR_LOG);
		}
		if(content.trim().equals("") )
			return new JSONArray();
		return  new JSONArray(content.split(("\\r?\\n")));
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
	private static void console(String sessionID, String tolog, String logType  ){
		if(logType == null)
			logType = Texts.FLAG_INFO_LOG;
		String tolog_str = "[ "+getServerTime()+" ]-[ "+sessionID+" ]-"+"[ "+logType+" ] :: "+tolog.toString();
		System.out.println(tolog_str);
	}
	
	

	/**
	 * **************************************************************
	 * Method to get the Server a time stamp 
	 * @param  void 
	 * @return String - 12 character string based on yyyy/mm/dd hh:mm:ss 
	 * **************************************************************
	 * 
	 */
	public static String getServerTime(){
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
