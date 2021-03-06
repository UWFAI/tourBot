import java.util.ArrayList;
import java.util.Arrays;

import jssc.*;

/**
* <h1>IRobot Communication</h1>
* The IRobot class acts as the middle man between
* the main navigation AI and the IRobot. The java simple serial 
* connector is used for the communication to make
* the whole thing as simple as possible.
* 
* @author AIRG
* @version 1.0.0.1
*/
public class IRobot{
	
	private SerialPort sPort;
	private ArrayList<Integer> opcodes = new ArrayList<Integer>();
	private byte[] bytes = new byte[256];
	
	// a list of the sensor packets that need to be updated
	public ArrayList<Integer> sensorList = new ArrayList<Integer>();
	
	// the index of the sensor list 
	private int sensorList_index = 0;
	
	/**
	* <h2>IRobot - Constructor</h2>
	* The IRobot class constructor
	* 
	*/
	public IRobot(){
		
		//
		String[] ports = SerialPortList.getPortNames();
		for(int i =0;i<ports.length;i++) {
			System.out.println(ports[i]);
		}
		
		/**
		 * <p>
		 * a small speed up helper
		 */
		for(int i=0; i<bytes.length; i++) {
			bytes[i] = (byte) (i & 0xFF);
		}
		
		/**
		 * <p>
		 * start the connection
		 */ 
		try {
			sPort = new SerialPort("COM3");
			sPort.openPort();
			sPort.setParams(115200, 8, 1, 0);
		} catch (SerialPortException e){
			e.printStackTrace();
		}
		
		/**
		 * <p>
		 * 
		 * The iRobot's fan motor is activated.  This motor is modified to a power DC source to the XBOX One Kinect. 
		 *  
		*/
		IO_start();
		opcodes.add(131);
		opcodes.add(144);
		opcodes.add(0);
		opcodes.add(0);
		opcodes.add(127);
		opcodes.add(0);
		IO_send();
		IO_start();
		opcodes.add(131);
		opcodes.add(144);
		opcodes.add(0);
		opcodes.add(0);
		opcodes.add(127);
		opcodes.add(0);
		IO_send();
		
		/**
		 * <p>
		 * add an event listener to the port
		 */
		try {
			sPort.addEventListener(new SerialPortEventListener() {
				public void serialEvent(SerialPortEvent event) {
					listenerEvent(event);
				}
			});
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * <h2>listenerEvent</h2>
	 * Event Listener 
	 * 
	 * @param event A Serial Port Event.
	 */	
	private void listenerEvent(SerialPortEvent event){
        if(event.isRXCHAR()){//If data is available
        	try {
        		byte[] got = sPort.readBytes();
        		parseInput(got);
        		
        		new Thread( new Runnable() {
        	        public void run()  {
        	            try  { Thread.sleep( 100 ); }
        	            catch (InterruptedException ie)  {}
        	            sensors_update();
        	        }
        		} ).start();
        		
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
        } else if(event.isCTS()){//If CTS line has changed state
            if(event.getEventValue() == 1){//If line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        } else if(event.isDSR()){///If DSR line has changed state
            if(event.getEventValue() == 1){//If line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }
	}

	/**
	 * <h2>parseInput</h2>
	 * add an event listener to the port
	 * 
	 * @param b A byte array.
	 */	
	private void parseInput(byte[] b){
		
		int packet = sensorList.get(sensorList_index);
		String data = Arrays.toString(b);
		System.out.println("got packet ["+packet+"] with data {"+data+"}");
		
		
		
		/*
		byte header = b[0]; // I keep geting 13, I don't really know why...
		byte nBytes = b[1]; //the number of bytes between the n-bytes byte and the checksum.
		
		if (header != 13) return;
		
		// the rest if for debug only
		// b[2] = 13 
		setDistance(b[3], b[4]);
		*/
	}

	/**
	* <h2>IO_send</h2>
	* Sends the list of commands to the iRobot using a for loop. Opcode list is cleared at the end of the function.
	* 
	*/
	public void IO_send(){
		try {
			for(int i = 0;i<opcodes.size(); i++) {
				sPort.writeByte(bytes[opcodes.get(i)]);
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		opcodes.clear();
	}
	
	/**
	* <h2>IO_start</h2>
	* Must be put into IO before sending data.
	* 
	*/
	public void IO_start(){
		opcodes.add(128);
	}
	
	/**
	* <h2>IO_reset</h2>
	* Reset the IO session. Used for testing purposes.
	* 
	*/
	public void IO_reset(){
		opcodes.add(7);
	}
	
	/**
	* <h2>IO_stop</h2>
	* Stop the IO session. Used for testing purposes.
	* 
	*/
	public void IO_stop(){
		opcodes.add(173);
	}

	/**
	* <h2>IO_add</h2>
	* Passes an integer array of opcodes. Function allows testing of opcodes. 
	* 
	* @param buffer An integer array.
	*/
	// an easy way to test commands
	public void IO_add(int[] buffer){
		for(int i = 0;i<buffer.length;i++){
			opcodes.add(buffer[i]);
		}
	}

	/**
	* <h2>setMode</h2>
	* Activate or disable "Safe" mode.  This is for cliff detection.
	* 
	* @param safe A boolean variable.
	*/
	public void setMode(boolean safe){
		if (safe)
			opcodes.add(131);
		else
			opcodes.add(132);
	}

	// TODO: the simple methods of just sending bytes
	// seekDock()
	// Drive(Velocity, Radius)
	// DriveDirect(Right velocity, Left velocity)
	// DrivePWM(Right PWM, Left PWM)
	// LEDs // there are 4
	// Digit_LEDs(d1, d2, d3)
	
	/**
	* <h2>setSong</h2>
	* Sends the list of commands to the iRobot using a for loop. Opcode list is cleared at the end of the function.
	* 
	* @param songIndex An integer variable.
	* @param song Why the hell are you singing a song with a tour robot(an integer array)
	*/
	public void setSong(int songIndex, int[] song){
		opcodes.add(140);
		opcodes.add(songIndex);
		opcodes.add(song.length/2);			
		for(int i = 0;i<song.length;i++) {
			opcodes.add(song[i]);
		}
	}

	/**
	* <h2>playSong</h2>
	* Specifies the song to be played 
	* 
	* @param songIndex An integer variable.
	*/
	public void playSong(int songIndex){
		opcodes.add(141);
		opcodes.add(songIndex);
	}
	
	// TODO: Input Commands

	/**
	* <h2>sensor_update</h2>
	* Input Commands
	* 
	*/
	public void sensors_update(){
		sensorList_index++;
		if (sensorList_index >= sensorList.size())
			sensorList_index = 0;
		
		IO_start();
		setMode(true);		
		opcodes.add(142);
		opcodes.add(sensorList.get(sensorList_index));
		IO_send();
	}

	/**
	* <h2>setDistance</h2>
	* This also changes the detection distance.
	* 
	*/
	// Distance - Packet ID: 19  - Data Bytes: 2, signed
	private int distance = 0;
	private void setDistance(int lByte, int rByte){
		int val = ((lByte & 0xFF) << 8) | (rByte & 0xFF);
		distance += val;
	}

	/**
	* <h2>getDistance</h2>
	* Returns the distance. 
	* 
	* @return dis An integer value.
	* 
	*/
	public int getDistance(){
		int dis = distance;
		//distance = 0;
		return dis;
	}
}
