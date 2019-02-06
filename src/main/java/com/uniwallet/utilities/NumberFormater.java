package com.uniwallet.utilities;

/**
 * Class to format phone numbers based on a given country code */

public class NumberFormater {

	public static String formatNumberGH(String msisdn){
		char bg = msisdn.charAt(0);
		if (bg == '0' && msisdn.length() == 10){
			msisdn = "233"+msisdn.substring(1);
		}
		
		if(bg == '0' && msisdn.length() > 12 ){
			msisdn = msisdn.substring(2);
		}
		
		if (bg == '+' && msisdn.length() == 13){
			msisdn = msisdn.substring(1);
		}
		
		return msisdn;
	}
	
	private static boolean verifyFormatGH(String msisdn){
		
		return (msisdn.startsWith("233") && msisdn.length() == 12);
	}
	
	
	public static boolean veriyFormat(String number,String countryCode){
		
		if (countryCode.equalsIgnoreCase("GH"))
			
			return verifyFormatGH(number);
		
		else return false;
	}
	
	/**
	 * Method to format number basde on the country code
	 * @param String phone number to format
	 * @param String country code
	 * @return String formated number if country code is valid*/
	public static String format(String number,String countryCode){
		if(countryCode.compareToIgnoreCase("gh") == 0)
			return formatNumberGH(number);
		
		return number;
	}
}
