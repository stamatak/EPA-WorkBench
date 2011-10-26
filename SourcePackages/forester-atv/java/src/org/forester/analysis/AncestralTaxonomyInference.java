// $Id: AncestralTaxonomyInference.java,v 1.10 2010/04/11 23:38:35 cmzmasek Exp $
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

package org.forester.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.forester.io.parsers.phyloxml.PhyloXmlDataFormatException;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Identifier;
import org.forester.phylogeny.data.Taxonomy;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.util.ForesterUtil;
import org.forester.ws.uniprot.UniProtTaxonomy;
import org.forester.ws.uniprot.UniProtTools;

public final class AncestralTaxonomyInference {

    private enum QUERY_TYPE {
        CODE, SN, CN, ID;
    }
    private static final HashMap<Taxonomy, UniProtTaxonomy> _sn_up_cache_map = new HashMap<Taxonomy, UniProtTaxonomy>();
    
    synchronized private static HashMap<Taxonomy, UniProtTaxonomy> getSnTaxCacheMap() {
        return _sn_up_cache_map;
    }

  

    synchronized private static boolean isHasAppropriateId( final Taxonomy tax ) {
        return ( ( tax.getIdentifier() != null ) && ( !ForesterUtil.isEmpty( tax.getIdentifier().getValue() ) && ( tax
                .getIdentifier().getProvider().equalsIgnoreCase( "ncbi" )
                || tax.getIdentifier().getProvider().equalsIgnoreCase( "uniprot" ) || tax.getIdentifier().getProvider()
                .equalsIgnoreCase( "uniprotkb" ) ) ) );
    }

    synchronized public static List<String> inferTaxonomyFromDescendents( final Phylogeny phy ) throws IOException {
        if ( getSnTaxCacheMap().size() > 20000 ) {
            getSnTaxCacheMap().clear();
        }
        final List<String> not_found = new ArrayList<String>();
        QUERY_TYPE qt = null;
        for( final PhylogenyNodeIterator iter = phy.iteratorPostorder(); iter.hasNext(); ) {
            final PhylogenyNode node = iter.next();
            // TODO need to distinguish between code and sn in nodename.
            Taxonomy tax = null;
            if ( node.getNodeData().isHasTaxonomy() ) {
                tax = node.getNodeData().getTaxonomy();
            }
            if ( ( ( tax != null ) && ( isHasAppropriateId( tax ) || !ForesterUtil.isEmpty( tax.getScientificName() )
                    || !ForesterUtil.isEmpty( tax.getTaxonomyCode() ) || !ForesterUtil.isEmpty( tax.getCommonName() ) ) )
                    || ( !ForesterUtil.isEmpty( node.getNodeName() ) && ( node.getNodeName().length() > 2 ) && ( node
                            .getNodeName().length() < 6 ) ) ) {
                String query = null;
                if ( tax != null ) {
                    if ( isHasAppropriateId( tax ) ) {
                        query = tax.getIdentifier().getValue();
                        qt = QUERY_TYPE.ID;
                    }
                    else if ( !ForesterUtil.isEmpty( tax.getScientificName() ) ) {
                        query = tax.getScientificName();
                        qt = QUERY_TYPE.SN;
                    }
                    else if ( !ForesterUtil.isEmpty( tax.getTaxonomyCode() ) ) {
                        query = tax.getTaxonomyCode();
                        qt = QUERY_TYPE.CODE;
                    }
                    else {
                        query = tax.getCommonName();
                        qt = QUERY_TYPE.CN;
                    }
                }
                else {
                    query = node.getNodeName();
                    qt = QUERY_TYPE.CODE;
                }
                List<UniProtTaxonomy> up_taxonomies = null;
                try {
                    switch ( qt ) {
                        case ID:
                            up_taxonomies = UniProtTools.getTaxonomiesFromId( query );
                            break;
                        case CODE:
                            up_taxonomies = UniProtTools.getTaxonomiesFromTaxonomyCode( query );
                            break;
                        case SN:
                            up_taxonomies = UniProtTools.getTaxonomiesFromScientificNameStrict( query );
                            break;
                        case CN:
                            up_taxonomies = UniProtTools.getTaxonomiesFromCommonNameStrict( query );
                            break;
                        default:
                            throw new IllegalStateException();
                    }
                }
                catch ( final IOException e ) {
                    e.printStackTrace();
                }
                if ( ( up_taxonomies != null ) && ( up_taxonomies.size() == 1 ) ) {
                    final UniProtTaxonomy up_tax = up_taxonomies.get( 0 );
                    if ( tax == null ) {
                        tax = new Taxonomy();
                        node.getNodeData().setTaxonomy( tax );
                        if ( !ForesterUtil.isEmpty( up_tax.getCode() ) ) {
                            tax.setTaxonomyCode( up_tax.getCode() );
                            node.setName( "" );
                        }
                    }
                    if ( ( qt != QUERY_TYPE.SN ) && !ForesterUtil.isEmpty( up_tax.getScientificName() )
                            && ForesterUtil.isEmpty( tax.getScientificName() ) ) {
                        tax.setScientificName( up_tax.getScientificName() );
                    }
                    if ( ( qt != QUERY_TYPE.CODE ) && !ForesterUtil.isEmpty( up_tax.getCode() )
                            && ForesterUtil.isEmpty( tax.getTaxonomyCode() ) ) {
                        tax.setTaxonomyCode( up_tax.getCode() );
                    }
                    if ( ( qt != QUERY_TYPE.CN ) && !ForesterUtil.isEmpty( up_tax.getCommonName() )
                            && ForesterUtil.isEmpty( tax.getCommonName() ) ) {
                        tax.setCommonName( up_tax.getCommonName() );
                    }
                    if ( !ForesterUtil.isEmpty( up_tax.getSynonym() ) ) {
                        tax.getSynonyms().add( up_tax.getSynonym() );
                    }
                    if ( !ForesterUtil.isEmpty( up_tax.getRank() ) && ForesterUtil.isEmpty( tax.getRank() ) ) {
                        tax.setRank( up_tax.getRank().toLowerCase() );
                    }
                    if ( ( qt != QUERY_TYPE.ID ) && !ForesterUtil.isEmpty( up_tax.getId() )
                            && ( tax.getIdentifier() == null ) ) {
                        tax.setIdentifier( new Identifier( up_tax.getId(), "ncbi" ) );
                    }
                }
            }
            if ( !node.isExternal() ) {
                inferTaxonomyFromDescendents( node, not_found );
            }
        }
        return not_found;
    }

