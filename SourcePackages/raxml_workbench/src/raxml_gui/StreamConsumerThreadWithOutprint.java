package raxml_gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class StreamConsumerThreadWithOutprint extends Thread {
	InputStream is;
	PrintStream outStream;
	boolean doClose;
	String name;
	public StreamConsumerThreadWithOutprint( InputStream is, File outfile ) {
		this.is = is;
		//System.out.printf( "is selectable: %s\n", is.
				
		if( outfile != null ) {
			try {
				outStream = new PrintStream(new FileOutputStream(outfile));
				doClose = true;
				name = outfile.getName();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Util.printErrors(e);
				// fall back, kind of ...
				outStream = System.out;
				doClose = false;
			}
		} else {
			outStream = System.out;
			doClose = false;
		}
	}
	
	@Override
	public void run() {
		int c = -666;
		
		try {
			
			while( !isInterrupted() &&  (c = is.read()) != -1 ) {
				outStream.write((char)c);
			}
			if( doClose ) {
				outStream.close();
				
			}
			
			outStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
//			e.printStackTrace();
//			
//			System.out.printf( "last read: %d\n", c );
			System.out.printf( "stream reader. exit due to io exception: %s\n", e.getMessage() );
			if( doClose && outStream != null ) {
				outStream.close();
			}
		} finally {
			if( doClose && outStream != null ) {
				outStream.close();
			}
		}
//		System.out.printf( "StreamConsumerThread: bye... %d %s %s\n", c, isInterrupted(), name );
	}
	
	public void stopPlease() {
		interrupt();
	}
	
	public void addComment( String comment ) {
		if( outStream != null ) {
			outStream.print( comment );
		}
	}
}
