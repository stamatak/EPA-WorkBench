package raxml_gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

	// Similar so RaxmlAndSendEmail.rb
public class Submission implements Runnable {
	
	private HashMap<String,String> _raxml_options = new HashMap<String, String>();
	private String _jobname;
	private String _jobpath;
	private boolean _use_readsfile = false;
	private String _readsfile;
	private boolean _use_clustering = false;
	private String _uclust_identity;
	private boolean _multi_gene_alignment = false;
	private boolean _tree_builder = false;
	private boolean _test_mapping = false;
	private ArrayList<String> _commands = new ArrayList<String>();
	private ArrayList<String> _log = new ArrayList<String>(); 
	
	private Job _job;
	private Jobtype _jobtype;
	private boolean _aborted = false;
	private Process _current_process;
	private boolean _testing = false;
	
	public Submission(String[] commands, Job job, final Jobtype jobtype, boolean testing){
		parseOptions(commands);
		_job = job;
		_jobtype = jobtype;
		_testing = testing;
		for (int i = 0; i < commands.length; i++){
			_log.add(commands[i]+" ");
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (_jobtype == Jobtype.SGA){
			if (_use_clustering && !_aborted){
				runUclust();
				Util.writeToFile(_log, _jobpath+File.separator+"log.txt");
			}
			if (_use_readsfile && !_aborted){
				runHmmer();
				Util.writeToFile(_log, _jobpath+File.separator+"log.txt");
			}
			if(!_aborted){
				runRAxML();
				if(!_aborted && !_tree_builder){
					buildRightTree();
					if(!_aborted){
						convertTreefileToPhyloXML();
					}
				}
				Util.writeToFile(_log, _jobpath+File.separator+"log.txt");
			}
		}
		else if (_jobtype == Jobtype.MGA){ // MGA
			if (_use_clustering && !_aborted){
				runUclust();
				Util.writeToFile(_log, _jobpath+File.separator+"log.txt");
			}
			multiGeneAlignment();
			if(!_aborted && !_tree_builder){
				buildRightTree();
				if(!_aborted){
					convertTreefileToPhyloXML();
				}
			}
			Util.writeToFile(_log, _jobpath+File.separator+"log.txt");
		}
		else {
			Util.printSomething("Unknown Jobtype!");
		}
		
		if (!_testing){
			try {
				if(_aborted){
					_job.switchToPreviousSubmissionPanel();
				}
				else if (_jobtype == Jobtype.SGA || _jobtype == Jobtype.MGA){
					_job.getMainFrame().setEnabled(true);
					JTabbedPane pane = _job.getMainFrame().getWorkflowPanel().getTabs();
					int index = pane.indexOfComponent(_job);
					pane.setTabComponentAt(index,WorkflowPanel.buildTab(pane,_job.getMainFrame().getConfiguration().getJobName(), _job));
					_job.switchToResultsPanel();
					_job.revalidate();
				}
				else if (_jobtype == Jobtype.PHY_CONV){
					_job.getMainFrame().setEnabled(true);
					_job.askForTreeViewerPermission();
					_job.switchToSubmissionPanel();
				}
				else{
					throw new IllegalArgumentException("Jobtype "+_jobtype+" does not exist!");
				}
			}
			catch(IllegalArgumentException e){
				if (_testing){
					e.printStackTrace();
				}
				else{
					Util.printErrors(e);
				}
			}
		}
		_job.setSubmissionThread(null);
	}
	
	private void parseOptions(String[] opts){
		try{
			for (int i = 0; i < opts.length; i++){
				if (opts[i].equals("-s")){
					_raxml_options.put("-s", opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-n")){
					_jobname = opts[i+1];
					_raxml_options.put("-n",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-H")){
					_raxml_options.put("-H",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-G")){
					_raxml_options.put("-G",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-t")){
					_raxml_options.put("-t",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-f")){
					_raxml_options.put("-f",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-m")){
					String[] a = opts[i+1].split("_");
					_raxml_options.put("-m", Util.join(a,(" ")));
					i++;
				}
				else if (opts[i].equals("-x")){
					_raxml_options.put("-x",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-N")){
					_raxml_options.put("-N",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-q")){
					_raxml_options.put("-q",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-jobpath")){
					_jobpath = opts[i+1];
					i++;
				}
				else if (opts[i].equals("-useR")){
					_use_readsfile = true;
					_readsfile = opts[i+1];
					i++;
				}
				else if (opts[i].equals("-useCl")){
					_use_clustering = true;
				}
				else if (opts[i].equals("-uId")){
					_uclust_identity = opts[i+1];
					i++;
				}
				else if (opts[i].equals("-mga")){
					_multi_gene_alignment = true;
				}
				else if (opts[i].equals("-test_mapping")){
					_test_mapping = true;	
				}
				else if (opts[i].equals("-T")){
					_raxml_options.put("-T",opts[i+1]);
					i++;
				}
				else if (opts[i].equals("-tb")){
					_tree_builder = true;
				}
				else if (opts[i].equals("-p")){
					_raxml_options.put("-p",opts[i+1]);
					i++;
				}
				else {
					throw new IllegalArgumentException("ERROR in options_parser!, unknown option "+opts[i]+" !");
				}
			}
		}
		catch (IllegalArgumentException e){
			if (_testing){
				e.printStackTrace();
			}
			else{
				Util.printErrors(e);
			}
		}
	}
	
	private void runHmmer(){
		Reformat ref = new Reformat(_raxml_options.get("-s"));
	    ref.reformatToStockholm();
	    Util.writeToFile(ref.getData(), _jobpath+File.separator+"alignmentfile.sto");
	    _log.add("Wrote "+_jobpath+File.separator+"alignmentfile.sto");
	    String command = Constants.HMMBUILD.getAbsolutePath()+" --dna "+_jobpath+File.separator+"alignmentfile.hmm "+_jobpath+File.separator+"alignmentfile.sto ";
	    _log.add("Executing hmmbuild: "+command);
	    run(command,null);
	    command = Constants.HMMALIGN.getAbsolutePath()+" -o "+_jobpath+File.separator+"alignmentfile2.sto --mapali "+_jobpath+File.separator+"alignmentfile.sto  "+_jobpath+File.separator+"alignmentfile.hmm "+_readsfile;
	    _log.add("Executing hmmalign: "+command);
	    run(command,null);
	    ref = new  Reformat(_jobpath+File.separator+"alignmentfile2.sto");
	    ref.reformatToPhylip();
	    Util.writeToFile(ref.getData(),_jobpath+File.separator+"new_alignmentfile.phy");
	    _log.add("Wrote "+_jobpath+File.separator+"new_alignmentfile.phy");
	    _raxml_options.put("-s", _jobpath+File.separator+"new_alignmentfile.phy");
	}
	
	private void runUclust(){
		String outfile = _jobpath+File.separator+"cluster" ;
		String command = "";
		
		if (_job.getMainFrame().getConfiguration().getUclustPath() != null){
			command = _job.getMainFrame().getConfiguration().getUclustPath()+ " --cluster "+_readsfile+" --uc "+outfile+".uc --id "+_uclust_identity+" --usersort --seedsout "+outfile+".fas ";
		}
		else{ //Testing
			command = Constants.UCLUST_TEST.getAbsolutePath()+ " --cluster "+_readsfile+" --uc "+outfile+".uc --id "+_uclust_identity+" --usersort --seedsout "+outfile+".fas ";
		}
//		JOptionPane.showMessageDialog(Configuration.getMainFrame(), command);
		_log.add("Executing uclust: "+command);
		run(command,null);
//		command = Constants.UCLUST+" --uc2fasta "+outfile+".uc --input "+_readsfile+" --output "+outfile+".fas ";
//		JOptionPane.showMessageDialog(Configuration.getMainFrame(), command);
//		run(command,null);
		_readsfile = outfile+".fas";
	}
	
	private void runRAxML(){
	    if (_raxml_options.get("-T") == null){
	    	String command =Constants.RAXML.getAbsolutePath()+" ";
	    	Set<String> keys = _raxml_options.keySet();
	    	for (String key : keys){
	    		command = command + key + " " + _raxml_options.get(key) + " ";
	    	}
	    	run(command,null);
	    }
	    else{
	    	String command =Constants.RAXML_PARALLEL.getAbsolutePath()+" ";
	    	Set<String> keys = _raxml_options.keySet();
	    	for (String key : keys){
	    		command = command + key + " " + _raxml_options.get(key) + " ";
	    	}
	    	run(command,null);
	    }
	}	
	
	private void run(String commands, String direct_out_to){
		ArrayList<String> data = new ArrayList<String>();
		String[] command = commands.split("\\s+");
		
		try{	
			ProcessBuilder builder2 = new ProcessBuilder(command);
			builder2.directory(new File(_jobpath));
			builder2.redirectErrorStream(true);
			_current_process = builder2.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(_current_process.getInputStream()));
			String line;
			
			while ((line = in.readLine()) != null) {
				if (direct_out_to == null){
					_log.add(line);
					_job.setJobLog(_log);
				}
				else{
					data.add(line);
				}
			}
			
			if (direct_out_to != null){
				Util.writeToFile(data, direct_out_to);
			}
			_current_process.waitFor();
		}
		catch (Exception e){
			if (_testing){
				e.printStackTrace();
			}
			else{
				Util.printErrors(e);
			}
		}
	}
	
	private void convertTreefileToPhyloXML(){
		String treefile =  _raxml_options.get("-t");
		String command = "java -jar "+Constants.CONVERT_TO_PHYLOXML.getAbsolutePath()+" "+treefile;
		if (_raxml_options.get("-x") == null){// bootstrapping activated?
			String file = _jobpath+File.separator+"RAxML_classificationLikelihoodWeights."+_jobname;
		    command = command+" "+file;
		    run(command, _jobpath+File.separator+"treefile.phyloxml");
		}
		else{
			String file  = _jobpath+File.separator+"RAxML_classification."+_jobname;
			command = command+" "+file+" "+_raxml_options.get("-N");
			run(command,_jobpath+File.separator+"treefile.phyloxml" );
		}
		// make empty placement file for the phyloxml converter
		File empty_file = new File(_jobpath+File.separator+"no_placements");
		try {
			boolean sucess = empty_file.createNewFile();
			if (!sucess){
				empty_file.delete();
				empty_file.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (_testing){
				e.printStackTrace();
			}
			else{
				Util.printErrors(e);
			}
		}
		command = "java -jar "+Constants.CONVERT_TO_PHYLOXML.getAbsolutePath()+" "+treefile+" "+_jobpath+File.separator+empty_file; 
		run(command,_jobpath+File.separator+"treefile_no_placements.phyloxml");
		empty_file.delete();
	}
	
	private void buildRightTree(){
		File job_folder = new File (_jobpath);
		File[] treefiles = job_folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return Util.matches(name,"RAxML_originalLabelledTree."+_jobname+".*$"  );
			}
		});
		String labeltree = treefiles[0].getAbsolutePath(); 
		String reftree = _raxml_options.get("-t");
		String command = "java -jar "+Constants.TREE_MERGE_LABELS.getAbsolutePath()+" "+reftree+" "+labeltree;
		run(command,_jobpath+File.separator+"final_tree.tree" );
		_raxml_options.put("-t",_jobpath+File.separator+"final_tree.tree");	

	}
	  
	public void abortJob(){
		_current_process.destroy();
		_aborted = true;
		_job.setSubmissionThread(null);
	}


	private  void multiGeneAlignment(){
		ArrayList<String> log = new ArrayList<String>();
		String alifile = _raxml_options.get("-s");                                                                                                                           
		String treefile = _raxml_options.get("-t");                                                                                                                          
		String partitionfile = _raxml_options.get("-q");                                                                                                                     
		String outname = _raxml_options.get("-n");                                                                                                                           
		_raxml_options.remove("-q");                                                                                                                              
		String id = _raxml_options.get("-n");                                                                                                                                
		HashMap<String,Integer> read_gene_mapping = new HashMap<String, Integer>();                                                                                                                             
		HashMap<String,Integer> read_gene_score = new HashMap<String, Integer>();                                                                                                                               
		HashMap<String,String> query_sequences = new ReadsfileParser(_readsfile).getSeqs();                                                                                                   
		// copy alignment file into job folder
		File mga_alignment = new File(_jobpath,"alignmentfile");
		Util.writeToFile(Util.readFile(alifile), mga_alignment.getAbsolutePath());
		alifile = mga_alignment.getAbsolutePath();
		// split the multi gene file based on the partition file  
		String command =Constants.RAXML+" -s "+alifile+" -m GTRGAMMA -p 12345  -n "+id+"_mga -fs -t "+treefile+" -q "+partitionfile;
		run(command,null);                                                                                                                                           

		// build fasta db file for each Gene and perform swps3        
		int gene_no = 0;
		// get genes sequence types from partitionfile                                                                                                            
		ArrayList<String> genes_types = new ArrayList<String>();                                                                                                                                         
		String[] par_data = Util.readFile(partitionfile);                                                                                                        
		for(int i=0; i < par_data.length; i++){
			if (Util.matches(par_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\s*,)*(\\s+\\d+\\s*-\\s*\\d+\\s*)$" )){
				genes_types.add(Util.matchesWithGroups(par_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\s*,)*(\\s+\\d+\\s*-\\s*\\d+\\s*)$" )[1]);
			}
			else if(Util.matches(par_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+,)*(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+)$")){
				genes_types.add(Util.matchesWithGroups(par_data[i],"^([A-Z]+),\\s+\\S+\\s+=(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+,)*(\\s+\\d+\\s*-\\s*\\d+\\\\\\d+)$")[1]);
			}
		}
		// Dir glob provides the Gene files in the right order         
		File job_folder = new File (_job.getMainFrame().getConfiguration().getWorkspace());
		File[] gene_files = job_folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				// TODO Auto-generated method stub
				return Util.matches(name, "\\.GENE\\.");
			}
		});
		Arrays.sort(gene_files);
		
		for(int i = 0; i < gene_files.length; i++){
			File fasta_db_file = new File(job_folder,"GENE"+gene_no+"_db.fas") ;    
			ArrayList<String> fasta_db_file_content = new ArrayList<String>();
			AlignmentfileParser gene = new AlignmentfileParser(gene_files[i].getAbsolutePath());     
			gene.parseSeqs();
			gene.disalign();   
			HashMap<String,String> seqs = gene.getSeqs();
			Set<String> taxa =seqs.keySet();
			ArrayList<String> taxa_to_delete = new ArrayList<String>();
			for(String taxon : taxa){
				if (seqs.get(taxon).length() < 1){ // sequences with no letter should be ignored          
					taxa_to_delete.add(taxon);  // Java seems not to be able to delete elements from a hash when iterating
					//					  seqs.remove(taxon);  
				}
				else{ 
					fasta_db_file_content.add(">"+taxon+"\n"+seqs.get(taxon));
				}
			}
			for (int k = 0; k < taxa_to_delete.size(); k++){
				seqs.remove(taxa_to_delete.get(k));
			}
			Util.writeToFile(fasta_db_file_content, fasta_db_file.getAbsolutePath());
			gene.setSeqs(seqs);

			// execute swps3 with every query sequence against the geneX sequences     
			Set <String> names = query_sequences.keySet();
			for(String name : names){
				File query_seq = new File(job_folder,"single_query.fas"); 
				String[] one_liner = new String[1];
				one_liner[0] =  ">"+name+"\n"+query_sequences.get(name);
				log.add(one_liner[0]);
				Util.writeToFile(one_liner, query_seq.getAbsolutePath());
				String swps3_out = new File(job_folder,"swps3.out").getAbsolutePath(); 
				command = "";
				if (Util.matches(genes_types.get(gene_no),"[Dd][Nn][Aa]")){                                                                                                             
					command = Constants.SWPS3+" -i -5 -e -2 "+Constants.MATRIX_DNA+" "+query_seq.getAbsolutePath()+" "+fasta_db_file.getAbsolutePath();
				}                                                                                                             
				else{                                                                                                                                                
					command = Constants.SWPS3+" "+Constants.MATRIX_AA+" "+query_seq.getAbsolutePath()+" "+fasta_db_file.getAbsolutePath();
				}
				log.add(command);
				log.add(name);
				log.add("GENE: "+gene_no);
				run(command,swps3_out);     

				// collect query readX x GeneY maximum score                                                                                                          
				String[] scorefile = Util.readFile(swps3_out);                                                                                                                 
				int score = 0;
				for(int j = 0; j< scorefile.length; j++){
					String[] g = Util.matchesWithGroups(scorefile[j],"^(\\d+)\\s+" );
					if ( g != null && g.length > 1){ 
						score = Integer.parseInt(g[1]);
						log.add("Score: "+score);
					}
					if(read_gene_mapping.get(name)== null){                                                                                                                     
						read_gene_score.put(name,score);                                                                                                                     
						read_gene_mapping.put(name,gene_no);
					}
					else if(read_gene_score.get(name) < score){                                                                                                                 
						read_gene_score.put(name,score);                                                                                                                     
						read_gene_mapping.put(name,gene_no);                                                                                                                 
					}                                                                                                                                              
				}                                                                                                                                                
			}                                                                                                                                                  
			gene_no++;                                                                                                                                    
		}    
		
		Set<String> blas = read_gene_mapping.keySet();
		for(String bla : blas){
			log.add(bla+" => "+read_gene_mapping.get(bla));
		}
		
		// build Gene specific query files 
		ArrayList<ArrayList<String>> genes_reads = new ArrayList<ArrayList<String>>();
		//preinitialize
		for(int a = 0; a < gene_no; a++){
			ArrayList<String> list = new ArrayList<String>();
			genes_reads.add(list);
		}
		Set<String> keys = read_gene_mapping.keySet();
		for(String key : keys){
			int gene = read_gene_mapping.get(key);                                                                                                                             
			genes_reads.get(gene).add(key);                                                                                                                                                                                                                                                      
		}		
		
		
		// get models and matrices from the partition file                                                                                                        
		ArrayList<String> matrices = new PartitionfileParser(partitionfile).getMatrices();                                                                                          

		String model = _raxml_options.get("-m");  
		for(int i = 0; i < gene_no; i++){
			String gene_queryfile = new File(_jobpath,"queryfile_GENE"+i+".fas").getAbsolutePath();                                                                                                  
			ArrayList<String> gene_queryfile_content = new ArrayList<String>();                                                                                                                   
			if (genes_reads.get(i) != null){  
				for(int j = 0; j < genes_reads.get(i).size(); j++){
					String name = genes_reads.get(i).get(j);
					gene_queryfile_content.add(">"+name);
					gene_queryfile_content.add(query_sequences.get(name));
				}
				// build Alignments with Hmmer and run RAxML    
				if (gene_queryfile_content.size()> 0){
					_raxml_options.put("-s", alifile+".GENE."+i);                                                                                                          
					_readsfile = gene_queryfile;                                                                                                                         
					_raxml_options.put("-n",outname+".GENE"+i);                                                                                                           
					Util.writeToFile(gene_queryfile_content, gene_queryfile);                                                                                                                                            
					runHmmer();                                                                                                                              
					String substmodel = "" ;                                                                                                                                    
					if (Util.matches(matrices.get(i),"^[Dd][Nn][Aa]$")){                                                                                                                   
						_raxml_options.put("-m","GTR"+model);
					}
					else{                                                                                                                                                 
						_raxml_options.put("-m","PROT"+model+matrices.get(i));  // PROTGAMMAWAGF                                                                               
					}                                                                                                                                                  
					runRAxML();      
				}
			}                                                                                                                                                   
		}                                                                                                                                                     

		// Concat result files                                                                                                                                    
		if (_raxml_options.get("-N") == null){ // no bootstrap samples, concat RAxML Classification Likelihood weights
			Util.concatenateFiles(_jobpath, "RAxML_classificationLikelihoodWeights\\."+outname+"\\.GENE.+$", new File(_jobpath,"RAxML_classificationLikelihoodWeights."+outname).getAbsolutePath());
		}
		else{    
			Util.concatenateFiles(_jobpath, "RAxML_classification\\."+outname+"\\.GENE.+$", new File(_jobpath,"RAxML_classification."+outname).getAbsolutePath());
		}
		Util.writeToFile(log, "/home/denis/Desktop/log.txt");
	}
	enum Jobtype{
		SGA,
		MGA,
		PHY_CONV
	}

}
