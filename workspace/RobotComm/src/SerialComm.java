import jssc.*;

public class SerialComm {
	
	IRobot bot;
	
	public SerialComm(){
		
		bot = new IRobot();
		//bot.IO_reset();
		//bot.setMode(true);
		
		int[] song = {88,32,83,16};
		//bot.IO_start();
		//bot.setSong(0,song);
		bot.playSong(0);
		//bot.IO_stop();
		
	}
	

	public static void main(String[] args) {
		//new SerialComm();
		// TODO Auto-generated method stub
		/*
		String[] ports = SerialPortList.getPortNames();
		for(int i =0;i<ports.length;i++)
		{
			System.out.println(ports[i]);
		}
		*/
		
		
		//int[] buffer = {7};//reset
		int[] buffer = {128,131, // start and in safe mode
						140, 0, 14, 
						88,32,83,16,84,16,86,16,88,8,86,8,84,16,83,16,81,32,81,16,84,16,88,32,86,16,84,16,83,48,84,16,86,32,88,32,84,32,81,32,81,64,0,
						141, 0,// play song 0
						};
		SerialPort sPort = new SerialPort("COM3");
		try {
			sPort.openPort();
			sPort.setParams(115200, 8, 1, 0);
			for(int i = 0;i<buffer.length;i++)
			{
				sPort.writeByte((byte) (buffer[i] & 0xFF));
			}
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
