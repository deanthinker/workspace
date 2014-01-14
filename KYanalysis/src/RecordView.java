import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.ScrollPaneConstants;

import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;


public class RecordView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	private MyModel model;
	JTable atable = new JTable()
    {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer,
                                         int row, int col)
        {
          Component comp = super.prepareRenderer(renderer, row, col);
          ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);
          return comp;
        }
      };			
/*
	public static void main(String[] args) {
		KYdb db = new KYdb();
		try {
			RecordView dialog = new RecordView(db.getResultset_vege_prod(),"ด๚ธี");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * Create the dialog.
	 */
	public RecordView() {
		setBounds(50, 50, 800, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);
		{
			 JScrollPane scrollPane = new JScrollPane(atable);
			 scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			 scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			 getContentPane().add(scrollPane);
					
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	public RecordView(ResultSet rs, String title) {
		setTitle(title);
		setBounds(50, 50, 800, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);
		{

			 this.model = new MyModel(rs);
			 atable.setModel(model);
			    RowSorter<MyModel> sorter = new TableRowSorter<MyModel>(model);
			    atable.setRowSorter(sorter);
			    
			 JScrollPane scrollPane = new JScrollPane(atable);
			 scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			 scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			 getContentPane().add(scrollPane);
					
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	public void setResultSet(ResultSet rs){
		 this.model = new MyModel(rs);
		 atable.setModel(model);		
	}
	
}

// class that extends the AbstractTableModel
class MyModel extends AbstractTableModel {
		
	// to store our elements it will be great to avoid parallel array and use 
	// an ArrayList<Animal> but for simplicity and not to have to add a new 
	// class with will use an ArrayList<Object> for each row
	private KYutil u = new KYutil();
	Vector<Vector> rows = new Vector<Vector>();
	Vector<String> header = new Vector<String>();
	
	
	// constructor 
	MyModel(ResultSet rs) {
		ResultSetMetaData rsmd;
		int colNo=0;
		Vector<Object> rowvec = new Vector<Object>();
		
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
