package raxml_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.SampleModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SpinnerNumberModel;

import raxml_gui.MyRunnable.Jobtype;

public class SgaFormPanel extends JPanel implements ActionListener {
	
	private Job _job;
	
	//BUTTONS, TEXTFIELDS, LABELS, etc. 
	
	private JLabel _alignment_headline;
	private JLabel _alignment_label;
	private JTextField _alignment_textfield;
	private JButton _alignment_button;
	
	private JLabel _use_reads_label;
	private JCheckBox _use_reads_checkbox;
	private JLabel _reads_label;
	private JTextField _reads_textfield;
	private JButton _reads_button;
	private JLabel _reads_error_label;
	
	private JLabel _use_clustering_label;
	private JCheckBox _use_clustering_checkbox;
	private JLabel _alignment_type_label;
	private JPanel _alignment_types_radios;
	private JRadioButton _dna_radio;
	private JRadioButton _aa_radio;
	private JRadioButton _partitioned_radio;
	private JLabel _rate_het_models_label;
	private JPanel _het_models_dna;
	private JComboBox _het_models_dna_combobox;
	private JPanel _het_models_aa;
	private JComboBox _het_models_aa_combobox;
	private JComboBox _aa_matrices_combobox;
	private JPanel _het_models_partitioned;
	private JComboBox _het_models_partitioned_combobox;
	private JLabel _partitionfile_label;
	private JTextField _partitionfile_textfield;
	private JButton _partitionfile_button;
	private JLabel _use_emp_base_freq_label;
	private JCheckBox _use_emp_base_freq_checkbox;
	private JLabel _tree_headline;
	private JLabel _tree_label;
	private JTextField _tree_textfield;
	private JButton _tree_button ;
	private JLabel _advanced_options_headline;
	private JLabel _cores_label;
	private JSpinner _cores_spinner;
	private JLabel _speed_label;
	private JCheckBox _speed_checkbox;
//	private JLabel _use_bootstrapping_label;
//	private JCheckBox _use_bootstrapping_checkbox;
//	private JLabel _bootstrap_rnd_seed_label;
//	private JTextField _bootstrap_rnd_seed_textfield;
//	private JLabel _bootstrap_samples_label;
//	private JTextField _bootstrap_samples_textfield;
	private JLabel _use_heuristic_label; 
	private JCheckBox _use_heuristic_checkbox;
	private JLabel _heuristic_model_label;
	private JComboBox _heuristic_models_combobox;
	private JLabel _heuristic_number_label;
	private JComboBox _heuristic_numbers_combobox;
	private JLabel _job_options_headline;
	private JLabel _jobname_label;
	private JTextField _jobname_textfield;
	private JLabel _jobfolder_label;
	private JTextField _jobfolder_textfield;
	private JButton _jobfolder_button ;
	private JButton _submit_button;
	private JButton _reset_button;
	private JButton _confirmation_button;
	private JFrame _confirmation_frame; 
	private JFileChooser _filechooser;
	
	//PARAMETERS FOR RAXML
	private HashMap<String,String> _parameters = new HashMap<String,String>();
	private String _alignmentfile = "alignmentfile";
	private String _use_reads = "use_reads";
	private String _readsfile = "readsfile";
	private String _use_clustering = "use_clustering";
	private String _alignmenttype = "alignment_type";
	private String _het_model = "het_model";
	private String _partitionfile = "partitionfile";
	private String _treefile = "treefile";
	private String _cores = "cores";
	private String _fast = "fast";
//	private String _use_bootstrap = "use_bootstrap";
//	private String _random_seed = "random_seed";
//	private String _samples = "samples";
	private String _use_heuristics = "use_heuristics";
	private String _heuristic_model = "heuristic_model";
	private String _heuristic_value = "heuristic_value";
	private String _jobname = "jobname";
	private String _jobfolder = "jobfolder";
	private String _mga = "mga";
	
	//Other
	private String _jobpath;
	private ArrayList <String> _commands = new ArrayList<String>();
	private HashMap<String,String> _input_errors = new HashMap<String, String>();
	private Configuration _configuration;
	
	public SgaFormPanel(LayoutManager mgr, Job parent){
		_job = parent;
		_configuration = _job.getMainFrame().getConfiguration();
		_jobpath = _job.getMainFrame().getConfiguration().getWorkspace()+_parameters.get(_jobname);
		this.setLayout(mgr);
		_parameters.put(_alignmentfile,null);
		_parameters.put(_use_reads, null);
		_parameters.put(_readsfile, null);
		_parameters.put(_use_clustering,null);
		_parameters.put(_alignmenttype, null);
		_parameters.put(_het_model, null);
		_parameters.put(_partitionfile, null);
		_parameters.put(_treefile, null);
		_parameters.put(_cores, null);
		_parameters.put(_fast, "y"); // fast heuristics default
//		_parameters.put(_use_bootstrap, null);
//		_parameters.put(_random_seed, null);
//		_parameters.put(_samples, null);
		_parameters.put(_use_heuristics, null);
		_parameters.put(_heuristic_model, null);
		_parameters.put(_heuristic_value, null);
		_parameters.put(_jobname, null);
		_parameters.put(_jobfolder, null);
		_parameters.put(_mga,null);
		_filechooser = _job.getMainFrame().getFilechooser();
		
		buildForm();
		resetForm();
	}
	
