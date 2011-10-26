// $Id: NodePanel.java,v 1.17 2009/10/30 03:00:51 cmzmasek Exp $
// FORESTER -- software libraries and applications
// for evolutionary biology research and applications.
//
// Copyright (C) 2008-2009 Christian M. Zmasek
// Copyright (C) 2008-2009 Burnham Institute for Medical Research
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
// WWW: www.phylosoft.org/

package org.forester.archaeopteryx;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Annotation;
import org.forester.phylogeny.data.BinaryCharacters;
import org.forester.phylogeny.data.Date;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.Event;
import org.forester.phylogeny.data.PhylogenyData;
import org.forester.phylogeny.data.PropertiesMap;
import org.forester.phylogeny.data.Property;
import org.forester.phylogeny.data.Reference;
import org.forester.phylogeny.data.Sequence;
import org.forester.phylogeny.data.Taxonomy;
import org.forester.util.ForesterUtil;

class BranchPanel extends JPanel implements TreeSelectionListener {

    static final String       DIST_ALTITUDE            = "Altitude";
    static final String       DIST_LONGITUDE           = "Longitude";
    static final String       DIST_LATITUDE            = "Latitude";
    static final String       DIST_GEODETIC_DATUM      = "Geodetic datum";
    static final String       DIST_DESCRIPTION         = "Description";
    static final String       DATE_UNIT                = "Unit";
    static final String       DATE_MAX                 = "Max";
    static final String       DATE_MIN                 = "Min";
    static final String       DATE_VALUE               = "Value";
    static final String       DATE_DESCRIPTION         = "Description";
    static final String       TAXONOMY_IDENTIFIER      = "Identifier";
    static final String       SEQ_ACCESSION            = "Accession";
    static final String       CONFIDENCE               = "Confidence";
    static final String       PROP                     = "Properties";
    static final String       BINARY_CHARACTERS        = "Binary characters";
    static final String       REFERENCE                = "Reference";
    static final String       LIT_REFERENCE            = "Reference";
    static final String       LIT_REFERENCE_DESC       = "Description";
    static final String       LIT_REFERENCE_DOI        = "DOI";
    static final String       DISTRIBUTION             = "Distribution";
    static final String       DATE                     = "Date";
    static final String       EVENTS                   = "Events";
    static final String       SEQUENCE                 = "Sequence";
    static final String       INSERTS                  = "Inserts";
    static final String       TAXONOMY                 = "Taxonomy";
    static final String       BASIC                    = "Basic";
    static final String       TAXONOMY_SCIENTIFIC_NAME = "Scientific name";
    static final String       SEQ_MOL_SEQ              = "Mol seq";
    static final String       SEQ_TYPE                 = "Type";
    static final String       SEQ_LOCATION             = "Location";
    static final String       SEQ_SYMBOL               = "Symbol";
    static final String       SEQ_URI                  = "URI";
    static final String       NODE_BRANCH_LENGTH       = "Branch length";
    static final String       NODE_NAME                = "Name";
    static final String       TAXONOMY_URI             = "URI";
    static final String       TAXONOMY_RANK            = "Rank";
    static final String       TAXONOMY_SYNONYM         = "Synonym";
    static final String       TAXONOMY_COMMON_NAME     = "Common name";
    static final String       TAXONOMY_AUTHORITY       = "Authority";
    static final String       TAXONOMY_CODE            = "Code";
    static final String       SEQ_NAME                 = "Name";
    static final String       EVENTS_GENE_LOSSES       = "Gene losses";
    static final String       EVENTS_SPECIATIONS       = "Speciations";
    static final String       EVENTS_DUPLICATIONS      = "Duplications";
    private static final long serialVersionUID         = 5120159904388100771L;
    static final String       CONFIDENCE_TYPE          = "type";
    private final JTree       _tree;
    private final JEditorPane _pane;

    public BranchPanel( final PhylogenyNode phylogeny_node ) {
    	String branch_name = "";
    	if ( phylogeny_node.getNodeData().isHasSequence() ) {
    		branch_name =  phylogeny_node.getNodeData().getSequences().get(0).getName();  //first sequence object holds branch name and histogram
 //   		data = ((Annotation) phylogeny_node.getNodeData().getSequence().getAnnotation(0)).getDesc();
    	}
        final DefaultMutableTreeNode top = new DefaultMutableTreeNode( "Branch " + branch_name );
        createNodes( top, phylogeny_node );
        _tree = new JTree( top );
        _tree.setEditable( false );
        getJTree().setToggleClickCount( 1 );
        expandPath( BASIC );
        expandPath( TAXONOMY );
        expandPath( SEQUENCE );
        expandPath( EVENTS ); 
        final JScrollPane tree_view = new JScrollPane( getJTree() );
        _pane = new JEditorPane();
        _pane.setEditable( false );
        //final JScrollPane data_view = new JScrollPane( _pane );

        String[][] table_data = getTableData(phylogeny_node);
        String[] column_names = {"Placement Name","EDPL","RAxML weight"};
        JTable table = new JTable(new BranchDataTable(table_data, column_names));
        table.setRowHeight(20);
        table.setRowMargin(2);
        table.setShowHorizontalLines(false);
        TableColumn column = null;
        for (int i = 0; i < column_names.length; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 2) {
            	column.setPreferredWidth(20);
            } 
            else if (i == 1){
                column.setPreferredWidth(20);
            }
        }
        JScrollPane data_view = new JScrollPane(table);

