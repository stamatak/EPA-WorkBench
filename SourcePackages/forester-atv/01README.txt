ATV version 2.03beta1
10/11/04


ESKC Changes since version 2.00beta1:
-------------------------------------
1. Applet only: clicking a sequence name will pop up a specified URL in a new window, 
passing the sequence name to that URL.
2. Applet and App: on some browsers, moving the mouse over a clickable object (e.g. a 
   node or sequence name) will turn the mouse cursor into a hand.
3. Applet and App: added a new click-on-node option, 'display sequences' which, when 
selected, will cause a list of sequences for each clicked node to be displayed in popup 
window.
4. Applet and App: Added light-weight support for reading XML files.
5. Applet and App: Controls can be configured by an XML config file.
6. Applet and App: XML config file can control species colors.
7. Applet and App: Added a "glyph" panel for displaying expression graphs for each
   sequence. Prototype only; needs lots of work.
8. Applet and App: Added a new click-on-node option to display expression graphs for 
  all sequences for that node.
9. Applet only: Added a new click-on-node option to display a specified URL in a new window, 
passing a comma-separated list of all terminal sequences from that node to the URL.

New packages:
forester/extensions -- for "glyph" panel and popup extensions such as displaying expression graphs
forester/xml -- lightwieght XML support.


Next set of proposed changes:
-----------------------------
1. Fix expression graph panel:
	a. More general so that other types of graphs can be easily added (e.g. intron-exon graphs).
  b. match sequences names in panel to sequence name order and location in the tree.