	public void actionPerformed( final ActionEvent e ) {
		// browse alignmentfile button
		if (e.getSource() == _alignment_button){
			showChooseAlignmentFile();
		}
		// use reads button
		else if (e.getSource() == _use_reads_checkbox ){
			if (_use_reads_checkbox.isSelected()){
				showReadsOptions();
			}
			else{
				hideReadsOptions();
			}
		}
		// browse readsfile button
		else if (e.getSource() == _reads_button){
			showChooseReadsFile();
		}
		// clustering checkbox
		else if (e.getSource() == _use_clustering_checkbox){
		}
		// alignmenttype dna radio button
		else if (e.getSource() == _dna_radio){
			showDnaTypeOptions();
			hideAaTypeOptions();
			hidePartitionedTypeOptions();
		}
		// alignmenttype aa radio buttion
		else if (e.getSource() == _aa_radio){
			hideDnaTypeOptions();
			showAaTypeOptions();
			hidePartitionedTypeOptions();
		}
		// alignmenttype partitioned radio button
		else if (e.getSource() == _partitioned_radio){
			hideDnaTypeOptions();
			hideAaTypeOptions();
			showPartitionedTypeOptions();
		}
		// partitionfile browse button
		else if (e.getSource() == _partitionfile_button && _partitioned_radio.isSelected()){
			 showChoosePartitionFile();
		}
		// treefile browse button
		else if (e.getSource() == _tree_button){
			showChooseTreeFile();
		}
		// fast checkbox
		else if (e.getSource() == _speed_checkbox){
			if (_speed_checkbox.isSelected()){
				_use_heuristic_checkbox.setSelected(false);
				hideHeuristicOptions();
			}
		}
//		// bootstrapping checkbox etc
//		else if (e.getSource() == _use_bootstrapping_checkbox){
//			if (_use_bootstrapping_checkbox.isSelected()){
//				_use_heuristic_checkbox.setSelected(false);
//				hideHeuristicOptions();
//				showBootstrapOptions();
//			}
//			else{
//				hideBootstrapOptions();
//			}
//		}
		// heuristic checkbox etc
		else if (e.getSource() == _use_heuristic_checkbox){
			if (_use_heuristic_checkbox.isSelected()){
				_speed_checkbox.setSelected(false);
//				_use_bootstrapping_checkbox.setSelected(false);
				showHeuristicOptions();
//				hideBootstrapOptions();
			}
			else {
//				_speed_checkbox.setSelected(true);
				hideHeuristicOptions();
			}
		}
		// Save Results Destination browse button
		else if (e.getSource() == _jobfolder_button){
			showChooseJobDir();
		}
		// submit button
		else if (e.getSource() == _submit_button){
			collectRemainingParameters();
			validateInput();
			if (_input_errors.size() < 1){
				_job.getMainFrame().getWorkflowPanel().setUsableSubmissionPanel(null);
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
		_alignment_headline = new JLabel("Alignment");
		_alignment_headline.setFont(Constants.HEADER_FONT);
		_alignment_headline.setOpaque(true);
		_alignment_headline.setBackground(Constants.HEADER_COLOR);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;
		c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 4;
		this.add(_alignment_headline,c);
		c.gridwidth =1;
		//Alignment label
		y++;
		_alignment_label = new JLabel("Select Alignmentfile:");
		_alignment_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 20, 0, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_alignment_label,c);
		
		//Alignment Textfield & Button
		_alignment_textfield = new JTextField("");
		_alignment_textfield.setPreferredSize(new Dimension(200, 20));
		_alignment_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_alignment_textfield,c);
		_alignment_button = new JButton("Browse");
		_alignment_button.setPreferredSize(new Dimension(80, 20));
		_alignment_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_alignment_button.addActionListener(this);
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_alignment_button,c);
		
//		//jalview button
//		JButton jalview = new JButton("Show Alignment");
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 2;
//		c.gridy = 0;
//		this.add(jalview,c);
//		
		//use reads label
		y++;
		_use_reads_label = new JLabel("Align reads:");
		_use_reads_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_use_reads_label,c);
		
		//use Reads Checkbox
		_use_reads_checkbox = new JCheckBox("");
		_use_reads_checkbox.addActionListener(this);
		_use_reads_checkbox.setSelected(false);
		_use_reads_checkbox.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = y;
		this.add(_use_reads_checkbox,c);
		
		//Reads label
		y++;
		_reads_label = new JLabel("Select Readsfile:");
		_reads_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		_reads_label.setVisible(false);
		this.add(_reads_label,c);
		
		//Reads Textfield  & Button
		_reads_textfield = new JTextField("");
		_reads_textfield.setPreferredSize(new Dimension(200, 20));
		_reads_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		_reads_textfield.setVisible(false);
		this.add(_reads_textfield,c);
		
		_reads_button = new JButton("Browse");
		_reads_button.setPreferredSize(new Dimension(80, 20));
		_reads_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_reads_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		_reads_button.setVisible(false);
		this.add(_reads_button,c);
		
		//use clustering label
		y++;
		_use_clustering_label = new JLabel("Cluster reads:");
		_use_clustering_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		_use_clustering_label.setVisible(false);
		this.add(_use_clustering_label,c);
		
		//use clustering Checkbox
		_use_clustering_checkbox = new JCheckBox("");
		_use_clustering_checkbox.addActionListener(this);
		_use_clustering_checkbox.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = y;
		_use_clustering_checkbox.setVisible(false);
		this.add(_use_clustering_checkbox,c);
		
		//Alignment Input Type Label
		y++;
		_alignment_type_label = new JLabel("Alignment Type:");
		_alignment_type_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(7, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_alignment_type_label,c);
		
		//Alignment Input Type Radios
		ButtonGroup alignment_types_radiogrp= new ButtonGroup();
		_dna_radio = new JRadioButton("DNA");
		_dna_radio.setSelected(true);
		_dna_radio.setBackground(Constants.BACKGROUND_COLOR);
		_dna_radio.addActionListener(this);
		_aa_radio = new JRadioButton("AA");
		_aa_radio.addActionListener(this);
		_aa_radio.setBackground(Constants.BACKGROUND_COLOR);
		_partitioned_radio = new JRadioButton("Partitioned");
		_partitioned_radio.addActionListener(this);
		_partitioned_radio.setBackground(Constants.BACKGROUND_COLOR);
		alignment_types_radiogrp.add(_dna_radio);
		alignment_types_radiogrp.add(_aa_radio);
		alignment_types_radiogrp.add(_partitioned_radio);
		_alignment_types_radios = new JPanel();
		_alignment_types_radios.setBackground(Constants.BACKGROUND_COLOR);
		_alignment_types_radios.add(_dna_radio);
		_alignment_types_radios.add(_aa_radio);
		_alignment_types_radios.add(_partitioned_radio);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_alignment_types_radios,c);
		
		//Rate Heterogenity Models Label
		y++;
		_rate_het_models_label = new JLabel("Rate Heterogenity Model:");
		_rate_het_models_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(10, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_rate_het_models_label,c);
		
		//Rate Heterogenity Models DNA
		_het_models_dna = new JPanel();
		_het_models_dna.setBackground(Constants.BACKGROUND_COLOR);
		Vector <String> models_dna = Constants.DNA_HET_MODELS;
		_het_models_dna_combobox = new JComboBox(models_dna);
		_het_models_dna.add(_het_models_dna_combobox);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_het_models_dna,c);
		
		//Rate Heterogenity Models aa
		_het_models_aa = new JPanel();
		_het_models_aa.setBackground(Constants.BACKGROUND_COLOR);
		Vector <String> models_aa = Constants.AA_HET_MODELS;
		_het_models_aa_combobox = new JComboBox(models_aa);
		Vector <String> matrices = Constants.AA_MATRICES;
		_aa_matrices_combobox = new JComboBox(matrices);
		_het_models_aa.add(_het_models_aa_combobox);
		_het_models_aa.add(_aa_matrices_combobox);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		_het_models_aa.setVisible(false);
		this.add(_het_models_aa,c);
		
		//Rate Heterogenity Models partitioned
		_het_models_partitioned = new JPanel();
		_het_models_partitioned.setBackground(Constants.BACKGROUND_COLOR);
		Vector <String> models_partitioned = Constants.PAR_HET_MODELS;
		_het_models_partitioned_combobox = new JComboBox(models_partitioned);
		_het_models_partitioned.add(_het_models_partitioned_combobox);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		_het_models_partitioned.setVisible(false);
		this.add(_het_models_partitioned,c);
		
		//Partitionfile Label
		y++;
		_partitionfile_label = new JLabel("Select Partitionfile:");
		_partitionfile_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		_partitionfile_label.setVisible(false);
		this.add(_partitionfile_label,c);
		
		//Partitionfile Textfield & Button
		_partitionfile_textfield = new JTextField("");
		_partitionfile_textfield.setPreferredSize(new Dimension(200, 20));
		_partitionfile_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		_partitionfile_textfield.setVisible(false);
		this.add(_partitionfile_textfield,c);
		
		_partitionfile_button = new JButton("Browse");
		_partitionfile_button.setPreferredSize(new Dimension(80, 20));
		_partitionfile_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_partitionfile_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		_partitionfile_button.setVisible(false);
		this.add(_partitionfile_button,c);
		
		//use empirical base frequencies label
		_use_emp_base_freq_label = new JLabel("Empirical Base Frequencies:");
		_use_emp_base_freq_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		_use_emp_base_freq_label.setVisible(false);
		this.add(_use_emp_base_freq_label,c);
		
		//use empirical base frequencies checkbox
		_use_emp_base_freq_checkbox = new JCheckBox("");
		_use_emp_base_freq_checkbox.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = y;
		_use_emp_base_freq_checkbox.setVisible(false);
		this.add(_use_emp_base_freq_checkbox,c);
		
		//Tree Headline
		y++;
		_tree_headline = new JLabel("Tree");
		_tree_headline.setFont(Constants.HEADER_FONT);
		_tree_headline.setOpaque(true);
		_tree_headline.setBackground(Constants.HEADER_COLOR);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 3;
		this.add(_tree_headline,c);
		c.gridwidth = 1;
		
		//tree label
		y++;
		_tree_label = new JLabel("Select Treefile:");
		_tree_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_tree_label,c);
		
		//Tree Textfield & Button
		_tree_textfield = new JTextField("");
		_tree_textfield.setPreferredSize(new Dimension(200, 20));
		_tree_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_tree_textfield,c);
		
		_tree_button = new JButton("Browse");
		_tree_button.setPreferredSize(new Dimension(80, 20));
		_tree_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_tree_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_tree_button,c);
		
		//Advanced Options Headline
		y++;
		_advanced_options_headline = new JLabel("Advanced Options");
		_advanced_options_headline.setFont(Constants.HEADER_FONT);
		_advanced_options_headline.setOpaque(true);
		_advanced_options_headline.setBackground(Constants.HEADER_COLOR);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 3;
		this.add(_advanced_options_headline,c);
		c.gridwidth = 1;
		
		//cores label
		y++;
		_cores_label = new JLabel("Cores:");
		_cores_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(16, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_cores_label,c);
		
		//cores label Spinner
		_cores_spinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(15, 0, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_cores_spinner,c);
		
		//speed label
		y++;
		_speed_label = new JLabel("Fast:");
		_speed_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_speed_label,c);
		
