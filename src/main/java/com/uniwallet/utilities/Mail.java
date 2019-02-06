package com.uniwallet.utilities;

/**
 * *******************************************************************************
 * 						##  Mail  Class  ##
 *  
 * This Class is used to send mails  
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
import java.util.HashMap;
import org.json.JSONArray;


public class Mail {
	/**
	 * ################################################################################################
	 * 										| MAIL CLASS  |
	 * ################################################################################################
	 * 
	 */
	private String sessionID = null;
	private String title  = Texts.MAIL_DEFAULT_TITLE;
	private String sender  = Texts.MAIL_DEFAULT_SENDER;
	private String className = "Mail";
	

	/**
	 * **************************************************************
	 * Default constructor 
	 * @param  void 
	 * @return 
	 * **************************************************************
	 * 
	 */
	public Mail(String _sessionID){
		this.sessionID = _sessionID;
	}
	
	

	
	public boolean sendMail(String to, String subject ,String message, String senderName ){
		String response = null;
		boolean bl_response = false;
		String module_name = "sendMail";
		String mail_url =  Texts.MAIL_API_URL;
		
		if(senderName == null )
			senderName = Texts.MAIL_DEFAULT_SENDER;
		
		if(subject == null)
			subject = title;
		
		HashMap<String,String> params_to_send = new HashMap<String,String>() ;
		params_to_send.put("key", "c45ssherbdd778");
		params_to_send.put("tag", "send_mail");
		params_to_send.put("rev_email", to);
		params_to_send.put("subject", subject);
		params_to_send.put("msg", message);
		params_to_send.put("senderName", senderName);
		
		try {
			//# TODO logging request 
			Logger.console(sessionID,"Sending mail . @email adrress(es) : "+to+" Request Detail :"+params_to_send.toString(),Texts.FLAG_INFO_LOG);
			response = HTTPClient.sendPost(mail_url, params_to_send, 60);
			if(response.equalsIgnoreCase("ok")){
				bl_response = true;
				Logger.console(sessionID,"Sending mail completed with status ::successful:: account Completed. @sessionID: "+sessionID+" Response from Mail Server : "+response,Texts.FLAG_INFO_LOG);
			}
			else{
				Logger.console(sessionID,"Sending mail completed with status ::failure:: account Completed. @sessionID: "+sessionID+" Response from Mail Server : "+response,Texts.FLAG_WARNING_LOG);
			}
				
		} catch (Exception e) {
			Logger.error(sessionID, "Error while sending mail . Reason:"+ e.getMessage(),  Auxiliary.getLineNumber(), module_name , className);
			Auxiliary.reply(Texts.FLAG_ERROR_LOG, "Exception while sending mail  to "+to+" @ sessionID : "+sessionID);
		}
		
		Logger.mail(sessionID, new JSONArray().put("REQUEST").put(params_to_send.toString()).put("RESPONSE").put(response).put(bl_response)  );
		return bl_response;
	}
	
	
	
	/**
	 * 
	 * 
	 * ################################################################################################
	 * 										|END MAIL CLASS  |
	 * ################################################################################################
	 */
}
