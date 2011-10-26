package raxml_gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyRunnable implements Runnable{
	
	private Process _p;
	private ProcessBuilder _builder;
	private Job _job;
	private Jobtype _jobtype;
	private boolean _aborted;
	
	public MyRunnable(ProcessBuilder builder, Job job, final Jobtype jobtype){
		_builder = builder;
		_job = job;
		_jobtype = jobtype;
		try{
			_p = _builder.start();
		}
		catch( IOException x ) {
			x.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if (_aborted){
			
			}
			else if (_job.getPhyloxmlConverterFormPanel().getParameters().get("save_as") != null){
				StreamConsumerThreadWithOutprint outconsumer = new StreamConsumerThreadWithOutprint(_p.getInputStream(),new File(_job.getPhyloxmlConverterFormPanel().getParameters().get("save_as")) );
				StreamConsumerThreadWithOutprint errorconsumer = new StreamConsumerThreadWithOutprint(_p.getErrorStream(),null);

				outconsumer.start();
				errorconsumer.start();
				_p.waitFor();	
				outconsumer.join();
				errorconsumer.join();
			}
			else{
				StreamConsumerThread outconsumer = new StreamConsumerThread(_p.getInputStream(), null);
				StreamConsumerThread errorconsumer = new StreamConsumerThread(_p.getErrorStream(),null);
				outconsumer.start();
				errorconsumer.start();
				_p.waitFor();	
				outconsumer.join();
				errorconsumer.join();
			}
		}  catch( InterruptedException x ) {
			Util.printErrors(x);
		}
		try {
			if (_aborted){
				_job.switchToPreviousSubmissionPanel();
			}
			else if (_jobtype == Jobtype.SGA || _jobtype == Jobtype.MGA){
				_job.getMainFrame().setEnabled(true);
				_job.switchToResultsPanel();
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
			Util.printErrors(e);
		}
	}
	public Process getProcess(){
		return _p;
	}
	
	public void abortJob(){
		_p.destroy();
		_aborted = true;
	}
	
	enum Jobtype{
		SGA,
		MGA,
		PHY_CONV
	}
}
