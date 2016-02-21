import jssc.*;

public class IRobot{
	
	private SerialPort sPort;
	
	//
	private SerialPortEventListener event;
	
	//
	private byte[] opcodes = new byte[1024];
	
	// instead of always casting it will be faster to use this
	private byte[] bytes = new byte[256];
	
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
		
		IO_start();
		
		//
		addListener();
	}
	
	private void addListener(){
		event = new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				// TODO
			}
		};
	}

	
	public void send(){
		
	}

	// start needs to be called before each command that sends
	public void IO_start(){
		try {
			sPort.writeByte(bytes[128]);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	public void IO_reset(){
		try {
			sPort.writeByte(bytes[128]);
			sPort.writeByte(bytes[7]);
			sPort.writeByte(bytes[173]);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	// the last thing the program needs to run is this to free the port
	public void IO_stop(){
		try {
			sPort.writeByte(bytes[173]);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	// an easy way to test commands
	public void IO_send(int[] buffer){
		IO_start();
		try {
			for(int i = 0;i<buffer.length;i++){
				sPort.writeByte(bytes[buffer[i]]);
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	// true - safe
	// false - full
	public void setMode(boolean safe){
		IO_start();
		try {
			if (safe)
				sPort.writeByte(bytes[131]);
			else
				sPort.writeByte(bytes[132]);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	// TODO: the simple methods of just sending bytes
	// seekDock()
	// Drive(Velocity, Radius)
	// DriveDirect(Right velocity, Left velocity)
	// DrivePWM(Right PWM, Left PWM)
	// LEDs // there are 4
	// Digit_LEDs(d1, d2, d3)
	public void setSong(int songIndex, int[] song){
		
		try {
			//sPort.writeByte(bytes[128]); // start
			//sPort.writeByte(bytes[131]); // safe mode
			sPort.writeByte(bytes[140]); // start making song
			sPort.writeByte(bytes[songIndex]); // song index
			sPort.writeByte(bytes[song.length/2]); // number of notes
			for(int i = 0;i<song.length;i++) {
				sPort.writeByte(bytes[song[i]]);
			}
			sPort.writeByte((byte) (0 & 0xFF));// complete
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		
	}
	public void playSong(int songIndex){
		try {
			//sPort.writeByte(bytes[128]);
			sPort.writeByte(bytes[131]);
			sPort.writeByte(bytes[141]);
			sPort.writeByte(bytes[songIndex]);
			//sPort.writeByte(bytes[173]);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		
	}
	
	// TODO: Input Commands
	
	
}
