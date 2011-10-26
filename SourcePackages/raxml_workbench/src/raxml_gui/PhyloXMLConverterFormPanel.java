package raxml_gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import raxml_gui.MyRunnable.Jobtype;

public class PhyloXMLConverterFormPanel extends JPanel implements ActionListener {
private Job _job;
	
	//BUTTONS, TEXTFIELDS, LABELS, etc. 
	
	private JLabel _phyloXMLConverter_headline;
	private JLabel _classificationfile_label;
	private JTextField _classificationfile_textfield;
	private JButton _classificationfile_button;
	
	private JLabel _treefile_label;
	private JTextField _treefile_textfield;
	private JButton _treefile_button ;
	private JTextField _jobname_textfield;
	private JLabel _save_as_label;
	private JTextField _save_as_textfield;
	private JButton _save_as_button ;
	private JButton _submit_button;
	private JButton _reset_button;
	private JButton _confirmation_button;
	private JFrame _confirmation_frame; 
	private JFileChooser _filechooser;
	
	//PARAMETERS FOR RAXML
	private HashMap<String,String> _parameters = new HashMap<String,String>();
	private String _classificationfile = "classificationfile";
	private String _treefile = "treefile";
	private String _save_as = "save_as";
	
	//Other
	private ArrayList <String> _commands = new ArrayList<String>();
	private HashMap<String,String> _input_errors = new HashMap<String, String>();
	private Configuration _configuration;
	
	public PhyloXMLConverterFormPanel(LayoutManager mgr, Job parent){
		_job = parent;
		_configuration = _job.getMainFrame().getConfiguration();
		this.setLayout(mgr);
		_parameters.put(_classificationfile,null);
		_parameters.put(_treefile, null);
		_parameters.put(_save_as,null);
		_filechooser = _job.getMainFrame().getFilechooser();
		
		buildForm();
		resetForm();
	}
	
	public void actionPerformed( final ActionEvent e ) {
		// browse alignmentfile button
		if (e.getSource() == _classificationfile_button){
			showChooseClassificationFile();
		}
		// treefile browse button
		else if (e.getSource() == _treefile_button){
			showChooseTreeFile();
		}
		// Save Results Destination browse button
		else if (e.getSource() == _save_as_button){
			showChooseFileToSaveAs();
		}
		// submit button
		else if (e.getSource() == _submit_button){
			validateInput();
			if (_input_errors.size() < 1){
				submitJob(false);
			}
			else{
				showErrors();
			}
			
		}
		else if (e.getSource() == _reset_button){
			resetForm();
		}
		else if (e.getSource() == _confirmation_button){
			_job.getMainFrame().setEnabled(true);
			_confirmation_frame.setVisible(false);
			
		}
		
		
		
	}
	
	private void buildForm(){
		GridBagConstraints c  = new GridBagConstraints();
		int y = -1;
		this.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		//Alignment Headline
		y++;
		_phyloXMLConverter_headline = new JLabel("Input Data");
		_phyloXMLConverter_headline.setFont(Constants.HEADER_FONT);
		_phyloXMLConverter_headline.setOpaque(true);
		_phyloXMLConverter_headline.setBackground(Constants.HEADER_COLOR);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;
		c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 4;
		this.add(_phyloXMLConverter_headline,c);
		c.gridwidth =1;
		//Alignment label
		y++;
		_classificationfile_label = new JLabel("Select RAxML Classificationfile:");
		_classificationfile_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_classificationfile_label,c);
		
