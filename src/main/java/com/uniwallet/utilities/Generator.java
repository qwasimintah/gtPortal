package com.uniwallet.utilities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.UUID;

public class Generator {
	
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
	 *Method to generate a time stamp
	 * @return String 12 character string based on yymmddhhmmss 
	 * */
	public static String generateTimeStamp(){
		DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
		return  df.format(new Date());
	}
	
	/**
	* Method Generate the Day of year (integer representing the day between 1-366)
	* @param [Date] - date
    * @return [String] day of year
	* @by Tunde
	*/
	public static String generateDayOfYear( Date date){
		//Get Day of year (integer representing the day between 1-366)
	  	Calendar cal = new GregorianCalendar();
	  	cal.setTime(date); // Give your own date
	  	
	  	String dateOfYear = String.format("%03d", cal.get(Calendar.DAY_OF_YEAR));
	  	/*
	  	System.out.println("Today is " + new Date());
	  	System.out.println("And is the " + dateOfYear + "day");
	  	*/
	  	return dateOfYear;
	}
	

	/**
	 *Method to generate a time stamp with ISO Express savings and loan format
	 * @return String 12 character string based on yymmddhhmmss 
	 * */
	public static String generateDateAndTime(){
		DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
		return  df.format(new Date());
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
			//IWritter.consoleLog(e.getMessage(),Texts.SYS_DBG);
			return null;
		}
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
	     * autoGenerate a string base on a seed range
	     *
	     * @param length the length of the expected string
	     * @return random string
	     */
	    public static String getRandomString(int length) {
	        String allowedChars = "0123456789";

	        if (allowedChars.trim().length() == 0 || length <= 0) {
	            throw new IllegalArgumentException("Please provide valid input");
	        }

	        Random rand = new Random();
	        StringBuilder sb = new StringBuilder();

	        for (int i = 0; i < length; i++) {
	            sb.append(allowedChars.charAt(rand.nextInt(allowedChars.length())));
	        }
	        return sb.toString();
	    }

	    
	    /**
	     * autoGenerate an alphanumeric string base on a seed range
	     * @param length the length of the expected string
	     * @return random string
	     */
	    public static String getRandomAlphaNumString(int length) {
	        String allowedChars = "0123456789abcdefghijklmnopqrstuvzxyz.#$&ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	        if (allowedChars.trim().length() == 0 || length <= 0) {
	            throw new IllegalArgumentException("Please provide valid input");
	        }

	        Random rand = new Random();
	        StringBuilder sb = new StringBuilder();

	        for (int i = 0; i < length; i++) {
	            sb.append(allowedChars.charAt(rand.nextInt(allowedChars.length())));
	        }
	        return sb.toString();
	    }
	    
	    
	    
	    /*
	     * Generate random integer between 1-100
	     * 
	     */

		public static int generateRandomInt() {
			
			Random rand = new Random();

			int  n = rand.nextInt(3) + 1;
			
			return n;
		}

	

}
