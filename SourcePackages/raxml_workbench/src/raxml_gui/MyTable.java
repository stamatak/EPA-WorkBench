package raxml_gui;

import javax.swing.table.AbstractTableModel;



public class MyTable extends AbstractTableModel{

	private String[] _columnNames;
    private Object[][] _data;
	
    public MyTable(Object[][] rowData, String[] columnNames){
    	this._columnNames = columnNames;
    	this._data = rowData;
    }
    
	public int getColumnCount() {
        return _columnNames.length;
    }

    public int getRowCount() {
        return _data.length;
    }

    public String getColumnName(int col) {
        return _columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return _data[row][col];
    }

    public boolean isCellEditable(int row, int col) {
       return false;
    }
	

}
