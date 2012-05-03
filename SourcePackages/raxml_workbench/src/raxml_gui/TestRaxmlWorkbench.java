package raxml_gui;

import java.io.File;
import java.util.HashMap;

// RAxML Workbench Testsuite

public class TestRaxmlWorkbench {
	
	//PARAMETERS FOR RAXML
	private static String _s = File.separator;
	private static HashMap<String,String> _parameters = new HashMap<String,String>();
	private static String _alignmentfile = "alignmentfile";
	private static String _use_reads = "use_reads";
	private static String _readsfile = "readsfile";
	private static String _use_clustering = "use_clustering";
	private static String _alignment_type = "alignment_type";
	private static String _het_model = "het_model";
	private static String _partitionfile = "partitionfile";
	private static String _treefile = "treefile";
	private static String _cores = "cores";
	private static String _fast = "fast";
	private static String _use_bootstrap = "use_bootstrap";
	private static String _random_seed = "random_seed";
	private static String _samples = "samples";
	private static String _use_heuristics = "use_heuristics";
	private static String _heuristic_model = "heuristic_model";
	private static String _heuristic_value = "heuristic_value";
	private static String _jobname = "jobname";
	private static String _jobfolder = "jobfolder";
	private static String _classificationfile = "classificationfile";
	private static String _save_as = "save_as";
	private static String _mga = "mga";
	
	private static MainFrame _main_frame;
	
	private static String _test_alignmentfile = new File(new File(Constants.TESTFOLDER, "alignmentfile"),"alignment_file").getAbsolutePath();
	private static String _test_alignmentfile_phylip_error =new File(new File(Constants.TESTFOLDER,"alignmentfile"),"alignment_file_error").getAbsolutePath();
	private static String _test_alignmentfile_phylip = new File(new File(Constants.TESTFOLDER,"alignmentfile"),"alignment_file").getAbsolutePath();
	private static String _test_alignmentfile_fasta_error = new File(new File(Constants.TESTFOLDER,"alignmentfile"),"fasta_format_test_error.fas").getAbsolutePath();
	private static String _test_alignmentfile_fasta = new File(new File(Constants.TESTFOLDER,"alignmentfile"),"fasta_format_test.fas").getAbsolutePath();
	private static String _test_alignmentfile_interleaved_duplicate_names = new File(new File(Constants.TESTFOLDER,"alignmentfile"),"alignment_file_duplicate_names.phy").getAbsolutePath();
	private static String _test_alignmentfile_aa = new File(new File(Constants.TESTFOLDER, "alignmentfile"),"alignment_file_aa").getAbsolutePath();
	
	private static String _test_readsfile=new File(new File(Constants.TESTFOLDER,"readsfile"),"queryfile.fas").getAbsolutePath();
	private static String _test_readsfile_fasta_error = new File(new File(Constants.TESTFOLDER,"readsfile"),"fasta_error.fas").getAbsolutePath();
	private static String _test_readsfile_alignment_error = new File(new File(Constants.TESTFOLDER,"readsfile"),"alignment_input.fas").getAbsolutePath();
	private static String _test_readsfile_tree_error = new File(new File(Constants.TESTFOLDER,"readsfile"),"tree_input.tree").getAbsolutePath();
	private static String _test_readsfile_duplicate_names = new File(new File(Constants.TESTFOLDER,"readsfile"),"duplicate_names.ref").getAbsolutePath();
	
	private static String _test_partitionfile = new File(new File(Constants.TESTFOLDER,"partitionfile"),"partition_file_shouldwork.par").getAbsolutePath();
	private static String _test_partitionfile_length_error =new File(new File(Constants.TESTFOLDER,"partitionfile"),"partition_file_length_error.par").getAbsolutePath();
	private static String _test_partitionfile_overlap_error = new File(new File(Constants.TESTFOLDER,"partitionfile"),"partition_file_overlap_error.par").getAbsolutePath();
	
	private static String _test_treefile = new File(new File(Constants.TESTFOLDER,"treefile"),"Raxml_fast_treefile.tree").getAbsolutePath();
	private static String _test_treefile_error = new File(new File(Constants.TESTFOLDER,"treefile"),"Raxml_fast_treefile_error.tree").getAbsolutePath();
	private static String _test_treefile_duplicate_names = new File(new File(Constants.TESTFOLDER,"treefile"),"duplicate_names.tree").getAbsolutePath();
	private static String _test_treefile_aa = new File(new File(Constants.TESTFOLDER,"treefile"),"treefile_aa.tree").getAbsolutePath();
	
