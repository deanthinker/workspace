import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;


public abstract class PanelCustSalesRecord extends JPanel {
	public String custcode;
	public String crop;
	public String selectedcrop = null;
	public String pcode;
	public String ys;
	public String ye;
	private MyTableModel model = null;
	private JTable atable = new JTable();
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	
	public static void main(String[] args) {

		try {
			JFrame frame = new JFrame();
			PanelCustSalesRecord pcustsales = new PanelCustSalesRecord(){
				public void update(){
					
				}
			};
					
			
			frame.getContentPane().add(pcustsales);
			frame.setVisible(true);
			frame.setSize(700, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			pcustsales.setCustcode("2013","2013","K Y V", "���");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public PanelCustSalesRecord() {
		this.setLayout(new BorderLayout());
		JPanel tablepane = new JPanel(new GridLayout(1,0,0,0));
		JPanel titlepane = new JPanel(new FlowLayout());
		JLabel title = new JLabel("�Ȥ����������� ");
		
		titlepane.add(title);
		
		//tablepane.add(title, BorderLayout.NORTH);
		

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)atable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( JLabel.RIGHT );
		
		JScrollPane scrollPane = new JScrollPane(atable);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		atable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(final MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					final JTable target = (JTable)e.getSource();
					final int row = target.getSelectedRow();
					String pcode = (String)target.getValueAt(row, 0);
					u.debug (pcode);
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					
				}
			}

		});	
		tablepane.add(scrollPane);
		
		this.add(titlepane, BorderLayout.NORTH);
		this.add(tablepane, BorderLayout.CENTER);
	}
	
	public void setCustcode(String ys, String ye, String custcode, String crop){
		this.ys = ys;
		this.ye = ye;
		this.custcode = custcode;
		this.crop = crop;
		
		this.model = new MyTableModel(db.getResultset_cust_sales_detail(ys,ye,custcode,crop));
		
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		u.debug("records:" + atable.getRowCount());
		//setColumnWidth();
		atable.repaint();
	

	}
	/*
	private void setColumnWidth(){
		TableColumn column = null;
		for (int c=0;c<atable.getColumnCount();c++){
			column = atable.getColumnModel().getColumn(c);
			column.setPreferredWidth(100);
		}
		
	}
	*/
	public abstract void update();
}
