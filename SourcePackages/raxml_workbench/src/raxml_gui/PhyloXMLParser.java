package raxml_gui;

import java.util.ArrayList;

public class PhyloXMLParser {

	private String[] _data;
	
	public PhyloXMLParser(String file){
		_data = Util.readFile(file);
	}
	
	public boolean checkFormat(){
		int clades_open = 0;
		int	clades_closed = 0;
		if (!(_data[0].trim().equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))){
			return false;
		}
		for (int i = 0; i < _data.length; i++){
			String line = _data[i];
			
			int idx = 0;
			while( (idx = line.indexOf("<clade", idx)) != -1 ) {
				idx += 6;
				clades_open++;
			
			}
		
			
			idx = 0;
			while( (idx = line.indexOf("</clade>", idx)) != -1 ) {
				idx += 8;	
				clades_closed++;
			
			}
//			while (Util.matches(line,"<clade")){
//				line = line.replaceFirst("<clade","");
//		        clades_open++;
//			}
//			while (Util.matches(line, "<\\/clade>")){
//		        line = line.replaceFirst("<\\/clade>","");
//		        clades_closed++;
//			}
		}
		return clades_open == clades_closed;
	}
	
}