	private static String _test_labeled_treefile = new File(new File(Constants.TESTFOLDER,"phyConv"),"final_tree.tree").getAbsolutePath();
	private static String _test_classificationfile = new File(new File(Constants.TESTFOLDER,"phyConv"),"RAxML_classificationLikelihoodWeights.job1").getAbsolutePath();
	private static String _test_save_as = new File(new File(Constants.TESTFOLDER,"phyConv"),"test.phyloxml").getAbsolutePath();
	
	private static String _test_mga_alignmentfile_fasta = new File(new File(Constants.TESTFOLDER,"mga"),"alignment.phy").getAbsolutePath();
	private static String _test_mga_partitionfile = new File(new File(Constants.TESTFOLDER,"mga"),"partitionfile").getAbsolutePath();
	private static String _test_mga_treefile = new File(new File(Constants.TESTFOLDER,"mga"),"treefile.newick").getAbsolutePath();
	private static String _test_mga_readsfile = new File(new File(Constants.TESTFOLDER,"mga"),"queryfile").getAbsolutePath();
	
	private static int _success = 0;
	private static int _failure = 0;
	
	
	private static Job _test;
	
	public static void main(String[] args) {
		_main_frame = new MainFrame("test");
		System.out.println("Starting Tests...");
		_test = new Job(_main_frame);
		testSgaPanel();

	}

	private static void testSgaPanel(){
//		// test Validations
		testAlignmentfileValidation();
		testReadsfileValidation();
		testPartitionfileValidation();
		testTreefileValidation();
		testCoresValidation();
//////		testBootstrappingValidation(); // Bootstrapping is not supported anymore
		testJobnameValidation();
		
		// test Submissions
		testSimpleSubmission();
		testSimpleAASubmission();
		testHmmerSubmission();
////		testHmmerSubmissionInterleaved(); // The provided alignment file is not correct. The names within the alignment are cutted and therefore RAxML throws an error because some taxa of the tree are missing.
		testClusteringSubmission();
		testMGASubmission();
//		
		// test PhyloXML Converter
		testPhyloXMLConverter();
		printTestresult();
		
	}
	
