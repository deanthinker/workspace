import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

import javax.swing.SwingConstants;


public class PanelForecast_old extends JPanel {
	private int YEAR_RANGE = 10;
	private int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	private int YS = YE - YEAR_RANGE; // start year

	int thisyear = Calendar.getInstance().get(Calendar.YEAR);
	int nextyear = thisyear + 1;
	int lastyear = thisyear - 1;
	int last2year = thisyear - 2;
	int last3year = thisyear - 3;	
	
	public String pcode = "0000";
	private String pcodeTitle = "";
	private KYutil u = new KYutil();
	private KYdb db = new KYdb();
	
	private JTextField txf_keyword;
	private DefaultListModel<String> listVegeProd_model = new DefaultListModel<String>();
	public JList<String> listVegeProd = new JList<String>(listVegeProd_model);
	public JComboBox<String> cbx_class = new JComboBox<String>(db.getVegeCropVec());
	public JComboBox<String> cbx_year = new JComboBox<String>(u.create10yearVector());		
	final JCheckBox chkOrder = new JCheckBox("排序");
	final JComboBox<String> cbxSort = new JComboBox<String>();	
	
	JPanel chartpanel = new JPanel();
	JLabel lblimg = new JLabel("            ");
	
	public PanelForecast_old() {
		setLayout(new GridLayout(0, 1, 0, 0));
		//setLayout(new GridLayout(2, 0, 0, 0));
		//setBorder(new TitledBorder(null, "品種清單",TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JSplitPane splitpanel = new JSplitPane();
		JPanel settingpanel = new JPanel();
		JPanel filterpanel = new JPanel();
		JPanel imgpanel = new JPanel();
		
		JScrollPane scpVegeList = new JScrollPane(listVegeProd);
		settingpanel.setLayout(new GridLayout(3, 0, 0, 0));

		settingpanel.add(imgpanel);
		lblimg.setVerticalAlignment(SwingConstants.TOP);
		
		
		imgpanel.add(lblimg);
		
		settingpanel.add(filterpanel);
		settingpanel.add(scpVegeList);	
		
		splitpanel.setDividerLocation(350);
		splitpanel.add(settingpanel, "left");
		splitpanel.add(chartpanel, "right");
		add(splitpanel);
		

		filterpanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_0 = new JPanel();
		filterpanel.add(panel_0);
		
		JLabel label = new JLabel("顯示作物類別");
		panel_0.add(label);
		
		panel_0.add(cbx_class);
	
		cbx_class.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkOrder.setSelected(false);
				if (cbx_class.getSelectedIndex()==0) 
					db.fillList_vege_prod(listVegeProd,false, null);
				else
					db.fillList_vege_prod(listVegeProd,true, (String)cbx_class.getSelectedItem());
				
			}
		});	
		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chkOrder.isSelected()){
					cbxSort.setEnabled(true);
					showWithSort();
				}
				else{
					cbxSort.setEnabled(false);
					showNoSort();
					
				}
			}
		};	
		
		cbxSort.addActionListener(actl);
		cbxSort.setEnabled(false);
		cbxSort.setModel(new DefaultComboBoxModel<String>(new String[] 
				{
				thisyear+"年銷售NT$", 
				lastyear+"年銷售NT$", 
				thisyear+"-"+lastyear+"總銷售NT$", 
				thisyear+"-"+last2year+"總銷售NT$", 
				thisyear+"-"+last3year+"總銷售NT$",
				thisyear+"年銷售Kg", 
				lastyear+"年銷售Kg", 
				thisyear+"-"+lastyear+"總銷售Kg", 
				thisyear+"-"+last2year+"總銷售Kg", 
				thisyear+"-"+last3year+"總銷售Kg",
				}));
		
		
		chkOrder.addActionListener(actl);
		
		JPanel panel_1 = new JPanel();
		filterpanel.add(panel_1);
		
		
		panel_1.add(chkOrder);
		
		
		panel_1.add(cbxSort);
		
		JPanel panel_2 = new JPanel();
		filterpanel.add(panel_2);
		
		txf_keyword = new JTextField();
		txf_keyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.fillList_vege_prod_keyword(listVegeProd,txf_keyword.getText());
			}
		});
		panel_2.add(txf_keyword);
		txf_keyword.setColumns(10);
		
		JButton btn_search_kw = new JButton("搜尋關鍵字");
		btn_search_kw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.fillList_vege_prod_keyword(listVegeProd,txf_keyword.getText());
			}
		});
		
		panel_2.add(btn_search_kw);

		listVegeProd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scpVegeList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		db.fillList_vege_prod(listVegeProd,false, null);
		listVegeProd.setSelectedIndex(0);		
		listVegeProd.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;

				JList theList = (JList) e.getSource();
				if (theList.isSelectionEmpty()) {
				} else {
					int index = theList.getSelectedIndex();
					pcodeTitle = theList.getSelectedValue().toString();
					pcode = pcodeTitle.substring(0, 4); // get 4
					
					lblimg.setIcon(db.getImage_by_pcode(pcode));
					
					final XYDataset tdataset = getJFCdataset_forecast();
					final JFreeChart tchart = getJFCchart_forecast(tdataset);
					final ChartPanel jfcPanel = new ChartPanel(tchart);

					chartpanel.removeAll();
					chartpanel.revalidate(); // This removes the old chart
					chartpanel.setLayout(new BorderLayout());
					chartpanel.add(jfcPanel);
					chartpanel.repaint(); // This method makes the new chart
														// appear					
				}

			}
		});
		
		listVegeProd.repaint();
	}

	private void showWithSort(){
		String filter;
		if (cbx_class.getSelectedIndex() > 0)  
			filter = (String)cbx_class.getSelectedItem();
		else
			filter = null;
				
		cbxSort.setEnabled(true);
		
		switch (cbxSort.getSelectedIndex()){
			case 0: //sales,thisyear
				db.fillList_exp_prod_orderby_sales(listVegeProd, thisyear, nextyear, filter); break;
			case 1: //sales,lastyear
				db.fillList_exp_prod_orderby_sales(listVegeProd, lastyear, thisyear, filter); break;
			case 2: //sales,this + last
				db.fillList_exp_prod_orderby_sales(listVegeProd, lastyear, nextyear, filter); break;
			case 3: //sales,last2year
				db.fillList_exp_prod_orderby_sales(listVegeProd, last2year, nextyear, filter); break;
			case 4: //sales,last3year
				db.fillList_exp_prod_orderby_sales(listVegeProd, last3year, nextyear, filter); break;
	
			case 5: //weight,thisyear
				db.fillList_exp_prod_orderby_weight(listVegeProd, thisyear, nextyear, filter); break;
			case 6: //weight,lastyear
				db.fillList_exp_prod_orderby_weight(listVegeProd, lastyear, thisyear, filter); break;
			case 7: //weight,this + last
				db.fillList_exp_prod_orderby_weight(listVegeProd, lastyear, nextyear, filter); break;
			case 8: //weight,last2year
				db.fillList_exp_prod_orderby_weight(listVegeProd, last2year, nextyear, filter); break;
			case 9: //weight,last3year
				db.fillList_exp_prod_orderby_weight(listVegeProd, last3year, nextyear, filter); break;
		}

		
	}
	
	private void showNoSort(){
		if (cbx_class.getSelectedIndex()==0) 
			db.fillList_vege_prod(listVegeProd,false, null);
		else
			db.fillList_vege_prod(listVegeProd,true, (String)cbx_class.getSelectedItem());
	}
	
	
	private XYDataset getJFCdataset_forecast() {

		float previous_cost = 0;
		float previous_price = 0;
		float current_cost = 0;
		float current_price = 0;
		float buykg = 0;
		float soldkg = 0;
		long totalRecord = 0;

		XYSeries datasales = new XYSeries("國外部銷售");
		XYSeries dataproduction = new XYSeries("計畫生產");
		XYSeries databuyTW = new XYSeries("國內採購");
		XYSeries databuyForeign = new XYSeries("國外採購");
		XYSeries dataforecast = new XYSeries("預購");

		Vector<KeyValue_int_float> salesvec = db.getKeyValue_int_float_EXPSOLD(pcode,"soldkg");//將db資料存入vector 
		for (KeyValue_int_float kv : salesvec){datasales.add(kv.key, Math.round(kv.value));	} //資料轉入圖表data

		Vector<KeyValue_int_float> productionvec = db.getKeyValue_int_float_PRODUCTION(pcode);
		for (KeyValue_int_float kv : productionvec){dataproduction.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyTWvec = db.getKeyValue_int_float_BUYTW(pcode,"buykg");
		for (KeyValue_int_float kv : buyTWvec){databuyTW.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyForeignvec = db.getKeyValue_int_float_BUYFOREIGN(pcode,"buykg");
		for (KeyValue_int_float kv : buyForeignvec){databuyForeign.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> forecastvec = db.getKeyValue_int_float_EXPFORECAST(pcode);
		for (KeyValue_int_float kv : forecastvec){dataforecast.add(kv.key,  Math.round(kv.value));	}
	

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(datasales);
		dataset.addSeries(dataproduction);
		dataset.addSeries(databuyTW);
		dataset.addSeries(databuyForeign);
		dataset.addSeries(dataforecast);

		return dataset;
	}
	
	private JFreeChart getJFCchart_forecast(final XYDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setSmallFont(new Font("新細明體", Font.BOLD, 12));
		
		ChartFactory.setChartTheme(standardChartTheme);
		
		
		// create the chart...
		String title = "品種預購/生產/銷售/採購 比較圖\n" + pcodeTitle + "\n";
		final JFreeChart chart = ChartFactory.createXYLineChart(title, // chart
				"年度", // x axis label
				"公斤", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
		NumberAxis domain = (NumberAxis) plot.getDomainAxis(); // Y Axis
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		domain.setStandardTickUnits(ticks); // show integer (no decimal)

		range.setStandardTickUnits(ticks); // show integer (no decimal)
		//domain.setRange(YS, YE+1);
		domain.setTickUnit(new NumberTickUnit(1));

		plot.setBackgroundPaint(Color.lightGray);

		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));
		renderer.setSeriesItemLabelGenerator(0, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString();
			}
		});
		renderer.setSeriesItemLabelGenerator(1, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		renderer.setSeriesItemLabelGenerator(2, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});

		renderer.setSeriesItemLabelGenerator(3, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		renderer.setSeriesItemLabelGenerator(4, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
				
		renderer.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));
		renderer.setSeriesItemLabelGenerator(0, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString();
			}
		});
		renderer.setSeriesItemLabelGenerator(1, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		
		renderer.setSeriesItemLabelGenerator(2, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});

		renderer.setSeriesItemLabelGenerator(3, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		
		renderer.setSeriesItemLabelGenerator(4, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		
		
		renderer.setSeriesItemLabelsVisible(0, true);
		renderer.setSeriesItemLabelsVisible(1, true);
		renderer.setSeriesItemLabelsVisible(2, true);
		renderer.setSeriesItemLabelsVisible(3, true);
		renderer.setSeriesItemLabelsVisible(4, true);
		renderer.setSeriesStroke(0, new BasicStroke(2f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(1, new BasicStroke(2f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		renderer.setSeriesStroke(2, new BasicStroke(2f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(3, new BasicStroke(2f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(4, new BasicStroke(2f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		plot.setRenderer(renderer);

		return chart;
	}

}


