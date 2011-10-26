package raxml_gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Configuration {
	
	private String _s = File.separator;
	private String _workspace = "";
	private String _uclust_path = null;
	private float _uclust_id = 0.9f;
	private MainFrame _main_frame;
	private static MainFrame _m;
	
	public Configuration(MainFrame main_frame){
		_main_frame = main_frame;
		_m = main_frame;
	}
	
	public String getWorkspace(){
		return _workspace;
	}
	
	public float getUclustID(){
		return _uclust_id;
	}
	
	public String getJobName(){
		if (_workspace != null){
			if (Util.matches(_workspace, Util.getSeparatorForRegex()+"[\\w_-]+"+Util.getSeparatorForRegex()+"$")){
				return Util.matchesWithGroups(_workspace, Util.getSeparatorForRegex()+"([\\w_-]+)"+Util.getSeparatorForRegex()+"$")[1];
			}
			else if (Util.matches(_workspace,Util.getSeparatorForRegex()+"[\\w_-]+$" )){
				return Util.matchesWithGroups(_workspace, Util.getSeparatorForRegex()+"([\\w_-]+c)$")[1];
			}
			else{
				return _workspace;
			}
		}
		else {
			return _workspace;
		}
	}
	
	public void setWorkspace(String new_workspace){
		if (new_workspace != null){
			if (new_workspace.substring(new_workspace.length()-1, new_workspace.length()-1).equals(File.separator)){
				_workspace = new_workspace;
			}
			else{
				_workspace = new_workspace+File.separator;
			}
		}
		else{
			_workspace = new_workspace; // is null
		}
	}
	
	public void setUclustId(float identity){
		_uclust_id = identity;
	}
	
	public MainFrame getMainFrame(){
		return _main_frame;
	}
	
	public static MainFrame getMainFrameStatic(){ //Util needs static MainFrame reference 
		return _m;
	}
	
	public void setUclustPath(String path){
		_uclust_path = path;
	}
	
	public String getUclustPath(){
		return _uclust_path;
	}
	
	public void writeToFile(){
		Settings settings = new Settings(_uclust_path, _uclust_id);
		File file = Constants.CONFIGURATION;
		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(settings);
			o.close();
		} catch (FileNotFoundException e) {
			Util.printErrors(e);
		} catch (IOException e) {
			Util.printErrors(e);
		}
	}
	
	public void loadConfigurationFromFile(){
		try {
			FileInputStream in = new FileInputStream(Constants.CONFIGURATION);
			ObjectInputStream o = new ObjectInputStream(in);
			Settings settings = (Settings)o.readObject();
			o.close();
			_uclust_path = settings.getUclustPath();
			_uclust_id = settings.getUclustID();
			
		} catch (FileNotFoundException e) {
			Util.printErrors(e);
		} catch (IOException e) {
			Util.printErrors(e);
		} catch (ClassNotFoundException e) {

		JOptionPane.showMessageDialog(_main_frame, Constants.CONFIGURATION+" is broken, default preferences are loaded!","Broken file!",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private static class Settings implements Serializable{
		
		private static final long serialVersionUID = -829608966516054885L;
		private String _uclust_path;
		private float _uclust_id;
		
		public Settings(String path, float id){
			_uclust_path = path;
			_uclust_id = id;
		}
		
		public String getUclustPath(){
			return _uclust_path;
		}
		
		public float getUclustID(){
			return _uclust_id;
		}
		
	}
}