	private static void testAlignmentfileValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case  1
		String message = "Alignmentfile Fasta error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_alignmentfile,_test_alignmentfile_fasta_error);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) == null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Alignmentfile Fasta no_error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_fasta);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) != null){  //no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 3
		message = "Alignmentfile Phylip error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_phylip_error);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 4
		message = "Alignmentfile Phylip no_error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_phylip);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 5
		message = "Alignmentfile duplicate taxa and interleaved format no error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_interleaved_duplicate_names);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 6
		message = "Alignmentfile amino acid ";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_aa);
		parameters.put(_alignment_type, "aa");
		parameters.put(_het_model, "PROTGAMMAWAGF");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_alignmentfile) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}

	}
	
	private static void testReadsfileValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case 1
		String message = "Readsfile Fasta error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_alignment_type, "dna");
		parameters.put(_readsfile, _test_readsfile_fasta_error);
		parameters.put(_use_reads, "true");
		parameters.put(_het_model, "GTRGAMMA");
		
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_readsfile) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Readsfile Alignmenterror";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_fasta);
		parameters.put(_alignment_type, "dna");
		parameters.put(_readsfile, _test_readsfile_alignment_error);
		parameters.put(_use_reads, "true");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_readsfile) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 3
		message = "Readsfile Treeerror";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_phylip_error);
		parameters.put(_alignment_type, "dna");
		parameters.put(_readsfile, _test_readsfile_tree_error);
		parameters.put(_use_reads, "true");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_readsfile) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 4
		message = "Readsfile no_error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_phylip);
		parameters.put(_alignment_type, "dna");
		parameters.put(_readsfile, _test_readsfile);
		parameters.put(_use_reads, "true");
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_readsfile) != null){  // no error expected
				System.out.println(errors.get(_readsfile));
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
	}
	
	private static void testPartitionfileValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case 1
		String message = "Partitionfile Length error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_alignmentfile,_test_alignmentfile_fasta);
		parameters.put(_alignment_type, "par");
		parameters.put(_partitionfile, _test_partitionfile_length_error);
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_partitionfile) == null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Partitionfile Overlap error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile_fasta);
		parameters.put(_alignment_type, "par");
		parameters.put(_partitionfile, _test_partitionfile_overlap_error);
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_partitionfile) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 3
		message = "Partitionfile no_error";
		parameters.clear();
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_alignment_type, "par");
		parameters.put(_partitionfile, _test_partitionfile);
		parameters.put(_het_model, "GTRGAMMA");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_partitionfile) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				System.out.println(errors.get(_partitionfile));
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
	}
	
	private static void testTreefileValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();

		// Test case 1
		String message = "Treefile error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_treefile,_test_treefile_error);
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_treefile) == null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Treefile no_error";
		parameters.clear();
		parameters.put(_treefile,_test_treefile);
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_treefile) != null){  //no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
	}
	
	private static void testCoresValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case  1
		String message = "Cores no digit error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_cores,"a");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_cores) == null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Cores too many cores error";
		parameters.clear();
		parameters.put(_cores, "2000");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_cores) == null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 3
		message = "Cores no_error";
		parameters.clear();
		parameters.put(_cores, "2");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_cores) != null){  // error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
	}
	
	private static void testBootstrappingValidation(){
		SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case  1
		String message = "Samples & Random Seed no digit error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_use_bootstrap,"true");
		parameters.put(_samples,"1dg3");
		parameters.put(_random_seed, "asd2");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_samples) == null && errors.get(_random_seed) == null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Samples & Random Seed no_error";
		parameters.clear();
		parameters.put(_use_bootstrap,"true");
		parameters.put(_samples, "100");
		parameters.put(_random_seed, "1234");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_samples) != null && errors.get(_random_seed) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
	}
	
	private static void testJobnameValidation(){
			SgaFormPanel sga = _test.getSGAFormPanel();
		
		// Test case  1
		String message = "Jobname error";
		HashMap <String,String> parameters = new HashMap<String,String>();
		// simple submissiontest
		parameters.put(_jobname,"");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_jobname)== null){  //error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 2
		message = "Jobname no_error";
		parameters.clear();
		parameters.put(_jobname, "blabla");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (errors.get(_jobname) != null){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
		
		// Test case 3
		message = "Jobname substitution no_error";
		parameters.clear();
		parameters.put(_jobname, "bla"+_s+"bla bla");
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			HashMap<String,String> errors = sga.getInputErrors();
			if (!(parameters.get(_jobname).equals("bla_bla_bla"))){  // no error expected
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
			else{
				System.out.println(message+":\tOK");
				_success++;
			}
		}
	}
	
	private static void testSimpleSubmission(){
		String message = "SGA form test simple";
		SgaFormPanel sga = _test.getSGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		
		// simple submissiontest
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_use_reads, null);
		parameters.put(_readsfile, null);
		parameters.put(_use_clustering,null);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			
			sga.setParameters(parameters);
			sga.submitJob(true);
			try {
				if (checkFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
		
				deleteJobDirectory("test");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
//		System.exit(0);
		
//// NO BOOTSTRAPPING SUPPORTED ANYMORE		
//		// simple submissiontest 2 with Bootstrapping
//		parameters.put(_alignmentfile,_test_alignmentfile);
//		parameters.put(_use_reads, null);
//		parameters.put(_readsfile, null);
//		parameters.put(_use_clustering,null);
//		parameters.put(_alignment_type, "dna");
//		parameters.put(_het_model, "GTRGAMMA");
//		parameters.put(_partitionfile, null);
//		parameters.put(_treefile, _test_treefile);
//		parameters.put(_cores, null);
//		parameters.put(_fast, "y"); // fast heuristics default
//		parameters.put(_use_bootstrap, "true");
//		parameters.put(_random_seed, "4321");
//		parameters.put(_samples, "3");
//		parameters.put(_use_heuristics, null);
//		parameters.put(_heuristic_model, null);
//		parameters.put(_heuristic_value, null);
//		parameters.put(_jobname, "test");
//		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
//		if (_test.getSGAFormPanel() == null){
//			System.out.println("sga is null");
//		}
//		else{
//			sga.setParameters(parameters);
//			sga.submitJob(true);
//			try {
//			
//				if (checkFiles("test")){
//					System.out.println(message+"2:\tOK");
//					_success++;
//				}
//				else {
//					System.out.println(message+"2:\tFAILURE");
//					_failure++;
//				}
//				deleteJobDirectory("test");
//			}
//			catch (Exception e){
//				e.printStackTrace();
//			}
//		}
		
		// simple submissontest 3 with Heuristics
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_use_reads, null);
		parameters.put(_readsfile, null);
		parameters.put(_use_clustering,null);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); 
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, "true");
		parameters.put(_heuristic_model, "ML");
		parameters.put(_heuristic_value, "1/16");
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.submitJob(true);
			try {
				if (checkFiles("test")){
					System.out.println(message+"3:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"3:\tFAILURE");
					_failure++;
				}
				deleteJobDirectory("test");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	private static void testSimpleAASubmission(){
		String message = "SGA form test simple AA";
		SgaFormPanel sga = _test.getSGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		
		// simple submissiontest
		parameters.put(_alignmentfile,_test_alignmentfile_aa);
		parameters.put(_use_reads, null);
		parameters.put(_readsfile, null);
		parameters.put(_use_clustering,null);
		parameters.put(_alignment_type, "aa");
		parameters.put(_het_model, "PROTGAMMAWAGF");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile_aa);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.submitJob(true);
			try {
				if (checkFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
		
				deleteJobDirectory("test");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static void testHmmerSubmission(){
		String message = "SGA form test hmmer";
		SgaFormPanel sga = _test.getSGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_use_reads, "true");
		parameters.put(_readsfile, _test_readsfile);
		parameters.put(_use_clustering,null);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.submitJob(true);
			try {
				if (checkFiles("test")&& checkHmmerFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
				deleteJobDirectory("test");
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static void testHmmerSubmissionInterleaved(){
		String message = "SGA form test hmmer interleaved alignment";
		SgaFormPanel sga = _test.getSGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		parameters.put(_alignmentfile,_test_alignmentfile_interleaved_duplicate_names);
		parameters.put(_use_reads, "true");
		parameters.put(_readsfile, _test_readsfile_duplicate_names);
		parameters.put(_use_clustering,null);
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile_duplicate_names);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.validateInput();
			sga.submitJob(true);
			try {
				if (checkFiles("test")&& checkHmmerFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
				deleteJobDirectory("test");
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	private static void testClusteringSubmission(){
		String message = "SGA form test clustering";
		SgaFormPanel sga = _test.getSGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		parameters.put(_alignmentfile,_test_alignmentfile);
		parameters.put(_use_reads, "true");
		parameters.put(_readsfile, _test_readsfile);
		parameters.put(_use_clustering,"true");
		parameters.put(_alignment_type, "dna");
		parameters.put(_het_model, "GTRGAMMA");
		parameters.put(_partitionfile, null);
		parameters.put(_treefile, _test_treefile);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		if (_test.getSGAFormPanel() == null){
			System.out.println("sga is null");
		}
		else{
			sga.setParameters(parameters);
			sga.submitJob(true);
			try {
			
				if (checkFiles("test")&& checkHmmerFiles("test")&& checkClusteringFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
//				deleteJobDirectory("test");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	private static void testMGASubmission(){
		String message = "MGA form test";
		MgaFormPanel mga = _test.getMGAFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		parameters.put(_alignmentfile,_test_mga_alignmentfile_fasta);
		parameters.put(_readsfile, _test_mga_readsfile);
		parameters.put(_use_clustering,"true");
		parameters.put(_het_model, "GAMMA");
		parameters.put(_partitionfile, _test_mga_partitionfile);
		parameters.put(_treefile, _test_mga_treefile);
		parameters.put(_cores, null);
		parameters.put(_fast, "v"); // fast heuristics default
		parameters.put(_use_bootstrap, null);
		parameters.put(_random_seed, null);
		parameters.put(_samples, null);
		parameters.put(_use_heuristics, null);
		parameters.put(_heuristic_model, null);
		parameters.put(_heuristic_value, null);
		parameters.put(_jobname, "test");
		parameters.put(_jobfolder, Constants.TESTFOLDER.getAbsolutePath());
		parameters.put(_mga, "true");
		if (_test.getMGAFormPanel() == null){
			System.out.println("mga is null");
		}
		else{
			mga.setParameters(parameters);
			mga.submitJob(true);
			try {
			
				if (checkFiles("test")&& checkHmmerFiles("test")&& checkClusteringFiles("test")){
					System.out.println(message+"1:\tOK");
					_success++;
				}
				else {
					System.out.println(message+"1:\tFAILURE");
					_failure++;
				}
				deleteJobDirectory("test");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static void testPhyloXMLConverter(){
		String message = "PhyloConverter form test";
		PhyloXMLConverterFormPanel phy = _test.getPhyloxmlConverterFormPanel();
		HashMap <String,String> parameters = new HashMap<String,String>();
		parameters.put(_classificationfile,_test_classificationfile);
		parameters.put(_treefile, _test_labeled_treefile);
		parameters.put(_save_as, _test_save_as);
		if (_test.getSGAFormPanel() == null){
			System.out.println("Phy is null");
		}
		else{
			phy.setParameters(parameters);
			phy.submitJob(true);
			
			if (checkPhyloXMLFile(_test_save_as)){
				System.out.println(message+":\tOK");
				_success++;
			}
			else {
				System.out.println(message+":\tFAILURE");
				_failure++;
			}
		}
		
	}
	
	// simple check if RAxML files have been generated
	private static boolean checkFiles(String jobname){
		boolean state = true;
		File job_folder = new File (_main_frame.getConfiguration().getWorkspace());
		File[] job_files = job_folder.listFiles();
		// file counter
		if (job_files.length < 3){
			state = false;
			System.out.println("Error: There are only "+job_files.length+" file(s) present!"  );
		}
		String path =Raxml.class.getResource("Raxml.class").getPath();
//		System.out.println(path);
		//file parser
		String phyloXML = job_folder.getAbsolutePath()+_s+"treefile.phyloxml";
		if (!(new PhyloXMLParser(phyloXML)).checkFormat()){
			System.out.println("Error: PhyloXML format has errors!");
			state = false;
		}
			// check phyloXML
		return state;
	}
	
	private static boolean checkPhyloXMLFile(String file){
		boolean state = true;
		if (!(new PhyloXMLParser(file)).checkFormat()){
			System.out.println("Error: PhyloXML format has errors!");
			state = false;
		}
			// check phyloXML
		return state;
	}
	
	// simple check if all hmmer files are present
	private static boolean checkHmmerFiles(String jobname){
		boolean sto1 = false;
		boolean sto2 = false;
		boolean hmm = false;
		File job_folder = new File (_main_frame.getConfiguration().getWorkspace());
		File[] job_files = job_folder.listFiles();
		for (int i = 0; i< job_files.length; i++){
			if (job_files[i].getAbsolutePath().equals(job_folder+_s+"alignmentfile.sto")){
				sto1 = true;
			}
			else if (job_files[i].getAbsolutePath().equals(job_folder+_s+"alignmentfile2.sto")){
				sto2 = true;
			}
			else if (job_files[i].getAbsolutePath().equals(job_folder+_s+"alignmentfile.hmm")){
				hmm = true;
			}
		}
		if (!sto1){
			System.out.println("Error: Stockholmfile1 is missing!");
		}
		if (!sto2){
			System.out.println("Error: Stockholmfile2 is missing!");
		}
		if (!hmm){
			System.out.println("Error: HMMfile is missing!");
		}
		return (sto1 && sto2 && hmm);
	}
	
	// simple check if all clustering files are present
	private static boolean checkClusteringFiles(String jobname){
		boolean uc = false;
		boolean fas = false;
		File job_folder = new File (_main_frame.getConfiguration().getWorkspace());
		File[] job_files = job_folder.listFiles();
		for (int i = 0; i< job_files.length; i++){
			if (job_files[i].getAbsolutePath().equals(job_folder+_s+"cluster.uc")){
				uc = true;
			}
			else if (job_files[i].getAbsolutePath().equals(job_folder+_s+"cluster.fas")){
				fas = true;
			}
		}
		if (!uc){
			System.out.println("Error: cluster.uc is missing!");
		}
		if (!fas){
			System.out.println("Error: cluster.fas is missing!");
		}
		
		return (uc && fas);
	}
	
	private static void deleteJobDirectory(String jobname){
		File job_folder = new File (_main_frame.getConfiguration().getWorkspace());
		File[] job_files = job_folder.listFiles();
		for (int i = 0; i < job_files.length; i++){
			job_files[i].delete();
		}
		job_folder.delete();	
	}
	
	private static void printTestresult(){
		System.out.println("Finished Testing with "+ _success + " success(es) and "+_failure + " error(s)!");
	}
	
}