//		//// No Speedbox anymore
//		//speed checkbox
//		_speed_checkbox = new JCheckBox("");
//		_speed_checkbox.addActionListener(this);
//		_speed_checkbox.setBackground(Constants.BACKGROUND_COLOR);
//		c.anchor = GridBagConstraints.NORTHWEST;
//		c.fill = GridBagConstraints.NONE;
//		c.insets = new Insets(0, 0, 5, 0);
//		c.gridx = 1;
//		c.gridy = y;
//		this.add(_speed_checkbox,c);

//		//use bootstrapping label
//		y++;
//		_use_bootstrapping_label = new JLabel("Bootstrapping:");
//		_use_bootstrapping_label.setFont(Constants.LABEL_FONT);
//		c.anchor = GridBagConstraints.NORTHEAST;
//		c.insets = new Insets(2, 0, 5, 15);
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 0;
//		c.gridy =y;
//		this.add(_use_bootstrapping_label,c);
//		
//		//use bootstrapping checkbox
//		_use_bootstrapping_checkbox = new JCheckBox("");
//		_use_bootstrapping_checkbox.addActionListener(this);
//		_use_bootstrapping_checkbox.setBackground(Constants.BACKGROUND_COLOR);
//		c.anchor = GridBagConstraints.NORTHWEST;
//		c.fill = GridBagConstraints.NONE;
//		c.insets = new Insets(0, 0, 5, 0);
//		c.gridx = 1;
//		c.gridy = y;
//		this.add(_use_bootstrapping_checkbox,c);
//		
//		//Bootstrap random seed label
//		y++;
//		_bootstrap_rnd_seed_label = new JLabel("Random Seed:");
//		_bootstrap_rnd_seed_label.setFont(Constants.LABEL_FONT);
//		c.anchor = GridBagConstraints.NORTHEAST;
//		c.insets = new Insets(15, 0, 5, 15);
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 0;
//		c.gridy =y;
//		_bootstrap_rnd_seed_label.setVisible(false);
//		this.add(_bootstrap_rnd_seed_label,c);
//		
//		//Bootstrap random seed textfield
//		_bootstrap_rnd_seed_textfield = new JTextField("1234");
//		_bootstrap_rnd_seed_textfield.setPreferredSize(new Dimension(50,20));
//		c.anchor = GridBagConstraints.NORTHWEST;
//		c.insets = new Insets(15, 0, 5, 5);
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 1;
//		c.gridy = y;
//		_bootstrap_rnd_seed_textfield.setVisible(false);
//		this.add(_bootstrap_rnd_seed_textfield,c);
//		
//		//Bootstrap samples label
//		y++;
//		_bootstrap_samples_label = new JLabel("Samples:");
//		_bootstrap_samples_label.setFont(Constants.LABEL_FONT);
//		c.anchor = GridBagConstraints.NORTHEAST;
//		c.insets = new Insets(15, 0, 5, 15);
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 0;
//		c.gridy =y;
//		_bootstrap_samples_label.setVisible(false);
//		this.add(_bootstrap_samples_label,c);
//		
//		//Bootstrap samples textfield
//		_bootstrap_samples_textfield = new JTextField("100");
//		_bootstrap_samples_textfield.setPreferredSize(new Dimension(50, 20));
//		c.anchor = GridBagConstraints.NORTHWEST;
//		c.insets = new Insets(15, 0, 5, 5);
//		c.fill = GridBagConstraints.NONE;
//		c.gridx = 1;
//		c.gridy = y;
//		_bootstrap_samples_textfield.setVisible(false);
//		this.add(_bootstrap_samples_textfield,c);

		//use heuristic label
		y++;
		_use_heuristic_label = new JLabel("Heuristic:");
		_use_heuristic_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_use_heuristic_label,c);
		
		//use heuristics checkbox
		_use_heuristic_checkbox = new JCheckBox("");
		_use_heuristic_checkbox.addActionListener(this);
		_use_heuristic_checkbox.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 1;
		c.gridy = y;
		this.add(_use_heuristic_checkbox,c);
		
		//Heuristic model label
		y++;
		_heuristic_model_label = new JLabel("Model:");
		_heuristic_model_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		_heuristic_model_label.setVisible(false);
		this.add(_heuristic_model_label,c);
		
		//Heuristics model combobox
		Vector<String> heuristic_models = new Vector<String>();
