package raxml_gui;

import java.io.File;

import javax.swing.JScrollPane;


public class Raxml {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// initialize MainFrame
		MainFrame main_frame = new MainFrame(Constants.MAIN_FRAME_TITLE);
		main_frame.setSize(Constants.MAIN_FRAME_WIDTH, Constants.MAIN_FRAME_HEIGHT);
		main_frame.setDefaultCloseOperation(MainFrame.DO_NOTHING_ON_CLOSE);
		main_frame.setVisible(true);
		main_frame.setResizable(true); 

	}

}
