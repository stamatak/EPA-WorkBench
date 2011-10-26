package raxml_gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Util {

	
	public static void writeToFile(ArrayList<String> lines, String file){
	    try{
	        // Create file 
	        FileWriter fstream = new FileWriter(file);
	        BufferedWriter out = new BufferedWriter(fstream);
	        for (int i = 0; i < lines.size(); i++){
	        	out.write(lines.get(i)+"\n");
	        }
	        //Close the output stream
	        out.close();
	    }
	    catch (Exception e){//Catch exception if any
	    	Util.printErrors(e);
	    }
	}
	
	public static void writeToFile(String[] lines, String file){
	    try{
	        // Create file 
	        FileWriter fstream = new FileWriter(file);
	        BufferedWriter out = new BufferedWriter(fstream);
	        for (int i = 0; i < lines.length; i++){
	        	out.write(lines[i]+"\n");
	        }
	        //Close the output stream
	        out.close();
	    }
	    catch (Exception e){//Catch exception if any
	    	Util.printErrors(e);
	    }
	}
	
	public static String join(String[] s, String separator){
		StringBuilder b = new StringBuilder();
		b.append(s[0]);
		
		for (int i = 1; i < s.length; i++){
			b.append(separator+s[i]);
		}
		String joined_string = b.toString();
		return joined_string;
	}

	public static String[] matchesWithGroups(String text, String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if (m.find()){
			String[] groups = new String[m.groupCount()+1];
			for (int i = 0; i< m.groupCount()+1; i++){
				groups[i] = m.group(i);
			}
			return groups;
		}
		return null;
	}
	
	public static boolean matches(String text, String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if (m.find()){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public static String[] readFile(String file){
		ArrayList<String> data = new ArrayList<String>();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null)   {
				data.add(line);
			}
			in.close();
			return data.toArray(new String[data.size()]);
		}catch (Exception e){
			Util.printErrors(e);
			return null;
		}
	}
	
	public static ArrayList<String> readFileToList(String file){
		ArrayList<String> data = new ArrayList<String>();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null)   {
				data.add(line);
			}
			in.close();
			return data;
		}catch (Exception e){
			Util.printErrors(e);
			return null;
		}
	}
	
	public static String[] stringScan(String text, String regex){
		ArrayList<String> matches = new ArrayList<String>();
		while (matches(text, regex)){
			String[] groups = matchesWithGroups(text, "("+regex+")");
			matches.add(groups[1]);
			text = text.replaceFirst(regex, "");
		}
		
		return matches.toArray(new String[matches.size()]);
	}
	
	public  static void printErrors(Exception e){
		e.printStackTrace();
		String[] errors = new String[e.getStackTrace().length];
		for (int i = 0; i<e.getStackTrace().length; i++){
			errors[i] = e.getStackTrace()[i].toString();
		}
		JOptionPane.showMessageDialog(Configuration.getMainFrameStatic(), errors,"An Error occurred!",JOptionPane.ERROR_MESSAGE);
	}
	
	public static String[] textWrapping(HashMap <String,String> text){
		Set<String> keys = text.keySet();
		ArrayList<String> wrapped_text = new ArrayList<String>();
		for(String key : keys){
			String line = "*"+text.get(key);
			int rest = line.length();
			while (rest > Constants.TEXTWIDTH){
				for(int i = Constants.TEXTWIDTH; i >= 0 && line.length() > Constants.TEXTWIDTH; i--){
					if (line.substring(i, i+1).endsWith(" ")){
						wrapped_text.add(line.substring(0, i));
						line = line.substring(i+1);
						break;
					}
					if (i==0){
						wrapped_text.add(line.substring(0, Constants.TEXTWIDTH));
						line = line.substring(Constants.TEXTWIDTH);
						break;
					}
				}
				rest = line.length();
			}
			wrapped_text.add(line);
		}
		return wrapped_text.toArray(new String[wrapped_text.size()]);
	}

	
	public  static void printSomething(String e){
		JOptionPane.showMessageDialog(Configuration.getMainFrameStatic(), e,"An Error occurred!",JOptionPane.ERROR_MESSAGE);
	}
	
	static String getSeparatorForRegex(){
		if (File.separator.equals("/")){ // Linux
			return "\\/";
		}
		else if (File.separator.equals("\\")){ // Windows
			return "\\\\";
		}
		else {
			return File.separator;
		}
	}
	
	public static void concatenateFiles(String path, final String regex, String destination){
		File job_folder = new File (path);
		File[] class_files = job_folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				// TODO Auto-generated method stub
				return Util.matches(name, regex);
			}
		});
		Arrays.sort(class_files);
		ArrayList<String> all_file_content = new ArrayList<String>();
		for(int i = 0; i < class_files.length; i++){
			all_file_content.addAll(readFileToList(class_files[i].getAbsolutePath()));
		}
		writeToFile(all_file_content, destination);
	}
	
}
