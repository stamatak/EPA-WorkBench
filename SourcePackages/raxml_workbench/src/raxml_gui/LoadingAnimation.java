package raxml_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LoadingAnimation  extends JComponent implements Runnable{

	private static final int XSIZE = 20;
	private static final int YSIZE = 20;
	private int x = 0;
	private int y = 0;
	private int dx = 1;
	private int dy = 0;
	
	private WaitingPanel _parent_waiting_panel;
	
	
	public LoadingAnimation(WaitingPanel parent){
		_parent_waiting_panel = parent;
	}
	@Override
	public void paint(Graphics g) {
		
		g.setColor(Constants.BACKGROUND_COLOR);
		g.fillRect(x, y+5, XSIZE, YSIZE);
		g.setXORMode(this.getBackground());
		g.setColor(Constants.HEADER_COLOR);
		
		x += dx;
		y += 0;
	    Dimension d = this.getSize();
	    // x reaches left border of the component
	    if (x < 0) {
	      x = 0;
	      dx = -dx; 
	      g.fillRect(x, y+5, XSIZE, YSIZE);
	    }
	    // x reaches right border of the component
	    else if (x + XSIZE >= d.width) {
	      x = d.width - XSIZE;
	      dx = -dx;
	      g.fillRect(x, y+5, XSIZE, YSIZE);
	    }
	    // x is in between
	    else {
	    	g.setColor(Constants.HEADER_COLOR);
	 	    g.fillRect(x, y+5, XSIZE, YSIZE);
	    }
//	    if (y < 0) {
//	      y = 0;
//	      dy = -dy;
//	    }
//	    if (y + YSIZE >= d.height) {
//	      y = d.height - YSIZE;
//	      dy = -dy;
//	    }
//	    g.setColor(Constants.HEADER_COLOR);
//	    g.fillRect(x, y, XSIZE, YSIZE);
	   
//	    g.dispose();
	  }

	  public void run() {
		  int counter = 0;
		  try {
			  while( !Thread.currentThread().isInterrupted() ) {
				  repaint();
				  counter++;
				  Thread.sleep(5);
				  if (counter > 100){
					  counter = 0;
					  refreshLogArea();
				  }
			  }
		  }
		  catch (InterruptedException e) {
		  }  
	  }
	  
	  private void refreshLogArea(){
		  JTextArea log = _parent_waiting_panel.getSubmissionLog();
		  ArrayList<String> job_log = _parent_waiting_panel.getJob().getJobLog();
		  if(job_log != null){
			  String[] content = job_log.toArray(new String[job_log.size()]);
			  StringBuilder text = new StringBuilder();
			  for (int i = 0; i < content.length; i++){
				  text.append(content[i]+"\n");
			  }
			  log.setText(text.toString());
		  }
	  } 
}
