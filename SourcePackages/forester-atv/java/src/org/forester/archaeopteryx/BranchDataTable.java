package org.forester.archaeopteryx;

import javax.swing.table.AbstractTableModel;

public class BranchDataTable extends AbstractTableModel{
	private String[] columnNames;//same as before...
    private Object[][] data;//same as before...

    public BranchDataTable(Object[][] rowData, String[] columnNames){
    	this.columnNames = columnNames;
    	this.data = rowData;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public boolean isCellEditable(int row, int col) {
       return false;
    }

    
    
    
    
    
    

    
    
	


}
