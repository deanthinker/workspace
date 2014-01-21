import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

// class that extends the AbstractTableModel
class MyTableModel extends AbstractTableModel {
		

	private static final long serialVersionUID = 1L;
	// to store our elements it will be great to avoid parallel array and use 
	// an ArrayList<Animal> but for simplicity and not to have to add a new 
	// class with will use an ArrayList<Object> for each row
	private KYutil u = new KYutil();
	Vector<Vector> rows = new Vector<Vector>();
	Vector<String> header = new Vector<String>();
	
	
	// constructor 
	MyTableModel(ResultSet rs) {
		setResultSet(rs);
		
	}
	
	public void setResultSet(ResultSet rs){
		ResultSetMetaData rsmd;
		int colNo=0;
		Vector<Object> rowvec = new Vector<Object>();
		rows.removeAllElements();
		header.removeAllElements();
		
		try {
			rsmd = rs.getMetaData();
			colNo = rsmd.getColumnCount();
			for (int h=1;h<=colNo;h++){
				u.debug("total:"+colNo+"  col:"+h + "   head:"+rsmd.getColumnName(h));
				header.add(rsmd.getColumnName(h));
			}

			while(rs.next()){
				rowvec = new Vector<Object>();
				for (int i=0;i<colNo;i++){
					rowvec.add(rs.getObject(i+1));
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
		return rows.get(rowIndex).get(columnIndex).toString();
	}
	
	public Vector<String> getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	// a method to return the column name 
	public String getColumnName(int index) {
		return header.get(index);
	}
	

}

