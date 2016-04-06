import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
	
	// the time in ms for the sensors to update
	private int sensorList_update_time = 100;
	
	/// TODO: create a output list instead of sending stuff when ever
	// need to redo a lot of stuff
	// the list will be an object list that removes the first element
	// after an amount of time. I think that this should be done in a timer
	// or a thread
	
	private short cur_speed = 0;
	private short cur_direction = 0;
	private short sent_speed = 0;
	private short sent_direction = 0;
	
	public String CurMove = "";
	
	// variables that store the input from the sensors
	private int distance = 0;
	private int battery_Charge = 0;
	private int temperature = 0;
	private boolean wheel_drop_left = false;
	private boolean wheel_drop_right = false;
	private boolean bump_left = false;
	private boolean bump_right = false;

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
		/**/
		System.out.println("--  starting Kinect");
		IO_start();
		opcodes.add(132);
		opcodes.add(144);
		opcodes.add(0);
		opcodes.add(0);
		opcodes.add(127);
		opcodes.add(0);
		IO_send();
		/**/
		IO_start();
		opcodes.add(132);
		opcodes.add(144);
		opcodes.add(0);
		opcodes.add(0);
		opcodes.add(127);
		opcodes.add(0);
		IO_send();
		
		System.out.println("--  Kinect should be started");
		
		
		/**
		 * 
		 * 
		 */
		
		sensorList.add(25); // Battery Charge
		sensorList.add(7); // Bumps and Wheel Drops
		sensorList.add(19); // Distance
		sensorList.add(20); // Angle
		sensorList.add(24); // Temperature
		sensorList.add(39); // Velocity /// TODO
		sensorList.add(40); // Radius /// TODO
		
		///////////////////////////////////////////////////////////////////////////
		//move(10);
		/*
		IO_start();
		opcodes.add(131);
		opcodes.add(137);
		
		//opcodes.add(0x80);
		opcodes.add(0x7F); // [Velocity high byte]
		opcodes.add(0xFF); // [Velocity low byte]
		
		// 0x8000 - strate
		opcodes.add(0x80); // [Radius high byte]
		opcodes.add(0x00); // [Radius low byte]
		IO_send();
		///////////////////////////////////////////////////////////////////////////
		*/
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
		
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { update(); }
		    }, 0, 100);
		//update();
		//sensors_update();
		
		
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
        		
	        	try{
	        		parseInput(sensorList_index, got);
	        		//System.out.println("got");
				} catch (IndexOutOfBoundsException e){
					//System.out.println("nope");
					//e.printStackTrace();
				}
        		
        		/*
	            try  { Thread.sleep( sensorList_update_time ); }
	            catch (InterruptedException ie)  {}
	            sensors_update();
	            */
	        	
        		/*
        		new Thread( new Runnable() {
        	        public void run()  {
        	            try  { Thread.sleep( sensorList_update_time ); }
        	            catch (InterruptedException ie)  {}
        	            sensors_update();
        	            return;
        	        }
        		} ).start();
        		*/
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
	private void parseInput(int listIndex, byte[] b){
		
		String input_name = "";
		switch(sensorList.get(listIndex)) {
		case 7: 
			input_name = "Bumps and Wheel Drops";
			parse_bumpWheel(b[0]);
			break;
		case 19: 
			input_name = "Distance"; 
			parse_distance(b[0], b[1]);
			break;
		case 20: input_name = "Angle"; break;
		case 24: 
			input_name = "Temperature"; 
			parse_temperature(b[0]);
			break;
		case 25: 
			input_name = "Battery Charge"; 
			parse_batteryCharge(b[0], b[1]);// don't know what order
			break;
		}
		
		int packet = sensorList.get(sensorList_index);
		String data = Arrays.toString(b);
		//System.out.println("got packet ["+packet+", "+input_name+"] \n\t {"+data+"}\n");
	}
	
	// input parsers
	private void parse_distance(byte b1, byte b2) {
		distance += (int) ((b1 << 8) | (b2 & 0xFF));
		//System.out.println("--  distance = " + distance + " mm");
	}
	private void parse_batteryCharge(byte b1, byte b2) {
		battery_Charge = (int) (((b1 & 0xFF) << 8) | (b2 & 0xFF));
		//battery_Charge = (int) ((b1 *255) + (b2));
		//System.out.println("--  battery charge: ["+battery_Charge+"] = " + ((battery_Charge/65535.0*10000.0))+"%");
		//System.out.println("--  ["+b1+"]["+b2+"]");
	}
	private void parse_temperature(byte b1) {
		temperature = (int) b1;
		int f = (int) (temperature*1.8 + 32);
		//System.out.println("--  temperature = [" + ((int)(temperature)) +" C,  "+f+" F]");
	}
	private void parse_bumpWheel(byte b1) {
		wheel_drop_left = ((b1 & 0x08) > 0);
		wheel_drop_right = ((b1 & 0x04) > 0);
		bump_left = ((b1 & 0x02) > 0);
		bump_right = ((b1 & 0x01) > 0);
		//System.out.println("--  wheel = [L:"+wheel_drop_left+",R:"+wheel_drop_right+"]");
		//System.out.println("--  bump  = [L:"+bump_left+",R:"+bump_right+"]");
	}
	
	/**
	* <h2>getDistance</h2>
	* Returns the distance. 
	* 
	* @return dis An integer value.
	* 
	*/
	public int getDistance(){
		//int dis = distance;
		//distance = 0;
		return distance;
	}
	
	/// TODO - this
	public int getDirection(){
		return 0;
	}
	
	/// TODO - this
	public int getBumpers(){
		int out = 0;
		if (bump_right) out += 1;
		if (bump_left) out += 2;
		return out;
		/// TODO - set this to:
		// left bumper = 1;
		// front bumper = 2;
		// right bumper = 4;
		// return L+F+R
	}

	public int getBatteryCharge(){
		return (int) (battery_Charge/65535.0*10000.0);
	}

	/**
	* <h2>IO_send</h2>
	* Sends the list of commands to the iRobot using a for loop. Opcode list is cleared at the end of the function.
	* 
	*/
	public void IO_send(){
		
		String debug = "";
		
		try {
			for(int i = 0;i<opcodes.size(); i++) {
				debug += (int)bytes[opcodes.get(i)] + ",";
				sPort.writeByte(bytes[opcodes.get(i)]);
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		opcodes.clear();
		

		//System.out.println("sent {"+debug+"}");
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
	
	
	///////////////////////////////////
	public void update(){
		//System.out.println("update");
		
		IO_start();
		setMode(true);
		
		sensors_update();
		update_move();
		
		IO_send();
	}
	
	public void update_move(){
		if (sent_speed == cur_speed && sent_direction == cur_direction)
			return;
		
		int sp_h = (int) ((cur_speed >> 8) & 0xFF);
		int sp_l = (int) (cur_speed & 0xFF);
		
		int dr_h = (int) ((cur_direction >> 8) & 0xFF);
		int dr_l = (int) (cur_direction & 0xFF);
		
		opcodes.add(137);
		
		opcodes.add(sp_h); // [Velocity high byte]
		opcodes.add(sp_l); // [Velocity low byte]
		if (cur_direction == 0) {
			opcodes.add(0x80); // [Radius high byte]
			opcodes.add(0x00); // [Radius low byte]
			CurMove = "137 "+sp_h+" "+sp_l+" 0x80 0x00";
		} else {
			opcodes.add(dr_h); // [Radius high byte]
			opcodes.add(dr_l); // [Radius low byte]
			CurMove = "137 "+sp_h+" "+sp_l+" "+dr_h+" "+dr_l;
		}
		
		sent_speed = cur_speed;
		sent_direction = cur_direction;
		
		
	}
	
	/**
	* <h2>sensor_update</h2>
	* Input Commands
	* 
	*/
	public void sensors_update(){
		sensorList_index++;
		if (sensorList_index >= sensorList.size())
			sensorList_index = 0;
		
		//IO_start();
		//setMode(true);
		opcodes.add(142);
		opcodes.add(sensorList.get(sensorList_index));
		//IO_send();
	}
	
	///////////////////////////////////
	public void move(double speed, double direction) {
		cur_speed = (short)(speed * 5.0);
		cur_direction = (short)(direction * 5.0);
		/*
		short out_speed = (short)(speed * 5.0);
		int sp_h = (int) ((out_speed >> 8) & 0xFF);
		int sp_l = (int) (out_speed & 0xFF);
		
		short out_direction = (short)(direction * 5.0);
		int dr_h = (int) ((out_direction >> 8) & 0xFF);
		int dr_l = (int) (out_direction & 0xFF);
		
		if (cur_speed != out_speed || cur_direction != out_direction) {
			IO_start();
			opcodes.add(132);
			opcodes.add(137);
			
			opcodes.add(sp_h); // [Velocity high byte]
			opcodes.add(sp_l); // [Velocity low byte]
			if (out_direction == 0) {
				opcodes.add(0x80); // [Radius high byte]
				opcodes.add(0x00); // [Radius low byte]
			} else {
				opcodes.add(dr_h); // [Radius high byte]
				opcodes.add(dr_l); // [Radius low byte]
			}
			IO_send();
			
			cur_direction = out_direction;
			cur_speed = out_speed;
		}
		*/
	}
	
	/// TODO - speed should be between -100.0 and 100.0
	public void move(double speed){
		
		short out_speed = (short)(speed * 5.0);
		int h = (int) ((out_speed >> 8) & 0xFF);
		int l = (int) (out_speed & 0xFF);
		
		IO_start();
		opcodes.add(132);
		opcodes.add(137);
		
		opcodes.add(h); // [Velocity high byte]
		opcodes.add(l); // [Velocity low byte]
		
		// 0x8000 - no turn
		opcodes.add(0x80); // [Radius high byte]
		opcodes.add(0x00); // [Radius low byte]
		IO_send();
	}

	/// TODO - speed should be between -100 and 100
	public void rotate(int speed){
		
	}
}
