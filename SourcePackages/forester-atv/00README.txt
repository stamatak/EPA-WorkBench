ATV 3.1 BETA
  1/1/06
============

Quastions, comments: cmzmasek@yahoo.com, ethy@a415software.com

Table of Contents
1. Running ATV
2. Applet parameters
3. PhyloXML
4. New Hampshire Extended (NHX)
  4.1 Custom tags in NHX:
  4.2. New (regular) tags in NHX:
  
  
1. Running ATV
______________

ATV uses an XML config file to determine what controls to display 
in the control panel, and what to label them (if you don't like the
default text). A sample XML config file is included in the distribution.


2. Applet parameters
____________________

None of these are required.

url_of_tree_to_load = URL of initial tree too load into the applet.
config_file         = specify an alternative XML config file or path;
                      default is ATVconfig.xml in the doc root.
sequence_info       = URL to go to when a sequence name is clicked.
                      The sequence name is appended to the end of the URL


3. phyloXML
___________

phyloXML is available in this release of ATV.


4. New Hampshire Extended (NHX)
_______________________________

See the various "demo_....nhx" trees in the examples folder.

  4.1 Custom tags in NHX:
  -----------------------
  
  Format:
  :X[N|B]=[S|D|L|B|U]=<tag>=<value>[=<unit>]
  
  tag and value are mandatory
  unit is optional
  
  N: for node properties
  B: for parent branch properties
  
  types:
  S: string
  D: double
  L: long (integer)
  B: boolean
  U: URL
  
  e.g. "[&&NHX:S=alligatot:XN=S=status=certain:XN=D=genome=4.3=MB]"
  
  4.2. New (regular) tags in NHX:
  -------------------------------
  
    Branch Color:
    -------------
    
    Format:
    :C=<red>.<green>.<blue>
    
    All three color components must be integers between (and including) 
    0 and 255. This affects the parent branch of the node being described.
    
    e.g. "[&&NHX:C=255.0.0]" for bright red.
    
    
    Branch Width:
    -------------
    
    Format:
    :W=<width>
    
    width must be an integer greater or equal to zero.
    This affect the parent branch of the node being described.
    
    e.g. "[&&NHX:W=3]"





