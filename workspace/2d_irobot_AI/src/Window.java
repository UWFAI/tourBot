import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

@SuppressWarnings("all")
public class Window extends JPanel{
	private JFrame frame;
	//private Controller controller;
	
	JSlider direction;
	JSlider speed;
	
	JLabel label_distance;
	JLabel label_angle;
	JLabel label_temperature;
	JLabel label_bumps;
	JLabel label_battery;
	
	
	public Window() {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		////////////////////////////////////////////////////
		speed = new JSlider(JSlider.VERTICAL,
                -100, 100, 0);
		speed.setMajorTickSpacing(50);
		speed.setMinorTickSpacing(10);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		this.add(speed);
		
		////////////////////////////////////////////////////
		direction = new JSlider(JSlider.HORIZONTAL,
                -100, 100, 0);
		direction.setMajorTickSpacing(50);
		direction.setMinorTickSpacing(10);
		direction.setPaintTicks(true);
		direction.setPaintLabels(true);
		this.add(direction);
		
		////////////////////////////////////////////////////
		label_distance = new JLabel("distance");
		label_angle = new JLabel("angle");
		label_temperature = new JLabel("temp");
		label_bumps = new JLabel("bumps");
		label_battery = new JLabel("battery");
		
		////////////////////////////////////////////////////
		this.add(label_distance);
		this.add(label_angle);
		this.add(label_temperature);
		this.add(label_bumps);
		this.add(label_battery);
		
		////////////////////////////////////////////////////
		frame.setSize(500,500);
		frame.setVisible(true);
	}
	
	public int get_speed(){
		return speed.getValue();
	}
	
	public int get_direction(){
		int out = 0;
		
		if (direction.getValue() < 0) {
			out = -100 + direction.getValue()*-1;
		} else
		if (direction.getValue() > 0) {
			out = 100 - direction.getValue();
		} else
			out = direction.getValue();
		
		return out*-1;
	}
}