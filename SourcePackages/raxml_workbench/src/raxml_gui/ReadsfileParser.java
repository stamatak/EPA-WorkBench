package raxml_gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ReadsfileParser {

	private boolean _valid_format;
	private String[] _data;
	private String _error = "Invalid Readsfile format!(only FASTA format allowed) \n";
	private String _filename;
	
	public ReadsfileParser(String file){
		_filename = file;
		_data = Util.readFile(file);
		checkFormat();
	}
	
	private void checkFormat(){
		int j = 0;
		for (int i = 0; i< _data.length; i=j){
			if (Util.matches(_data[i],"^>")){ // fasta Format?
				if (_data.length > i+1){
					for( j = i+1; j < _data.length  && Util.matches(_data[j],"^[a-zA-Z\\s]+$"); j++){
					}
				}
				else{
					_error += _filename+" line: "+i+"  => Missing sequence for "+_data[i];
					_valid_format = false;
					return;
				}
			}
			else{
				_error += _filename+" line: "+i+"  => "+_data[i];
				_valid_format = false;
				return;
			}
		}
		_valid_format = true;
	}
	
	public boolean isValidFormat(){
		return _valid_format;
	}
	
	public String getErrorMessage(){
		return _error;
	}
	
	public HashMap<String,String> getSeqs(){
		HashMap<String,String> seqs = new HashMap<String, String>();
		String name = null;
		for(int i = 0; i < _data.length; i++){
			if (Util.matches(_data[i], ">\\s*(.+)")){
				name = Util.matchesWithGroups(_data[i], ">\\s*(.+)")[1];
				seqs.put(name, "");
			}
			else if(Util.matches(_data[i], "(\\w+)")){
				seqs.put(name, seqs.get(name)+Util.matchesWithGroups(_data[i], "(\\w+)")[1]);
			}
		}
		ArrayList<String> taxa_to_delete = new ArrayList<String>();
		Set <String> keys = seqs.keySet();
		for (String key : keys){
			if ((seqs.get(key) != null) && (seqs.get(key).length() < 2)){                                                                                                         
				System.out.println("Warning: Query sequence "+key+" has less than 2 letters and will be ignored!");
				taxa_to_delete.add(key);
			}
		}
		for (int k = 0; k < taxa_to_delete.size(); k++){
			seqs.remove(taxa_to_delete.get(k));
		}
		return seqs;
	}
}
