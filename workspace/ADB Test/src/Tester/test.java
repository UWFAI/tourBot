package Tester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = null;
		// run the adb bridge
				try {
					System.out.println("Attempting to run ADB...");
					Process p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb devices");
					
					//Gets output from command
					BufferedReader stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
					
					/*
					
					// Forward ports for socket communication
					 
					 p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb forward tcp:38300 tcp:38300");
					
					stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
					
					*/
					
					// Run app on device
					// Set com.package.name to correct name for the app
					 
					  p = Runtime.getRuntime().exec("C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb shell am start -n com.example.airg.adbtest/com.example.airg.adbtest.MyActivity");
					
					stdInput = new BufferedReader(new
			                 InputStreamReader(p.getInputStream()));
					
					while ((s = stdInput.readLine()) != null) {
		                System.out.println(s);  
		            } 
					
					//Run socket host 
					  
					 
					
					
				} 
				catch (Exception e) {
					System.out.println(e.toString());
				}


	}
	
	
}