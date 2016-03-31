package Connect;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Conn {
	
	private Socket socket;
	private PrintWriter out;
	public Scanner sc;
	private String s = null;
	 
	 /**
	     * Initialize connection to the phone
	     *
	     */
	    public void initializeConnection(){
	        //Create socket connection
	        try{
	        	System.out.println("Creating socket connection...\n");
	            socket = new Socket("localhost", 38300);
	            out = new PrintWriter(socket.getOutputStream(), true);
	            sc = new Scanner(socket.getInputStream());

	            
	            // add a shutdown hook to close the socket if system crashes or exists unexpectedly
	            Thread closeSocketOnShutdown = new Thread() {
	                public void run() {
	                    try {
	                        socket.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            };

	            Runtime.getRuntime().addShutdownHook(closeSocketOnShutdown);

	        } catch (UnknownHostException e) {
	            System.out.println("Socket connection problem (Unknown host)" + e.getStackTrace());
	        } catch (IOException e) {
	            System.out.println("Could not initialize I/O on socket " + e.getStackTrace());
	        }
	    }

}
