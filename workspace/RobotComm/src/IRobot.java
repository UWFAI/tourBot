import jssc.*;

public class IRobot{
	
	private SerialPort sPort;
	
	//
	private SerialPortEventListener event;
	
	// instead of always casting it will be faster to use this
	private byte[] bytes = new byte[256];
	
	public IRobot(){
		
		// a small speed up helper
		for(int i=0; i<bytes.length; i++){
			bytes[i] = (byte) (i & 0xFF);
		}
		
		// start the connection
		try {
			sPort = new SerialPort("COM3");
			sPort.setParams(115200, 8, 1, 0);
		} catch (SerialPortException e){
			e.printStackTrace();
		}
		
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
			sPort.writeByte(bytes[7]);
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
	// Song(index, int[])
	// play(index)
	
	// TODO: Input Commands
	
	
}
