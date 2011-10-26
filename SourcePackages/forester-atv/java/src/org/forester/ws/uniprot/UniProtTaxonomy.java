// $Id: UniProtTaxonomy.java,v 1.3 2010/04/11 00:02:23 cmzmasek Exp $
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

import java.util.ArrayList;
import java.util.List;

import org.forester.util.ForesterUtil;

public final class UniProtTaxonomy {

    private final String[] _lineage;
    private final String   _code;
    private final String   _scientific_name;
    private final String   _common_name;
    private final String   _synonym;
    private final String   _rank;
    private final String   _id;

    public UniProtTaxonomy( final String line ) {
        final String[] items = line.split( "\t" );
        _id = items[ 0 ].trim();
        _code = items[ 1 ].trim();
        _scientific_name = items[ 2 ].trim();
        _common_name = items[ 3 ].trim();
        _synonym = items[ 4 ].trim();
        _rank = items[ 7 ].trim();
        final String[] lin = items[ 8 ].split( "; " );
        if ( lin != null && lin.length > 0 ) {
            final List<String> temp = new ArrayList<String>();
            for( final String t : lin ) {
                if ( !ForesterUtil.isEmpty( t ) ) {
                    temp.add( t.trim() );
                }
            }
            _lineage = new String[ temp.size() ];
            for( int i = 0; i < temp.size(); ++i ) {
                _lineage[ i ] = temp.get( i );
            }
        }
        else {
            _lineage = new String[ 0 ];
        }
    }

    public String[] getLineage() {
        return _lineage;
    }

    public String getCode() {
        return _code;
    }

    public String getScientificName() {
        return _scientific_name;
    }

    public String getCommonName() {
        return _common_name;
    }

    public String getSynonym() {
        return _synonym;
    }

    public String getRank() {
        return _rank;
    }

    public String getId() {
        return _id;
    }
}
