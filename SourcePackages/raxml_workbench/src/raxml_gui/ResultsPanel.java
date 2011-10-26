package raxml_gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import org.forester.archaeopteryx.ExternalMainFrameApplication;
import org.forester.archaeopteryx.ExternalTreeviewerStarter;

public class ResultsPanel extends JPanel{
	
	private JScrollPane _jobfiles_scroll_pane;
	private JList _list_jobfiles;
	private JScrollPane _file_content_scroll_pane;
	private JPanel _show_file_content;
	private JTextArea _file_text_content;
	private JTable _file_table_content;
	private Job _job;
	private MainFrame _main_frame;
	private String _jobpath;
	
	public ResultsPanel(LayoutManager mgr, Job parent, String jobpath){
		_job = parent;
		_main_frame = _job.getMainFrame();
		_jobpath = jobpath;
		this.setLayout(mgr);	
		
		GridBagConstraints c = new GridBagConstraints();
		JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitter.setSize(new Dimension(1000,400));
		_show_file_content = new JPanel(new GridBagLayout());
		_file_text_content = new JTextArea();
		_file_text_content.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		_jobfiles_scroll_pane = new JScrollPane();
		_file_content_scroll_pane = new JScrollPane();
		_list_jobfiles = new JList(new String[0]);
		_list_jobfiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Configures MouseClicked Action when an item in the JList is pressed
		_list_jobfiles.addMouseListener(new ResultsMouseAdapter()); 
		
		_jobfiles_scroll_pane.setViewportView(_list_jobfiles);
		
		splitter.setDividerLocation(0.2);
		splitter.setLeftComponent(_jobfiles_scroll_pane);
		splitter.setRightComponent(_file_content_scroll_pane);
		
		
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		updateJobFiles();
		this.add(splitter,c);
	}
	
	
	private  String[] getJobFiles(){
		boolean raxml_info_file_is_present = false;
		boolean phyloxml_file_is_present = false;
		File job_folder = new File (_jobpath);
		File[] job_files = job_folder.listFiles();
		Arrays.sort(job_files);
		String[] files = new String[job_files.length];
		for (int i = 0; i < job_files.length; i++){
			if (!job_files[i].isDirectory()){
				if (Util.matches(job_files[i].getName(),"RAxML_info")){
					raxml_info_file_is_present = true;
				}
				if (Util.matches(job_files[i].getName(),"\\.phyloxml")){
					phyloxml_file_is_present = true;
				}
				files[i] = job_files[i].getName();
			}
		}
		if (raxml_info_file_is_present || phyloxml_file_is_present){
			return files;
		}
		else{
			return new String[0];
		}
	}
	
	private void updateJobFiles(){
		_list_jobfiles.setListData(getJobFiles());
	}
	
	private class ResultsMouseAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent evt) {
			String filename = _jobpath+_list_jobfiles.getSelectedValue();
			// Files displayed as tables
			
			if (filename.indexOf("RAxML_classification") != -1){
				ClassificationfileParser cp = new ClassificationfileParser(filename);
				if(cp.getAmountOfTokens() > 3){ //Complete Classificationfile
					String[] column_names = {"Name","Branch","Weight","Sum of weights"};
					_file_table_content = cp.getTableWithData(column_names);
					_file_table_content.setAutoCreateRowSorter(true);
					_file_table_content.setRowHeight(20);
					_file_table_content.setRowMargin(2);
					_file_table_content.setShowHorizontalLines(false);
			        TableColumn column = null;
			        for (int i = 0; i < column_names.length; i++) {
			            column = _file_table_content.getColumnModel().getColumn(i);
			            if (i == 2) {
			            	column.setPreferredWidth(20);
			            } 
			            else if (i == 1){
			                column.setPreferredWidth(20);
			            }
			        }
					_file_content_scroll_pane.setViewportView(_file_table_content);
					_file_content_scroll_pane.validate();
					_file_content_scroll_pane.repaint();
				}
				else{ // Classificationfile with best placements
					String[] column_names = {"Name","Branch","Weight"};
					_file_table_content = cp.getTableWithData(column_names);
					_file_table_content.setAutoCreateRowSorter(true);
					_file_table_content.setRowHeight(20);
					_file_table_content.setRowMargin(2);
					_file_table_content.setShowHorizontalLines(false);
			        TableColumn column = null;
			        for (int i = 0; i < column_names.length; i++) {
			            column = _file_table_content.getColumnModel().getColumn(i);
			            if (i == 2) {
			            	column.setPreferredWidth(20);
			            } 
			            else if (i == 1){
			                column.setPreferredWidth(20);
			            }
			        }
					_file_content_scroll_pane.setViewportView(_file_table_content);
					_file_content_scroll_pane.validate();
					_file_content_scroll_pane.repaint();
				}
			}
			// if phyloxml file
			else if (filename.indexOf(".phyloxml") != -1){
				_file_content_scroll_pane.setViewportView(null);
				_job.getMainFrame().getWorkflowPanel().runTreeviewer(filename);
			}
			// files displayed as simple text
			else { 
				_file_content_scroll_pane.validate();
				ArrayList<String> text = Util.readFileToList(filename);
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < text.size(); i++){
					b.append(text.get(i)+"\n");
				}
				_file_text_content.setText(b.toString());
				_file_content_scroll_pane.setViewportView(_file_text_content);
				_file_content_scroll_pane.validate();
				_file_content_scroll_pane.repaint();
			}
        }
	 // end MouseAdapter Configuration
	}
}
