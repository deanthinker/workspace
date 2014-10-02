import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import javax.swing.JCheckBox;
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
import javax.swing.JButton;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;


public abstract class PanelCustSalesRecord extends JPanel {
	public String custcode;
	public String crop;
	public String selectedcrop = null;
	public String pcode;
	public String ys;
	public String ye;
	private String pcname;
	private MyTableModel model = null;
	private JTable atable = new JTable();
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	private final int EXPORT = 1;
	private final int DOMESTIC = 2;
	private int DATASRC = EXPORT;
	final JCheckBox chkGroup;

	private Connection con = db.getConnection();;
	public JButton btnShowSalesKG;
	
	private void group(boolean group){
		setCustcode(DATASRC, ys, ye, custcode, crop, group);
	}
	
	public PanelCustSalesRecord() {
		this.setLayout(new BorderLayout());
		JPanel tablepane = new JPanel(new GridLayout(1,0,0,0));
		JPanel titlepane = new JPanel(new FlowLayout());
		JLabel title = new JLabel("客戶期間交易紀錄 ");

		chkGroup = new JCheckBox("品種彙總");
		
		chkGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkGroup.isSelected()){
					group(true);
				}
				else{
					group(false);
				}
			}
		});				
		
		
		titlepane.add(title);
		titlepane.add(chkGroup);
		
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
				final JTable target = (JTable)e.getSource();
				final int row = target.getSelectedRow();
				String pcode = (String)target.getValueAt(row, 0); //SQL must set pcode as 1st field
				pcname = (String)target.getValueAt(row, 2); //SQL must set pcname as 3rd field
				
				if (e.getClickCount() == 1) //1:single click   2:double click
				{
					setShowSalesChartButton(pcode, pcname);
					
					//------------IMPORTANT----------------
					update(); //need to be implemented by inheritor!!!!!
					//-------------------------------------
				}
				else if (e.getClickCount() == 2){
					PanelCustSalesChart sc = new PanelCustSalesChart();
					sc.setVisible(true);
					sc.setParameter(DATASRC, custcode, pcode, ys, ye);
					u.debug(custcode + " "  );
				}
			}

		});	
		tablepane.add(scrollPane);
		
		this.add(titlepane, BorderLayout.NORTH);
		
		JButton btnPricelistExport = new JButton("Price List");
		btnPricelistExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				build_PriceListParam();
			}
		});
		titlepane.add(btnPricelistExport);
		
		btnShowSalesKG = new JButton("客戶品種歷年銷售量");
		btnShowSalesKG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PanelCustSalesChart sc = new PanelCustSalesChart();
				sc.setVisible(true);
				sc.setParameter(DATASRC, custcode, pcode, ys, ye);
				u.debug(custcode + " "  );
			}
		});
		btnShowSalesKG.setEnabled(false);
		titlepane.add(btnShowSalesKG);
				
		this.add(tablepane, BorderLayout.CENTER);
	}
	
	public void setShowSalesChartButton(String pcode, String pcname){
		u.debug (pcode);
		this.pcode = pcode;
		btnShowSalesKG.setText("客戶:" + custcode + " [" + pcode + ":" + pcname + "]歷年銷售量");
		btnShowSalesKG.setEnabled(true);
		
	}
	
	public void setGroupDisabled(){
		chkGroup.setSelected(false);
	}
	public void setCustcode(final int dbsrc, final String ys, final String ye, final String custcode, final String crop, final boolean group){
		this.ys = ys;
		this.ye = ye;
		this.custcode = custcode;
		this.crop = crop;
		this.DATASRC = dbsrc;
		
		this.model = new MyTableModel(db.getResultset_cust_sales_detail(dbsrc,ys,ye,custcode,crop, group));
		
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		u.debug("records:" + atable.getRowCount());
		//setColumnWidth();
		atable.repaint();
		
		btnShowSalesKG.setEnabled(false);
		btnShowSalesKG.setText("客戶品種歷年銷售量");
		
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
	private void build_PriceListParam() {
		String subtitle = "";
		String title = "";
		
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(14);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);

		TextColumnBuilder<String>  colcustcode  = col.column("客戶編號", "custcode", type.stringType()).setWidth(2);
		TextColumnBuilder<String>  coldest_country  = col.column("地區", "dest_country", type.stringType()).setWidth(3);
		TextColumnBuilder<String>  colinvoice_date  = col.column("訂單日", "invoice_date", type.stringType()).setWidth(3);
		TextColumnBuilder<String>  colpcode  = col.column("品編", "pcode", type.stringType()).setWidth(2);
		TextColumnBuilder<String>  collevel2  = col.column("作物", "level2", type.stringType()).setWidth(2);
		TextColumnBuilder<String>  colpcname  = col.column("中文品名", "pcname", type.stringType()).setWidth(3);
		TextColumnBuilder<String>  colpename  = col.column("英文品名", "pename", type.stringType()).setWidth(4);
		TextColumnBuilder<String>  coltrade_term  = col.column("條件", "trade_term", type.stringType()).setWidth(1);
		TextColumnBuilder<BigDecimal>  colup  = col.column("單價", "up", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(2);
		TextColumnBuilder<String>  colpack_size  = col.column("包裝尺寸", "pack_size", type.stringType()).setWidth(2);
		TextColumnBuilder<BigDecimal>  coltotal_pack  = col.column("數量", "total_pack", type.bigDecimalType())
				.setPattern("#,##0.0")
				.setWidth(2);
		TextColumnBuilder<String>  colpack_type  = col.column("包裝類型", "pack_type", type.stringType()).setWidth(2);

		title = "Price List: " + custcode + "(" + db.getCustmerName(DATASRC, custcode) + ")";
		
		subtitle = "銷售統計期間:" + ys +  " - " + ye;		
		
		//ColumnGroupBuilder itemGroup = grp.group(collevel2);
		//itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colcustcode, coldest_country, colinvoice_date, colpcode, collevel2, colpename, colpcname, coltrade_term, colup, colpack_size, coltotal_pack, colpack_type )
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  		//.sortBy(desc(colsort))
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_PriceList())
			  .ignorePagination() //good for exporting to EXCEL
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_PriceList() {
		Statement stat = null;
		ResultSet rs = null;
		DRDataSource dataSource = new DRDataSource("custcode", "dest_country", "invoice_date", "pcode", "level2", "pename", "pcname", "trade_term", "up", "pack_size", "total_pack", "pack_type" );
		String sql = "";
		if (DATASRC == EXPORT){
			sql = "select custcode, cust_name, " + 
					"LTRIM(substring(dest_country, LOCATE(',', dest_country)+1)) dest_country," +
					"invoice_date, pcode, level2, " +
					"LTRIM(substring(pename, LOCATE(',', pename)+1)) pename," +
					"pcname, trade_term,  (unit_price * toUSrate) up, pack_size, total_pack, pack_type " +
					"from market.sao430 where custcode like '" + custcode +"' and " +
					"year(invoice_date) >= " + ys + " and " +
					"year(invoice_date) >= " + ys + " and " +
					"sample = 'N' and class = '標準品' and unit_price > 0 " +
					"group by pcode, year(invoice_date), unit_price, pack_type " +
					"order by level2, pename, year(invoice_date) desc limit 9999";
		}
		else { //(DATASRC == DOMESTIC)
			sql = 
					"select a.custcode, b.name cust_name, region1 dest_country, invoice_date, pcode, level2, "+
					"LTRIM(substring(pename, LOCATE(',', pename)+1)) pename, pcname, '' trade_term,  (packprice) up, "+ 
					"packtype pack_size, actlqty total_pack, ' ' pack_type "+ 
					"from market.dom430 as a, market.dom130 as b "+
					"where a.custcode = b.custcode and " +
					"a.custcode like '" + custcode +"' and "+ 
					"year(invoice_date) >= " + ys + " and " +
					"year(invoice_date) <= " + ye + " and " +
					"sample = 'N' and class = '標準品' and packprice > 0 "+
					"group by pcode, year(invoice_date), packprice, packtype "+
					"order by level2, pename, year(invoice_date) desc limit 9999";
		}
		
		System.out.println(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {	 //loop throught all pcodes
				
				dataSource.add(
						rs.getString("custcode"), rs.getString("dest_country"), 
						rs.getString("invoice_date"), rs.getString("pcode"), rs.getString("level2"), 
						rs.getString("pename"), rs.getString("pcname"), rs.getString("trade_term"), 
						new BigDecimal(rs.getString("up")), rs.getString("pack_size"), new BigDecimal(rs.getString("total_pack")), 
						rs.getString("pack_type")	);
			}
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			u.debug("getJRDS_PriceList Exception :" + e.toString());
		}
		
		return dataSource;
	}	
	public abstract void update();
}
