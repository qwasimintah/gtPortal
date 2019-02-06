package com.uniwallet.utilities;

import java.awt.List;

import org.json.JSONObject;
import org.json.JSONArray;

public class Parsor {
	
	/**
	 * 
	 * @param elements
	 * @param fields
	 * @param enforce_empty 
	 * @return
	 */
	public static JSONObject  analyseInput( List elements , JSONObject fields, boolean enforce_empty )
	{
		JSONObject res = new JSONObject();
		res.put("code", "00");
		res.put("msg", "All parameters are set.");

		try{
			
			for( int i = 0 ; i < elements.getItemCount() ; i++ ){
				
				if( fields.isNull((elements.getItem(i).toString()) ) )
				{
					res.put("code", "01");
					res.put("msg", String.format("The parameter %s does not exist. Please fill it and Resubmit.",elements.getItem(i).toString() ));
					return  res;
				}else if(fields.get((elements.getItem(i).toString())).toString().trim().isEmpty() && enforce_empty == true){
					res.put("code", "01");
					res.put("msg", String.format("The parameter %s is empty. Please fill it and Resubmit.",elements.getItem(i).toString() ));
					return  res;
				}
			}
		}catch( Exception ex) {
			res.put("code", "02");
			res.put("msg", String.format("Exception Occured while validating params." ));
			Logger.error(Texts.SERVER_SESSION_ID,"Execption on Utitity analyseInput, Reason :" + ex.getMessage(),null,null,null);
			return res;
		}
		
		return res;
	}
	
	
	
	
	/**
	 * 
	 * @param elements
	 * @param fields
	 * @param enforce_empty 
	 * @return
	 */
	public static JSONObject  analyseInput( JSONArray elements , JSONObject fields, boolean enforce_empty )
	{
		JSONObject res = new JSONObject();
		res.put("code", "00");
		res.put("msg", "All parameters are set.");

		try{
			
			for( int i = 0 ; i < elements.length() ; i++ ){
				
				if( fields.isNull((elements.get(i).toString()) ) )
				{
					res.put("code", "01");
					res.put("msg", String.format("The parameter %s does not exist. Please fill it and Resubmit.",elements.get(i).toString() ));
					return  res;
				}else if(fields.get((elements.get(i).toString())).toString().trim().isEmpty() && enforce_empty == true){
					res.put("code", "01");
					res.put("msg", String.format("The parameter %s is empty. Please fill it and Resubmit.",elements.get(i).toString() ));
					return  res;
				}
			}
		}catch( Exception ex) {
			res.put("code", "02");
			res.put("msg", String.format("Exception Occured while validating params." ));
			Logger.error(Texts.SERVER_SESSION_ID,"Execption on Utitity analyseInput, Reason :" + ex.getMessage(),null,null,null);
			return res;
		}
		
		return res;
	}
	
	
	public static boolean parseApiVersion(String str){
		str = str.toUpperCase();
		if(str.matches("V[0-9]+_[0-9]+"))
			return true;
		return false;
		
	}
	
    public static boolean parseAmount(String str){

		if(str.trim().matches("^([0-9]{1,9}((\\.[0-9]{1,2})?))$"))
			return true;
		return false;
		
	}
    
    public static boolean parseApiResponse(String sessionID, String str){
    	try{
    	JSONObject resposne = new JSONObject(str);
    	boolean is_valid = !(resposne.isNull("code") || resposne.isNull("msg") || resposne.isNull("data"));
    	if(!is_valid)
    	Logger.error(sessionID, "API response does not align with standard", Auxiliary.getLineNumber(), "parseApiResponse", "Parsor");
    	return is_valid;
    	}catch(Exception e ) {
    		Logger.error(sessionID, "API response does not align with standard", Auxiliary.getLineNumber(), "parseApiResponse", "Parsor");
    		return false ;
    	}
	}
    
    
    public static boolean parseStandardPinCode(String pinCode){
    	if(pinCode.trim().matches("^([0-9]{4}$"))
			return true;
		return false;
	}
	
	//Todo  
}
