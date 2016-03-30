import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JSlider;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;

public class DebugWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JSlider direction_slider;
	private JSlider speed_slider;
	
	public int get_speed(){
		return speed_slider.getValue();
	}
	public int get_direction(){
		return direction_slider.getValue();
	}
	
	public void set_speed(int speed){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(speed, 0, 1);
	}
	public void set_angle(int angle){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(angle, 1, 1);
	}
	public void set_distance(int distance){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(distance, 2, 1);
	}
	public void set_direction(int direction){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(direction, 3, 1);
	}
	public void set_temperature(int temperature){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(temperature, 4, 1);
	}
	public void set_bumps(int bumps){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(bumps, 5, 1);
	}
	public void set_battery(int battery){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt(battery, 6, 1);
	}

	/**
	 * Create the frame.
	 */
	public DebugWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 763, 573);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		panel.setBounds(12, 13, 200, 200);
		contentPane.add(panel);
		panel.setLayout(null);
		
		direction_slider = new JSlider();
		direction_slider.setPaintTicks(true);
		direction_slider.setMinorTickSpacing(10);
		direction_slider.setMajorTickSpacing(50);
		direction_slider.setValue(0);
		direction_slider.setMinimum(-100);
		direction_slider.setBounds(30, 170, 170, 30);
		panel.add(direction_slider);
		
		speed_slider = new JSlider();
		speed_slider.setPaintTicks(true);
		speed_slider.setMinorTickSpacing(10);
		speed_slider.setMajorTickSpacing(50);
		speed_slider.setValue(0);
		speed_slider.setMinimum(-100);
		speed_slider.setOrientation(SwingConstants.VERTICAL);
		speed_slider.setBounds(0, 0, 30, 170);
		panel.add(speed_slider);
		
		JPanel panel_1 = new JPanel();
		panel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

		        System.out.println("Mouse x: " + e.getX());
		        System.out.println("Mouse y: " + e.getY());
			}
		});
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(30, 0, 170, 170);
		panel.add(panel_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(234, 13, 500, 500);
		contentPane.add(tabbedPane);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Tree", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Other", null, panel_3, null);
		
		JButton btnTurnOff = new JButton("turn off");
		btnTurnOff.setBounds(12, 488, 200, 25);
		contentPane.add(btnTurnOff);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(12, 226, 200, 249);
		contentPane.add(panel_4);
		
		table = new JTable();
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Speed", null},
				{"Angle", null},
				{"Distance", null},
				{"Direction", null},
				{"Temperature", null},
				{"Bumps", null},
				{"Battery", null},
			},
			new String[] {
				"Propertie", "value"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(89);
		panel_4.setLayout(new BorderLayout(0, 0));
		panel_4.add(table);
		
		setVisible(true);
	}
}