//		heuristic_models.add("MP");
		heuristic_models.add("ML");
		_heuristic_models_combobox = new JComboBox(heuristic_models);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		_heuristic_models_combobox.setVisible(false);
		this.add(_heuristic_models_combobox,c);
		
		//Heuristic number label
		y++;
		_heuristic_number_label = new JLabel("Model:");
		_heuristic_number_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		_heuristic_number_label.setVisible(false);
		this.add(_heuristic_number_label,c);
		
		//Heuristics number combobox
		Vector<String> heuristic_numbers = new Vector<String>();
		heuristic_numbers.add("1/2");heuristic_numbers.add("1/4");heuristic_numbers.add("1/8");
		heuristic_numbers.add("1/16");heuristic_numbers.add("1/32");heuristic_numbers.add("1/64");
		_heuristic_numbers_combobox = new JComboBox(heuristic_numbers);
		_heuristic_numbers_combobox.setSelectedIndex(3);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		_heuristic_numbers_combobox.setVisible(false);
		this.add(_heuristic_numbers_combobox,c);
		
		//Job Options Headline
		y++;
		_job_options_headline = new JLabel("Job Options");
		_job_options_headline.setFont(Constants.HEADER_FONT);
		_job_options_headline.setOpaque(true);
		_job_options_headline.setBackground(Constants.HEADER_COLOR);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 3;
		this.add(_job_options_headline,c);
		c.gridwidth = 1;
		
		//Job name label
		y++;
		_jobname_label = new JLabel("Job Name:");
		_jobname_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(2, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_jobname_label,c);
		
		//Job name textfield
		_jobname_textfield = new JTextField("110");
		_jobname_textfield.setPreferredSize(new Dimension(50, 20));
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_jobname_textfield,c);
		
		//Save Location label
		y++;
		_jobfolder_label = new JLabel("Select Results Destination:");
		_jobfolder_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_jobfolder_label,c);
		
		//Save Location Textfield & Button
		_jobfolder_textfield = new JTextField("");
		_jobfolder_textfield.setPreferredSize(new Dimension(200, 20));
		_jobfolder_textfield.setEditable(false);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_jobfolder_textfield,c);
		
		_jobfolder_button = new JButton("Browse");
		_jobfolder_button.setPreferredSize(new Dimension(80, 20));
		_jobfolder_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_jobfolder_button.addActionListener(this);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_jobfolder_button,c);
		
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
	
	private void showChooseAlignmentFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_alignment_textfield.setText(selected_file);
			_parameters.put(_alignmentfile, selected_file);
		}
	}
	
	private void showReadsOptions(){
		_reads_label.setVisible(true);
		_reads_textfield.setVisible(true);
		_reads_button.setVisible(true);
		_use_clustering_label.setVisible(true);
		_use_clustering_checkbox.setVisible(true);
		_parameters.put(_use_reads, "true");
	}
	
	private void showChooseReadsFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_reads_textfield.setText(selected_file);
			_parameters.put(_readsfile, selected_file);
		}
	}
	
	private void showDnaTypeOptions(){
		_het_models_dna.setVisible(true);
		_parameters.put(_alignmenttype, "dna");
	}
	
	private void showAaTypeOptions(){
		_het_models_aa.setVisible(true);
		_use_emp_base_freq_label.setVisible(true);
		_use_emp_base_freq_checkbox.setVisible(true);
		_parameters.put(_alignmenttype, "aa");
	}
	
	private void showPartitionedTypeOptions(){
		_het_models_partitioned.setVisible(true);
		_partitionfile_label.setVisible(true);
		_partitionfile_textfield.setVisible(true);
		_partitionfile_button.setVisible(true);
		_parameters.put(_alignmenttype, "par");
	}
	
	private void showChoosePartitionFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_partitionfile_textfield.setText(selected_file);
			_parameters.put(_partitionfile, selected_file);
		}
	}
	
	private void showChooseTreeFile(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_tree_textfield.setText(selected_file);
			_parameters.put(_treefile, selected_file);
		}
	}
	
	private void showChooseJobDir(){
		_filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_jobfolder_textfield.setText(selected_file);
			_parameters.put(_jobfolder, selected_file);
		}
	}
	
	private void showHeuristicOptions(){
		_heuristic_model_label.setVisible(true);
		_heuristic_models_combobox.setVisible(true);
		_heuristic_number_label.setVisible(true);
		_heuristic_numbers_combobox.setVisible(true);
	}
	
