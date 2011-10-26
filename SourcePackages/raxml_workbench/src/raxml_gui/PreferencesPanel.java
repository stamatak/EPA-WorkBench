package raxml_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lowagie.text.pdf.TextField;

public class PreferencesPanel extends JPanel implements ActionListener{
	
	private Configuration _configuration;
	private HashMap<String,String> _preferences;
	private HashMap<String,String> _errors;
	
	private String _uclust_path = "uclust_path";
	private String _uclust_id   = "uclust_id";
	
	private JLabel _uclust_headline;
	private JLabel _uclust_path_label;
	private JTextField _uclust_path_textfield;
	private JButton _uclust_path_button;
	private JLabel _uclust_id_label;
	private JTextField _uclust_id_textfield;
	private JFileChooser _filechooser;
	
	public PreferencesPanel(LayoutManager mgr, Configuration conf){
		this.setLayout(mgr);
		_configuration = conf;
		_preferences = new HashMap<String, String>();
		_errors = new HashMap<String, String>();
		_filechooser = conf.getMainFrame().getFilechooser();
		buildForm();
		this.setPreferredSize(new Dimension(600,100));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _uclust_path_button){
			showChooseUclustExecutable();
		}
		
	}
	
	private void buildForm(){
		GridBagConstraints c  = new GridBagConstraints();
		int y = -1;
		c.anchor = GridBagConstraints.NORTHWEST;
		//Uclust Headline
		y++;
		_uclust_headline = new JLabel("Activate uclust");
		_uclust_headline.setFont(Constants.HEADER_FONT);
		_uclust_headline.setOpaque(true);
		c.insets = new Insets(0, 20, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;
		c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = y;
		c.gridwidth = 4;
		this.add(_uclust_headline,c);
		c.gridwidth =1;
		//Uclust Path label
		y++;
		_uclust_path_label = new JLabel("Select uclust executable:");
		_uclust_path_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 0, 0, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = y;
		this.add(_uclust_path_label,c);
		
		//Uclust Path Textfield & Button
		_uclust_path_textfield = new JTextField("");
		_uclust_path_textfield.setPreferredSize(new Dimension(200, 20));
		_uclust_path_textfield.setEditable(false);
		_uclust_path_textfield.setBackground(Constants.BACKGROUND_COLOR);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0, 0, 5, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = y;
		this.add(_uclust_path_textfield,c);
		_uclust_path_button = new JButton("Browse");
		_uclust_path_button.setPreferredSize(new Dimension(80, 20));
		_uclust_path_button.setFont(Constants.BROWSE_BUTTON_FONT);
		_uclust_path_button.addActionListener(this);
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = y;
		this.add(_uclust_path_button,c);
		
		//Parsimony random seed label
		y++;
		_uclust_id_label = new JLabel("Cluster Identity:");
		_uclust_id_label.setFont(Constants.LABEL_FONT);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(15, 0, 5, 15);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy =y;
		this.add(_uclust_id_label,c);
		
		//Parsimony random seed textfield
		_uclust_id_textfield = new JTextField("0.9");
		_uclust_id_textfield.setPreferredSize(new Dimension(50,20));
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(15, 0, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = y;
		this.add(_uclust_id_textfield,c);
		
	}
		
	public boolean hasValidPreferences(){
		boolean valid = true;
		Set <String> keys = _preferences.keySet();
		for (String key : keys){
			//check if Uclust works
			if (key.equals(_uclust_path) && _preferences.get(_uclust_path)!= null){
				File uclust = new File(_preferences.get(_uclust_path));
				if (uclust.exists()){
					if (uclust.canExecute() && uclust.canRead()){
					}
					else{
						valid = false;
						if (!uclust.canExecute()){
							_errors.put(_uclust_path, "Uclust is not executable!");
						}
						else if (!uclust.canRead()){
							_errors.put(_uclust_path, "Uclust is not readable!");
						}
					}
				}
				else{
					valid = false;
					_errors.put(_uclust_path, "The entered path(uclust) does not exist!");
				}
			}
			//Check if cluster identity value is numeric x with 0 <= x < 1
			else if (key.equals(_uclust_id) && _preferences.get(_uclust_id) != null){
				if (!(_preferences.get(_uclust_id).matches("^0\\.\\d+$"))){
					valid = false;
					_errors.put(_uclust_id, "Cluster Identity has to be numeric and between 0 and 1!");
				}
			}
		}
		return valid;
		
	}
	
	public void savePreferences(){
		_configuration.setUclustPath(_preferences.get(_uclust_path));
		_configuration.setUclustId(Float.valueOf(_preferences.get(_uclust_id)));
	}
	
	public void  showErrors(){
		Set <String> errors = _errors.keySet();
		for (String error : errors){
			if (error.equals(_uclust_path)){
				highlightError(_uclust_path_label, _errors.get(error));
			}
			if (error.equals(_uclust_id)){
				highlightError(_uclust_id_label, _errors.get(error));
			}
			
		}
		
		if (_errors.get(_uclust_path) == null){
			_uclust_path_label.setForeground(null);
		}
		if (_errors.get(_uclust_id) == null){
			_uclust_id_label.setForeground(null);
		}
		

		if (_configuration.getMainFrame().isVisible()){ // don't show confirmation dialogue when testing
			JOptionPane.showMessageDialog(this, errorsToString(),"There some errors in your input!",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String[] errorsToString(){
		Set<String> keys = _errors.keySet();
		String[] e = new String[_errors.size()];
		int i = 0;
		for(String key : keys){
			e[i] = "* "+_errors.get(key); 
			i++;
		}
		return e;
	}
	

	private void highlightError(JLabel field, String error){
		field.setForeground(Constants.ERROR_COLOR);
	}
	
	private void showChooseUclustExecutable(){
		_filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
		_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_filechooser.showOpenDialog(this);
		if (_filechooser.getSelectedFile() != null){
			_filechooser.setCurrentDirectory(_filechooser.getSelectedFile());
			String selected_file = _filechooser.getSelectedFile().getAbsolutePath();
			_uclust_path_textfield.setText(selected_file);
			_preferences.put(_uclust_path, selected_file);
		}
	}
	
	protected void resetPreferences(){
		_preferences.clear();
		_errors.clear();
		loadPreferences();
		// Remove error coloring if present
		if (_errors.get(_uclust_path) == null){
			_uclust_path_label.setForeground(null);
		}
		if (_errors.get(_uclust_id) == null){
			_uclust_id_label.setForeground(null);
		}
		
	}
	
	protected void loadPreferences(){
		// load preferences from Configuration
		
		_preferences.put(_uclust_path,_configuration.getUclustPath());
		_preferences.put(_uclust_id,String.valueOf(_configuration.getUclustID()));
		
		// show uclust preferences
		if (_preferences.get(_uclust_path) != null && _preferences.get(_uclust_id) != null){
			_uclust_path_textfield.setText(_preferences.get(_uclust_path));
			_uclust_id_textfield.setText(_preferences.get(_uclust_id));
			// Check if older preferences are valid and set to null if not
			if (!hasValidPreferences()){
				_configuration.setUclustPath(null);
				_configuration.setUclustId(0.9f);
				resetPreferences();
			}
		}
		else{
			_uclust_path_textfield.setText("");
			_uclust_id_textfield.setText("0.9");
		}
	}
	
	void collectRemainingPreferences(){
		_preferences.put(_uclust_id, _uclust_id_textfield.getText());
	}
	
}

	
