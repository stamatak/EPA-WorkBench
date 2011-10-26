package raxml_gui;

import java.util.ArrayList;

public class PartitionfileParser {

	private boolean _valid_format;
	private String[] _data;
	private String _error = "Invalid partitionfile format! \n";
	private String _filename;
	private int _len;
	private int[] _occ;
	private ArrayList<String> _matrices = new ArrayList<String>();
	
	public PartitionfileParser(String file, int alignmentlength){
		_filename = file;
		_data =Util.readFile(file);
		_len = alignmentlength;
		_occ = new int[_len];
		checkFormat();
		getRaxmlModelParameters();
	}
	
	public PartitionfileParser(String file){
		_filename = file;
		_data =Util.readFile(file);
		_valid_format = true;
		getRaxmlModelParameters();
	}
	
	
	private void checkFormat(){
		int n = 1;
		ArrayList<Integer[]> reads = new ArrayList<Integer[]>();
		for (int i = 0; i < _data.length; i++){
			String[] groups1 = Util.matchesWithGroups(_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\s*,)*(\\s+\\d+\\s*-\\s*\\d+\\s*)$");
			String[] groups2 = Util.matchesWithGroups(_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+,)*(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+)$");
			if (_data[i].matches("^\\s*$")){
			}
			else if (groups1 != null){
				_matrices.add(groups1[1]);
				String[] digits = Util.stringScan(_data[i],"\\d+\\-\\d+");
				for (int j = 0; j < digits.length; j++){
					String[] groups = Util.matchesWithGroups(digits[j], "(\\d+)\\-(\\d+)");
					if (groups != null ){
						if (Integer.valueOf(groups[1]) > _len || Integer.valueOf(groups[2]) > _len){
							_error += "Read position to long(Alignment length is "+_len+")! \n"+_filename+" line: "+n+" => "+_data[i];
							return;
						}
					}
				}
				if (_data[i].matches("^[A-Z]+,\\s+\\S+\\s+=(\\s+\\d+-\\d+,)*(\\s+\\d+-\\d+)$")){
					String[] a  = Util.stringScan(_data[i],"\\d+\\-\\d+");
					String[] groups = Util.matchesWithGroups(_data[i], "(\\d+)\\-(\\d+)");
					if (groups != null ){
						Integer[] s = {Integer.valueOf(groups[1]), Integer.valueOf(groups[2])};
						reads.add(s);
					}
				}
			}
			else if (groups2 != null){
				_matrices.add(groups2[1]);
				String[] digits = Util.stringScan(_data[i],"\\d+\\-\\d+");
				for (int j = 0; j < digits.length; j++){
					String[] groups = Util.matchesWithGroups(digits[j], "(\\d+)\\-(\\d+)");
					if (groups != null ){
						if (Integer.valueOf(groups[1]) > _len || Integer.valueOf(groups[2]) > _len){
							_error += "Read position to long(Alignment length is "+_len+")! \n"+_filename+" line: "+n+" => "+_data[i];
							return;
						}
					}
				}
				if (_data[i].matches("^[A-Z]+,\\s+\\S+\\s+=(\\s+\\d+-\\d+,)*(\\s+\\d+-\\d+)$")){
					String[] a  = Util.stringScan(_data[i],"\\d+\\-\\d+");
					String[] groups = Util.matchesWithGroups(_data[i], "(\\d+)\\-(\\d+)");
					if (groups != null ){
						Integer[] s = {Integer.valueOf(groups[1]), Integer.valueOf(groups[2])};
						reads.add(s);
					}
				}
			}
			else{
				_error += _filename+" line: "+n+" => "+_data[i];
				return;
			}
			n = n+1;
		}
		for (int i = 0; i < reads.size(); i++){
			int x = reads.get(i)[0];
			int y = reads.get(i)[1];
			if (!occupySequence(x,y)){
				_error += "Reads are not allowed to overlap! \n"+_filename;
				return;
			}
		}
		_valid_format = true;
		 
	}
	
	private boolean occupySequence(int i, int j){
		i = i-1;
		while (i < j){
			if (_occ[i] == 1){
				return false;
			}
			else{
				_occ[i] = 1;
			}
			i = i+1;
	    }
	    return true;
	}

	private void getRaxmlModelParameters(){
		for(int i = 0; i<_data.length;i++){
			String[] groups1 = Util.matchesWithGroups(_data[i], "^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+-\\d+,)*(\\s+\\d+-\\d+)$");
			String[] groups2 = Util.matchesWithGroups(_data[i], "^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+-\\d+\\\\\\d+,)*(\\s+\\d+-\\d+\\\\\\d+)$");
			if (_data[i].matches("^\\s*$")){
				
			}
			else if(groups1 != null){
				_matrices.add(groups1[1]);
			}
			else if(groups2 != null){
				_matrices.add(groups2[1]);
			}
			else{
				_error += _filename+" line: "+i+" => "+_data[i];
			}
		}
	}
	
	public boolean isValidFormat(){
		return _valid_format;
	}
	
	public String getErrorMessage(){
		return _error;
	}
	
	public ArrayList<String> getMatrices(){
		return _matrices;
	}
	
}
