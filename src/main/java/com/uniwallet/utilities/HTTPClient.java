package com.uniwallet.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;



public class HTTPClient {
	
	private static final String USER_AGENT = "FUSION";
	public static int responseCode; 
	
	/**
	 * @methdod to send data via GET method
	 * @param String url
	 * @param Map<String,String> parameters
	 * @param int timeout that specifies how long in seconds it should wait be fore timing out a connection
	 * @retun string server response
	 * */
	public static String sendGet(String url,Map<String,String> parameters, int timeout) throws Exception {
		timeout = timeout*1000;
		String listOfParams = "";
		if(!parameters.isEmpty() && parameters != null){
			listOfParams = "?";
			
			Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry element = (Map.Entry) iterator.next();
				listOfParams +=element.getKey()+"="+URLEncoder.encode((String) element.getValue(),"UTF-8")+"&";
			}
			/*for (Entry<String,String> element : parameters.entrySet()) {
				listOfParams +=element.getKey()+"="+URLEncoder.encode(element.getValue(),"UTF-8");
			}*/
		}
		
		URL obj = new URL(url+listOfParams);
		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setConnectTimeout(timeout);
		// optional default is GET
		con.setRequestMethod("GET");
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setConnectTimeout(20);
		
		responseCode = con.getResponseCode();
		/*System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);*/
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return response.toString();
 
	}
	
 
	/**
	 * @methdod to send data via POST method
	 * @param String url
	 * @param Map<String,String> parameters
	 * @param int timeout that specifies how long in seconds it should wait be fore timing out a connection
	 * @return string server response
	 * */
	public static String sendPost(String url,Map<String,String> parameters, int timeout) throws Exception {
		timeout = timeout*1000;
 
		String listOfParams = "";
		if(!parameters.isEmpty() && parameters != null){
			
			Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry element = (Map.Entry) iterator.next();
				listOfParams +=element.getKey()+"="+URLEncoder.encode((String) element.getValue(),"UTF-8")+"&";
			}
		}
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//Set the connection  timeout
		con.setConnectTimeout(timeout);
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(listOfParams);
		
		//System.out.println("sending request" + listOfParams);
		
		wr.flush();
		wr.close();
 
		responseCode = con.getResponseCode();
		/*System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + listOfParams);
		System.out.println("Response Code : " + responseCode);*/
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		return response.toString();
 
	}
	
	/**
	 * @methdod to send JSON data via POST method
	 * @param String url
	 * @param JSONObject
	 * @param int timeout that specifies how long it should wait be fore timing out a connection
	 * @retunr string server response
	 * */
	public static String sendJSONPost(String url,JSONObject parameters, int timeout) throws Exception {
		timeout = timeout*1000;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//Set the connection timeout
		con.setConnectTimeout(timeout);
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Content-Type", "application/json");
		
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                System.out.println(parameters.toString());
		wr.writeBytes(parameters.toString());
		wr.flush();
		wr.close();
 
		responseCode = con.getResponseCode();
		/*System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + listOfParams);
		System.out.println("Response Code : " + responseCode);*/
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return response.toString();
	}
	
	/**
	 * @methdod to send JSON data via POST method with extra headers if needed
	 * @param String URL to send request to
	 * @param JSONObject parameters to pass through post data
	 * @param JSONObject extra headers to add to the request
	 * @param int timeout that specifies how long it should wait be fore timing out a connection
	 * @retunr string server response
	 * */
	public static String sendJSONPost(String url,JSONObject parameters,JSONObject headers, int timeout) throws Exception {
		timeout = timeout*1000;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//Set the connection timeout
		con.setConnectTimeout(timeout);
		//Add extra headers if there are some needed
		if(headers.length() > 0){
			Logger.console(null,"new headers to add", Texts.FLAG_INFO_LOG);
			
			JSONArray hNames= headers.names();
			for (int i = 0; i < hNames.length(); i++) {
				Logger.console(null,hNames.getString(i)+" : "+headers.getString(hNames.getString(i)), Texts.FLAG_INFO_LOG);
				con.setRequestProperty(hNames.getString(i), headers.getString(hNames.getString(i)));
			}			
		}
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Cache-Control", "no-cache");
		
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters.toString());
		wr.flush();
		wr.close();
		
		/*System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + listOfParams);
		System.out.println("Response Code : " + responseCode);*/
 
		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//return result string from URL Request
		return response.toString();
	}
	
	/**
	 * @methdod to send XML data via POST method
	 * @param String url
	 * @param JSONObject
	 * @param int timeout that specifies how long it should wait be fore timing out a connection
	 * @retunr string server response
	 * */
	public static String sendXMLPost(String str_url,String parameters, int timeout) throws Exception {
		
		timeout = timeout *1000;
		 // create URL object (endpoint)
        URL url = new URL(str_url);
        InputStreamReader read;
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

        // send request via POST
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setConnectTimeout(timeout);
        // specify the content type. Web-service will reject the request if it is not XML
        httpConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		
		// several more definitions to request
        // get XML String lenth to be sent to the endpoint
        int len = parameters.length();
        
        //rc.setRequestProperty("SOAPAction:", "\"document/http://someurl.com/OfflineBatchSync/Order:CreateOrderHeader\"");
        httpConnection.setRequestProperty("Content-Length", Integer.toString(len));
        httpConnection.setRequestProperty("Connection:", "Keep-Alive");
        httpConnection.connect(); // connect to the http

        //write XML to the server
        OutputStreamWriter outStr = new OutputStreamWriter(httpConnection.getOutputStream());
        outStr.write(parameters, 0, len);
        outStr.flush();

        try {
            read = new InputStreamReader(httpConnection.getInputStream());
        } catch (Exception exception) {
            //if something wrong instead of the output, read the error
            read = new InputStreamReader(httpConnection.getErrorStream());
        }

        //read server response
        StringBuilder sb = new StringBuilder();
        int ch = read.read();
        while (ch != -1) {
            sb.append((char) ch);
            ch = read.read();
        }
        String responseTr = sb.toString();
        return responseTr;
        

	}
	
	

	/**
	 * @methdod to send data via POST method
	 * @param String url
	 * @param String parameters
	 * @param int timeout that specifies how long in seconds it should wait be fore timing out a connection
	 * @return string server response
	 * */
	public static String sendPost2(String url,Map<String,String> parameters, int timeout) throws Exception {
		timeout = timeout*1000;
 
		String listOfParams = "";
		if(!parameters.isEmpty() && parameters != null){
			
			Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry element = (Map.Entry) iterator.next();
				listOfParams +=element.getKey()+"="+URLEncoder.encode((String) element.getValue(),"UTF-8")+"&";
			}
		}
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//Set the connection  timeout
		con.setConnectTimeout(timeout);
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(listOfParams);
		
		//System.out.println("sending request" + listOfParams);
		
		wr.flush();
		wr.close();
 
		responseCode = con.getResponseCode();
		/*System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + listOfParams);
		System.out.println("Response Code : " + responseCode);*/
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		return response.toString();
 
	}
	
	
	
}
