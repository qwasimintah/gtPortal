package com.uniwallet.utilities;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Texts;



public class Auxiliary {

	public static String className = "Auxiliary"; 
	
	
	/** Get the current line number.
	 * @return string - Current line number.
	 */
	public static String getLineNumber() {
	    return String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber());
	}
	
	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
	 * @return method name
	 */
	public static String getMethodName(final int depth)
	{
	  final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	  return ste[ste.length - 1 - depth].getMethodName(); //Thank you Tom Tresansky
	}
	
	
	
	/**
	 *Method to generate 4 digits PIN 
	 * @param void
	 * @return String 4 digits pin 
	 * */
	public static String generatePin(){
		String PIN;
		int rand = (int)(Math.random()*9000)+1000;
		PIN = String.valueOf(rand);
		return PIN;
	}
	
	/**
	 *Method to generate character string 
	 * @param String type of generator UUID | SecureRandom 
	 * @return String 4 character string 
	 * */
	public static String generateString(String type){
		String generated = "";
		//Default generator 
		if (type.compareToIgnoreCase("UUID") == 0 || type.isEmpty()  || type == null){
			String uuid = UUID.randomUUID().toString().replace("-", "");
			generated = uuid;
		}
		if(type.compareToIgnoreCase("SecureRandom") == 0){
			SecureRandom random = new SecureRandom();
			generated =  new  BigInteger(130, random).toString(32);
		}
		return generated;
	}
	
	/**
	 *Method to generate transaction ID  
	 * @return String 13 character string randomly generated 
	 * */
	public static String generateTransactionID(){
		SecureRandom random = new SecureRandom();
		return  new  BigInteger(130, random).toString(32).substring(0, 12);
	}
	
	
	
	/**
	 *Method to generate transaction ID  
	 * @return String 13 character string randomly generated 
	 * */
	public static String generateSessionID(){

		return  "AS"+generateString("UUID").toUpperCase().substring(0, 12);
	}
	
	/**
	 *Method to generate a time stamp
	 * @return String 12 character string based on yymmddhhmmss 
	 * */
	public static String generateTimeStamp(){
		DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
		return  df.format(new Date());
	}
	
	/**
	 *Method to generate a time stamp
	 * @return String 12 character string based on yymmddhhmmss 
	 * */
	public static String generateTimeStamp(String format){
		try{
		DateFormat df = new SimpleDateFormat(format);
		return  df.format(new Date());
		}catch(Exception ex)
		{
			return generateTimeStamp();
		}
	}
	
	/**
	 *Method to generate a time stamp
	 * @return String based on the time stamp in nano seconds 
	 * */
	public static String generateTimeStampMillis(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	/**
	 *Method to generate hashed version of a string  
	 * @param String string to application 
	 * @return String hashed value of inputed string using SHA-256 encryption algorithm 
	 * */
	public static String hash(String stringToEncrypt){
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.update(stringToEncrypt.getBytes());
			return msgDigest.digest().toString();
		} catch (NoSuchAlgorithmException e) {
			Logger.console(Texts.SERVER_SESSION_ID,e.getMessage(),Texts.FLAG_ERROR_LOG);
			return null;
		}
	}
	
	/**
	 *Method to mask an account number based on the   
	 * @param String string to application 
	 * @return String hashed value of inputed string using SHA-256 encryption algorithm 
	 * */
	public static String obfuscate( String account , String housePattern )
	{
		switch(housePattern)
		{
		case "ASL":
			//5xxxxx5
			account =  maskNumber(account, "#####xxxxx#####");
		case "MTN":
			break;
		case "AIRTEL":
			break;
		case "TIGO":
			break;
		case "PBL":
			account =  maskNumber(account, "###xxxxxx####");
		default :
			//3xxxxxx4
			account =  maskNumber(account, "###xxxxxx####"); 
			break;
		}
		
		
		return account;
		
	}
	
	
	
	   /**
	   * @param number The number in plain format
	   * @param mask The  mask pattern. 
	   *    Use # to include the digit from the position. 
	   *    Use x to mask the digit at that position.
	   *    Any other char will be inserted.
	   *
	   * @return The masked card number
	   */
	   public static String maskNumber(String number, String mask) {
	 
	      int index = 0;
	      StringBuilder masked = new StringBuilder();
	      for (int i = 0; i < mask.length(); i++) {
	         char c = mask.charAt(i);
	         if (c == '#') {
	            masked.append(number.charAt(index));
	            index++;
	         } else if (c == 'x') {
	            masked.append(c);
	            index++;
	         } else {
	            masked.append(c);
	         }
	      }
	      return masked.toString();
	   }
	


		/**
		 * Method to find if a particular value is in a JSONArray ( not quite Cost effective )
		 * @param val
		 * @param arr
		 * @return Boolean 
		 */
		public static boolean is_value_in_JSONArray(String val , JSONArray arr)
		{
			if(arr.length() > 0)
			return arr.toJSONObject(arr).has(val) ? true : false;
			return false;
		}
		
		
		
		/**
		 * 
		 * @param isJson
		 * @return
		 */
		@SuppressWarnings("unused")
		public static boolean isJson(String jsonEncoded) {
		    try  {
		        JSONObject obj = new JSONObject(jsonEncoded);
		        return true;
		    }  catch (Exception e) {
		        return false;
		    }
		}
		
		
		/**
		 * Method to reply 
		 * @param String code
		 * @param String msg
		 * @return JSONObject 
		 */
		public static JSONObject  reply(String code, String msg){
			return reply(code,  msg,new JSONArray());
		}
		
		/**
		 * Method to reply 
		 * @param String code
		 * @param String msg
		 * @param JSONObject msg
		 * @return JSONObject 
		 */
		public static JSONObject  reply(String code, String msg,JSONObject data){
			return new JSONObject().put("code", code).put("msg", msg).put("data", data);
		}
		
		/**
		 * Method to reply 
		 * @param String code
		 * @param String msg
		 * @param JSONObject msg
		 * @return JSONObject 
		 */
		public static JSONObject  reply(String code, String msg,JSONArray data){
			return new JSONObject().put("code", code).put("msg", msg).put("data", data);
		}
		
		/**
		 * 
		 * @param className
		 * @return
		 */
		public static boolean isClass(String className) {
		    try  {
		    	
		        Class.forName(className);
		        return true;
		    }  catch (final ClassNotFoundException e) {
		        return false;
		    }
		}
		
		/**
		 * 
		 * @param className
		 * @return
		 */

	public static String capitalize(final String line) {
		try {
			return Character.toUpperCase(line.charAt(0)) + line.substring(1);
		} catch (final Exception e) {
			return line;
		}

	}

	/**
	 * 
	 * @param className
	 * @return
	 */

	public static String removeWiteSpace(final String line) {
		try {
			return line.replaceAll("\\\\n", "").replace("\"", "")
					.replace("\\", "");
		} catch (final Exception e) {
			return line;
		}

	}

	
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public static String renderSocket(String sessionID,String line) {
		try {
			return line;
		}finally{
			try {
				// # Mark API User Access
				Logger.access(sessionID,"response", new JSONArray().put(line));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}	
	
	
	public static String getVariableValue(Class<?> c,String fieldName){
		 
        String result  = null;
        try {
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            if(f.isAccessible()){
                result = (String) f.get(null);
          
            }else {
                return null;
            }
             
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        return result ;
 
    }
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static  List<String> getVariableValue(Class<?> c){
	try{
		Field[] declaredFields =  c.getDeclaredFields();
		List<String> staticFields = new ArrayList<String>();
		for (Field field : declaredFields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				String[] list = field.toString().split("\\.");
				staticFields.add(list[list.length-1]);
			}
		}
		return staticFields;
		}catch(Exception e ){
		return new ArrayList<String>();
		}
	}
	
	/**
	 * 
	 * @param c
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setVariableValue(Class<?> c, String key, String value){
		String moduleName = "setVariableValue";
	try{
		Field[] declaredFields =  c.getDeclaredFields();
		String field_str =null;
		for (Field field : declaredFields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				String[] list = field.toString().split("\\.");
				if(list.length>0)
					field_str = list[list.length-1];
				if(field_str.equals(key)){
					//System.out.println("Field found ");
					//Logger.console(null, "", logType);
					Object o= new Object() ;
					o = value;
					if(field.getType().toString().toLowerCase().equals("int") )
						field.setInt(field.getType(),(int)Integer.valueOf(value) );
					else if (field.getType().toString().equals("float") )
						field.setFloat(field.getType(),Float.parseFloat(value));
					else if (field.getType().toString().toLowerCase().equals("double") )
						field.setDouble(field.getType(), Double.parseDouble(value) );
					else if(field.getType().toString().toLowerCase().equals("boolean"))
						field.setBoolean(field.getType(), Boolean.parseBoolean(value) );
					else if(field.getType().toString().toLowerCase().equals("long"))
						field.setLong(field.getType(), Long.parseLong(value) );
					else 
						field.set(field.getType().newInstance(), o );

					return true;
				}
			}
		}
		
		
		
		}catch(Exception e ){
			Logger.error(Texts.SERVER_SESSION_ID, e.getMessage(), getLineNumber(), moduleName, className);
		return false;
		}
		return false ;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static List<String> getAllKeys( JSONObject  obj){
		Iterator x = obj.keys();
		List<String> list = new ArrayList<String>();
		
		while (x.hasNext()){
		    String key = (String) x.next();
		    list.add( key);
		}
		
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static JSONArray getAllKeys_json_array(JSONObject  obj){
		Iterator x = obj.keys();
		JSONArray  list = new JSONArray();
		
		while (x.hasNext()){
		    String key = (String) x.next();
		    list.put( key);
		}
		
		return list;
	}

	

	
	
	
}
