package com.uniwallet.userTest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.uniwallet.controllers.User;
import com.uniwallet.models.SQLDBLayer;
import com.uniwallet.servers.AlertTask;
import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.HTTPClient;
import com.uniwallet.utilities.Logger;
import com.uniwallet.utilities.Mail;
import com.uniwallet.utilities.Security;
import com.uniwallet.utilities.Texts;

public class UnitTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Test on" );
		
		//######  MODEL 
		
		//list 
		/*
		System.out.println(SQLDBLayer.list_mno());
		System.out.println(SQLDBLayer.list_user_type());
		System.out.println(SQLDBLayer.list_access_level_type());
		System.out.println(SQLDBLayer.list_receiving_house_status_type());
		System.out.println(SQLDBLayer.list_transaction_status_type());
		*/
		
		//Get 
		//System.out.println(SQLDBLayer.get_transaction("MT", "MT83172349566"," tt.* "));
		//Update 
		//System.out.println(SQLDBLayer.mark_transaction_successful("25455"));
		//System.out.println(SQLDBLayer.mark_transaction_ambigous("25455"));
		//System.out.println(SQLDBLayer.mark_transaction_non_ambigous("25455"));
		
		// Add
		
	 /*	JSONObject params = new JSONObject();
    	params.put("transID","25455");
    	params.put("receivingHouse","1");
    	params.put("receivingUserID","2345667723");
    	params.put("receivingHouseType","1");
    	params.put("metadataID","23434455656");
    	params.put("bulk_id","MT31285887707");
    	params.put("amount","0.1");
    	params.put("status","2");
    	params.put("transChecksum","chunk");
		System.out.println(SQLDBLayer.add_transaction(params));

		// authenticate 
		System.out.println(SQLDBLayer.is_api_user_exist("0657015ea18b7233bb2841dd0499cdf4f2fce88c", "12"));
		System.out.println(SQLDBLayer.authenticate_api_user("0657015ea18b7233bb2841dd0499cdf4f2fce88c", "33"));
		
		///UPDATE 
		//System.out.println(SQLDBLayer. mark_bulk_as_process_initiated("MT1"));
		
		
		//######## CONTROLLER USER 

	
		
		//String tag = "/api/fusion/mc/5faa1be2c85a4c5ca4cd2b6f8065bb13";
		
		
		
		//# Format Request Field 
			//Set the request fields according to the Fusion API

		//# Encripting Data 
			//Pass data to be sent in the json object for encryption
		/*HashMap<String, String > params_json = new HashMap<String, String > ();
		JSONObject serviceDetails = new JSONObject();

		serviceDetails.put("amount", "1");
		serviceDetails.put("msisdn", "233265456003");
		serviceDetails.put("network", "AIRTEL");
		serviceDetails.put("api_key", "55d6e54fda0c8");
		serviceDetails.put("institution_id", "150821084607");
		
		params_json.put("kuwaita", "kulipa_mkoba");
		params_json.put("msisdn", "233246785296");
		params_json.put("author", "UNIBANK");
		params_json.put("sid", "BAE1440177409943");
		params_json.put("sender", "0400142026919");
		params_json.put("sendingHouse", "UNIBANK");
		params_json.put("msisdn", "233265456003");
		params_json.put("channel", "ussd");
		params_json.put("amount", "1");
		params_json.put("serviceDetails", serviceDetails.toString());
		
		params_json.put("type", "A2S");
		
		System.out.println( params_json) ;
		
		JSONObject airtime = fs.send2FsuionAPI(tag, params_json);
		
		System.out.print( airtime) ;
		
*/		
		
		
		
		//HMM hmm = new HMM("0657015ea18b7233bb2841dd0499cdf4f2fce88c");
		
		//String URL = "http://192.168.1.124:8180/test/url";
		//String URL = "http://192.168.1.104:8080/hmm";
		
		//System.out.print( hmm.hmm(URL, "250", "femi") );
		
		
		/*
		JSONObject params = new JSONObject(); 
		
		//params.put("firstName", "Tunde");
		//params.put("lastName", "DeoGratias");
		params.put("phoneNumber", "0241872853");
		params.put("pin", "1234");
		params.put("newQuestion", "ok?");
		params.put("newAnswer", "yes");
		//params.put("secretQuestion", "U tunde?");
		//params.put("answer", "yes yes");
		
		System.out.print( us.change_secret_question(params)) ;
		
		*/
		/*
		params.put("userID", "233265456003");
		params.put("accountno", "4000125362715");
		params.put("pin", "3819");
		System.out.print( bc.account_balance(params).toString() ) ;
		JSONObject params1 = new JSONObject();
		params.put("msisdn", "233279108129");
		System.out.print( bc.account_basic_info(params).toString() ) ;
		*/

		
	}
	
		
}
