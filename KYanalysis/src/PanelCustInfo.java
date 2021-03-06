import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.AttributedString;

import javax.swing.JButton;
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
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import java.math.BigDecimal;

import javax.swing.JSplitPane;


public abstract class PanelCustInfo extends JPanel {
	static final int EXPORT = 1;
	static final int DOMESTIC = 2;
	int DATASRC = EXPORT;
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	public String custcode;
	//public String crop;
	public String selectedcrop = null;
	public String pcode;
	public String ys;
	public String ye;
	private String totalSales = "";
	private String custSales = "";
	private String cropSales = "";
	private MyTableModel model = null;
	//private MyTableModel model = new MyTableModel(db.getResultset_CustSales("2013","2013","AE",null,"all"));
	private JLabel lbltitle = new JLabel();
	private JLabel lblcust = new JLabel();
	private JLabel lblname = new JLabel();
	private JLabel lblcountry = new JLabel();
	private JLabel lblregion = new JLabel();
	private JLabel lblsales = new JLabel();
	private JTable atable = new JTable();
	JPanel panelcustSales = new JPanel();
	JPanel paneltotalSales = new JPanel();
	private final JSplitPane splitPaneChart = new JSplitPane();
	private final JSplitPane splitPaneMain = new JSplitPane();
	private JButton btnVarietyProduction = new JButton("[所有作物]預購/銷售/生產/達成率分析");
	
