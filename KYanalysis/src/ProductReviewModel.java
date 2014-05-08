import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

// class that extends the AbstractTableModel
class ProductReviewModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	// to store our elements it will be great to avoid parallel array and use 
	// an ArrayList<Animal> but for simplicity and not to have to add a new 
	// class with will use an ArrayList<Object> for each row
	private KYutil u = new KYutil();
	Vector<Object> rows = new Vector<Object>();
	Vector<Object> allR = new Vector<Object>();
	Vector<String> header = new Vector<String>();
	Vector<String> allH = new Vector<String>();
		
	ProductReviewModel(ResultSet rs) {
		setResultSet(rs);
	}

	ProductReviewModel(ResultSet rs, Vector<String> selHeaders) {
		setResultSet(rs);
	}

	public void reload(){
		header = allH;
		rows = allR;
	}
	
	public void hideH(String col){
		int idx = header.indexOf(col);
		if (idx < 0)
			return;
		
		header.removeElement(idx);
		System.out.println("remove:"+idx);
		
		Iterator it = rows.iterator();
		int count = 0;		
		while(count < rows.size()){
			System.out.println(count);
			Vector<String> v = (Vector<String>)it.next();
			System.out.println("pass1");
			v.removeElementAt(idx);
			System.out.println("pass2");
			count++;
		}
		
	}
	
	public void showHeader(String col){
		
		header.insertElementAt(col, 3);
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
			allH = header; //make a copy

			while(rs.next()){
				rowvec = new Vector<Object>();
				for (String col : header){
					switch (col){
						case "pcode":
						case "level2":
						case "pcname":
							rowvec.add(rs.getString(col));
							break;
							
						default:
							rowvec.add(Float.valueOf(rs.getFloat(col)));
							break;
					}
				
				}
				rows.add(rowvec);
			}
			allR = rows;
			rs.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public Vector<String> getHeader(){
		return header;
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		return String.valueOf(  ((Vector<Object>)rows.get(rowIndex)).get(columnIndex));
	}
	
	public Vector<Object> getRow(int rowIndex) {
		return (Vector<Object>)rows.get(rowIndex);
	}
	// a method to return the column name 
	public String getColumnName(int index) {
		return header.get(index);
	}
	

}

