package raxml_gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Vector;

public class Constants {
	
	// GUI Configurations
	public static final String MAIN_FRAME_TITLE = "RAxML Workbench";
	public static final int MAIN_FRAME_HEIGHT = 700;
	public static final int MAIN_FRAME_WIDTH = 800;
	public static final int ABOUT_FRAME_HEIGHT = 650;
	public static final int ABOUT_FRAME_WIDTH = 400;
	public static final Font BROWSE_BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 10);
	public static final Font HEADER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	public static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	public static final Color BACKGROUND_COLOR = new Color(255,255,255);
//	public static final Color BACKGROUND_COLOR = Color.red;
	public static final Color HEADER_COLOR = new Color(226,236,243);
	public static final Color ERROR_COLOR = Color.red;
	public static final String VERSION = "1.0";
	public static final Dimension FILECHOOSER_SIZE = new Dimension(800,600);
	public static final int TEXTWIDTH = 100;
	
	
	public static final File JAR_PATH = projectPath();
	
	private static final File BIOPROGS =  new File(JAR_PATH,"bioprogs");
	private static final File MISC= new File(JAR_PATH,"misc");
	private static final File JARS = new File(JAR_PATH, "jars");
	
	private static final File RAXML_SSE3 = new File(BIOPROGS,"raxml_SSE3");
	private static final File RAXML_PTHREADS_SSE3 = new File(BIOPROGS,"raxml_pthreads_SSE3");
	private static final File HMMER = new File(new File(BIOPROGS,"hmmer"),"src");
	
	public static final File RAXML = new File(RAXML_SSE3,"raxmlHPC-SSE3");
	public static final File RAXML_PARALLEL = new File(RAXML_PTHREADS_SSE3,"raxmlHPC-PTHREADS-SSE3");
	public static final File HMMBUILD = new File(HMMER,"hmmbuild");
	public static final File HMMALIGN = new File(HMMER,"hmmalign");
	public static final File SWPS3 = new File(new File(BIOPROGS,"swps3"),"swps3");
	public static final File PWDIST = new File(new File(BIOPROGS,"swps3"),"pw_dist"); // Simons Smith Waterman implementation for Windows user
	public static final File MATRIX_DNA = new File(new File(new File(BIOPROGS,"swps3"),"matrices"),"dna_matrix.mat");
	public static final File MATRIX_AA = new File(new File(new File(BIOPROGS,"swps3"),"matrices"),"blosum62.mat");
	public static final File CONVERT_TO_PHYLOXML = new File(JARS,"convertToPhyloXML.jar");
	public static final File TREECHECK = new File(JARS,"treecheck.jar");
	public static final File TREE_MERGE_LABELS = new File(JARS,"treeMergeLengthsLabels.jar");
	public static final File ARCHAEOPTERYX_CONFIGURATIONFILE = new File(JARS,"_aptx_configuration_file");
	public static final File HELP = new File(MISC,"help.pdf");
	public static final File ABOUT = new File(MISC,"about.html");
	public static final File CONFIGURATION = new File(JAR_PATH,"raxml_wb.conf");
	
	public static final File UCLUST_TEST = new File(new File(BIOPROGS,"uclust"),"uclust32");
	
	public static final String PDF_READER_LINK = "http://get.adobe.com/de/reader/";
	
	public static final File TESTFOLDER = new File(JAR_PATH,"testfiles");
	
	
	// Data Constants
	public static final Vector <String> DNA_HET_MODELS = dnaHetModels();
	public static final Vector <String> AA_HET_MODELS = aaHetModels();
	public static final Vector <String> AA_MATRICES = aaMatrices();
	public static final Vector <String> PAR_HET_MODELS = parHetModels();
	public static final Vector <String> MGA_HET_MODELS = mgaHetModels();
	

	
	
	
//	// Constants Constructors
//	private static String projectPath(){
//		String path = "";
//		path = new File(Constants.class.getResource("Constants.class").getFile().replaceAll("file:","")).getParentFile().getParentFile().getParent();
//		return path;
//	}
	
	private static File projectPath(){
		File path = new File(Constants.class.getResource("Constants.class").getFile().replaceAll("file:","")).getParentFile().getParentFile().getParentFile();
		return path;
	}
	
	private static Vector<String> dnaHetModels(){
		Vector <String> dna_het_models = new Vector<String>();
		dna_het_models.add("GTRGAMMA");
		dna_het_models.add("GTRGAMMAI");
		dna_het_models.add("GTRCAT");
		dna_het_models.add("GTRCATI");
		return dna_het_models;
	}
	private static Vector<String> aaHetModels(){
		Vector <String> aa_het_models = new Vector<String>();
		aa_het_models.add("PROTGAMMA");
		aa_het_models.add("PROTGAMMAI");
		aa_het_models.add("PROTCAT");
		aa_het_models.add("PROTCATI");
		return aa_het_models;
	}
	private static Vector<String> aaMatrices(){
		Vector <String> aa_matrices = new Vector<String>();
		aa_matrices.add("DAYHOFF");
		aa_matrices.add("DCMUT");
		aa_matrices.add("JTT");
		aa_matrices.add("MTREV");
		aa_matrices.add("WAG");
		aa_matrices.add("RTREV");
		aa_matrices.add("CPREV");
		aa_matrices.add("VT");
		aa_matrices.add("BLOSUM62");
		aa_matrices.add("MITMAM");
		aa_matrices.add("LG");
		return aa_matrices;
	}
	private static Vector<String> parHetModels(){
		Vector <String> par_het_models = new Vector<String>();
		par_het_models.add("GAMMA");
		par_het_models.add("GAMMAI");
		par_het_models.add("CAT");
		par_het_models.add("CATI");
		return par_het_models;
	}
	private static Vector<String> mgaHetModels(){
		Vector <String> par_het_models = new Vector<String>();
		par_het_models.add("GAMMA");
		par_het_models.add("GAMMAI");
		par_het_models.add("CAT");
		par_het_models.add("CATI");
		return par_het_models;
	}
}
