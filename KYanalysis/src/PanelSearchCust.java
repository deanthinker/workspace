import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;


public abstract class PanelSearchCust extends JPanel {
	public final int EXPORT = 1;
	public final int DOMESTIC = 2;
	public int DATASRC = EXPORT;
	private JLabel lbltitle = new JLabel();
	private JTextField txfKeyword = new JTextField();
	private JCheckBox chkSort = new JCheckBox("排序");
	private JComboBox<String> cbxSort = new JComboBox<String>();
	public PanelYearRange panelyr = new PanelYearRange("統計期間");
	private String selectedpcode = ""; 
	
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	
	private MyTableModel model;
	
	private JTable atable = new JTable();
	public String selectedcustcode = "";
	public String title = "";
	/*
	public static void main(String[] args) {
		KYdb db = new KYdb();
		try {
			JFrame frame = new JFrame();
			PanelSearchCust pcust = new PanelSearchCust(){
				public void update(){
					
				}
			};
			
			frame.getContentPane().add(pcust);
			frame.setVisible(true);
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public PanelSearchCust(int src) {
		DATASRC = src;
		
		model = new MyTableModel(db.getResultset_customer_all(DATASRC, null));
		
		this.setLayout(new GridLayout(2, 0, 0, 0));
		JPanel panelFilter = new JPanel();
		JPanel panelTable = new JPanel();
		this.add(panelFilter);
		this.add(panelTable);
		
		chkSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chkSort.isSelected())
					cbxSort.setEnabled(true);
				else
					cbxSort.setEnabled(false);
				updateList();
			}
		});	
		
		cbxSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		});	
		
		cbxSort.setEnabled(false); //set disabled by default
		
		panelFilter.setLayout(new GridLayout(3, 0, 0, 0));
		panelFilter.add(compKeyword());
		panelFilter.add(panelyr);
		panelFilter.add(compSort());
		
		panelTable.setLayout(new GridLayout(1, 1, 0, 0));
		panelTable.add(compTable());

		
	}
	
	public void setPcode(String pcode){
		selectedpcode = pcode;
		title = "以下為品種"+pcode+"的客戶清單";
		chkSort.setSelected(true);
		lbltitle.setText(title);
		updateList();
	}

	private void updateList(){
		if (chkSort.isSelected()){
				cbxSort.setEnabled(true);
				model = null; System.gc(); //free the memory
				String filter = null;
				
				if (txfKeyword.getText().length()==0)
					filter = "";
				else
					filter = "custcode like '%" + txfKeyword.getText() + "%' or cust_name like '%" + txfKeyword.getText() + "%' and";
				
				switch (cbxSort.getSelectedIndex()){
					case 0: //sales
						model = new MyTableModel(db.getResultset_customer_pcode(DATASRC,selectedpcode,panelyr.getYS(), panelyr.getYE(), filter, "tsales"));
						resetTableModel(model);
						System.out.println("record count:"+model.getRowCount());
						break;
						
					case 1: //weight
						model = new MyTableModel(db.getResultset_customer_pcode(DATASRC,selectedpcode,panelyr.getYS(), panelyr.getYE(), filter, "tweight"));
						resetTableModel(model);
						System.out.println("record count:"+model.getRowCount());
						break;
				}
		}

		txfKeyword.setText("");
	}
	
	private void resetTableModel(MyTableModel m){
		atable.setModel(m);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(m);
		atable.setRowSorter(sorter);
		
	}
	
	private JComponent compTable(){
		JPanel tablepane = new JPanel(new BorderLayout());
		tablepane.add(lbltitle, BorderLayout.NORTH);
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		
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
					selectedcustcode = ((String)target.getValueAt(row, column)).trim();
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					u.debug (selectedcustcode);
				}
			}

		});	
		tablepane.add(scrollPane, BorderLayout.CENTER);
		
		return tablepane;
	}
	
	private JComponent compKeyword(){
		JPanel panel = new JPanel();
		JButton btnSearchKeyWord = new JButton("搜尋客戶關鍵字");
		JButton btnReset = new JButton("重置");
		txfKeyword.setColumns(10);
		panel.add(txfKeyword);
		panel.add(btnSearchKeyWord);
		panel.add(btnReset);

		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String k = txfKeyword.getText();
				//String filter = "custcode like '%" + k + "%' or cust_name like '%" + k + "%' and ";
				String filter = "custcode ='" + k + "' and ";
				model = null; System.gc(); //free the memory
				//model = new MyTableModel(db.getResultset_customer_all(filter));
				model = new MyTableModel(db.getResultset_customer_pcode(DATASRC,selectedpcode,panelyr.getYS(), panelyr.getYE(), filter, "tweight"));
				resetTableModel(model);
				System.out.println("record count:"+model.getRowCount());

			}
		};
		
		btnSearchKeyWord.addActionListener(actl);
		txfKeyword.addActionListener(actl);
		btnReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				selectedpcode = "";
				lbltitle.setText("");
				updateList();
			}			
		});
		
		
		
		return panel;
	}
	private JComponent compSort(){
		JPanel panel = new JPanel();
		JPanel topp = new JPanel();
		JPanel downp = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		cbxSort.setModel(new DefaultComboBoxModel<String>(new String[] 
				{
				"年銷售總額NT$", 
				"年銷售總重Kg"
				}));
		panel.add(topp);
		panel.add(downp);
		topp.add(chkSort);
		topp.add(cbxSort);

		return panel;
	}	

	public abstract void update();
}
