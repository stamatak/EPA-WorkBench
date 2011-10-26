package raxml_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class ControlPanel extends JPanel{
	
	private JFrame _main_frame;
	private ArrayList <String> _finished_jobs = new ArrayList<String>();
	private ArrayList <String> _running_jobs = new ArrayList<String>();
	private GridBagConstraints _c  = new GridBagConstraints();
	public ControlPanel(JFrame parent) {
		_main_frame = parent;
//		this.setBounds(200, 200, 200, 200);
//		this.setPreferredSize(new Dimension(200, 800));
		this.setBackground(Constants.BACKGROUND_COLOR);
//		this.setLayout(new BorderLayout());
		this.setLayout(new GridBagLayout());
		_c.anchor = GridBagConstraints.NORTHWEST;
		_c.fill = GridBagConstraints.BOTH;
		
		buildControlOptionsPanel();
		buildControlJobsPanel();
		//buildControlFinishedJobsPanel();
		//buildControlRunningJobsPanel();
	}
	
	public String[] scanForFinishedJobs(){
		_finished_jobs.add("job1_f");
		_finished_jobs.add("job2_f");
		_finished_jobs.add("job3_f");
		String[] finished_jobs = new String[_finished_jobs.size()];
		for (int i = 0; i < _finished_jobs.size(); i++){
			finished_jobs[i]  = _finished_jobs.get(i);
		}
		return finished_jobs;
	}
	
	public String[] scanForRunningJobs(){
		_running_jobs.add("job1_r");
		_running_jobs.add("job2_r");
		_running_jobs.add("job3_r");
		String[] running_jobs = new String[_running_jobs.size()];
		for (int i = 0; i < _running_jobs.size(); i++){
			running_jobs[i]  = _running_jobs.get(i);
		}
		return running_jobs;
	}
	
	private void buildControlOptionsPanel(){
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton new_job = new JButton("New Job Submission");
		JButton open = new JButton("Open");
		JButton save = new JButton("Save");
		toolbar.add(new_job);
		toolbar.add(open);
		toolbar.add(save);
		_c.insets = new Insets(0,2,1,2);
		_c.gridx = 0;
		_c.gridy = 0;
		this.add(toolbar,_c);
	}
	
	private void buildControlFinishedJobsPanel(){
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitter.setSize(new Dimension(200,400));
		
		JScrollPane list_finished_jobs = new JScrollPane();
//		list_finished_jobs.setSize(50, 600);
		JList list = new JList(scanForFinishedJobs());
		list_finished_jobs.setViewportView(list);
		JButton delete_jobs = new JButton("Delete Job(s)");
//		delete_jobs.setPreferredSize(new Dimension(20,100));
		splitter.setDividerLocation(0.9);
		splitter.setTopComponent(list_finished_jobs);
		splitter.setBottomComponent(delete_jobs);
		
		this.add(splitter, BorderLayout.CENTER);
	}
	
	private void buildControlRunningJobsPanel(){
//		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
//		
//		JScrollPane list_running_jobs = new JScrollPane();
//		list_running_jobs.setSize(50, 300);
//		JList list = new JList(scanForRunningJobs());
//		list_running_jobs.setViewportView(list);
//		
//		JButton abort_jobs = new JButton("Abort Job(s)");
////		abort_jobs.setPreferredSize(new Dimension(50,100));
//		
//		splitter.setTopComponent(list_running_jobs);
//		splitter.setBottomComponent(abort_jobs);
//		splitter.setDividerLocation(0.5);
//		
//		this.add(splitter, BorderLayout.SOUTH);
	}
	private void buildControlJobsPanel(){
		JList finished_jobs = new JList(scanForFinishedJobs());
		JScrollPane list_finished_jobs = new JScrollPane();
		list_finished_jobs.setViewportView(finished_jobs);
		_c.insets = new Insets(0,1,0,1);
		_c.weighty = 0.5;
//		_c.fill = GridBagConstraints.BOTH;
		_c.gridx = 0;
		_c.gridy = 1;
		this.add(list_finished_jobs, _c);
		JButton delete_jobs_button = new JButton("Delete Job(s)");
		_c.insets = new Insets(0,1,4,2);
		_c.weighty = 0.01;
		_c.gridx = 0;
		_c.gridy = 2;
		this.add(delete_jobs_button,_c);
		
		
		JList running_jobs = new JList(scanForRunningJobs());
		JScrollPane list_running_jobs = new JScrollPane();
		list_running_jobs.setViewportView(running_jobs);
		_c.insets = new Insets(0,1,0,1);
		_c.weighty = 0.5;
		_c.fill = GridBagConstraints.BOTH;
		_c.gridx = 0;
		_c.gridy = 4;
		this.add(list_running_jobs, _c);
		JButton abort_jobs_button = new JButton("Abort Job(s)");
		_c.insets = new Insets(0,1,0,2);
		_c.weighty = 0.01;
		_c.gridx = 0;
		_c.gridy = 5;
		this.add(abort_jobs_button,_c);
	}
	
	public JFrame getMainFrame(){
		return _main_frame;
	}
	
}