//	private void showBootstrapOptions(){
//		_bootstrap_rnd_seed_label.setVisible(true);
//		_bootstrap_rnd_seed_textfield.setVisible(true);
//		_bootstrap_samples_label.setVisible(true);
//		_bootstrap_samples_textfield.setVisible(true);
//	}
	
	private void hideReadsOptions(){
		_reads_label.setVisible(false);
		_reads_textfield.setVisible(false);
		_reads_button.setVisible(false);
		_use_clustering_label.setVisible(false);
		_use_clustering_checkbox.setVisible(false);
		_use_clustering_checkbox.setSelected(false);
		_reads_textfield.setText("");
		_parameters.put(_use_reads, null);
		_parameters.put(_readsfile, null);
		_parameters.put(_use_reads, null);
	}
	
	private void hideDnaTypeOptions(){
		_het_models_dna.setVisible(false);
		_het_models_dna_combobox.setSelectedIndex(0);
	}
	
	private void hideAaTypeOptions(){
		_het_models_aa.setVisible(false);
		_het_models_aa_combobox.setSelectedIndex(0);
		_aa_matrices_combobox.setSelectedIndex(0);
		_use_emp_base_freq_label.setVisible(false);
		_use_emp_base_freq_checkbox.setVisible(false);
		_use_emp_base_freq_checkbox.setSelected(false);
	}
	
	private void hidePartitionedTypeOptions(){
		_het_models_partitioned.setVisible(false);
		_het_models_partitioned_combobox.setSelectedIndex(0);
		_partitionfile_label.setVisible(false);
		_partitionfile_textfield.setVisible(false);
		_partitionfile_button.setVisible(false);
		_partitionfile_textfield.setText("");
		_parameters.put(_partitionfile, null);
	}
	
	private void hideHeuristicOptions(){
		_heuristic_model_label.setVisible(false);
		_heuristic_models_combobox.setVisible(false);
		_heuristic_models_combobox.setSelectedIndex(0);
		_heuristic_number_label.setVisible(false);
		_heuristic_numbers_combobox.setVisible(false);
		_heuristic_numbers_combobox.setSelectedIndex(3);
		_parameters.put(_use_heuristics, null);
		_parameters.put(_heuristic_model, null);
		_parameters.put(_heuristic_value, null);
	}
	
