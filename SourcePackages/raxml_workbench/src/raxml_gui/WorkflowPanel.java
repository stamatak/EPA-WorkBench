package raxml_gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.TableColumn;

import org.forester.archaeopteryx.ExternalMainFrameApplication;
import org.forester.archaeopteryx.ExternalTreeviewerStarter;
import org.forester.archaeopteryx.MainFrameApplication;
import org.forester.io.parsers.phyloxml.PhyloXmlParser;
import org.forester.phylogeny.factories.ParserBasedPhylogenyFactory;

public class WorkflowPanel extends JPanel{
	
	
	private JTabbedPane _job_tabs;
	private MainFrame _main_frame;
	private Job _usable_submission_panel;
	
	private ExternalTreeviewerStarter _treeviewer;
	
	public WorkflowPanel(MainFrame parent) {
		_main_frame = parent;
		this.setBounds(200, 200, 200, 200);
		this.setBackground(Constants.BACKGROUND_COLOR);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c  = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		
		_job_tabs = new JTabbedPane();
		
		Job first_job = new Job(_main_frame);
		_job_tabs.addTab("", first_job);
		_job_tabs.setTabComponentAt(0,buildTab(_job_tabs,"Submit Job", first_job));
		_job_tabs.setSelectedIndex(0);
		_usable_submission_panel = first_job;
		this.add(_job_tabs,c);
	}
	
	public void newJobSubmission(){
		if (_usable_submission_panel != null){
			_job_tabs.setSelectedComponent(_usable_submission_panel);
		}
		else{
			Job new_job = new Job(_main_frame);
			_job_tabs.addTab("", new_job);
			int index = _job_tabs.getTabCount()-1;
			_job_tabs.setTabComponentAt(index,buildTab(_job_tabs,"Submit Job", new_job));
			_job_tabs.setSelectedIndex(index);
			_usable_submission_panel = new_job;
		}
	}
	
	public void newResultsPanel(){
		if (_main_frame.getConfiguration().getWorkspace() != null){
			Job results = new Job(_main_frame);
			_job_tabs.addTab("", results);
			results.switchToResultsPanel();
			int index = _job_tabs.getTabCount()-1;
			_job_tabs.setTabComponentAt(index,buildTab(_job_tabs,_main_frame.getConfiguration().getJobName(), results));
			_job_tabs.setSelectedIndex(index);
		}
	}
	
	public static JPanel buildTab(JTabbedPane pane, String title, Job res){
		JPanel panel = new JPanel();
		JLabel label = new JLabel(title);
		TabButton button = new TabButton(pane,res);
		panel.add(label);
		panel.add(button);
		panel.setOpaque(false);
		return panel;
		
	}

	void runTreeviewer(String filename){
		if (_treeviewer == null || !_treeviewer.getExternalMainFrameApplication().isVisible()){
		_treeviewer = new ExternalTreeviewerStarter(Constants.ARCHAEOPTERYX_CONFIGURATIONFILE.getAbsolutePath(), filename);
		}
		else{
			((ExternalMainFrameApplication)_treeviewer.getExternalMainFrameApplication()).addNewTreefileTab(filename);
		}
	}

	public void setUsableSubmissionPanel(Job submission){
		_usable_submission_panel = submission;
	}
	
	public Job getUsableSubmissionPanel(){
		return _usable_submission_panel;
	}
	
	public MainFrame getMainFrame(){
		return _main_frame;
	}
	public JTabbedPane getTabs(){
		return _job_tabs;
	}
	
}