	public static void main(String[] args) {

		try {
			JFrame frame = new JFrame();
			PanelCustInfo pcust = new PanelCustInfo(EXPORT){
				public void update(){
					
				}
			};
						
			frame.getContentPane().add(pcust);
			frame.setVisible(true);
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//pcust.updateTable("2013","2013","TK",null,"all"); //test1
			pcust.setCustcode(EXPORT,"2013","2013","KYD",null,"all"); //test2
			pcust.refreshCustSalesChart();
			pcust.refreshCustVarietySalesChart();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PanelCustInfo(int dbsrc) {
		DATASRC = dbsrc;
		setLayout(new GridLayout(1,3,0,0));
	
		
		splitPaneChart.setLeftComponent(panelcustSales);
		splitPaneChart.setRightComponent(paneltotalSales);
		splitPaneChart.setDividerLocation(280);
		
		splitPaneMain.setLeftComponent(compSalesCropTable());
		splitPaneMain.setRightComponent(splitPaneChart);
		splitPaneMain.setDividerLocation(280);
		add(splitPaneMain);

	}
	

	private void refreshCustSalesChart(){
		final DefaultPieDataset dataset_custSales = getJFCdataset_custSales();
		JFreeChart custSalesPieChart = getJFCchart_custSales(dataset_custSales);
		ChartPanel custSalesPanel = new ChartPanel(custSalesPieChart);
		
		panelcustSales.removeAll();
		panelcustSales.setLayout(new BorderLayout());
		panelcustSales.add(custSalesPanel);
		panelcustSales.validate();
		panelcustSales.repaint();

	}

	private void refreshCustVarietySalesChart(){

		final DefaultPieDataset dataset_VarietySales = getJFCdataset_custVarietySales();
		JFreeChart custVarietySalesPieChart = getJFCchart_custVarietySales(dataset_VarietySales);
		ChartPanel custVarietySalesPanel = new ChartPanel(custVarietySalesPieChart);

		paneltotalSales.removeAll();
		paneltotalSales.setLayout(new BorderLayout());
		paneltotalSales.add(custVarietySalesPanel);
		paneltotalSales.validate();
		paneltotalSales.repaint();		
	}
	
	private JFreeChart getJFCchart_custSales(PieDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 13));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 13));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 13));
		ChartFactory.setChartTheme(standardChartTheme);
		String title = "";
		BigDecimal wan = new BigDecimal(10000);
		BigDecimal cs = new BigDecimal(custSales).divide(wan);
		
		title = ys + " - " + ye + "購買占比\n 合計 NT$" + cs.toString() + "萬元";

		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				false, // include legend
				true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();

        plot.setDirection(Rotation.ANTICLOCKWISE);
        plot.setLabelGenerator(new PieChartLabel());   
        
		plot.setCircular(false);
		plot.setLabelGap(0.05);
		
		plot.setNoDataMessage("No data available");

		return chart;
	}

	private JFreeChart getJFCchart_custVarietySales(PieDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 13));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 13));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 13));
		ChartFactory.setChartTheme(standardChartTheme);
		String title = "";
		
		title = selectedcrop+" 品種占比\n 合計 NT$" + cropSales + "萬元";

		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				false, // include legend
				true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();

        plot.setDirection(Rotation.ANTICLOCKWISE);
        plot.setLabelGenerator(new PieChartLabel());   
        
		plot.setCircular(false);
		plot.setLabelGap(0.05);
		
		plot.setNoDataMessage("No data available");

		return chart;
	}

	private DefaultPieDataset getJFCdataset_custVarietySales() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		String cropname = "";
		float cropsales = 0;
		if (selectedcrop == null)  return dataset;
		
		ResultSet rs = db.getResultset_CustVarietySalesByCrop(DATASRC, ys, ye, custcode, selectedcrop);
		

		try {
			while(rs.next()){
				cropname = rs.getString("pcname");
				cropsales = Float.valueOf(rs.getString("ntsales").replace(",", ""));
				dataset.setValue(cropname, cropsales);
			}
			rs.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataset;
	}
	
	private DefaultPieDataset getJFCdataset_custSales() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		String cropname = "";
		float cropsales = 0;
		
		for (int r=0;r<atable.getRowCount();r++){
			cropname = (String)atable.getValueAt(r, 0);
			cropsales = Float.valueOf(  ((String)atable.getValueAt(r, 2)).replace(",","")   );
			dataset.setValue(cropname , cropsales);
			//u.debug("row"+r+": "+(String)atable.getValueAt(r, 0)+"\t"+Float.valueOf((String)atable.getValueAt(r, 2)));
		}
		
		return dataset;
	}
	
	private JComponent comeTotalSalesChart(){
		JPanel panel = new JPanel();
		
		return panel;		
	}

	
	private JComponent compSalesCropTable(){
		
		JPanel tablepane = new JPanel(new BorderLayout());
		JPanel titlepane = new JPanel(new GridLayout(7,1,0,0));
		titlepane.add(lbltitle);
		titlepane.add(lblcust);
		titlepane.add(lblname);
		titlepane.add(lblcountry);
		titlepane.add(lblregion);
		titlepane.add(lblsales);
		titlepane.add(btnVarietyProduction);
		
		btnVarietyProduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PanelCustVarietyProduction custvarprodFrame = new PanelCustVarietyProduction(custcode, selectedcrop, ys, ye, "ussales", new Float(0.8));
				custvarprodFrame.setVisible(true);
				
			}
		});
		btnVarietyProduction.setEnabled(false);
		
		tablepane.add(titlepane, BorderLayout.NORTH);
		
		/*
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		*/
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)atable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment( JLabel.RIGHT );
		
		JScrollPane scrollPane = new JScrollPane(atable);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		atable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(final MouseEvent e)
			{
				final JTable target = (JTable)e.getSource();
				final int row = target.getSelectedRow();
				//final int column = target.getSelectedColumn();
				
				if (e.getClickCount() == 1)
				{
					selectedcrop = (String)target.getValueAt(row, 0);
					
					cropSales = (String)target.getValueAt(row, 2);
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					u.debug (selectedcrop);
					btnVarietyProduction.setText("["+selectedcrop+ "] 預購/銷售/生產/達成率分析");
					refreshCustVarietySalesChart();
				}
				else if (e.getClickCount() == 2){
					//PanelCustCropSalesChart sc = new PanelCustCropSalesChart();
					PanelCustCropSalesStackedChart sc = new PanelCustCropSalesStackedChart();
					sc.setVisible(true);
					sc.setParameter(DATASRC, custcode, selectedcrop, ys, ye);
					u.debug(selectedcrop + " "  );					
					
				}
				
				
			}

		});	
		tablepane.add(scrollPane, BorderLayout.CENTER);
		

		
		return tablepane;
	}


	public void setCustcode(int dbsrc, String ys, String ye, String custcode, String pcode, String crop){
		this.ys = ys;
		this.ye = ye;
		this.custcode = custcode;
		this.pcode = pcode;
		this.selectedcrop = crop;
		
		this.model = new MyTableModel(db.getResultset_CustSales(dbsrc, ys,ye,custcode,null,crop));
		
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		setColumnWidth();
		
		btnVarietyProduction.setText("[所有作物]預購/銷售/生產/達成率分析");
		btnVarietyProduction.setEnabled(true);
		
		this.totalSales = db.getYearRangeSales(dbsrc, ys,ye);
		this.custSales = db.getYearRangeSalesByCust(dbsrc, ys,ye,custcode);
		setLabel();
		paneltotalSales.removeAll(); //clean out the chart
		paneltotalSales.repaint();
	}

	private void setLabel(){
		String percent ="";
		float tmpcs = 0;
		float tmpts = 0;
		
		BigDecimal ts = new BigDecimal(totalSales);
		tmpts = ts.divide(new BigDecimal("10000"),1).floatValue();
		BigDecimal cs = new BigDecimal(custSales);
		tmpcs = cs.divide(new BigDecimal("10000"),1).floatValue();
		
		percent = u.f1decimal(tmpcs/tmpts * 100);
		
		lbltitle.setText("KY總營收:NT$" + tmpts + "萬    統計期間:"+ys+"~"+ye);
		lblcust.setText("客戶代號:" + custcode  );
		lblsales.setText("銷售NT$"+  tmpcs+"萬    (佔總營收"+ percent+ "%)");
		db.setCustInfoLabel(DATASRC,custcode, lblcountry, lblregion, lblname);
		refreshCustSalesChart();
	}
	
	private void setColumnWidth(){
		TableColumn column = null;
		for (int c=0;c<atable.getColumnCount();c++){
			column = atable.getColumnModel().getColumn(c);
			column.setPreferredWidth(100);
		}
		
	}
	static class PieChartLabel implements PieSectionLabelGenerator {
		private int percent = 1;
		
		private float getTotal(final PieDataset dataset){
			float total =0;
			for (int i=0;i<dataset.getItemCount();i++){
				total = total +  dataset.getValue(i).floatValue();
			}
			return total;
		}
		
		public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
			String result = null;
			float total = getTotal(dataset);
			String title = key.toString();
			String landuse = String.format("%.1f", dataset.getValue(title).floatValue());
			String percent = String.format("%.1f", dataset.getValue(title).floatValue() / total * 100);
			
			if (dataset != null) {
				result = title + "(" + landuse + "萬)" + "("+ percent +"%)"; 
			}

			return result;
		}

		public AttributedString generateAttributedSectionLabel(PieDataset piedataset, Comparable comparable) {
			AttributedString attributedstring = null;
			String s = comparable.toString();
			String s1 = s + " : " + String.valueOf(piedataset.getValue(comparable));
			attributedstring = new AttributedString(s1);
			attributedstring.addAttribute(TextAttribute.WEIGHT,	TextAttribute.WEIGHT_BOLD, 0, s.length() - 1);
			return attributedstring;
		}

		PieChartLabel() {

		}
	}
	
	public abstract void update();

}