//	private void hideBootstrapOptions(){
//		_bootstrap_rnd_seed_label.setVisible(false);
//		_bootstrap_rnd_seed_textfield.setVisible(false);
//		_bootstrap_rnd_seed_textfield.setText("1234");
//		_bootstrap_samples_label.setVisible(false);
//		_bootstrap_samples_textfield.setVisible(false);
//		_bootstrap_samples_textfield.setText("100");
//		_parameters.put(_use_bootstrap, null);
//		_parameters.put(_random_seed, null);
//		_parameters.put(_samples, null);
//	}
	
	public void resetForm(){
		// reset saved parameters
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			_parameters.put(key, null);
		}
		// reset form
		_alignment_textfield.setText("");
		_use_reads_checkbox.setSelected(false);
		hideReadsOptions();
		_dna_radio.setSelected(true);
		hideDnaTypeOptions();
		hideAaTypeOptions();
		hidePartitionedTypeOptions();
		showDnaTypeOptions();
		_tree_textfield.setText("");
//		_speed_checkbox.setSelected(true);
		_cores_spinner.setValue(1);
//		_use_bootstrapping_checkbox.setSelected(false);
//		hideBootstrapOptions();
		_use_heuristic_checkbox.setSelected(false);
		hideHeuristicOptions();
		_jobname_textfield.setText("");
		_jobfolder_textfield.setText("");
	}
	
	private void collectRemainingParameters(){
		// collect some parameters
		if (_use_clustering_checkbox.isSelected()){
			_parameters.put(_use_clustering, "true");
		}
		else{
			_parameters.put(_use_clustering, null);
		}
		
		if (_parameters.get(_alignmenttype).equals("dna")){
			_parameters.put(_het_model, _het_models_dna_combobox.getSelectedItem().toString());
		}
		else if (_parameters.get(_alignmenttype).equals("aa")){
			String model = _het_models_aa_combobox.getSelectedItem().toString();
			String matrix = _aa_matrices_combobox.getSelectedItem().toString();
			String use_emp_base_freq = "";
			if (_use_emp_base_freq_checkbox.isSelected()){
				use_emp_base_freq = "F";
			}
			_parameters.put(_het_model, model+matrix+use_emp_base_freq);
			
		}
		else { // "par"
			_parameters.put(_het_model, _het_models_partitioned_combobox.getSelectedItem().toString());
		}
		
		
		_parameters.put(_cores, _cores_spinner.getValue().toString());
		
//		if (_speed_checkbox.isSelected()){
//			_parameters.put(_fast, "y");
//		}
//		else{
			_parameters.put(_fast, "v");
//		}
		
//		if (_use_bootstrapping_checkbox.isSelected()){
//			_parameters.put(_use_bootstrap, "true");
//			_parameters.put(_random_seed, _bootstrap_rnd_seed_textfield.getText());
//			_parameters.put(_samples, _bootstrap_samples_textfield.getText());
//		}
//		else{
//			_parameters.put(_use_bootstrap,null);
//		}
		
		if (_use_heuristic_checkbox.isSelected()){
			_parameters.put(_use_heuristics,"true");
			_parameters.put(_heuristic_model, _heuristic_models_combobox.getSelectedItem().toString());
			_parameters.put(_heuristic_value, _heuristic_numbers_combobox.getSelectedItem().toString());
		}
		else{
			_parameters.put(_use_heuristics, null);
		}
		
		_parameters.put(_jobname, _jobname_textfield.getText());
	}
	
	public void submitJob(boolean testing){
		_configuration.setWorkspace(_parameters.get(_jobfolder));
		_jobpath = _configuration.getWorkspace()+_parameters.get(_jobname);
		_configuration.setWorkspace(_jobpath);
		String[] submission_parameters = parametersCommand();
	
		boolean status = new File(_jobpath).mkdir();
		//execute shellfile
        if (_job.getMainFrame().isVisible()){ // don't show confirmation dialogue when testing
        	_job.switchToWaitingPanel();
//        	_job.getMainFrame().setEnabled(false);
        }
        Submission sub = new Submission(submission_parameters,_job, Submission.Jobtype.SGA, testing);
        _job.setSubmissionThread(sub);
		Thread t = new Thread(sub);
		t.start();
		if(testing ) {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	
	private String[] parametersCommand(){
		ArrayList<String> c = new ArrayList<String>();
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			String p = _parameters.get(key);
			// Alignmentfile
			if (key.equals(_alignmentfile) && p != null){
				c.add("-s");c.add(p);
			}
			// Alignment type and substitution model
			else if (key.equals(_het_model) && p != null){
				c.add("-m");c.add(p);
			}
			// Partitionfile
			else if (key.equals(_partitionfile) && p != null){
				c.add("-q");c.add(p);
			}
			// Treefile
			else if (key.equals(_treefile) && p != null){
				c.add("-t");c.add(p);
			}
			// Cores
			else if (key.equals(_cores) && p != null && Integer.valueOf(p) > 1 ){
				c.add("-T");c.add(p);
			}
			// Fast
			else if (key.equals(_fast)){
				c.add("-f");c.add(p);
			}
//			// Bootstrapping
//			else if (key.equals(_use_bootstrap) && p != null){
//				c.add("-x");c.add(_parameters.get(_random_seed));
//				c.add("-N");c.add(_parameters.get(_samples));
//			}
			// Heuristics
			else if (key.equals(_use_heuristics) && p != null){
				if (_parameters.get(_heuristic_model).equals("MP")){
					c.add("-H");c.add(String.valueOf(convertFractionToFloat(_parameters.get(_heuristic_value))));
				}
				else{ // ML
					c.add("-G");c.add(String.valueOf(convertFractionToFloat(_parameters.get(_heuristic_value))));
				}
			}
			// Jobname
			else if (key.equals(_jobname) && p != null){
				c.add("-n");c.add(p);
			}	
			else if (key.equals(_use_clustering) && p != null){
				c.add("-useCl");
			}
			else if (key.equals(_use_reads) && p != null){
				c.add("-useR");c.add(_parameters.get(_readsfile));
			}
			else if (key.equals(_mga) && p != null){
				c.add("-mga");
			}
		}
		c.add("-jobpath");c.add(_jobpath);
		c.add("-uId");c.add(new Float(_configuration.getUclustID()).toString());
		return c.toArray(new String[c.size()]);
	}
	
	private float convertFractionToFloat(String fraction){
		String[] groups = Util.matchesWithGroups(fraction, "(\\d+)\\/(\\d+)");
		int frac = (int)((Float.valueOf(groups[1]) /  Float.valueOf(groups[2])) * 1000);
		return ((float)(frac))/ 1000f;
		
	}
	
	public void validateInput(){
		_input_errors.clear();
		Set <String> keys = _parameters.keySet();
		for (String key : keys){
			String p = _parameters.get(key);
			// Check alignmentfile
			if (key.equals(_alignmentfile) ){
				if (p != null && !p.equals("") && (new File(p)).exists() ){
					AlignmentfileParser a = new AlignmentfileParser(p,_parameters.get(_het_model), _parameters.get(_partitionfile));
					if (!a.isValidFormat()){
						_input_errors.put(_alignmentfile, a.getErrorMessage());
					}
					// check partitionfile if "Partitioned" is selected
					if (_parameters.get(_alignmenttype).equals("par")){
						if (_parameters.get(_partitionfile) != null && !_parameters.get(_partitionfile).equals("") && (new File(_parameters.get(_partitionfile)).exists())  ){
							PartitionfileParser  par = new PartitionfileParser(_parameters.get(_partitionfile), a.getAlignmentLength());
							if (!par.isValidFormat()){ // Check if readsfile is in Fasta format
								_input_errors.put(_partitionfile,par.getErrorMessage());
							}
						}
						// if partitionfile is missing
						else if (p == null || p.equals("")){
						_input_errors.put(_partitionfile,"Please choose a partitionfile!");
						}
						else if (!( new File(p)).exists()){
						_input_errors.put(_partitionfile,"The selected partitionfile: "+_parameters.get(_partitionfile)+" does not exist!");
						}
						else {
						_input_errors.put(_partitionfile,"Partitionfile is not valid!");
						}
					}
					
				}
				// if alignmentfile is missing
				else if (p == null || p.equals("")){
					_input_errors.put(_alignmentfile,"Please choose an alignmentfile!");
				}
				else if (!( new File(p)).exists()){
					_input_errors.put(_alignmentfile,"The selected alignmentfile: "+_parameters.get(_alignmentfile)+" does not exist!");
				}
				else{
					_input_errors.put(_alignmentfile,"Alignmentfile is not valid!");
				}
			}
			//check readsfileformat
			else if (key.equals(_readsfile) &&  _parameters.get(_use_reads) != null){
				if (p != null && !p.equals("") && (new File(p)).exists() ){
					ReadsfileParser r = new ReadsfileParser(p);
					if (!r.isValidFormat()){ // Check if readsfile is in Fasta format
						_input_errors.put(_readsfile,r.getErrorMessage());
					}
				}
				else if (p == null || p.equals("")){
					_input_errors.put(_readsfile,"Please choose a readsfile!");
				}
				else if (!( new File(p)).exists()){
					_input_errors.put(_readsfile,"The selected readsfile: "+_parameters.get(_readsfile)+" does not exist!");
				}
				else{
					_input_errors.put(_readsfile,"Readstfile is not valid!");
				}
			}
			// Check treefile
			else if (key.equals(_treefile) ){
				if (p != null && !p.equals("") && (new File(p)).exists() ){
					TreefileParser t = new TreefileParser(p);
					if (!t.isValidFormat()){ // Check if readsfile is in Fasta format
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
			// Cores
			else if (key.equals(_cores) ){
				if (p != null && p.matches("^\\d+$") && Integer.valueOf(p) > 0){
					// Check if there are enough cores available
					Runtime runtime = Runtime.getRuntime();
					int nrOfProcessors = runtime.availableProcessors();
		      	  if (Integer.valueOf(p) > nrOfProcessors ){
		      		  _input_errors.put(_cores, "There are only "+nrOfProcessors+" available!");
		      	  }
				}
				else{
					_input_errors.put(_cores, "Invalid amount of cores!");
				}
			}
//			// Bootstrapping
//			else if (key.equals(_use_bootstrap) && p != null){
//				//Check if "random seed" is an integer
//				if (!(_parameters.get(_random_seed).matches("^\\d+$"))){
//					_input_errors.put(_random_seed, "This value has to be an integer!");
//				}
//				//check if "samples" is an integer
//				if (!(_parameters.get(_samples).matches("^\\d+$"))){
//					_input_errors.put(_samples, "This value has to be an integer!");
//				}
//			}
			// Jobname
			else if (key.equals(_jobname) ){
				if ( p != null && !p.isEmpty()){
					//insert "_" if jobname whitespaces
					_parameters.put(_jobname, _parameters.get(_jobname).replaceAll("\\s+", "_"));
					// check if the name includes no "/"
					_parameters.put(_jobname, _parameters.get(_jobname).replaceAll(Util.getSeparatorForRegex(), "_"));
				}
				else{
					_input_errors.put(_jobname, "Please enter a jobname!");
				}
			}			
			// Jobfolder
			else if (key.equals(_jobfolder) ){
				if ( p != null && !p.isEmpty() && (new File(p).exists()) ){
				}
				else{
					_input_errors.put(_jobfolder, "Please select a destination where the results can be saved!");
				}
			}	
			
			else if (key.equals(_mga) && p != null){
			}
		}
		// check if the Jobpath exists already and let the user decide if he wants to delete the older files. 
		if (_parameters.get(_jobfolder) != null && _parameters.get(_jobname)!= null){
			String new_workspace = _parameters.get(_jobfolder);
			if (new_workspace.substring(new_workspace.length()-1, new_workspace.length()-1).equals(File.separator)){
			}
			else{
				new_workspace += File.separator;
			}
			new_workspace += _parameters.get(_jobname);
			if (new_workspace.substring(new_workspace.length()-1, new_workspace.length()-1).equals(File.separator)){
			}
			else{
				new_workspace += File.separator;
			}
			// Show question dialog when the entered path already exists.
			if (new File(new_workspace).exists()){
				int selected = JOptionPane.showOptionDialog(
						this, 
						"The selected folder for the RAxML files already exists! RAxML will override older files in that folder! \nDo you want to proceed?", 
						"Warning!", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, 
						null, null,null);
				// Delete if Yes is pressed.
				if (selected==0){
					try {
						File old_info = new File(new_workspace+"RAxML_info."+_parameters.get(_jobname));
						
						if (!old_info.canWrite()){
							_input_errors.put(_jobname, "Cannot delete write protected file:"+old_info.getName()+", please enter a different Jobname or Jobfolder!");
						}
						if (old_info.isDirectory()){
							_input_errors.put(_jobname, old_info.getAbsolutePath()+ " is a directory!, please enter a different Jobname or Jobfolder!");
						}
						if (old_info.exists() && old_info.canWrite() && !old_info.isDirectory()){
							old_info.delete();
						}
					}
					catch(IllegalArgumentException e){
						Util.printErrors(e);
					}
				}
				else{
					_input_errors.put(_jobname, "The chosen Jobpath already exists, please enter a different Jobname or Jobfolder!");
				}
			}
		}
	}
	
	private void showErrors(){
		
		Set <String> errors = _input_errors.keySet();
		for (String error : errors){
			System.out.println(error+ " : " + _input_errors.get(error));
			if (error.equals(_alignmentfile)){
				highlightError(_alignment_label, _input_errors.get(error));
			}
			else if (error.equals(_readsfile)){
				highlightError(_reads_label, _input_errors.get(error));
			}
			else if (error.equals(_partitionfile)){
				highlightError(_partitionfile_label, _input_errors.get(error));
			}
			else if (error.equals(_treefile)){
				
				highlightError(_tree_label, _input_errors.get(error));
			}
			else if (error.equals(_cores)){
				highlightError(_cores_label, _input_errors.get(error));
			}
//			else if (error.equals(_samples)){
//				highlightError(_bootstrap_samples_label, _input_errors.get(error));
//			}
//			else if (error.equals(_random_seed)){
//				highlightError(_bootstrap_rnd_seed_label, _input_errors.get(error));
//			}
			else if (error.equals(_jobname)){
				highlightError(_jobname_label, _input_errors.get(error));
			}
			else if (error.equals(_jobfolder)){
				highlightError(_jobfolder_label, _input_errors.get(error));
			}
		}
		
		if (_input_errors.get(_alignmentfile) == null){
			_alignment_label.setForeground(null);
		}
		else if (_input_errors.get(_readsfile) == null){
			_reads_label.setForeground(null);
		}
		else if (_input_errors.get(_partitionfile) == null){
			_partitionfile_label.setForeground(null);
		}
		else if (_input_errors.get(_treefile) == null){
			_tree_label.setForeground(null);
		}
		else if (_input_errors.get(_cores) == null){
			_cores_label.setForeground(null);
		}
//		else if (_input_errors.get(_samples) == null){
//			_bootstrap_samples_label.setForeground(null);
//		}
//		else if (_input_errors.get(_random_seed) == null){
//			_bootstrap_rnd_seed_label.setForeground(null);
//		}
		else if (_input_errors.get(_jobfolder) == null){
			_jobfolder_label.setForeground(null);
		}
		else if (_input_errors.get(_jobname) == null){
			_jobname_label.setForeground(null);
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
	
	public void enableUclust(boolean b){
		_use_clustering_checkbox.setEnabled(b);
		this.repaint();
		this.revalidate();
		
	}
}
