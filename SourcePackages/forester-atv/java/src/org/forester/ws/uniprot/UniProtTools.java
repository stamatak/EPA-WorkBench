// $Id: UniProtTools.java,v 1.7 2010/04/09 02:04:17 cmzmasek Exp $
// forester -- software libraries and applications
// for genomics and evolutionary biology research.
//
// Copyright (C) 2010 Christian M Zmasek
// Copyright (C) 2010 Sanford-Burnham Medical Research Institute
// All rights reserved
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
//
// Contact: cmzmasek@yahoo.com
// WWW: www.phylosoft.org/forester

package org.forester.ws.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.forester.util.ForesterUtil;

public final class UniProtTools {

    final static String         LINE_SEPARATOR = "\n";
    private final static String BASE_URL       = "http://www.uniprot.org/";
    private final static String URL_ENC        = "UTF-8";

    public static String getResult( final String query ) throws IOException {
        System.out.println( "query: " + query );
        final URL url = new URL( BASE_URL + query );
        final URLConnection urlc = url.openConnection();
        final BufferedReader in = new BufferedReader( new InputStreamReader( urlc.getInputStream() ) );
        String line;
        final StringBuffer result = new StringBuffer();
        while ( ( line = in.readLine() ) != null ) {
            result.append( line );
            result.append( LINE_SEPARATOR );
        }
        in.close();
        return result.toString().trim();
    }

    public static List<UniProtTaxonomy> getTaxonomiesFromTaxonomyCode( final String code ) throws IOException {
        final String result = getTaxonomyStringFromTaxonomyCode( code );
        if ( !ForesterUtil.isEmpty( result ) ) {
            return parseUniProtTaxonomy( result );
        }
        return null;
    }

    public static List<UniProtTaxonomy> getTaxonomiesFromScientificName( final String sn ) throws IOException {
        final String result = getTaxonomyStringFromScientificName( sn );
        if ( !ForesterUtil.isEmpty( result ) ) {
            return parseUniProtTaxonomy( result );
        }
        return null;
    }

    /**
     * Does not return "sub-types".
     * For example, for "Mus musculus" only returns "Mus musculus"
     * and not "Mus musculus", "Mus musculus bactrianus", ...
     * 
     */
    public static List<UniProtTaxonomy> getTaxonomiesFromScientificNameStrict( final String sn ) throws IOException {
        final List<UniProtTaxonomy> taxonomies = getTaxonomiesFromScientificName( sn );
        if ( ( taxonomies != null ) && ( taxonomies.size() > 0 ) ) {
            final List<UniProtTaxonomy> filtered_taxonomies = new ArrayList<UniProtTaxonomy>();
            for( final UniProtTaxonomy taxonomy : taxonomies ) {
                if ( taxonomy.getScientificName().equals( sn ) ) {
                    filtered_taxonomies.add( taxonomy );
                }
            }
            return filtered_taxonomies;
        }
        return null;
    }

    public static List<UniProtTaxonomy> getTaxonomiesFromCommonName( final String cn ) throws IOException {
        final String result = getTaxonomyStringFromCommonName( cn );
        if ( !ForesterUtil.isEmpty( result ) ) {
            return parseUniProtTaxonomy( result );
        }
        return null;
    }

    public static List<UniProtTaxonomy> getTaxonomiesFromCommonNameStrict( final String cn ) throws IOException {
        final List<UniProtTaxonomy> taxonomies = getTaxonomiesFromCommonName( cn );
        if ( ( taxonomies != null ) && ( taxonomies.size() > 0 ) ) {
            final List<UniProtTaxonomy> filtered_taxonomies = new ArrayList<UniProtTaxonomy>();
            for( final UniProtTaxonomy taxonomy : taxonomies ) {
                if ( taxonomy.getCommonName().equals( cn ) ) {
                    filtered_taxonomies.add( taxonomy );
                }
            }
            return filtered_taxonomies;
        }
        return null;
    }

    public static List<UniProtTaxonomy> getTaxonomiesFromId( final String id ) throws IOException {
        final String result = getTaxonomyStringFromId( id );
        if ( !ForesterUtil.isEmpty( result ) ) {
            return parseUniProtTaxonomy( result );
        }
        return null;
    }

    private static String getTaxonomyStringFromTaxonomyCode( final String code ) throws IOException {
        return getResult( "taxonomy/?query=mnemonic%3a%22" + encode( code ) + "%22&format=tab" );
    }

    private static String getTaxonomyStringFromScientificName( final String sn ) throws IOException {
        return getResult( "taxonomy/?query=scientific%3a%22" + encode( sn ) + "%22&format=tab" );
    }

    private static String getTaxonomyStringFromCommonName( final String cn ) throws IOException {
        return getResult( "taxonomy/?query=common%3a%22" + encode( cn ) + "%22&format=tab" );
    }

    private static String getTaxonomyStringFromId( final String id ) throws IOException {
        return getResult( "taxonomy/?query=id%3a%22" + encode( id ) + "%22&format=tab" );
    }

    private static String encode( final String str ) throws UnsupportedEncodingException {
        return URLEncoder.encode( str.trim(), URL_ENC );
    }

    public static void main( final String[] args ) throws Exception {
        System.out.println( getTaxonomyStringFromTaxonomyCode( "NEMVE" ) );
        System.out.println();
        System.out.println( getTaxonomyStringFromScientificName( "nematostella" ) );
        System.out.println();
        System.out.println( getTaxonomyStringFromCommonName( "sea anemone" ) );
    }

    private static List<UniProtTaxonomy> parseUniProtTaxonomy( final String str ) throws IOException {
        final List<UniProtTaxonomy> taxonomies = new ArrayList<UniProtTaxonomy>();
        final String[] items = str.split( LINE_SEPARATOR );
        for( final String line : items ) {
            if ( ForesterUtil.isEmpty( line ) ) {
                // Ignore empty lines.
            }
            else if ( line.startsWith( "Taxon" ) ) {
                //TODO next the check format FIXME
            }
            else {
                taxonomies.add( new UniProtTaxonomy( line ) );
            }
        }
        return taxonomies;
    }
}
