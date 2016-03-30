package Connect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;

import Connect.Conn;

public class Init { 

	public static void main(String[] args) {
		
		String s = null;
		
		// run the adb bridge
				try {
					System.out.println("Attempting to run ADB...\n");
					Process p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb devices");
					
					System.out.println("Starting adb and checking for device...\n");
					
					//Gets output from command
					BufferedReader stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
					
					System.out.println("Waiting 5 Secords for adb to start...\n ");
					Thread.sleep(5000);
					
					// Forward ports for socket communication
					 
					 p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb forward tcp:38300 tcp:38300");
					
					 System.out.println("Running: adb forward tcp:38300 tcp:38300 \n");
					 
					stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
				
					//kill app if already running
					
					 p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb shell am force-stop com.example.airg.adbtest");
						
						stdInput = new BufferedReader(new
				                 InputStreamReader(p.getInputStream()));
						
						while ((s = stdInput.readLine()) != null) {
			                System.out.println(s);  
			            } 
					
					// Run app on device
					// Set com.package.name to correct name for the app
					 
					  p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb shell am start -n com.example.airg.adbtest/com.example.airg.adbtest.MyActivity");
					
					stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
					
					System.out.println("Waiting 5 Secords for app to start...\n ");
					Thread.sleep(5000);                 //1000 milliseconds is one second.
				
					System.out.println("Initializing connection...\n");
					//Run socket host 
					 final Conn c = new Conn();
				     c.initializeConnection();
				     
				     
				     new Thread( new Runnable() {
				            public void run() {

			            	while(c.sc.hasNext()) {
			            		System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, 
			            				DateFormat.MEDIUM).format(System.currentTimeMillis()) + ": " + c.sc.nextLine());
			            	}
			            }
			        }).start();	
				     
				     
				     
				     
				}
			
				catch (Exception e) {
					System.out.println(e.toString());
				}

	}
	
	
	

}
