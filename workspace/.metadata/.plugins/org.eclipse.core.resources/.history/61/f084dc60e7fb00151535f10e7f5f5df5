package worldObjects;
import java.util.ArrayList;
import jssc.*;

/**
* <h1>IRobot Communication</h1>
* The IRobot class acts as the middle man between
* the main AI and the IRobot. The java simple serial 
* connector is used for the communication to make
* the whole thing as simple as possible.
*/
public class IRobot{
	
	private SerialPort sPort;
	private ArrayList<Integer> opcodes = new ArrayList<Integer>();
	private byte[] bytes = new byte[256];
	private int[] senserData = new int[51];
	
	public IRobot(){
		
		//
		String[] ports = SerialPortList.getPortNames();
		for(int i =0;i<ports.length;i++) {
			System.out.println(ports[i]);
		}
		
		// a small speed up helper
		for(int i=0; i<bytes.length; i++) {
			bytes[i] = (byte) (i & 0xFF);
		}
		
		// start the connection
		try {
			sPort = new SerialPort("COM3");
			sPort.openPort();
			sPort.setParams(115200, 8, 1, 0);
		} catch (SerialPortException e){
			e.printStackTrace();
		}
		
		// start the giving power to the kinect
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
		
		// add an event listener to the port
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
	
	private void listenerEvent(SerialPortEvent event){
        if(event.isRXCHAR()){//If data is available
        	try {
        		System.out.println("HexString = " + sPort.readHexString());
        		//byte[] got = sPort.readBytes();
        		//System.out.println("HexString = " + sPort.readHexString());
        		//parseInput(got);
        		//System.out.println("Distance = " + distance);
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
	//
	private void parseInput(byte[] b){
		byte header = b[0]; // I keep geting 13, I don't really know why...
		byte nBytes = b[1]; //the number of bytes between the n-bytes byte and the checksum.
		
		if (header != 13) return;
		
		// the rest if for debug only
		// b[2] = 13 
		setDistance(b[3], b[4]);
	}

	/**
	* This method is used to send the list of commands 
	* and clear the opcode list. 
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

	public void IO_start(){
		opcodes.add(128);
	}
	public void IO_reset(){
		opcodes.add(7);
	}
	public void IO_stop(){
		opcodes.add(173);
	}

	// an easy way to test commands
	public void IO_add(int[] buffer){
		for(int i = 0;i<buffer.length;i++){
			opcodes.add(buffer[i]);
		}
	}

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
	public void setSong(int songIndex, int[] song){
		opcodes.add(140);
		opcodes.add(songIndex);
		opcodes.add(song.length/2);			
		for(int i = 0;i<song.length;i++) {
			opcodes.add(song[i]);
		}
	}
	public void playSong(int songIndex){
		opcodes.add(141);
		opcodes.add(songIndex);
	}
	
	// TODO: Input Commands
	public void sensors_update(){
		opcodes.add(148);
		opcodes.add(1);
		opcodes.add(7);
	}
	
	// Distance - Packet ID: 19  - Data Bytes: 2, signed
	private int distance = 0;
	private void setDistance(int lByte, int rByte){
		int val = ((lByte & 0xFF) << 8) | (rByte & 0xFF);
		distance += val;
	}
	public int getDistance(){
		int dis = distance;
		//distance = 0;
		return dis;
	}
}