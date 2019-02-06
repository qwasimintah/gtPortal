package com.uniwallet.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.uniwallet.utilities.Logger;

public class TCPClient {

	private static String _IP = "127.0.0.1";
	private static int _PORT = Texts.PORT_DB;
	private Socket client;
	
	/**
	 * Creates a TCPClient Class with the default database IP and Port
	 * @exception UnknownHostException if the client could not connect to the ip 
	 * @execption IOException if an error occurred creating the socket
	 * @return void*/
	public TCPClient() throws UnknownHostException,IOException,SocketTimeoutException{
		
		client = new Socket();
		try {
			client.connect(new InetSocketAddress(TCPClient._IP,TCPClient._PORT), 5000);	
		} catch (SocketTimeoutException e) {
			Logger.console(Texts.SERVER_SESSION_ID, e.getMessage(), Texts.FLAG_ERROR_LOG);
		}
		
		Logger.console(Texts.SERVER_SESSION_ID,"Initiating connection to " + TCPClient._IP
                + " on port " + TCPClient._PORT,Texts.FLAG_SYS_EVENT);
	}
	
	/**
	 * Creates a TCPClient Class with custom IP and Port
	 * @param String ip address to connect
	 * @param String port
	 * @exception UnknownHostException if the client could not connect to the ip 
	 * @execption IOException if an error occurred creating the socket
	 * @return void*/
	public TCPClient(String IP, int PORT) throws UnknownHostException,IOException,SocketTimeoutException{
		client = new Socket();
		try {
			client.connect(new InetSocketAddress(IP,PORT), 5000);	
		} catch (SocketTimeoutException e) {
			Logger.console(Texts.SERVER_SESSION_ID,e.getMessage(), Texts.FLAG_ERROR_LOG);
		}
		
		Logger.console(Texts.SERVER_SESSION_ID,"Initiating connection to "+ IP+" on port "+PORT,Texts.FLAG_SYS_EVENT);
	}
	
	/**
	 * Method to write on a socket
	 * @param byte[] data array of data to write
	 * @exception IOException if an error occurred while writing or there is no socket
	 * @return String response from the server written
	 * */
	public String write(byte[] data) throws IOException{
		OutputStream out = client.getOutputStream();
		out.write(data);
		out.flush();
		InputStream in = client.getInputStream();
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(in));
		return inFromServer.readLine();
	}
	
	/**
	 * Method to close the socket open
	 * @param void
	 * @return void
	 * */
	public void close() throws IOException{
		if(!client.isClosed())
			client.close();
	}

}
