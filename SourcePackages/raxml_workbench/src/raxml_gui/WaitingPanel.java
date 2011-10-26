package raxml_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WaitingPanel  extends JPanel implements ActionListener{
	
	private Job _job;
	private Configuration _configuration;
	
	private JLabel _text;
	private LoadingAnimation _animation;
	private JTextArea  _log;
	private JButton _abort_button;
	
	public WaitingPanel(LayoutManager mgr, Job parent){
		_job = parent;
		_configuration = _job.getMainFrame().getConfiguration();
		this.setLayout(mgr);
		buildForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == _abort_button){
			_job.abortSubmissionThread();
		}
	}
	
	private void buildForm(){
		GridBagConstraints c  = new GridBagConstraints();
		int y = -1;
		this.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.CENTER;
		//Info Text at the Top
		y++;
		_text = new JLabel("Your Job is running. Your results will appear in this window after RAxML is finished.");
		_text.setFont(Constants.HEADER_FONT);
		_text.setOpaque(true);
		_text.setHorizontalAlignment(SwingConstants.CENTER);
		_text.setBackground(Constants.BACKGROUND_COLOR);
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 3;
		this.add(_text,c);
		
		// Empty space at the left of the animation
		y++;
		c.gridwidth = 1;
		JPanel empty_left= new JPanel();
		empty_left.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 0.2;
		this.add(empty_left,c);
		
		// Waiting Animation
		_animation = new LoadingAnimation(this);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 0.6;
		c.weighty= 0.5;
		this.add(_animation,c);
		
		// Empty space on the right of the animation
		JPanel empty_right= new JPanel();
		empty_right.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = y;
		c.weightx = 0.2;
		this.add(empty_right,c);
		
		// Log visualization
		y++;
		JScrollPane pane = new JScrollPane();
		
		_log = new JTextArea();
		_log.setText("");
		_log.setEditable(false);
		pane.setPreferredSize(new Dimension(300, 300));
		_log.setBackground(Constants.HEADER_COLOR);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 0.2;
		c.weighty= 0.5;
		pane.setViewportView(_log);
		this.add(pane,c);
		
		//Abort Button
		y++;
		_abort_button = new JButton("Abort Job");
		_abort_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,0,0,0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_abort_button,c);
		
	}
	
	public LoadingAnimation getLoadingAnimation(){
		return _animation;
	}
	
	public JTextArea getSubmissionLog(){
		return _log;
	}
	
	public Job getJob(){
		return _job;
	}

	
}
