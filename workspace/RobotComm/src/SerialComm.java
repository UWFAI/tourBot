

public class SerialComm {
	
	IRobot bot;
	
	public SerialComm(){
		bot = new IRobot();
		
		bot.setMode(true);
		
		int[] clean = {135};
		bot.IO_send(clean);
	}
	

	public static void main(String[] args) {
		new SerialComm();
		// TODO Auto-generated method stub
		/*String[] ports = SerialPortList.getPortNames();
		for(int i =0;i<ports.length;i++)
		{
			System.out.println(ports[i]);
		}
		
		byte[] buffer = {(byte)128,(byte)130,(byte)135};
		SerialPort sPort = new SerialPort("COM3");
		try {
			sPort.openPort();
			sPort.setParams(115200, 8, 1, 0);
			for(int i = 0;i<buffer.length;i++)
			{
				sPort.writeByte(buffer[i]);
			}
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
	}

}