		//Alignment Textfield & Button
		_classificationfile_textfield = new JTextField("");
		_classificationfile_textfield.setPreferredSize(new Dimension(200, 20));
		_classificationfile_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_classificationfile_textfield,c);
		_classificationfile_button = new JButton("Browse");
		_classificationfile_button.setPreferredSize(new Dimension(80, 20));
		_classificationfile_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_classificationfile_button.addActionListener(this);
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_classificationfile_button,c);
		
		//treefile label
		y++;
		_treefile_label = new JLabel("Select labeled Treefile:");
		_treefile_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_treefile_label,c);
		
		//Tree Textfield & Button
		_treefile_textfield = new JTextField("");
		_treefile_textfield.setPreferredSize(new Dimension(200, 20));
		_treefile_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_treefile_textfield,c);
		
		_treefile_button = new JButton("Browse");
		_treefile_button.setPreferredSize(new Dimension(80, 20));
		_treefile_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_treefile_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_treefile_button,c);
		
		//Save Location label
		y++;
		_save_as_label = new JLabel("Select Results Destination:");
		_save_as_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_save_as_label,c);
		
		//Save Location Textfield & Button
		_save_as_textfield = new JTextField("");
		_save_as_textfield.setPreferredSize(new Dimension(200, 20));
		_save_as_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_save_as_textfield,c);
		
		_save_as_button = new JButton("Browse");
		_save_as_button.setPreferredSize(new Dimension(80, 20));
		_save_as_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_save_as_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_save_as_button,c);
		
		
		//Submission & Reset button
		y++;
		JPanel submit_reset = new JPanel();
		submit_reset.setBackground(Constants.BACKGROUND_COLOR);
		_submit_button = new JButton("Submit");
		_submit_button.addActionListener(this);
		
		submit_reset.add(_submit_button);
		_reset_button = new JButton("Reset");
		_reset_button.addActionListener(this);
		submit_reset.add(_reset_button);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,20,5,0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(submit_reset,c);
	}
	
	public HashMap<String,String> getParameters(){
		return _parameters;
	}
	
	public HashMap<String,String> getInputErrors(){
		return _input_errors;
	}
	
	private void showChooseClassificationFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_classificationfile_textfield.setText(selected_file);
			_parameters.put(_classificationfile, selected_file);
		}
	}
	
	private void showChooseTreeFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_treefile_textfield.setText(selected_file);
			_parameters.put(_treefile, selected_file);
		}
	}
	
	private void showChooseFileToSaveAs(){
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_save_as_textfield.setText(selected_file);
			_parameters.put(_save_as, selected_file);
		}
	}
	
	public void resetForm(){
		// reset saved parameters
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			_parameters.put(key, null);
		}
		// reset form
		_classificationfile_textfield.setText("");
		_treefile_textfield.setText("");
		_save_as_textfield.setText("");
	}
	
	
	public void submitJob(boolean testing){
		
		_configuration.setWorkspace(new File(_parameters.get(_save_as)).getParentFile().getAbsolutePath());
		// collect shell commands
		Process p;
		ArrayList<String> command = new ArrayList<String>();
		command.add("java");command.add("-jar"); command.add(Constants.CONVERT_TO_PHYLOXML.getAbsolutePath());
		command.add(_parameters.get(_treefile));command.add(_parameters.get(_classificationfile));
		
		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
			
		//execute shellfile
		if (_job.getMainFrame().isVisible()){ // don't show confirmation dialogue when testing
			_job.switchToWaitingPanel();
			_job.getMainFrame().setEnabled(false);
		}
		final PhyloXMLConverterFormPanel form = this;
		MyRunnable r = new MyRunnable(builder, _job,Jobtype.PHY_CONV);
		Thread t = new Thread(r);
		t.start();
		if(testing ) {
			try {
				t.join();
			} catch (InterruptedException e) {
				Util.printErrors(e);
			}
		}
				
	}
	
	public String parametersToString(){
		String s = "";
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			s += key+" : "+_parameters.get(key)+"\n";
		}
		
		return s;
	}
	
	
	public void validateInput(){
		_input_errors.clear();
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			String p = _parameters.get(key);
			// Check alignmentfile
			if (key.equals(_classificationfile) ){
				if (p != null && !p.equals("") && (new File(p)).exists() ){
					ClassificationfileParser c = new ClassificationfileParser(p);
					if (!c.isValidFormat()){
						_input_errors.put(_classificationfile, c.getErrorMessage());
					}
					// check partitionfile if "Partitioned" is selected
				}
				// if classificationfile is missing
				else if (p == null || p.equals("")){
					_input_errors.put(_classificationfile,"Please choose a classificationfile!");
				}
				else if (!( new File(p)).exists()){
					_input_errors.put(_classificationfile,"The selected alignmentfile: "+_parameters.get(_classificationfile)+" does not exist!");
				}
				else{
					_input_errors.put(_classificationfile,"Alignmentfile is not valid!");
				}
			}
			// Check treefile
			else if (key.equals(_treefile) ){
				if (p != null && !p.equals("") && (new File(p)).exists() ){
					TreefileParser t = new TreefileParser(p);
					if (!t.isValidFormat() ){ 
						_input_errors.put(_treefile,t.getErrorMessage());
					}
				}
				else if (p == null || p.equals("")){
					_input_errors.put(_treefile,"Please choose a treefile!");
				}
				else if (!( new File(p)).exists()){
					_input_errors.put(_treefile,"The selected treefile: "+_parameters.get(_treefile)+" does not exist!");
				}
				else{
					_input_errors.put(_treefile,"Treefile is not valid!");
				}
			}
			// save_as file
			else if (key.equals(_save_as) ){
				if ( p != null && !p.isEmpty() && (new File(p).exists()) ){
				}
				else if (p != null && !p.isEmpty() && (new File(p).getParentFile().exists())){
				}
				else{
					_input_errors.put(_save_as, "Please select a destination where the results can be saved!");
				}
			}	
		}
	}
	
	private void showErrors(){
		
		Set <String> errors = _input_errors.keySet();
		for (String error : errors){
			System.out.println(error+ " : " + _input_errors.get(error));
			if (error.equals(_classificationfile)){
				highlightError(_classificationfile_label, _input_errors.get(error));
			}
			else if (error.equals(_treefile)){
				highlightError(_treefile_label, _input_errors.get(error));
			}
			else if (error.equals(_save_as)){
				highlightError(_save_as_label, _input_errors.get(error));
			}
		}
		
		if (_input_errors.get(_classificationfile) == null){
			_classificationfile_label.setForeground(null);
		}
		else if (_input_errors.get(_treefile) == null){
			_treefile_label.setForeground(null);
		}
		else if (_input_errors.get(_save_as) == null){
			_save_as_label.setForeground(null);
		}
		
		if (_job.getMainFrame().isVisible()){ // don't show confirmation dialogue when testing
			JOptionPane.showMessageDialog(this, errorsToString(),"There some errors in your input!",JOptionPane.ERROR_MESSAGE);
        }
	}
	
	private String[] errorsToString(){
		return Util.textWrapping(_input_errors);
//		Set<String> keys = _input_errors.keySet();
//		String[] e = new String[_input_errors.size()];
//		int i = 0;
//		for(String key : keys){
//			e[i] = "* "+_input_errors.get(key); 
//			i++;
//		}
//		return e;
	}
	
	private void highlightError(JLabel field, String error){
		field.setForeground(Constants.ERROR_COLOR);
	}
	
	//  only used by testing methods
	public void setParameters(HashMap <String,String> parameters){
		_parameters = parameters;
	}
}
