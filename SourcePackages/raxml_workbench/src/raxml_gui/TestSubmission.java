package raxml_gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestSubmission {
	
	private static String _s = File.separator;
	private static String _jobpath = _s+"home"+_s+"denis"+_s+"Desktop"+_s+"test_gui"+_s+"test";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String command = "java -jar "+_s+"home"+_s+"denis"+_s+"workspace"+_s+"raxml_workbench"+_s+"jars"+_s+"treeMergeLengthsLabels.jar "+_s+"home"+_s+"denis"+_s+"work"+_s+"RAxMLWS"+_s+"testfiles"+_s+"Raxml_fast_treefile.tree "+_s+"home"+_s+"denis"+_s+"Desktop"+_s+"test_gui"+_s+"test"+_s+"RAxML_originalLabelledTree.test ";
		Runtime runtime = Runtime.getRuntime();
        int nrOfProcessors = runtime.availableProcessors();
        System.out.println(nrOfProcessors);
	}

	
	private static void run(String commands, String direct_out_to){
		Process p; 
		ArrayList<String> data = new ArrayList<String>();
//		try{
		String[] command = commands.split("\\s+");
	
		try{
			ProcessBuilder builder2 = new ProcessBuilder(command);
			builder2.directory(new File(_jobpath));
			p = builder2.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				if (direct_out_to == null){
					System.out.println(line);
				}
				else{
					data.add(line);
				}
			}
			
			p.waitFor();
			Util.writeToFile(data, direct_out_to);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
