package org.forester.phylogeny.data;

import java.io.IOException;
import java.io.Writer;

import org.forester.io.parsers.nhx.NHXtags;
import org.forester.io.parsers.phyloxml.PhyloXmlMapping;
import org.forester.io.parsers.phyloxml.PhyloXmlUtil;
import org.forester.util.ForesterUtil;

public class Inserts implements PhylogenyData {

    
    private double             _value;
    private String             _type;

    public Inserts() {
        init();
    }

    public Inserts( final double value, final String type ) {
        setValue( value );
        setType( type );
    }

    public StringBuffer asSimpleText() {
        return new StringBuffer().append( ForesterUtil.FORMATTER_6.format( getValue() ) );
    }

    public StringBuffer asText() {
        final StringBuffer sb = new StringBuffer();
        if ( !ForesterUtil.isEmpty( getType() ) ) {
            sb.append( "[" );
            sb.append( getType() );
            sb.append( "] " );
        }
        sb.append( ForesterUtil.FORMATTER_6.format( getValue() ) );
        return sb;
    }

    public boolean isEqual( final PhylogenyData inserts ) {
        if ( inserts == null ) {
            return false;
        }
        if ( !( inserts instanceof Inserts ) ) {
            return false;
        }
        final Inserts s = ( Inserts ) inserts;
        if ( s.getValue() != getValue() ) {
            return false;
        }
        if ( !s.getType().equals( getType() ) ) {
            return false;
        }
        return true;
    }


    public PhylogenyData copy() {
        return new Inserts( getValue(), new String( getType() ) );
    }

    public String getType() {
        return _type;
    }

    public double getValue() {
        return _value;
    }

    public void init() {
        setType( "" );
    }

    

    public void setType( final String type ) {
        _type = type;
    }

    public void setValue( final double value ) {
        _value = value;
    }

    public StringBuffer toNHX() {
        final StringBuffer sb = new StringBuffer();
        sb.append( NHXtags.SUPPORT );
        sb.append( getValue() );
        return sb;
    }

    public void toPhyloXML( final Writer writer, final int level, final String indentation ) throws IOException {
        
        writer.write( ForesterUtil.LINE_SEPARATOR );
        writer.write( indentation );
        PhylogenyDataUtil.appendElement( writer,
                                         PhyloXmlMapping.CONFIDENCE,
                                         String.valueOf( ForesterUtil
                                                 .round( getValue(),
                                                         PhyloXmlUtil.ROUNDING_DIGITS_FOR_PHYLOXML_DOUBLE_OUTPUT ) ),
                                         PhyloXmlMapping.CONFIDENCE_TYPE_ATTR,
                                         ForesterUtil.isEmpty( getType() ) ? "unknown" : getType() );
    }

    @Override
    public String toString() {
        return asText().toString();
    }

}