    synchronized private static void inferTaxonomyFromDescendents( final PhylogenyNode n, final List<String> not_found )
            throws IOException {
        if ( n.isExternal() ) {
            throw new IllegalArgumentException( "attempt to infer taxonomy from descendants of external node" );
        }
        final List<PhylogenyNode> descs = n.getDescendants();
        final List<String[]> lineages = new ArrayList<String[]>();
        int shortest_lin_length = Integer.MAX_VALUE;
        for( final PhylogenyNode desc : descs ) {
            if ( !desc.getNodeData().isHasTaxonomy()
                    || ForesterUtil.isEmpty( desc.getNodeData().getTaxonomy().getScientificName() ) ) {
                return;
            }
            else {
                String[] lineage = null;
                boolean in_map = true;
                if ( getSnTaxCacheMap().containsKey( desc.getNodeData().getTaxonomy() ) ) {
                    lineage = getSnTaxCacheMap().get( desc.getNodeData().getTaxonomy() ).getLineage();
                }
                else {
                    in_map = false;
                    List<UniProtTaxonomy> up_taxonomies = null;
                    up_taxonomies = UniProtTools.getTaxonomiesFromScientificNameStrict( desc.getNodeData()
                            .getTaxonomy().getScientificName() );
                    if ( ( up_taxonomies != null ) && ( up_taxonomies.size() == 1 ) ) {
                        lineage = up_taxonomies.get( 0 ).getLineage();
                        //~ForesterUtil.printArray( lineage );
                        final String[] lin_plus_self = new String[ lineage.length + 1 ];
                        for( int i = 0; i < lineage.length; ++i ) {
                            lin_plus_self[ i ] = lineage[ i ];
                        }
                        lin_plus_self[ lineage.length ] = desc.getNodeData().getTaxonomy().getScientificName();
                        lineage = lin_plus_self;
                        //~ForesterUtil.printArray( lineage );
                    }
//                    if ( desc.getNodeData().getTaxonomy().getIdentifier() != null
//                            && ( ForesterUtil.isEmpty( desc.getNodeData().getTaxonomy().getIdentifier().getValue() ) ) ) {
//                        if ( desc.getNodeData().getTaxonomy().getIdentifier().getValue().equals( "2759" ) ) {
//                            lineage = new String[] { "Eukaryota" }; //TODO Hack!
//                        }
//                        else if ( desc.getNodeData().getTaxonomy().getIdentifier().getValue().equals( "2157" ) ) {
//                            lineage = new String[] { "Archaea" }; //TODO Hack!
//                        }
//                        else if ( desc.getNodeData().getTaxonomy().getIdentifier().getValue().equals( "2" ) ) {
//                            lineage = new String[] { "Bacteria" }; //TODO Hack!
//                        }
//                        else if ( desc.getNodeData().getTaxonomy().getIdentifier().getValue().equals( "10239" ) ) {
//                            lineage = new String[] { "Viruses" }; //TODO Hack!
//                        }
//                    }
                }
                if ( ( lineage == null ) || ( lineage.length < 1 ) ) {
                    not_found.add( desc.getNodeData().getTaxonomy().asText().toString() );
                    return;
                }
                if ( !in_map ) {
                    //getTaxonomyLineageCacheMap().put( desc.getNodeData().getTaxonomy(), lineage );
                }
                if ( lineage.length < shortest_lin_length ) {
                    shortest_lin_length = lineage.length;
                }
                lineages.add( lineage );
            }
        }
        String last_common_lineage = null;
        if ( shortest_lin_length > 0 ) {
            I: for( int i = 0; i < shortest_lin_length; ++i ) {
                final String lineage_0 = lineages.get( 0 )[ i ];
                for( int j = 1; j < lineages.size(); ++j ) {
                    if ( !lineage_0.equals( lineages.get( j )[ i ] ) ) {
                        break I;
                    }
                }
                last_common_lineage = lineage_0;
            }
        }
        //~System.out.println( "lcl=" + last_common_lineage );
        if ( last_common_lineage == null ) {
            return;
        }
        if ( !n.getNodeData().isHasTaxonomy() ) {
            n.getNodeData().setTaxonomy( new Taxonomy() );
        }
        final Taxonomy tax = n.getNodeData().getTaxonomy();
        tax.setScientificName( last_common_lineage );
        String rank = null;
        boolean in_map = true;
        List<UniProtTaxonomy> up_taxonomies = UniProtTools.getTaxonomiesFromScientificNameStrict( last_common_lineage );
        if ( ( up_taxonomies != null ) && ( up_taxonomies.size() == 1 ) ) {
            final UniProtTaxonomy up_tax = up_taxonomies.get( 0 );
            //~System.out.println( "internal tax==" + up_tax );
         //   if ( getTaxonomyRankCacheMap().containsKey( tax ) ) {
           //     rank = getTaxonomyRankCacheMap().get( tax );
          //  }
          //  else {
               
                rank = up_tax.getRank();
               //~ System.out.println( " internal rank==" + rank );
                in_map = false;
          //  }
            if ( !ForesterUtil.isEmpty( rank ) ) {
                if ( !in_map ) {
                    // getTaxonomyRankCacheMap().put( tax, rank );
                }
                try {
                    tax.setRank( rank.toLowerCase() );
                }
                catch ( final PhyloXmlDataFormatException ex ) {
                    tax.setRank( "" );
                }
            }
            in_map = true;
            String id = null;
           // if ( getTaxonomyIdCacheMap().containsKey( last_common_lineage ) ) {
                // id = getTaxonomyIdCacheMap().get( last_common_lineage );
           // }
           // else {
                //   id = TxSearch.getTxId( last_common_lineage );
                id = up_tax.getId();
                in_map = false;
           // }
            //~System.out.println( " internal tax id==" + id );
            if ( !ForesterUtil.isEmpty( id ) ) {
                tax.setIdentifier( new Identifier( id, "ncbi" ) );
                if ( !in_map ) {
                    // getTaxonomyIdCacheMap().put( last_common_lineage, id );
                }
            }
        }
        for( final PhylogenyNode desc : descs ) {
            if ( desc.getNodeData().isHasTaxonomy() && desc.getNodeData().getTaxonomy().isEqual( tax ) ) {
                desc.getNodeData().setTaxonomy( null );
            }
        }
    }
}
