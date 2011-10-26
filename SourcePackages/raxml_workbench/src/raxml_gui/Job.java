package raxml_gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.forester.archaeopteryx.ExternalTreeviewerStarter;

public class Job extends JPanel  {

	private MainFrame _main_frame;
	
	private SgaFormPanel _sga_form_panel ;
	private MgaFormPanel _mga_form_panel ;
	private PhyloXMLConverterFormPanel _phyloxml_converter_form_panel ;
	private TreeBuilderFormPanel _tree_builder_form_panel;
	
	private JTabbedPane _submission_panel;
	private WaitingPanel _waiting_panel;
	private ResultsPanel _results_panel;
	
	private GridBagConstraints _c;

	private Thread _animation_thread;
	private ArrayList<String> _job_log;
	private Runnable _submission_thread;

	public Job(MainFrame parent)  {
		_main_frame = parent;
		this.setBounds(200, 200, 200, 200);
		this.setBackground(Constants.BACKGROUND_COLOR);
		this.setLayout(new GridBagLayout());
		_c  = new GridBagConstraints();
		_c.anchor = GridBagConstraints.NORTH;
		_c.fill = GridBagConstraints.BOTH;
		_c.weighty = 1.0;
		_c.weightx = 1.0;
		_c.gridx = 0;
		_c.gridy = 0;
		
		buildSubmissionPanel();
		
		this.add(_submission_panel,_c,0);
		enableUclust(_main_frame.getConfiguration().getUclustPath() != null);
	}

	public void buildSubmissionPanel(){

		/*SGA Submission Input
		 *****************************************************/
		JPanel sga_submission = new JPanel(new GridBagLayout());
		sga_submission.setBackground(Constants.BACKGROUND_COLOR);
		GridBagConstraints c  = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		_sga_form_panel = new SgaFormPanel(new GridBagLayout(), this);	
		sga_submission.add(_sga_form_panel, c);


		/*MGA Submission Input
		 *****************************************************/
		JPanel mga_submission = new JPanel(new GridBagLayout());
		mga_submission.setBackground(Constants.BACKGROUND_COLOR);
		c  = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		_mga_form_panel = new MgaFormPanel(new GridBagLayout(), this);	
		mga_submission.add(_mga_form_panel, c);

		/*PhyloXML Converter Submission Input
		 *****************************************************/
		JPanel phyloxml_converter = new JPanel(new GridBagLayout());
		phyloxml_converter.setBackground(Constants.BACKGROUND_COLOR);
		c  = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		_phyloxml_converter_form_panel = new PhyloXMLConverterFormPanel(new GridBagLayout(), this);	
		phyloxml_converter.add(_phyloxml_converter_form_panel, c);
		
		/*RAxML Tree builder Input
		******************************************************/
		JPanel tree_builder_submission = new JPanel(new GridBagLayout());
		tree_builder_submission.setBackground(Constants.BACKGROUND_COLOR);
		c  = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		_tree_builder_form_panel = new TreeBuilderFormPanel(new GridBagLayout(), this);	
		tree_builder_submission.add(_tree_builder_form_panel, c);
		
		_submission_panel = new JTabbedPane(JTabbedPane.TOP);
		_submission_panel.add("EPA Single Gene Alignment", sga_submission);
		_submission_panel.add("EPA Multi Gene Alignment", mga_submission);
		_submission_panel.add("PhyloXML Converter", phyloxml_converter);
		_submission_panel.add("RAxML Tree Builder", tree_builder_submission);
	}

	public void switchToSubmissionPanel(){
		this.removeAll();
		buildSubmissionPanel();
		enableUclust(_main_frame.getConfiguration().getUclustPath() != null);
		this.add(_submission_panel,_c,0);
		this.revalidate();
		this.repaint();
		
	}
	
	public void switchToPreviousSubmissionPanel(){
		enableUclust(_main_frame.getConfiguration().getUclustPath() != null);
		this.removeAll();
		this.add(_submission_panel,_c,0);
		this.revalidate();
		this.repaint();
	}
	
	public void switchToWaitingPanel(){
		this.removeAll();
		_waiting_panel = new WaitingPanel(new GridBagLayout(), this);
		this.add(_waiting_panel,_c,0);
		_waiting_panel.getLoadingAnimation().setVisible(true);
		_animation_thread = new Thread(_waiting_panel.getLoadingAnimation());
		_animation_thread.start();
		this.revalidate();
		this.repaint();
	}
	
	public void switchToResultsPanel(){
		this.removeAll();
		if (_animation_thread != null){
			_animation_thread.interrupt();
		}
		_results_panel = new ResultsPanel(new GridBagLayout(), this,_main_frame.getConfiguration().getWorkspace());
		_results_panel.setVisible(true);
		this.add(_results_panel,_c,0);
		this.revalidate();
		this.repaint();
	}
	
	void askForTreeViewerPermission(){
		int selected = JOptionPane.showConfirmDialog(this, "Display phyloxml file in Treeviewer?" ,"Question",JOptionPane.YES_NO_OPTION);
		if (selected==0){
			_main_frame.getWorkflowPanel().runTreeviewer(this.getPhyloxmlConverterFormPanel().getParameters().get("save_as"));
		}
	}
	

	public SgaFormPanel getSGAFormPanel(){
		return _sga_form_panel;
	}

	public PhyloXMLConverterFormPanel getPhyloxmlConverterFormPanel(){
		return _phyloxml_converter_form_panel;
	}

	public MgaFormPanel getMGAFormPanel(){
		return _mga_form_panel;
	}
	
	public TreeBuilderFormPanel getTreeBuilderFormPanel(){
		return _tree_builder_form_panel;
	}

	public JPanel getResultsWorkflowPanel(){
		return _results_panel;
	}

	public JTabbedPane getSubmissionPanel(){
		return _submission_panel;
	}

	public MainFrame getMainFrame(){
		return _main_frame;
	}

	public WaitingPanel getWaitingPanel(){
		return _waiting_panel;
	}
	
	
	public void setJobLog(ArrayList<String> log){
		_job_log = log;
	}
	
	public ArrayList<String> getJobLog(){
		return _job_log;
	}
	
	public void setSubmissionThread(Submission t){
		_submission_thread = t;
	}
	
	public boolean abortSubmissionThread(){
		boolean b = true;
		if (_submission_thread != null){
			int selected = JOptionPane.showOptionDialog(
					this, 
					"Abort Job?", 
					"Warning!", 
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, 
					null, null,null);
			if (selected == JOptionPane.YES_OPTION){
				if (_submission_thread.getClass().getSimpleName().equals("Submission")){
					((Submission)_submission_thread).abortJob();
				}
				else if (_submission_thread.getClass().getSimpleName().equals("MyRunnable")){
					((MyRunnable)_submission_thread).abortJob();
				}
			}
			else{
				b= false;
			}
		}
		return b;
	}
	
	public void enableUclust(boolean b){
		_sga_form_panel.enableUclust(b);
		_mga_form_panel.enableUclust(b);
	}
	
}
