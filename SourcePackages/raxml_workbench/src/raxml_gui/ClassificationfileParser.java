package raxml_gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.JTable;

public class ClassificationfileParser {
	
	private boolean _valid_format;
	private String _error ="Invalid Classificationfile format!\n" ;
	private String _filename;
	
	public ClassificationfileParser(String file){
		_filename = file;
		_valid_format = true;
		checkFormat();
	}
	
	private void checkFormat(){
		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(_filename));
			String line = null;
			while ((line = in.readLine()) != null) {
				i++;
				StringTokenizer ts = new StringTokenizer(line);
				try {
					ArrayList <String> nlc = new ArrayList<String>();
					String name = ts.nextToken();
					String location = ts.nextToken();
					String confidence = ts.nextToken();
					if (!Util.matches(confidence,"\\d+\\.\\d+" )){
						_valid_format = false;
						_error += "Cannot read confidence value in line: "+i+" => "+line;
						return;
					}
					
				} catch( NoSuchElementException x ) {
					Util.printErrors(x);
					_valid_format = false;
					_error += "Cannot parse line: "+i+" => "+line;
					return;
				}
			}
		} catch (IOException e) {
			Util.printErrors(e);
		}
	}
	
	public boolean isValidFormat(){
		return _valid_format;
	}
	
	public String getErrorMessage(){
		return _error;
	}
	
	public JTable getTableWithData(String[] column_names){
		String[] lines = Util.readFile(_filename);
		StringTokenizer ts = new StringTokenizer(lines[0]);
		String[][] table_data = new String[lines.length][ts.countTokens()];
		for(int i = 0; i < lines.length; i++){
			ts = new StringTokenizer(lines[i]);
			
			try {
				String[] tokens = new String[ts.countTokens()];
				int t = ts.countTokens();
				for(int j = 0; j < t; j++){
					tokens[j] = ts.nextToken();
				}
				table_data[i] = tokens;
				
			} catch( NoSuchElementException x ) {
				Util.printErrors(x);
				throw new RuntimeException( "failed to parse line from classfile: " + lines[i] );
			}
		}
		return new JTable(new MyTable(table_data, column_names));
	}
	
	public int getAmountOfTokens(){
		String[] lines = Util.readFile(_filename);
		StringTokenizer ts = new StringTokenizer(lines[0]);
		return ts.countTokens();
	}
}
