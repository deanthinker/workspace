import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.AttributedString;

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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.ibm.icu.math.BigDecimal;


public abstract class PanelCustInfo extends JPanel {
	static final int EXPORT = 1;
	static final int DOMESTIC = 2;
	int DATASRC = EXPORT;
	private KYdb db = new KYdb();
	private KYutil u = new KYutil();
	public String custcode;
	public String crop;
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
	private JLabel lblsales = new JLabel();
	private JTable atable = new JTable();
	JPanel panelcustSales = new JPanel();
	JPanel paneltotalSales = new JPanel();
	
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
		add(compSalesCropTable());
		add(panelcustSales);
		add(paneltotalSales);
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
		
		ResultSet rs = db.getResultset_CustVarietySales(DATASRC, ys, ye, custcode, selectedcrop);
		

		try {
			while(rs.next()){
				cropname = rs.getString("pcname");
				cropsales = Float.valueOf(rs.getString("sales").replace(",", ""));
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
		JPanel titlepane = new JPanel(new GridLayout(2,1,0,0));
		titlepane.add(lbltitle);
		titlepane.add(lblsales);
		
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
				if (e.getClickCount() == 1)
				{
					final JTable target = (JTable)e.getSource();
					final int row = target.getSelectedRow();
					//final int column = target.getSelectedColumn();
					selectedcrop = (String)target.getValueAt(row, 0);
					cropSales = (String)target.getValueAt(row, 2);
					//------------IMPORTANT----------------
					update(); //need to be implemented!!!!!
					//-------------------------------------
					u.debug (selectedcrop);
					refreshCustVarietySalesChart();
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
		this.crop = crop;
		
		this.model = new MyTableModel(db.getResultset_CustSales(dbsrc, ys,ye,custcode,null,crop));
		
		atable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		atable.setModel(model);	
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(model);
		atable.setRowSorter(sorter);
		setColumnWidth();
		
		this.totalSales = db.getYearRangeSales(dbsrc, ys,ye);
		this.custSales = db.getYearRangeSalesByCust(dbsrc, ys,ye,custcode);
		setTitle();
		paneltotalSales.removeAll(); //clean out the chart
		paneltotalSales.repaint();
	}

	private void setTitle(){
		String percent ="";
		float tmpcs = 0;
		float tmpts = 0;
		
		BigDecimal ts = new BigDecimal(totalSales);
		tmpts = ts.divide(new BigDecimal("10000"),1).floatValue();
		BigDecimal cs = new BigDecimal(custSales);
		tmpcs = cs.divide(new BigDecimal("10000"),1).floatValue();
		
		percent = u.f1decimal(tmpcs/tmpts * 100);
		
		lbltitle.setText("客戶代號:" + custcode + "    統計期間:"+ys+"~"+ye);
		lblsales.setText("銷售NT$"+  tmpcs+"萬 \t(佔營收"+ percent+ "%)");
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
