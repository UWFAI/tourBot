import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* <h1>Tablet Communication</h1>
* The Connection class starts the tour bot's 
* tablet application and establishes a socket 
* connection for two way communication. 
* 
* @author AIRG
* @version 1.0.0.1
*/
public class Connection {
	
	private static String adbLocation = "C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb";
	private static int port = 38300;
	private static Socket socket;
	public static PrintWriter out;
	public static Scanner sc;

	/**
	* <h2>main</h2>
	* 
	* 
	*/
	public static void main(String[] args) {
		
		//Start new thread for tablet communication
		new Thread( new Runnable() {
            public void run() {
            	
            		runADB();	
            		
            		if (connect() == true){
            			startScanner();
            			sendMsgTest();
            		}
         	}
		}).start();
		
	}
	
	/**
	* <h2>Connection - Default Constructor</h2>
	* The Connection class constructor
	* 
	*/
	public Connection (){
		Connection.adbLocation = "C:\\Users\\AIRG\\AppData\\Local\\Android\\sdk\\platform-tools\\adb";
		Connection.port = 38300;
	}
	
	/**
	* <h2>Connection - Constructor</h2>
	* The Connection class constructor
	* 
	* @param adbLocation - Location that Android Debug bridge
	* 	is installed
	* 
	* @param port - Port to be used for communication
	* 
	*/
	public Connection (String adbLocation, int port){
		Connection.adbLocation = adbLocation;
		Connection.port = port;
	}
	
	
	/**<h2>runADB</h2>
	   * This method starts Android Debug Bridge,
	   * sets ports to forward to, closes any 
	   * instances of the tablet app currently running, 
	   * then restarts the tablet app.
	   * 
	   * @return Nothing.
	   */
	private static void runADB(){
		try {
			// run the adb bridge
			System.out.println("Attempting to run Android Debug Bridge.....\n");
			runConsoleCommand(adbLocation + " devices");
			
			//Wait 4 seconds so adb can start up if it needs to.	
			printWaitingDots("Starting ADB and checking for device.", 4);
	
			// Forward ports for socket communication
			System.out.println("Running: adb forward tcp:" + port +" tcp:"+ port +" \n");
			runConsoleCommand(adbLocation + " forward tcp:38300 tcp:38300");
				 
			//kill app if already running
			runConsoleCommand(adbLocation + " shell am force-stop com.example.airg.adbtest");
				
			// Run app on device 
			runConsoleCommand(adbLocation + " shell am start -n com.example.airg.adbtest/com.example.airg.adbtest.MyActivity");
			
			//Wait for app to start
			printWaitingDots("Waiting for app to start.", 5);
	
		}
	
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	/**<h2>connect</h2>
	   * This method creates the socket connection and 
	   * sets up the input and output stream.
	   * 
	   * @return true - Connection successful
	   * 
	   * @return false - Connection failed
	   */
	private static boolean connect(){
		  //Create socket connection
        try{
        	System.out.println("Attempting socket connection.....\n");
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
            
            return true;

        } catch (UnknownHostException e) {
            System.out.println("Socket connection problem (Unknown host)" + e.getStackTrace());
        } catch (IOException e) {
            System.out.println("Could not initialize I/O on socket " + e.getStackTrace());
        } 
        
        return false;
	}
	
	/**<h2>startScanner</h2>
	 * This method starts a scanner in a new thread to receive
	 * messages from the tablet app.
	 * 
	 * 
	 */
	private static void startScanner(){
		
		new Thread( new Runnable() {
            public void run() {
            	while(sc.hasNext()) {
            		System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, 
            				DateFormat.MEDIUM).format(System.currentTimeMillis()) + " Recieved: " + sc.nextLine() +"\n");
            	}
            }
		}).start();	
		
	}
	
	/**<h2>sendMsgTest</h2>
	 * This method starts a scanner to read command line
	 * input and then sends each line input to the tablet.
	 * 
	 * 
	 */
	private static void sendMsgTest(){
		// create a scanner so we can read the command-line input
	    Scanner scannerIn = new Scanner(System.in);
	    
	    String msg = "";
	    
	    while(scannerIn.hasNext()){
	    	msg = scannerIn.next();
	    	out.println(msg);
	    	System.out.println(DateFormat.getDateTimeInstance(DateFormat.SHORT, 
    				DateFormat.MEDIUM).format(System.currentTimeMillis()) + " Sent: " + msg + "\n");
	    }
	      
	     scannerIn.close();
	}
	
	
	
	/**<h2>runConsoleCommand</h2>
	   * This method runs a console command and prints its outputs
	   * 
	   * @param cmd - command you want to run
	   */
	private static void runConsoleCommand(String cmd){
		
		//String to save output to
		String s = null;
		
		try{
			//Set Process
			Process p = Runtime.getRuntime().exec(cmd);
		
			//Run and get output from command
			BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
		
			//Print output from command
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);  
			} 
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	/**<h2>printWaitingDots</h2>
	   * This method prints a message then a specified 
	   * numbers of periods once a second after the message. 
	   * 
	   * @param msg - Message to print 
	   * 
	   * @param sec - Seconds to wait, also periods to print.
	   */
	private static void printWaitingDots(String msg, int sec){
		
		try{
			//Print message
			System.out.print(msg);
			//Print one dot a second until next to last dot
			for (int i=1; i< sec; i++){
				Thread.sleep(1000);
				System.out.print(".");
			}
			//Wait one second then print last dot and go to new line
			Thread.sleep(1000);
			System.out.print(".\n\n");
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

}
