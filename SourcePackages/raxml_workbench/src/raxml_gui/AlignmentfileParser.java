package raxml_gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AlignmentfileParser {

	private boolean _valid_format;
	private String[] _data;
	private String _format;
	private String _error ="Invalid alignmentfile format!(Only Fasta and Phylip alignment formats are allowed)";
	private String _filename;
	private int _alignmentlength;
	private ArrayList <String> _log  = new ArrayList<String>();
	private HashMap <String,String> _seqs = new HashMap<String, String>();
	
	public AlignmentfileParser(String file){
		_filename = file;
		_data = Util.readFile(file);
		_valid_format = true;
		checkFormat();
		_alignmentlength = 0;
	    if (_valid_format){
	    	for (int i = 0; i< _data.length; i++){
	    		String[] groups = Util.matchesWithGroups(_data[i],"\\d+\\s+(\\d+)");
	    		if (groups != null){
	    			_alignmentlength = Integer.valueOf(groups[1]);
	    		}
	    	}
	    }
	}
	
	private void checkFormat(){	
		for (int i = 0; i < _data.length; i++){
			if (Util.matches(_data[i],"^>") && Util.matches(_data[i+1],"^([A-Z\\-]+)\\s*$")){ //fasta format?
				_format = "fas";
		        _data[i+1] = _data[i+1].replaceAll("\\.","-");
		        _valid_format = true;
		        for(int j = i+2; j< _data.length; j++){
		        	if (!(Util.matches(_data[j],"^[A-Z\\-]+\\s*$") || Util.matches(_data[j],"^>") && Util.matches(_data[j+1],"^[A-Z\\-]+\\s*$"))){
		        		_format = "unk";
		        		_valid_format = false;
		        		_error += _filename+" line "+j+": *"+_data[j]+"*";
		        		return;
		        	}
		        	else if (Util.matches(_data[j],"^[A-Z\\-]+\\s*$")){
		        		_data[j] = _data[j].replaceAll("\\.","-");
		        	}
		        }
		        Reformat phy = new Reformat(_data);
		        phy.reformatToPhylip();
		        _data = phy.getData().toArray(new String[phy.getData().size()]);
		        checkPhylipFormatWithRAxML();
		        break;
			}
			else if (Util.matches(_data[i],"\\s*\\d+\\s+\\d+")){ //phylip format?
				_format = "phl";
				_valid_format = true;
		        checkUniquenessOfNames();
		        checkPhylipFormatWithRAxML();
		        break;
			}
			else if (Util.matches(_data[i],"^\\s+$")){ // blank lines are ignored
		        //do nothing
			}
			else{
				_valid_format = false;
//		        _error = _message;
		        break;
			}
		}
	}
	
	private void checkPhylipFormatWithRAxML(){
		
		Process p; 
		
		try{	
			File temp = File.createTempFile("checkAlignment", null);
			Util.writeToFile(_data, temp.getAbsolutePath());
			String[] command = {Constants.RAXML.getAbsolutePath(),"-s", temp.getAbsolutePath(), "-fc", "-m", "GTRGAMMA", "-n", "checkFormat"};
			ProcessBuilder builder2 = new ProcessBuilder(command);
			builder2.directory(Constants.JAR_PATH);
			builder2.redirectErrorStream(true);
			p = builder2.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			
			while ((line = in.readLine()) != null) {
				if (!(Util.matches(line,"^Alignment\\sformat\\scan\\sbe\\sread\\sby\\sRAxML")) && !(Util.matches(line,"IMPORTANT\\s+WARNING"))){
		          _error += line;
		          _format = "unk"; 
		          _valid_format = false;
				}
				if (Util.matches(line,"^Alignment\\sformat\\scan\\sbe\\sread\\sby\\sRAxML")){ //Because of some other Warnings in windows version, it should suffice if RAxML prints this message
					_valid_format = true;
				}
			}
//			if (!_error.isEmpty() ){
//				_error += _error;
//			}
			File info_file = new File(Constants.JAR_PATH,"RAxML_info.checkFormat");
			if (info_file.exists()){
				info_file.delete();
			}
			temp.delete();
			p.waitFor();
		}
		catch (Exception e){
			Util.printErrors(e);
		}
	}
	
	private void checkUniquenessOfNames(){
		HashMap<String,Integer> names = new HashMap<String, Integer>();
		String first_name = "";
		int seqs = 0;
		int seq_len = 0;
		boolean read_names = false;
		String[] data_copy = _data.clone();
		for (int i = 0; i<_data.length; i++){
			String line = _data[i];
			String[] groups = Util.matchesWithGroups(line,"\\s*(\\d+)\\s+(\\d+)" );
			if (groups != null){
				seqs = Integer.valueOf(groups[1]);
		        seq_len = Integer.valueOf(groups[2]);
			}
			else if (Util.matches(line,"^\\s+$")){
				//all names have been read, quit procedure
				//clear the names hash 
		        //rename again
				if (read_names){
					read_names = false;
					if (names.size() > seqs){
						_data = data_copy;
						_log.add("RAxML_Alignmentfile_parser couldn't read format! Reseted alignmentfile!");
						return;
					}
					names.clear();
				}
			}
			else if (Util.matches(line,"\\s*\\S+\\s*")){ //A sequence line is read
				if (names.size() > seqs){
					_data = data_copy;
					_log.add("RAxML_Alignmentfile_parser couldn't read format! Reseted alignmentfile! ");
					return;
				}
				String s = line.replaceAll("\\s", "");
				// check if line length without whitespaces matches the sequence length given 
				if (s.length() > seq_len){ // Taxon and sequence in the line
					groups = Util.matchesWithGroups(line,"^\\s*(\\S+)\\s+" );
					if (groups != null){
						String name = groups[1];
						if (!read_names){ 
							if (!first_name.equals(name) && !first_name.equals("")){  // interleaved format without reoccurring taxon names
								return;
							}	
							first_name = name;
							read_names = true ;
						}
						if (names.get(name) == null){
							names.put(name,1);
						}
						else{
							names.put(name, names.get(name)+1);
							line = line.replaceFirst("^\\s*(\\S+)",name+"_"+names.get(name));
							_log.add("renamed "+name+" to "+name+"_"+names.get(name));
						}
					}
					else{
						_error += "ERROR: couldn't parse "+line;
						_valid_format = false;
					}
				}	
				else if (s.length() < seq_len){ // Only Taxon in the line or interleaved format
					groups = Util.matchesWithGroups(line,"^\\s*(\\S+)\\s*");
					if (groups != null){
						String name = groups[1];
						if (!read_names){ 
							if (!first_name.equals(name) && !first_name.equals("")){ // interleaved format without reoccurring taxon names
								return;
							}
							first_name = name;
							read_names = true ; 
						}
						if (names.get(name) == null){
							names.put(name,1);
						}
						else{
							names.put(name,names.get(name)+1);
							line = line.replaceFirst("^\\s*(\\S+)",name+"_"+names.get(name));
							_log.add("renamed "+name+" to "+name+"_"+names.get(name));
						}
					}
					else{
		        	  _error +=  "ERROR: couldn't parse "+line;
		        	  _valid_format = false;
					}
				}
				else{ // Only Sequence in the line
					// do nothing
				}
			}
		}
	}
	
	
	public boolean isValidFormat(){
		return _valid_format;
	}
	
	public String getErrorMessage(){
		return _error;
	}
	
	public int getAlignmentLength(){
		return _alignmentlength;
	}
	
	public void parseSeqs(){
		for(int i=0; i< _data.length; i++){
			String[] groups = Util.matchesWithGroups(_data[i], "^\\s*(\\S+)\\s+([A-Za-z\\s\\-]+)");
			if (groups != null && groups.length > 2){
				String taxon = groups[1];
				String seq = groups[2];
				_seqs.put(taxon, seq);
			}
		}
		
	}
	
	public void disalign(){
		Set<String> taxa = _seqs.keySet();
		for(String taxon : taxa){
			String seq_no_gaps = _seqs.get(taxon).replaceAll("\\-", "");
			_seqs.put(taxon, seq_no_gaps);
		}
	}
	
	public HashMap<String,String> getSeqs(){
		return _seqs;
	}
	public void setSeqs(HashMap<String,String> seqs){
		_seqs = seqs;
	}
	
}
