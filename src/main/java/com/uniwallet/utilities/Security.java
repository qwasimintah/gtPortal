package com.uniwallet.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.uniwallet.utilities.Texts;

public class Security {
	
	public static String className = "Securty";
	public static String compute_files_checksum(String filename ) {
		MessageDigest md;
		byte[] digest = null;
		try {
			md = MessageDigest.getInstance("MD5");
	
		try (InputStream is = Files.newInputStream(Paths.get("file.txt"));
		     DigestInputStream dis = new DigestInputStream(is, md)) 
		{
		  /* Read decorated stream (dis) to EOF as normal... */
		}
		digest = md.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		return String.valueOf(digest);
	}
	
	
	public static String compute_str_checksum(String str_content){

		MessageDigest messageDigest;
		String encryptedString = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
	
		messageDigest.update(str_content.getBytes());
		encryptedString = new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	
	/**
	 *Method to generate hashed version of a string  
	 * @param String string to application 
	 * @return String hashed value of inputed string using SHA-256 encryption algorithm 
	 * */
	public static String hash(String stringToEncrypt){
		String moduleName = "hash";
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.update(stringToEncrypt.getBytes());
			return msgDigest.digest().toString();
		} catch (NoSuchAlgorithmException e) {
			Logger.error(null, e.getMessage(), Auxiliary.getLineNumber(), moduleName, className);
			return null;
		}
	}
	
	public static String encrypt(String input, String key){
		  byte[] crypted = null;
		  try{
		    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.ENCRYPT_MODE, skey);
		      crypted = cipher.doFinal(input.getBytes());
		    }catch(Exception e){
		    	System.out.println(e.toString());
		    }
		    return new String(Base64.encodeBase64(crypted));
		}

		public static String decrypt(String input, String key){
		    byte[] output = null;
		    try{
		      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.DECRYPT_MODE, skey);
		      output = cipher.doFinal(Base64.decodeBase64(input));
		    }catch(Exception e){
		      System.out.println(e.toString());
		    }
		    return new String(output);
		}
		
		public static void main(String[] args) {
		  String key = "1234567891234567";
		  String data = "Jim";
		  
		  System.out.println(Security.encrypt(data, key));
		  //System.out.println(Security.decrypt(Security.encrypt(data, key), key));    
		}	
	

}


