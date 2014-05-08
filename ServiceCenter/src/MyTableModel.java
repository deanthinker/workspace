import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

// class that extends the AbstractTableModel
class MyTableModel extends AbstractTableModel {
		

	private static final long serialVersionUID = 1L;
	// to store our elements it will be great to avoid parallel array and use 
	// an ArrayList<Animal> but for simplicity and not to have to add a new 
	// class with will use an ArrayList<Object> for each row

	Vector<Vector> rows = new Vector<Vector>();
	ArrayList<String> header = new ArrayList<String>();
	private int count = 0;
	
	// constructor 
	MyTableModel(ResultSet rs) {
		setResultSet(rs);
		
	}
	public int getCount(){
		return this.count;
	}
	
	public ArrayList getHeaders(){
		return header;
	}
	
	public void setResultSet(ResultSet rs){
		ResultSetMetaData rsmd;
		int colNo=0;
		Vector<String> rowvec = new Vector<String>();
		rows.removeAllElements();
		header.clear();
		
		try {
			rsmd = rs.getMetaData();
			colNo = rsmd.getColumnCount();
			for (int h=1;h<=colNo;h++){
				System.out.println("total:"+colNo+"  col:"+h + "   head:"+rsmd.getColumnName(h));
				header.add(rsmd.getColumnName(h));
			}

			while(rs.next()){
				this.count++;
				rowvec = new Vector<String>();
				for (int i=0;i<colNo;i++){
					if (rs.getString(i+1) == null)
						rowvec.add("");
					else
						rowvec.add(rs.getString(i+1));
				}
				rows.add(rowvec);
			}

			rs.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	// method that needs to be overload. The row count is the size of the ArrayList
	public int getRowCount() {
		return rows.size();
	}

	// method that needs to be overload. The column count is the size of our header
	public int getColumnCount() {
		return header.size();
	}

	// method that needs to be overload. The object is in the arrayList at rowIndex
	public String getValueAt(int rowIndex, int columnIndex) {
		return String.valueOf(rows.get(rowIndex).get(columnIndex));
	}
	
	public Vector<String> getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	// a method to return the column name 
	public String getColumnName(int index) {
		return header.get(index);
	}
	

}

