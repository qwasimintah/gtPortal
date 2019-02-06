package com.uniwallet.utilities;

import java.io.File;

/**
 * Class holding all common texts and Codes being used throughout uniWallet*/
public class Texts {
	
	//SYSTEM STRINGS
	public static String SERVER = "Engine";
	public static long   PID = -1;
	public static String ADMIN_MSISDN = "233265456003";
	final public static String SERVICE_UNAVAILABLE = "Service currently unavailable. Kindly try again in a moment";
	final public static String SERVICE_UNAUTHORIZED = "Invalid apikey provided. Kindly check your credential and resubmit.";

	//TRANSACTION RESPONSE CODE 00 - 06
	final public static String CODE_SUCCESS = "00";
	final public static String CODE_FAILURE = "01";
	final public static String CODE_ERROR = "02";
	final public static String CODE_ASYNC = "03"; // to specify internally if call will be responded later or not
	final public static String CODE_INVALID_ACCESS = "05"; // UNAUTHORIZED ACCESS 
	
	//DATABASE RESPONSE CODES
	final public static String CODE_DB_SUCCESS = "00";
	final public static String CODE_DB_ERROR = "01";

	// SERVER SESSION 
	final public static String SERVER_SESSION_ID = "AS000000000001";
	final public static String SERVER_ALERT_SESSION_ID    = "AS000000000002";
	final public static String SERVER_SECURITY_SESSION_ID = "AS000000000003";
	public static int SERVER_NUMBER_TREADS = 500;
	
	//EXTERNAL SERVICES 
	public static long SERVER_ALERTING_SYS_PID = -1;
	public static long SERVER_SECURITY_SYS_PID = -1;
	
	public static long SERVER_ALERTING_SYS_CYCLE = 1;
	public static long SERVER_SECURITY_SYS_CYCLE = 1; //# Normally should on set to 15 minutes 
	
	// SQL DB
    public static String SQL_USERNAME = "root";
    public static String SQL_PASSWORD = "";
    public static String SQL_DB_STRING = "jdbc:mysql://localhost/svc_gtPortal";
    
    // DATABASE  LAYER RESPONSE  CODE 
    public static String DB_SUCCESS = "00";
    public static String DB_FAIL    = "01";
    public static String DB_ERROR   = "02";
	

	//ENVIRONMENT RUNNING FLAGS (INT)
    final public static int SYS_PRD = 100;
    final public static int SYS_DBG = 200;
    final public static int SYS_DEV = 300;
    public static int SYS_FLAG = 0;
	public static boolean RUNNING_BACKGROUD = false;
	
	
	//PRODUCTION PORTS
	public static int PORT_ENGINE = 5056;//12001;//5055
	public static int PORT_DB = 6970;

	
	//DEV  PORTS
	//public static int PORT_DB = 12001;
	//public static int PORT_ENGINE = 12000;;
	
	//LOG FLAGS
	final public static String FLAG_INFO_LOG = "INFO";
	final public static String FLAG_ERROR_LOG = "ERROR";
	final public static String FLAG_WARNING_LOG = "WARNING";
	final public static String FLAG_SYS_EVENT = "EEVENT";
	final public static String FLAG_SYS_ERROR = "EERROR";
	
	//CREDENTIAL 
	final public static String UNIWALLET_APIKEY    = "aed9a1f57de24797add3cf8978f19ebf";
	final public static String UNIWALLET_INSTID    = "56b34f13a088442264efb957";
	final public static String UNIWALLET_SHORTNAME = "UNIBANK";
	
	//PATH 
	final public static String DIR_ROOT = (new File(System.getProperty("user.dir")).getParent());
	public static String DIR_RELATIVE_FILE_UPLOAD  = "logs"+File.separator+"upload";
	public static String DIR_RELATIVE_SETTINGS  = "settings"+File.separator+"system"+File.separator+"config.json";
	public static String DIR_FIELD_RELATIVE_SETTINGS  = "settings"+File.separator+"system"+File.separator+"servicesConfig.json";
	public static String DIR_FILE_UPLOAD  = DIR_ROOT+File.separator+DIR_RELATIVE_FILE_UPLOAD;
	public static String DIR_SETTINGS  = DIR_ROOT+File.separator+DIR_RELATIVE_SETTINGS;
	public static String DIR_SERVICES_SETTINGS  = DIR_ROOT+File.separator+DIR_FIELD_RELATIVE_SETTINGS;
	
	// FUSION ENDPOINT 
	final public static String FUSION_THIRDPARTY_ENDPOINT = "https://fs.nsano.com:5000/api/fusion/tp/";
	//public static String FUSION_THIRDPARTY_ENDPOINT = "http://127.0.0.1:9000/test/service/";
	public static String CHECKSUM_API_URL  ="http://127.0.0.1:5505/file/get_checksum";
		
	//SMS  
	public static String SMS_AUTHENTICATION_KEY  = "dW5pYmFua2doOmhtdndvT0F3"; //UniB Key 
	public static String SMS_SOURCE  = "SMILE";
	public static String SMS_COUNTRY  = "GH";
	
	//MAIL 
	public static String MAIL_DEFAULT_SENDER= "uniWallet";
	public static String MAIL_DEFAULT_TITLE= "uniWallet";
	public static String MAIL_API_URL= "http://nsano.com/ /index.php";
	
	//SMARTDOG 
	public static String SMARTDOG_URL= "http://127.0.0.1:9000/apps/smartdog/";
	public static String SMARTDOG_APP_KEY= "GHEKDNN4DFKJE44DFV7SLAOZ";
	
	
	// EXTRA 
	public static String URL_BANK_CLIENT= "http://162.13.170.21:9001/mtn_sdp/proxy.php?req_url=http://172.30.200.122/unibank/unib.php";
	public static String URL_PORTAL_CLIENT= "http://162.13.170.21:9001/mtn_sdp/proxy.php?req_url=http://172.30.200.122/fusion_portal/services/ussd_router.php";
	public static String URL_FUSION_SERVER= "https://fs.nsano.com:5000" ;
	public static String ussd_router= "http://162.13.170.21:7007/fusion_portal/services/ussd_router.php";
	

}
