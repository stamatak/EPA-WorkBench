package raxml_gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reformat {

	private ArrayList<String> _data = new ArrayList<String>();
	private String _format = "unk";
	
	public Reformat(String file){
		_data = Util.readFileToList(file);
		_format = "unk";
		detectFormat();
	}
	public Reformat(String[] file){
		for(int i = 0; i< file.length; i++){
			_data.add(file[i]);
		}
		_format = "unk";
		detectFormat();
	}
	
	private void detectFormat(){
		// Fasta format
		int i = 0;
		while (i< _data.size()){
			if (Util.matches(_data.get(i),"^>")){ 
				while(  i < _data.size()){
					if (Util.matches(_data.get(i),"^>")){ 
						i++;
						while (i < _data.size() && Util.matches(_data.get(i),"[A-Za-z\\-]+$")){
							i++;
						}
					}	
					else{
						return;
					}
				}
				_format = "fas";
			}
			//Phylip format
			else if (Util.matches(_data.get(i),"^\\s*(\\d+)\\s+(\\d+)\\s*$")){ 
				ArrayList<String> data = new ArrayList<String>();
				String[] groups = Util.matchesWithGroups(_data.get(i),"^\\s*(\\d+)\\s+(\\d+)\\s*$");
				data.add(_data.get(i));
				int start = i+1;
				i++;
				int seqs = Integer.valueOf(groups[1]);
				int real_seqs = 0;
				int len = Integer.valueOf(groups[2]);
				int j = 1;
				while (i <  _data.size()){
					groups = Util.matchesWithGroups(_data.get(i),"^(\\S+)\\s+([A-Za-z\\-\\s]+)");
					String[] groups2 = Util.matchesWithGroups(_data.get(i),"^\\s*([A-Za-z\\-\\s]+)");
					if (groups != null){
						data.add(j, groups[1]+" "+groups[2].replaceAll("\\s",""));	
						j++;
					}
					else if (groups2 != null){
						data.add(j, data.get(j)+_data.get(i).replaceAll("\\s",""));   // convert to a unsplitted format 
						j++;
					}
					if (j > seqs){
						j = 1;
					}
					i++;
				}
				_data = data;
				i = 1;
				while (i < _data.size()){
					groups = Util.matchesWithGroups(_data.get(i),"^\\S+\\s+([A-Za-z\\-\\s]+)");
					String seqline ="";
					if (groups != null){
						real_seqs = real_seqs+1;
						seqline = groups[1];
						if (len != seqline.length()){
							System.out.println(len+"|"+(seqline.replaceAll("\\s","")).length());
							return;
						}
					}
					else if (Util.matches(_data.get(i),"^\\s*$")){
					}
					else {
						return;
					}
					i++;
				}
				if (real_seqs == seqs){
					_format = "phy";
				}
			}
			else if (Util.matches(_data.get(i),"^\\s*#\\s*[Ss][Tt][Oo][Cc][Kk][Hh][Oo][Ll][Mm]\\s*\\d+\\.\\d+\\s*$")){ //Stockholm format
				i++;
				while (i < _data.size()){
					if (!(Util.matches(_data.get(i),"^\\S+\\s+[a-zA-Z\\-\\.]+\\s*$")
							|| Util.matches(_data.get(i),"^#=") 
							|| Util.matches(_data.get(i),"^\\s*$") 
							|| Util.matches(_data.get(i),"^\\s*\\/\\/\\s*$")
					)){
						return;
					}
					i++;
				}
				_format = "sto";
			}
			// Blank lines are ignored
			else if (Util.matches(_data.get(i),"^\\s*$")){
				i++;
				continue;
			}
			else {
				return;
			}
		}
	}
	// Export sequence representatives selected by Uclust
	public void exportClusterRepresentatives(){
		ArrayList<String> reps = new ArrayList<String>();
		if (_format.equals("fas")){
			int i = 0;	
			while  (i < _data.size()){
				String[] groups = Util.matchesWithGroups(_data.get(i),"^(>\\d+\\|\\*\\|.*)\\s{0,1}");
				if (groups != null){  //reps are marked by |*|
					reps.add(groups[1]);
					i++;
					groups = Util.matchesWithGroups(_data.get(i),"^([A-Za-z\\-]+)\\s*$");
					while (groups != null){
						reps.add(groups[1]);
		                i++;
					}
		            continue;
				}
				i++;	
			}
			_data =  reps;
		}
		else {
			System.err.println("Data has wrong format for this function!");
		}
	}
	
	// Convert different formats to Phylip format
	public void reformatToPhylip(){
		 String[] supported = {"fas","sto"};
		 if (_format.equals("fas")){
			 ArrayList<String> phylip = new ArrayList<String>();
			 Object[] o = readfa(_data);
			 String[] names = (String[])o[0];
			 HashMap<String,String> seqs = (HashMap<String,String>)o[1];
			 Integer[] lens = (Integer[])o[2];
			 Integer rows = (Integer)o[3];
			 int max_name =0;
			 for (int i = 0; i < names.length;i++ ){
				 if (names[i].length() > max_name){
					 max_name = names[i].length();
				 }
			 }
			 phylip.add(names.length+" "+lens[0]);
			 for (int i = 0; i<names.length; i++){
				 phylip.add(pad_right(max_name + 1, names[i])+seqs.get(names[i]));
			 }
			 _data = phylip;
			 _format = "phy";
		 }
		 else if (_format.equals("sto")){  
			 HashMap<String,String> seqs =  new HashMap<String,String>();
			 HashMap<String,Integer> local_names = new HashMap<String,Integer>();
			 ArrayList<String> names = new ArrayList<String>();
			 ArrayList<String> phylip = new ArrayList<String>();
			 int len = -1;
			 int maxnlen = 0;
			 for (int i = 0; i< _data.size();i++){ 
				 String line = _data.get(i);
				 String[] groups = Util.matchesWithGroups(line, "^(\\S+)\\s+(\\S+)\\s*$");
				 if  (!(Util.matches(line,"^#")) && groups != null){
					 String name = groups[1];
					 String seq = groups[2];
					 name = name.replaceAll("\\|\\*\\|", "\\|\\*\\|");
					 if (Util.matches(_data.get(i+1),"^#=GR\\s+"+name)){  // in case there are reads that have the same name as the alignment sequences
						 name = name+"_GR_1";
						 if (local_names.get(name) == null){
							 local_names.put(name,1);
						 }
						 else{
							 groups = Util.matchesWithGroups(name, "(.+GR_)(\\d+)");
							 if (groups != null){
								 int n = local_names.get(name)+1;
								 local_names.put(name, n);
								 name = groups[1]+n;
								 local_names.put(name,n);
							 }
							 else{
								 System.err.println("Error");
							 }
						 }
						 if (seqs.get(name) == null){
							 seqs.put(name,seq.replaceAll( "\\.", "-" ));
							 names.add(name);
						 }
						 else{
							 seqs.put(name, seqs.get(name)+seq.replaceAll( "\\.", "-" ));
						 }
					 }
					 else if (seqs.get(name)== null){
						 seqs.put(name, seq.replaceAll( "\\.", "-" ));
						 names.add(name);
					 }
					 else{
						 seqs.put(name,seqs.get(name)+seq.replaceAll( "\\.", "-" ));
					 }
					 maxnlen = Math.max(maxnlen, name.length());
				 }
				 else if (Util.matches(line,"^\\s*$")){
					 local_names.clear();
				 }
			 }
			 Set<String> keys = seqs.keySet();
			 for (String key : keys){
				 if (len < 0){
					 len = seqs.get(key).length();
				 }
				 else if (len != seqs.get(key).length()){
					 System.err.println("not equal seq lengths");	
				 }
			 }
			 phylip.add(names.size()+" "+len);
			 for (int i = 0; i< names.size(); i++){
				 String s = "";
				 for (int j = 0; j < maxnlen + 1 - names.get(i).length(); j++){
					 s += " ";
				 }
				 phylip.add(names.get(i)+s+seqs.get(names.get(i)));
			 }
			 _data = phylip;
			 _format = "phy";
		 }
		 else{
			 String message =  "Cannot convert "+_format+" to Phylip format! Following formats a supported:";
			 for (int j = 0; j< supported.length;j++){
				 message = message+" "+supported[j];
			 }
			 System.err.println(message);
		 }
	}
	
	// Convert Phylip format alignment to Stockholm format for Hmmer
	public void reformatToStockholm(){
		String[] supported = {"phy"};
		int i = 0;

		if (_format.equals("phy")){
			while (i < _data.size()){
				if (Util.matches(_data.get(i),"^\\s*\\d+\\s+\\d+\\s*$")){	
					_data.set(i,"# STOCKHOLM 1.0");
					_format = "sto";
					break;
				}
				i++;	
			}             
			_data.add("//");
		}
		else{
			String message =  "Cannot convert "+_format+" to Stockholm format! Following formats a supported:";
			for (int j = 0; j< supported.length;j++){
				message = message+" "+supported[j];
			}
			System.err.println(message);
		}
	}
	
	public ArrayList<String> getData(){
		return _data;
	}
	
	public String getFormat(){
		return _format;
	}
	
	private Object[] readfa(ArrayList<String> lines){
		int rows = 0;
		ArrayList<String> names = new ArrayList<String>();
		HashMap <String,String> seqs = new HashMap<String, String>();
		ArrayList<Integer> lens = new ArrayList<Integer>();
		String curname = null;
		String curseq = null;
		int minlen = 1000000;
		int maxlen = -1;
		for (int i = 0; i< lines.size(); i++){
			String line = lines.get(i);
			String name = null;
		    String data = null;
		    String[] groups = Util.matchesWithGroups(line,">\\s*(\\S+)" );
		    if (groups != null){
		    	if( curseq != null ){
		    		seqs.put(curname,curseq);
		    		names.add(curname);
		    		lens.add(curseq.length());
		    	}
		        rows++;
		        curname = groups[1];
		        curseq = "";
		    }
		    else if (curname != null){
		        line.replaceAll( "\\s", "" );
		        curseq += line;
		    }
		}
		    
		seqs.put(curname,curseq);
		names.add(curname);
		lens.add(curseq.length());
		Object[] result =     {names.toArray(new String[names.size()]), seqs, lens.toArray(new Integer[lens.size()]), rows};
		return result;
	}
	private static String pad_right( int n, String s ){
	    if( s.length() < n ){
	    	for (int i = 0; i < n - s.length(); i++){
	    		s+=" ";
	    	}
	      return s;
	    }
	    else{
	      return s;
	    }
	}	
}
