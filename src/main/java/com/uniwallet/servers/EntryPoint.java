package com.uniwallet.servers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.json.JSONObject;

import com.uniwallet.utilities.Auxiliary;
import com.uniwallet.utilities.Security;
import com.uniwallet.utilities.Texts;


@Path("/")
public class EntryPoint {

	
	
	//##################################### (  GH  ) ######################################################
    @Context
    private String apiSessionID;
    ArrayList<String> listStrings;

   
    
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("text/plain")
    public String handle_api_v1(@Context HttpServletRequest client, InputStream incomingData) {
        
    	StringBuilder rawInputData = new StringBuilder();
        
    	try {
        
    		BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            
    		String line = null;
            
    		while ((line = in.readLine()) != null) {
            
    			rawInputData.append(line);
            
    		}
        } catch (Exception e) {
            
        	return Auxiliary.reply(Texts.CODE_ERROR,Texts.SERVICE_UNAVAILABLE).toString();
        }
        
        String data = rawInputData.toString();
        
        APITask task = new APITask(client);
        
        String task2 = task.handleRequest(data);
        
        JSONObject checker = new JSONObject(task2);
        
      
   
        
        System.out.println("Sending response : " + task2);
        
        return task2;
        
    
    }
    
    
    //######################################  END GH ##############################################
    
}
