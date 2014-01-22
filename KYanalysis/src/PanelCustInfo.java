import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.ibm.icu.math.BigDecimal;


public abstract class PanelCustInfo extends JPanel {
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	public String custcode;
	public String crop;
	public String pcode;
	public String selectedcrop;
	public String ys;
	public String ye;
	private String totalSales = "";
	private String custSales = "";
	private MyTableModel model = null;
	//private MyTableModel model = new MyTableModel(db.getResultset_CustSales("2013","2013","AE",null,"all"));
	private JLabel lbltitle = new JLabel();
	private JTable atable = new JTable();

	
	public static void main(String[] args) {

		try {
			JFrame frame = new JFrame();
			PanelCustInfo pcust = new PanelCustInfo(){
				public void update(){
					
				}
			};
						
			frame.getContentPane().add(pcust);
			frame.setVisible(true);
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			pcust.setTable("2013","2013","TK",null,"all"); //test1
			pcust.setTable("2013","2013","AE",null,"all"); //test2
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		
	
	public PanelCustInfo() {
		setLayout(new GridLayout(2,0,0,0));
		add(compTable());
		
	}

	private JComponent compTable(){
		
		JPanel tablepane = new JPanel(new BorderLayout());
		tablepane.add(lbltitle, BorderLayout.NORTH);
		/*
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		*/
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)atable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( JLabel.RIGHT );
		
		JScrollPane scrollPane = new JScrollPane(atable);
		scrollPane.setPreferredSize(new Dimension(50,100));
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
					//final int column = target.getSelectedColumn();
					final int column = 0; //custcode
					selectedcrop = (String)target.getValueAt(row, column);
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					u.debug (selectedcrop);
				}
			}

		});	
		tablepane.add(scrollPane, BorderLayout.CENTER);
		

		
		return tablepane;
	}
	private void setTitle(){
		BigDecimal bi = new BigDecimal(custSales);
		bi = bi.divide(new BigDecimal("10000"),1);
		lbltitle.setText("客戶"+custcode+"  銷售NT$"+  bi.toString()+"萬 (佔總營收%)  期間:"+ys+"~"+ye);
	}

	private void setTable(String ys, String ye, String custcode, String pcode, String crop){
		this.ys = ys;
		this.ye = ye;
		this.custcode = custcode;
		this.pcode = pcode;
		this.crop = crop;
		
		this.model = new MyTableModel(db.getResultset_CustSales(ys,ye,custcode,null,crop));
		
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);	
		
		this.totalSales = db.getYearRangeSales(ys,ye);
		this.custSales = db.getYearRangeSalesByCust(ys,ye,custcode);
		setTitle();
	}
	
	public void updateTable(){
			
	}
	
	
	public abstract void update();

}
