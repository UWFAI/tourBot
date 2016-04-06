/**
 *  This class was made to be the bridge between the IRobot class
 *  and the hardware or the simulator. 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import jssc.*;

public class Communication {
	Controller con;
	
	private SerialPort sPort;
	private ArrayList<Integer> opcodes = new ArrayList<Integer>();
	private byte[] bytes = new byte[256];

	private boolean sim_com_con = false;
	
	private void start_iRobot_com() {
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
	
	private void start_sim_com() {
		sim_com_con = true;
	}
	
	public void IO_add(int op){
		opcodes.add(op);
	}
	
	public void IO_send(){
		if(sim_com_con){
			con.sim_con.io.set_input(opcodes.toString());
		}
	}
	
	public Communication(Controller con){
		this.con = con;
		start_sim_com();
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
	        		//parseInput(sensorList_index, got);
				} catch (IndexOutOfBoundsException e){
				}
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
}