        final JSplitPane split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split_pane.setTopComponent( tree_view );
        split_pane.setBottomComponent( data_view );
        data_view.setMinimumSize( Constants.BRANCH_PANEL_MINIMUM_SIZE);
        tree_view.setMinimumSize( Constants.NODE_PANEL_SPLIT_MINIMUM_SIZE );
        split_pane.setDividerLocation( 200 );
        split_pane.setPreferredSize( Constants.NODE_PANEL_SIZE );
        add( split_pane );
    }
    
    private static String[][] getTableData(PhylogenyNode phylogeny_node){
    	List<Sequence> data = phylogeny_node.getNodeData().getSequences();
    	ArrayList <String[]> tmp = new ArrayList<String[]>();
    	if ( ( data.get(1).getAnnotations().size() )> 1){
    		for(int i = 1; i < data.size(); i++){
    			String insert_name = data.get(i).getName();
    			String pplacer_score =String.valueOf(((Annotation) data.get(i).getAnnotation(0)).getConfidence().getValue());
    			String raxml_weight = String.valueOf(((Annotation) data.get(i).getAnnotation(1)).getConfidence().getValue());
    			tmp.add(new String[]{insert_name,pplacer_score,raxml_weight});
    		}
    	}
//    	Pattern p1 = Pattern.compile("\\s*(\\S+):\\s+(\\d\\.\\d+);(\\d\\.\\d+)\\s*");
//    	String[] lines = data.split(",");
//    	ArrayList <String[]> tmp = new ArrayList<String[]>();
//    	
//    	for (int i = 0; i<lines.length; i++){
//    		Matcher m = p1.matcher(lines[i]);
//    		if (m.matches()){
//    			tmp.add(new String[]{m.group(1),m.group(2),m.group(3)});
//    		}
//    	}
    	String[][] out = new String[tmp.size()][3];
    	for (int i = 0; i<tmp.size();i++){
    		out[i] = tmp.get(i);
    	}
    	
    	return out;
    }
    
    
    private void expandPath( final String name ) {
        final TreePath tp = getJTree().getNextMatch( name, 0, Position.Bias.Forward );
        if ( tp != null ) {
            getJTree().expandPath( tp );
        }
    }

    private JTree getJTree() {
        return _tree;
    }

    @Override
    public void valueChanged( final TreeSelectionEvent e ) {
        // Do nothing.
    }

    private static void addAnnotation( final DefaultMutableTreeNode top, final Annotation ann, final String name ) {
        DefaultMutableTreeNode category;
        category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, REFERENCE, ann.getRef() );
        addSubelement( category, "Names & Condidences","" );
        addSubelement( category, "Source", ann.getSource() );
        addSubelement( category, "Type", ann.getType() );
        addSubelement( category, "Evidence", ann.getEvidence() );
        if ( ann.getConfidence() != null ) {
            addSubelement( category, CONFIDENCE, ann.getConfidence().asText().toString() );
        }
        if ( ann.getProperties() != null ) {
            addProperties( category, ann.getProperties(), PROP );
        }
    }


    
    private static void addAnnotations( final DefaultMutableTreeNode top,
                                        final List<PhylogenyData> annotations,
                                        final DefaultMutableTreeNode category ) {
        if ( ( annotations != null ) && ( annotations.size() > 0 ) ) {
            final DefaultMutableTreeNode last = top.getLastLeaf();
            int i = 0;
            for( final PhylogenyData ann : annotations ) {
                addAnnotation( last, ( Annotation ) ann, "Annotation " + ( i++ ) );
            }
        }
    }

    private static void addBasics( final DefaultMutableTreeNode top,
                                   final PhylogenyNode phylogeny_node,
                                   final String name ) {
        final DefaultMutableTreeNode category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, NODE_NAME, phylogeny_node.getNodeName() );
        if ( phylogeny_node.getDistanceToParent() != PhylogenyNode.DISTANCE_DEFAULT ) {
            addSubelement( category, NODE_BRANCH_LENGTH, ForesterUtil.FORMATTER_6.format( phylogeny_node
                    .getDistanceToParent() ) );
        }
        if ( phylogeny_node.getBranchData().isHasConfidences() ) {
            for( final PhylogenyData conf : phylogeny_node.getBranchData().getConfidences() ) {
                addSubelement( category, CONFIDENCE, conf.asText().toString() );
            }
        }
        if ( !phylogeny_node.isExternal() ) {
            addSubelement( category, "Children", String.valueOf( phylogeny_node.getNumberOfDescendants() ) );
            addSubelement( category, "External children", String.valueOf( phylogeny_node.getAllExternalDescendants()
                    .size() ) );
            final int tax_count = PhylogenyMethods.calculateSumOfDistinctTaxonomies( phylogeny_node );
            if ( tax_count > 0 ) {
                addSubelement( category, "Distinct external taxonomies", String.valueOf( tax_count ) );
            }
        }
        if ( !phylogeny_node.isRoot() ) {
            addSubelement( category, "Depth", String.valueOf( PhylogenyMethods.calculateDepth( phylogeny_node ) ) );
            final double d = PhylogenyMethods.calculateDistanceToRoot( phylogeny_node );
            if ( d > 0 ) {
                addSubelement( category, "Distance to root", String.valueOf( ForesterUtil.FORMATTER_6.format( d ) ) );
            }
        }
    }

    private static void addBinaryCharacters( final DefaultMutableTreeNode top,
                                             final BinaryCharacters bc,
                                             final String name ) {
        DefaultMutableTreeNode category;
        category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, "Gained", String.valueOf( bc.getGainedCount() ) );
        addSubelement( category, "Lost", String.valueOf( bc.getLostCount() ) );
        addSubelement( category, "Present", String.valueOf( bc.getPresentCount() ) );
        final DefaultMutableTreeNode chars = new DefaultMutableTreeNode( "Lists" );
        category.add( chars );
        addSubelement( chars, "Gained", bc.getGainedCharactersAsStringBuffer().toString() );
        addSubelement( chars, "Lost", bc.getLostCharactersAsStringBuffer().toString() );
        addSubelement( chars, "Present", bc.getPresentCharactersAsStringBuffer().toString() );
    }

    private static void addDate( final DefaultMutableTreeNode top, final Date date, final String name ) {
        DefaultMutableTreeNode category;
        category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, DATE_DESCRIPTION, date.getDesc() );
        addSubelement( category, DATE_VALUE, String.valueOf( date.getValue() ) );
        addSubelement( category, DATE_MIN, String.valueOf( date.getMin() ) );
        addSubelement( category, DATE_MAX, String.valueOf( date.getMax() ) );
        addSubelement( category, DATE_UNIT, date.getUnit() );
    }

    private static void addDistribution( final DefaultMutableTreeNode top, final Distribution dist, final String name ) {
        DefaultMutableTreeNode category;
        category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, DIST_DESCRIPTION, dist.getDesc() );
        addSubelement( category, DIST_GEODETIC_DATUM, dist.getGeodeticDatum() );
        addSubelement( category, DIST_LATITUDE, String.valueOf( dist.getLatitude() ) );
        addSubelement( category, DIST_LONGITUDE, String.valueOf( dist.getLongitude() ) );
        addSubelement( category, DIST_ALTITUDE, String.valueOf( dist.getAltitude() ) );
    }

    private static void addEvents( final DefaultMutableTreeNode top, final Event events, final String name ) {
        DefaultMutableTreeNode category;
        category = new DefaultMutableTreeNode( name );
        top.add( category );
        if ( events.getNumberOfDuplications() > 0 ) {
            addSubelement( category, EVENTS_DUPLICATIONS, String.valueOf( events.getNumberOfDuplications() ) );
        }
        if ( events.getNumberOfSpeciations() > 0 ) {
            addSubelement( category, EVENTS_SPECIATIONS, String.valueOf( events.getNumberOfSpeciations() ) );
        }
        if ( events.getNumberOfGeneLosses() > 0 ) {
            addSubelement( category, EVENTS_GENE_LOSSES, String.valueOf( events.getNumberOfGeneLosses() ) );
        }
        addSubelement( category, "Type", events.getEventType().toString() );
        if ( events.getConfidence() != null ) {
            addSubelement( category, CONFIDENCE, events.getConfidence().asText().toString() );
        }
    }

    private static void addProperties( final DefaultMutableTreeNode top,
                                       final PropertiesMap properties,
                                       final String string ) {
        final SortedMap<String, Property> properties_map = properties.getProperties();
        final DefaultMutableTreeNode category = new DefaultMutableTreeNode( "Properties " );
        top.add( category );
        for( final String key : properties_map.keySet() ) {
            final Property prop = properties_map.get( key );
            category.add( new DefaultMutableTreeNode( prop.getRef() + " " + prop.getValue() + " " + prop.getUnit()
                    + " [" + prop.getAppliesTo().toString() + "]" ) );
        }
    }

    private static void addReference( final DefaultMutableTreeNode top, final Reference ref, final String name ) {
        final DefaultMutableTreeNode category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, LIT_REFERENCE_DOI, ref.getDoi() );
        addSubelement( category, LIT_REFERENCE_DESC, ref.getValue() );
    }

    private static void addSequence( final DefaultMutableTreeNode top, final Sequence seq, final String name ) {
        final DefaultMutableTreeNode category = new DefaultMutableTreeNode( name );
        top.add( category );
        addSubelement( category, SEQ_NAME, seq.getName() );
        addSubelement( category, SEQ_SYMBOL, seq.getSymbol() );
        if ( seq.getAccession() != null ) {
            addSubelement( category, SEQ_ACCESSION, seq.getAccession().asText().toString() );
        }
        addSubelement( category, SEQ_LOCATION, seq.getLocation() );
        addSubelement( category, SEQ_TYPE, seq.getType() );
        addSubelement( category, SEQ_MOL_SEQ, seq.getMolecularSequence() );
        if ( seq.getUri() != null ) {
            addSubelement( category, NodePanel.SEQ_URI, seq.getUri().toString() );
        }
        addAnnotations( top, seq.getAnnotations(), category );
    }

    private static void addSubelement( final DefaultMutableTreeNode node, final String name, final String value ) {
        if ( !ForesterUtil.isEmpty( value ) ) {
            node.add( new DefaultMutableTreeNode( name + ": " + value ) );
        }
    }

    private static void addTaxonomy( final DefaultMutableTreeNode top, final Taxonomy tax, final String name ) {
        final DefaultMutableTreeNode category = new DefaultMutableTreeNode( name );
        top.add( category );
        if ( tax.getIdentifier() != null ) {
            addSubelement( category, TAXONOMY_IDENTIFIER, tax.getIdentifier().asText().toString() );
        }
        addSubelement( category, TAXONOMY_CODE, tax.getTaxonomyCode() );
        addSubelement( category, TAXONOMY_SCIENTIFIC_NAME, tax.getScientificName() );
        addSubelement( category, TAXONOMY_AUTHORITY, tax.getAuthority() );
        addSubelement( category, TAXONOMY_COMMON_NAME, tax.getCommonName() );
        for( final String syn : tax.getSynonyms() ) {
            addSubelement( category, TAXONOMY_SYNONYM, syn );
        }
        addSubelement( category, TAXONOMY_RANK, tax.getRank() );
        if ( tax.getUri() != null ) {
            addSubelement( category, TAXONOMY_URI, tax.getUri().toString() );
        }
    }

    private static void createNodes( final DefaultMutableTreeNode top, final PhylogenyNode phylogeny_node ) {
        addBasics( top, phylogeny_node, BASIC );
        // Taxonomy
        if ( phylogeny_node.getNodeData().isHasTaxonomy() ) {
            addTaxonomy( top, phylogeny_node.getNodeData().getTaxonomy(), TAXONOMY );
        }
        // Sequence
        if ( phylogeny_node.getNodeData().isHasSequence() ) {
        	//final DefaultMutableTreeNode last = top.getLastLeaf();
        	//final DefaultMutableTreeNode category = new DefaultMutableTreeNode( INSERTS );
        	//top.add( category );
            //addAnnotation(top, (Annotation) phylogeny_node.getNodeData().getSequence().getAnnotations().get(0), INSERTS );
        }
        // Events
        if ( phylogeny_node.getNodeData().isHasEvent() ) {
            addEvents( top, phylogeny_node.getNodeData().getEvent(), EVENTS );
        }
        // Date
        if ( phylogeny_node.getNodeData().isHasDate() ) {
            addDate( top, phylogeny_node.getNodeData().getDate(), DATE );
        }
        // Distribution
        if ( phylogeny_node.getNodeData().isHasDistribution() ) {
            addDistribution( top, phylogeny_node.getNodeData().getDistribution(), DISTRIBUTION );
        }
        // Reference
        if ( phylogeny_node.getNodeData().isHasReference() ) {
            addReference( top, phylogeny_node.getNodeData().getReference(), LIT_REFERENCE );
        }
        // BinaryCharacters
        if ( phylogeny_node.getNodeData().isHasBinaryCharacters() ) {
            addBinaryCharacters( top, phylogeny_node.getNodeData().getBinaryCharacters(), BINARY_CHARACTERS );
        }
        // Properties
        if ( phylogeny_node.getNodeData().isHasProperties() ) {
            addProperties( top, phylogeny_node.getNodeData().getProperties(), PROP );
        }
    }
}
