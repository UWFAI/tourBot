package create2_environment;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

@SuppressWarnings("all")
public class Window extends JFrame
{
	//
	static Controller controller;
	
	//
	Room room;
	DepthPanel depthImg;
	Scanner scanner;
	
	JTextArea input = new JTextArea();
	JTextArea output = new JTextArea();
	
	public Window(World world)
	{
		room = new Room(world);
		depthImg = new DepthPanel();
		
		setMinimumSize(new Dimension(600, 500));
		setBounds(100, 100, 1424, 780);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		setJMenuBar(new Menu());
		
		initialize();
		
		setVisible(true);
	}
	
	public void update()
	{
		
	}
	public void draw()
	{
		room.repaint();
	}
	
	private void initialize() {
		
		JPanel IO = new JPanel();
		IO.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		this.getContentPane().add(IO, BorderLayout.WEST);
		GridBagLayout gbl_IO = new GridBagLayout();
		gbl_IO.columnWidths = new int[]{126, 0};
		gbl_IO.rowHeights = new int[] {200, 30, 200, 0};
		gbl_IO.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_IO.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		IO.setLayout(gbl_IO);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		input.setLineWrap(true);
		scrollPane.setViewportView(input);
		input.setMargin(new Insets(2, 2, 2, 2));
		input.setRows(10);
		input.setColumns(15);
		input.setWrapStyleWord(true);
		
		JLabel lblNewLabel = new JLabel("Input");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblNewLabel);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		IO.add(scrollPane, gbc_scrollPane);
		
		JButton btnNewButton = new JButton("Send Commands");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				press_sendCommand();
			}
		});
		
				
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.weighty = 0.5;
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.insets = new Insets(0, 5, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		IO.add(btnNewButton, gbc_btnNewButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(null);
		scrollPane_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane_1.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		output.setMargin(new Insets(2, 2, 2, 2));
		output.setLineWrap(true);
		scrollPane_1.setViewportView(output);
		output.setWrapStyleWord(true);
		output.setRows(10);
		output.setColumns(15);
		
		JLabel lblNewLabel_1 = new JLabel("Output");
		lblNewLabel_1.setBackground(Color.GRAY);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblNewLabel_1);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 5, 5, 5);
		gbc_scrollPane_1.weighty = 1.0;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 2;
		IO.add(scrollPane_1, gbc_scrollPane_1);
		
		JPanel panel_1 = new JPanel();
		this.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_1.add(depthImg, BorderLayout.SOUTH);

		panel_1.add(room, BorderLayout.CENTER);
	}
	
	public void press_sendCommand()
	{	
		String inp = input.getText();
		controller.io.set_input(inp);
		input.setText("");
	}
	
	
	
	public String get_output()
	{
		return output.getText();
	}
	public void set_output(String string)
	{
		output.setText(string);;
	}
	

	class Menu extends JMenuBar
	{
		public Menu()
		{
			addFile();
			addSet();
		}
		
		public void addFile()
		{
			JMenu menu = new JMenu("File");
			add(menu);
			
			JMenuItem menuItem = new JMenuItem("Item");
			menu.add(menuItem);
			
			JMenuItem exitItem = new JMenuItem("exit");
			exitItem.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		            System.exit(0);
		        }
			});
			menu.add(exitItem);
		}
		
		public void addSet()
		{
			JMenu menu = new JMenu("Set");
			add(menu);
			
			JMenuItem setRes = new JMenuItem("Set Res");
			setRes.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		            //System.exit(0);
		        }
			});
			menu.add(setRes);

		}
	}

	//
	class DepthPanel extends JPanel
	{
		int resolution = 100;
		
		public DepthPanel()
		{
			super();
			setBackground(Color.green);
			setMinimumSize(new Dimension(60000, 30));
			setPreferredSize(new Dimension(60000, 30));
		}
	}
	
	//
	class Room extends JPanel
	{
		World world;
		int worldX = 0;
		int worldY = 0;
		
		int worldX_mouse_dif = 0;
		int worldY_mouse_dif = 0;
		
		public Room(World world)
		{
			super();
			setBackground(Color.red);
			this.world = world;

            addMouseMotionListener(new MouseMotionListener() {
				
				@Override
				public void mouseMoved(MouseEvent e) {}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					if (getMousePosition() != null)
						updateView(getMousePosition().x, getMousePosition().y);
				}
			});
            
            // just want to know when the mouse is pressed
            addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
					worldX_mouse_dif = worldX - getMousePosition().x;
					worldY_mouse_dif = worldY - getMousePosition().y;
					updateView(getMousePosition().x, getMousePosition().y);
				}
				

				@Override
				public void mouseClicked(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
			});
			
		}
		
    	void updateView(int x, int y)
    	{
    		worldX = x+worldX_mouse_dif;
    		worldY = y+worldY_mouse_dif;
    	}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			//
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 99999, 99999);
			
			//
			world.draw(g2,worldX,worldY);
			
			//
			g.setColor(Color.BLACK);
			int y = this.getHeight()-5;
			
			int[] items = getWorldScale();
			int width = items[0];
			
			g.drawLine(5,       y-10, 5,         y);
			g.drawLine(5,       y-5,  5+width-1, y-5);
			g.drawLine(5+width, y-10, 5+width,   y);
			g.drawString(Integer.toString(items[1]) + " meter", 5+width+5, y);
	    }
		
		public int[] getWorldScale()
		{
			int[] items = {0,0};
			
			items[0] = world.get_pixelsPerMeter();
			items[1] = 1;
			
			return items;
		}
	}
}
















