package raxml_gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TreefileParser {
	
	private boolean _valid_format;
	private String _error ="Invalid Treefile format!" ;
	private String _filename;
	
	public TreefileParser(String file){
		_filename = file;
		_valid_format = true;
		checkFormat();
	}
	
	private void checkFormat(){
		Process p; 
		String[] command = {"java","-jar", Constants.TREECHECK.getAbsolutePath(), _filename};
		
		try{	
			ProcessBuilder builder2 = new ProcessBuilder(command);
			builder2.redirectErrorStream(true);
			p = builder2.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while ((line = in.readLine()) != null) {
				if (!(line.matches("good"))){
		          _valid_format = false;
				}
			}
			p.waitFor();
		}
		catch (Exception e){
			Util.printErrors(e);
		}
	}
	
	public boolean isValidFormat(){
		return _valid_format;
	}
	
	public String getErrorMessage(){
		return _error;
	}
}
