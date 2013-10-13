package com.maxl.java.simpleclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class SimpleClient {
	
	int mPort = 7777;
	
	public static void main(String[] args) {
		try {
			new SimpleClient().startClient();
		} catch (Exception e) {
			System.out.println("SimpleClient could not be started: " + e.getMessage());
			e.printStackTrace();
		}
	}
 
	public void startClient() throws IOException { 
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		InetAddress host = null;
		BufferedReader stdIn = null;
 
		try {
			host = InetAddress.getLocalHost();
			socket = new Socket(host.getHostName(), mPort);
 
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
 
			// Send request messages to server
			// t = title, e = eancode, r = regnr
			String ArrayOfRegnrs[] = {"r 58809", "t Aspirin", "e 7680569190089", "r 51795", "r 45604", 
					"r 62069", "r 55603", "t Ugaga", "t Lev", "t Elevit"};

			for (int i=0; i<20; ++i) {
				for (int j=0; j<10; ++j) {
					out.println(ArrayOfRegnrs[j]);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			// Read from socket and write back the response to server. 
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server response: " + fromServer);
				if (fromServer.equals("exit"))
					break;
 
				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client - " + fromUser);
					out.println(fromUser);
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Cannot find the host: " + host.getHostName());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Could not read/write from the connection: " + e.getMessage());
			System.exit(1);
		} finally { 
			// Make sure to always clean up the mess...
			out.close();
			in.close();
			stdIn.close();
			socket.close();
		}
	}
}
