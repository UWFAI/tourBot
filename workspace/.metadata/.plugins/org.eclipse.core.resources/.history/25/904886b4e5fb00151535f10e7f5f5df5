package create2_environment;
import java.util.Scanner;

public class Robot_IO {
	static public Controller controller;
	
	static public Scanner scr;
	private String input = "";
	private String output = "";
	
	
	// 202 151 02 585 2
	// r = 202
	// 151 02 585 2
	public double getNextInput()
	{
		scr = new Scanner(input);
		double a = 0;
		
		String s = "";
		try{
			s = scr.next();
			a = Double.parseDouble(s);
			input = input.substring(s.length()+1,input.length());
			//System.out.println(s);
		} catch(Exception e){}
		
		return a;
	}
	// getters and setters for the raw values
	public String get_input() {
		return input;
	}
	public void set_input(String input) {
		this.input = input;
	}
	public String get_output() {
		return output;
	}
	public void set_output(String output) {
		this.output = output;
	}
}
