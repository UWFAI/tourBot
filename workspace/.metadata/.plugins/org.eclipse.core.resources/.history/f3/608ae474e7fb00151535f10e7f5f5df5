package worldObjects;
import jssc.*;
import java.util.ArrayList;

public class SerialComm {
	
	IRobot bot;
	
	public SerialComm(){
		
		bot = new IRobot();
		
		/*
		// reset the bot
		bot.IO_reset();
		//bot.IO_stop();
		bot.IO_send();
		*/
		
		/*
		// play test song
		int[] song = {88,32,83,16,84,16,86,16,88,8
		};
		bot.IO_start();
		bot.setMode(true);
		bot.setSong(0,song);
		bot.playSong(0);
		bot.IO_send();
		*/
		
		int[] song = {88,32,83,16,84,16,86,16,88};

		bot.IO_start();
		bot.setMode(true);
		bot.setSong(0,song);
		bot.playSong(0);
		bot.sensors_update();
		
		bot.IO_send();
		
		
	}
	

	public static void main(String[] args) {
		new SerialComm();
		// TODO Auto-generated method stub
		/*
		String[] ports = SerialPortList.getPortNames();
		for(int i =0;i<ports.length;i++)
		{
			System.out.println(ports[i]);
		}
		*/
		
		
		//int[] buffer = {128, 131, 144, 0,0,127};//reset
		/*
		int[] buffer = {128,131, // start and in safe mode
						140, 0, 41, 
						88,32,83,16,84,16,86,16,88,8,86,8,84,16,83,16,
						81,32,81,16,84,16,88,32,86,16,84,16,
						83,48,84,16,86,32,88,32,
						84,32,81,32,81,64,
						30,16,86,32,89,16,93,32,91,16,89,16,
						88,48,84,16,88,32,86,16,84,16,
						83,32,83,16,84,16,86,32,88,32,
						84,32,81,32,81,32,30,32,
						0,
						141, 0,// play song 0
						};
		
		SerialPort sPort = new SerialPort("COM3");
		try {
			sPort.openPort();
			sPort.setParams(115200, 8, 1, 0);
			for(int i = 0;i<buffer.length;i++)
			{
				//System.out.println(((int) (buffer[i] & 0xFF)));
				sPort.writeByte((byte) (buffer[i] & 0xFF));
			}
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
	}

}