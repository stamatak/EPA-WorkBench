package org.forester.archaeopteryx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.phyloxml.PhyloXmlParser;
import org.forester.phylogeny.Phylogeny;
import org.forester.util.ForesterUtil;

public class ExternalTreeviewerStarter {

	MainFrame _ext_app;
	
	public ExternalTreeviewerStarter(String configfile, String phyloxmlfile){
		Configuration conf = new Configuration(configfile, false, false);
		Phylogeny[] phylogenies = null;
		File f = new File( phyloxmlfile );
        final String err = ForesterUtil.isReadableFile( f );
        if ( !ForesterUtil.isEmpty( err ) ) {
            ForesterUtil.fatalError( Constants.PRG_NAME, err );
        }
        PhylogenyParser p;
		try {
			p = ForesterUtil.createParserDependingOnFileType( f, conf
			        .isValidatePhyloXmlAgainstSchema() );
			if ( p instanceof PhyloXmlParser ) {
	            MainFrameApplication.warnIfNotPhyloXmlValidation( conf );
	        }
			phylogenies = Util.readPhylogenies( p, f );
			
			_ext_app = ExternalMainFrameApplication.createInstance( phylogenies, conf, phyloxmlfile );
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void addPhyloXMLFile(String phyloxmlfile){
		
	}
	public MainFrame getExternalMainFrameApplication(){
		return _ext_app;
	}
	
}
