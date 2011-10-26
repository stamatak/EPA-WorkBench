package raxml_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class MainFrame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ControlPanel _control_panel;
	private WorkflowPanel _workflow_panel;
	private Configuration _configuration;
	private PreferencesPanel _prefs_panel;
	
	private JFileChooser _filechooser;
	
	// The Menu
	JMenuBar			_menu_bar;
	// File Menu
	JMenu 				_file_menu;
	JMenuItem			_new_job;
	JMenuItem			_open_job;
	JMenuItem			_open_phyloxml;
	JMenuItem			_preferences;
	JMenuItem			_exit;

	// Help Menu
	JMenu				_help_menu;
	JMenuItem			_help;
	JMenuItem			_about;
	
	JFrame 				_about_frame;
	
	public MainFrame(String title) {
		_filechooser = new JFileChooser();
		_filechooser.setPreferredSize(Constants.FILECHOOSER_SIZE);
		// load custom preferences from raxml_wb.conf if file is available or load default preferences
		loadConfiguration();
		_prefs_panel = new PreferencesPanel(new GridBagLayout(), _configuration);
		// check if the loaded preferences are valid
		_prefs_panel.resetPreferences();
		this.setTitle(title);
		this.getContentPane().setLayout(new BorderLayout());
	        
		// Build Menu Bar
		_menu_bar = new JMenuBar();
		buildFileMenu();
		buildHelpMenu();
		setJMenuBar(_menu_bar);
	        
		// ControlPanel
//		_control_panel = new ControlPanel(this);
//		this.add(_control_panel, BorderLayout.WEST);
	        
		// WorkflowPanel
		_workflow_panel = new WorkflowPanel(this);
//		JScrollPane main_scroll_pane = new JScrollPane(_workflow_panel);
//		main_scroll_pane.setViewportView(_workflow_panel);
//		this.add(main_scroll_pane);
		this.add(_workflow_panel);
//		this.add(_workflow_panel, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(Constants.MAIN_FRAME_WIDTH,Constants.MAIN_FRAME_HEIGHT));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				int confirmed = JOptionPane.showConfirmDialog(_workflow_panel.getMainFrame(), "Exit RAxML Workbench?", "Exit?", JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
		
	        
	}
	 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _open_job){
			showChooseJobDir();
			if (_configuration.getWorkspace() != null){
				if (checkForJobfiles(_configuration.getWorkspace())){
					_workflow_panel.newResultsPanel();
				}
				else{
					JOptionPane.showMessageDialog(this, "There are no RAxML job files within the chosen folder!", "Message", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		else if (e.getSource() == _new_job){
			_workflow_panel.newJobSubmission();
		}
		else if (e.getSource() == _open_phyloxml){
			String file = showChooseFile();
			if (file != null){
				_workflow_panel.runTreeviewer(file);
			}
		}
		else if (e.getSource() == _preferences){
			showPreferencesPanel();
		}
		
		else if (e.getSource() == _exit){
			int click = JOptionPane.showConfirmDialog(this, "Exit RAxML Workbench?", "Exit?", JOptionPane.YES_NO_OPTION);
			if(click == JOptionPane.YES_OPTION){
				System.exit(0);
			}
		}
		else if (e.getSource() == _help){
			showHelp();
		}
		else if (e.getSource() == _about){
			showAbout();
		}
	}
	
	private void buildFileMenu(){
		_file_menu = createMenu("File");
		_new_job = new JMenuItem("New Submission");
		_new_job.addActionListener(this);
		_file_menu.add(_new_job);
		
		_open_job = new JMenuItem("Open Job");
		_open_job.addActionListener(this);
		_file_menu.add(_open_job);
		
		_open_phyloxml = new JMenuItem("Open PhyloXML-file in Treeviewer");
		_open_phyloxml.addActionListener(this);
		_file_menu.add(_open_phyloxml);
		
		_preferences = new JMenuItem("Preferences");
		_preferences.addActionListener(this);
		_file_menu.add(_preferences);
		
		_exit = new JMenuItem("Exit");
		_exit.addActionListener(this);
		_file_menu.add(_exit);
		_menu_bar.add(_file_menu);		
	 }	 
	 
	private void buildHelpMenu(){
		_help_menu = createMenu("Help");
		_help = new JMenuItem("Help");
		_help.addActionListener(this);
		_help_menu.add(_help);
			
		_about = new JMenuItem("About");
		_about.addActionListener(this);
		_help_menu.add(_about);
		_menu_bar.add(_help_menu);
	}
		
	private JMenu createMenu(String title){
		JMenu menu = new JMenu(title);
		// add Color & Font Customization here
		return menu;
	}
			 
	public ControlPanel getControlPanel(){
		return _control_panel;
	}
	
	public WorkflowPanel getWorkflowPanel(){
		return _workflow_panel;
	}
	
	public Configuration getConfiguration(){
		return _configuration;
	}
	
	private void loadConfiguration(){
		_configuration = new Configuration(this);
		if (Constants.CONFIGURATION.exists()){
			_configuration.loadConfigurationFromFile();
		}
	}

	private void showChooseJobDir(){
		_filechooser.setPreferredSize(Constants.FILECHOOSER_SIZE);
		_filechooser.setMultiSelectionEnabled(false);
		_filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int result = _filechooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION){ //OK is pressed
			if (_filechooser.getSelectedFile() != null){
				_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
				String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
				_configuration.setWorkspace(selected_file);
			}
		}
		else {
			_configuration.setWorkspace(null);
			return;
		}
	}
	
	private String showChooseFile(){
		_filechooser.setPreferredSize(Constants.FILECHOOSER_SIZE);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			PhyloXMLParser p = new PhyloXMLParser(selected_file);
			if (p.checkFormat()){
				return selected_file;
			}
			else {
				JOptionPane.showMessageDialog(this,"Selected file: "+selected_file+" is not in a valid phyloXML format!","There some errors in your input!",JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		return null;
	}
	
	private void showHelp(){
		Desktop desktop = Desktop.getDesktop();
		try {
			File help = Constants.HELP;
			if (help.exists()){
				desktop.open(help);
			}
			else{
				
				JOptionPane.showMessageDialog(this,"The help file is not present!","An Error Occured!",JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,"You have no PDF viewer installed. We recommend that download and install the Acrobat Reader from here: "+Constants.PDF_READER_LINK+".","An Error Occured!",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void showPreferencesPanel(){
		String[] opts = {"Save","Cancel"};
		int selected = JOptionPane.showOptionDialog(this, _prefs_panel, "Preferences", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, opts,opts[1] );
		if (selected == JOptionPane.YES_OPTION){
			_prefs_panel.collectRemainingPreferences();
			if(_prefs_panel.hasValidPreferences()){
				_prefs_panel.savePreferences();
				_workflow_panel.getUsableSubmissionPanel().enableUclust(_configuration.getUclustPath() != null);
				_configuration.writeToFile();
			}
			else{
				_prefs_panel.showErrors();
				showPreferencesPanel();
			}
		}
		else{
			_prefs_panel.resetPreferences();
		}
	}
	
	public boolean checkForJobfiles(String path){
		boolean raxml_info_file_is_present = false;
		boolean phyloxml_file_is_present = false;
		File job_folder = new File (path);
		File[] job_files = job_folder.listFiles();
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
			return true;
		}
		else{
			return false;
		}
	}
	
	
	private void showAbout(){
//		String about = "" +
//				"<html><body>" +
//				"RAxML Workbench<br/>" +
//				"<br/>" +
//				"Version: "+Constants.VERSION+"<br/>" +
//				"<br/>" +
//				"(c) RAxML Workbench contributors.<br/>" +
//				"<br/>" +
//				"<b>RAxML-EPA:</b><ul><li> S.A. Berger, A. Stamatakis, Evolutionary Placement of Short Sequence Reads. <a href=\"http://arxiv.org/abs/0911.2852v1\" target=\"_blank\">arXiv:0911.2852v1</a> [q-bio.GN](2009)</li></ul>" +
//				"<b>Archaeopteryx Treeviewer:</b><ul><li>Han, Mira V.; Zmasek, Christian M. (2009). phyloXML: XML for evolutionary biology and comparative genomics. BMC Bioinformatics (United Kingdom: BioMed Central) 10: 356. doi:10.1186/1471-2105-10-356. <a href=\"http://www.biomedcentral.com/1471-2105/10/356\" target=\"_blank\">http://www.biomedcentral.com/1471-2105/10/356</a></li>" +
//				"<li>Zmasek, Christian M.; Eddy, Sean R. (2001). ATV: display and manipulation of annotated phylogenetic trees. Bioinformatics (United Kingdom: Oxford Journals) 17 (4): 383–384. <a href=\"http://bioinformatics.oxfordjournals.org/cgi/reprint/17/4/383\" target=\"_blank\">http://bioinformatics.oxfordjournals.org/cgi/reprint/17/4/383</a></li></ul>" +
//				"<b>EDPL:</b><ul><li>Frederick A Matsen, Robin B Kodner and E Virginia Armbrust, pplacer: linear time maximum-likelihood and Bayesian phylogenetic placement of sequences onto a fixed reference tree. <a href=\"http://arxiv.org/abs/1003.5943v1\" target=\"_blank\">arXiv:1003.5943v1</a>  [q-bio.PE]</li></ul>" +
//				"<b>Hmmer:</b><ul><li>S. R. Eddy., A New Generation of Homology Search Tools Based on Probabilistic Inference. Genome Inform., 23:205-211, 2009.</li></ul>" +
//				"<b>uclust:</b><ul><li><a href=\"http://www.drive5.com/uclust\" target=\"_blank\">http://www.drive5.com/uclust</a></li></ul>" +
//				"</body></html>";
		_about_frame = new JFrame("About");
		_about_frame.getContentPane().setLayout(new BorderLayout());
		URL content = null;
		final JEditorPane about_text = new JEditorPane();
		try {
			about_text.setContentType("text/html");
			content = Constants.ABOUT.toURI().toURL();
			about_text.setPage(content);
			HyperlinkListener h_listener = new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						try {
							Desktop desktop = Desktop.getDesktop();
							URL u = e.getURL();
							desktop.browse(u.toURI());
						} catch (IOException ioe) {
							Util.printErrors(ioe);
						}
						catch (URISyntaxException e1) {
							Util.printErrors(e1);
						}
					}

				}
			};
			about_text.setEditable(false);
			about_text.setBackground(Color.white);
			about_text.setOpaque(true);
			about_text.setPreferredSize(new Dimension(Constants.ABOUT_FRAME_WIDTH,Constants.ABOUT_FRAME_HEIGHT));
			about_text.addHyperlinkListener(h_listener);
			JScrollPane scroll_pane = new JScrollPane(about_text);
			
//			scroll_pane.setPreferredSize(new Dimension(200,200));
			_about_frame.add(scroll_pane, BorderLayout.CENTER);
			_about_frame.pack();
			_about_frame.setVisible(true);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.printErrors(e);
			Util.printSomething(content.getFile());
		}
		
	}
	
	public JFileChooser getFilechooser(){
		return _filechooser;
	}
	
}
 