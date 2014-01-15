import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mysql.jdbc.Statement;

import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.*;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;


public class PanelProductAnalysis extends JPanel {

	public int YEAR_RANGE = 10;
	public int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	public int YS = YE - YEAR_RANGE; // start year
	
	public final int MODE_COSTPRICE = 1;
	public final int MODE_COSTPRICETW = 2;
	public final int MODE_EXPGP = 3;
	//public final int MODE_DOMGP = 4;  //自產成本無法計算 因此沒辦法推算GP
	public final int MODE_VOLGP = 5;
	public final int MODE_VOLPRICE = 6;
	public final int MODE_3YRDIST = 7;
	private int chartMode = 1; 
	
	JPanel chartpanel = new JPanel();
	KYutil u = new KYutil();
	KYdb db = new KYdb();
	
	PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.NOIMAGE){
		private static final long serialVersionUID = 1L;
		public void update() {
			JFreeChart tchart = null;
			ChartPanel jfcPanel = null;

			
			switch (chartMode){
			case MODE_COSTPRICE: tchart = getChart_costpricechange(); 	break;
			case MODE_COSTPRICETW: tchart = getChart_costpricechange();	break;
			case MODE_EXPGP: tchart = getChart_costpriceGPchange(); break;
			//case MODE_DOMGP: tchart = getChart_DOMGPchange(); break;
			
			case MODE_VOLGP: tchart = getChart_weightGPchange(); break;
			case MODE_3YRDIST:
				tchart = getJFCchart_3yrMonthlySalesDistribution(getJFCdataset_3yrMonthlySalesDistribution()); break;
				
			default:
				break;
							
			}
			
			 jfcPanel = new ChartPanel(tchart, true, true, true, false, true);

			
			ChartMouseListener cl = new ChartMouseListener() {
			    public void chartMouseClicked(ChartMouseEvent chartmouseevent)
			    {
			    	String title;
			    	String year;
			    	int series;
			    	ChartEntity entity = chartmouseevent.getEntity();
			    	if(entity instanceof XYItemEntity){
			           XYItemEntity ce = (XYItemEntity) entity;
			           year = String.valueOf(ce.getDataset().getX(ce.getSeriesIndex(),  ce.getItem()).intValue());
			           series = ce.getSeriesIndex();
			            u.debug("Item    " + ce.getItem());
			            u.debug("Series  " + ce.getSeriesIndex());
			            u.debug("X Value " + ce.getDataset().getX(ce.getSeriesIndex(),  ce.getItem()));
			            u.debug("Y Value " + ce.getDataset().getY(ce.getSeriesIndex(),  ce.getItem()));
			            u.debug("pcode:"+selectedpcode);
			            
			            switch (series){
			            	case 1:
		            			title = year + "年度 " + selectedpcode + "xxxxxxx";
			            		u.debug("case 1");
			            		break;
	
			            }

			    	}
			    }

			    public void chartMouseMoved(ChartMouseEvent chartmouseevent)
			    {

			    }
			};						

			jfcPanel.addChartMouseListener(cl);
			
			chartpanel.removeAll();
			chartpanel.revalidate(); // This removes the old chart
			chartpanel.setLayout(new BorderLayout());
			chartpanel.add(jfcPanel);
			chartpanel.repaint(); // This method makes the new chart
			
		}
	};		
	
	public PanelProductAnalysis() {
		this.setLayout(new GridLayout(1, 1, 0, 0));
		JSplitPane splitpanel = new JSplitPane();
		JPanel settingpanel = new JPanel(new GridLayout(2, 1, 0, 0));
		settingpanel.setBorder(new TitledBorder(null, "",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel options = new JPanel(new GridLayout(4, 2, 0, 0));
		addRadioButtons(options);
		
		settingpanel.add(options);
		settingpanel.add(panelSearchPcode);
		
		splitpanel.setDividerLocation(350);
		splitpanel.add(settingpanel, "left");
		splitpanel.add(chartpanel, "right");
		
		this.add(splitpanel);
	}
	
	private void addRadioButtons(JPanel panel){
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton radCOSTPRICE = new JRadioButton("進價與售價");
		radCOSTPRICE.setActionCommand("COSTPRICE");
		JRadioButton radCOSTPRICETW = new JRadioButton("進價與售價(含台灣售價)");
		radCOSTPRICETW.setActionCommand("COSTPRICETW");
		JRadioButton radEXPGP = new JRadioButton("出口進價與售價(毛利)");
		radEXPGP.setActionCommand("EXPGP");
		JRadioButton radVOLGP = new JRadioButton("銷售量與毛利");
		radVOLGP.setActionCommand("VOLGP");
		JRadioButton rad3YRDIST = new JRadioButton("三年銷售旺季分佈");
		rad3YRDIST.setActionCommand("3YRDIST");
		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()){
					case "COSTPRICE": 	chartMode = MODE_COSTPRICE; break;
					case "COSTPRICETW":	chartMode = MODE_COSTPRICETW;break;
					case "EXPGP":		chartMode = MODE_EXPGP;		break;
					case "VOLGP":		chartMode = MODE_VOLGP;		break;					
					case "3YRDIST":		chartMode = MODE_3YRDIST;	break;				
				}
				
				panelSearchPcode.update();
			}
		};
		
		radCOSTPRICE.addActionListener(actl);
		radCOSTPRICETW.addActionListener(actl);
		radEXPGP.addActionListener(actl);
		radVOLGP.addActionListener(actl);
		rad3YRDIST.addActionListener(actl);
		
		group.add(radCOSTPRICE);
		group.add(radCOSTPRICETW);
		group.add(radEXPGP);
		group.add(radVOLGP);
		group.add(rad3YRDIST);
		
		radCOSTPRICE.setSelected(true);
		
		panel.add(radCOSTPRICE);
		panel.add(radCOSTPRICETW);
		panel.add(radEXPGP);
		panel.add(radVOLGP);
		panel.add(rad3YRDIST);
		
	}

	
	private JFreeChart getChart_costpricechange() {
				
		XYSeries dataBUYFOREIGNCOST = new XYSeries("國外生產價");
		XYSeries dataBUYTWCOST = new XYSeries("台灣生產價");
		XYSeries dataEXPSOLDPRICE = new XYSeries("國外售價");
		XYSeries dataDOMSOLDPRICE = new XYSeries("台灣售價");

		Vector<KeyValue_int_float> vecBUYFOREIGNCOST = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"avgntcost");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYFOREIGNCOST){dataBUYFOREIGNCOST.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		
		//部分品種是台灣採種 例如0786
		Vector<KeyValue_int_float> vecBUYTWCOST = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"avgntcost");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYTWCOST){dataBUYTWCOST.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		

		Vector<KeyValue_int_float> vecEXPSOLDPRICE = db.getKeyValue_int_float_EXPSOLD(panelSearchPcode.selectedpcode,"avgntprice");//將db資料存入vector 
		for (KeyValue_int_float kv : vecEXPSOLDPRICE){dataEXPSOLDPRICE.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data				

		if (chartMode == MODE_COSTPRICETW){
			Vector<KeyValue_int_float> vecDOMSOLDPRICE = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode,"avgntprice");//將db資料存入vector 
			for (KeyValue_int_float kv : vecDOMSOLDPRICE){dataDOMSOLDPRICE.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data				
		}
		
		final XYSeriesCollection xyseriescollection1 = new XYSeriesCollection();
		xyseriescollection1.addSeries(dataBUYFOREIGNCOST);
		xyseriescollection1.addSeries(dataEXPSOLDPRICE);
		if (chartMode == MODE_COSTPRICETW){
			xyseriescollection1.addSeries(dataBUYTWCOST);
			xyseriescollection1.addSeries(dataDOMSOLDPRICE);
		}
		
		XYSeries dataBUYFOREIGNKG = new XYSeries("國外採種");
		XYSeries dataBUYTWKG = new XYSeries("台灣採種");
		XYSeries dataEXPSOLDKG = new XYSeries("外銷重量");
		XYSeries dataDOMSOLDKG = new XYSeries("內銷重量");
		
		Vector<KeyValue_int_float> vecBUYFOREIGNKG = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYFOREIGNKG){dataBUYFOREIGNKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		
		Vector<KeyValue_int_float> vecBUYTWKG = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYTWKG){dataBUYTWKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		
		Vector<KeyValue_int_float> vecEXPSOLDKG = db.getKeyValue_int_float_EXPSOLD(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecEXPSOLDKG){dataEXPSOLDKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data

		Vector<KeyValue_int_float> vecDOMSOLDKG = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecDOMSOLDKG){dataDOMSOLDKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		
		final XYSeriesCollection xyseriescollection2 = new XYSeriesCollection();
		
		xyseriescollection2.addSeries(dataBUYFOREIGNKG);
		xyseriescollection2.addSeries(dataBUYTWKG);
		xyseriescollection2.addSeries(dataEXPSOLDKG);
		xyseriescollection2.addSeries(dataDOMSOLDKG);

		
		XYDataset xydataset1 = xyseriescollection1;
		XYDataset xydataset2 = xyseriescollection2;
		
		XYLineAndShapeRenderer ren1 = new XYLineAndShapeRenderer();
		ren1.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));
        NumberAxis numberaxis1 = new NumberAxis("NT$");
        numberaxis1.setAutoRangeIncludesZero(false);
        XYPlot xyplot1 = new XYPlot(xydataset1, null, numberaxis1, ren1);
        xyplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyplot1.setBackgroundPaint(Color.lightGray);
        xyplot1.setDomainGridlinePaint(Color.white);
        xyplot1.setRangeGridlinePaint(Color.white);
        
        for(int x =0; x < 4 ; x++){
	        ren1.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren1.setSeriesItemLabelsVisible(x, true);
	        ren1.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		} 
        
        
        XYLineAndShapeRenderer ren2 = new XYLineAndShapeRenderer();
        ren2.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));       
        NumberAxis numberaxis2 = new NumberAxis("KG");
        numberaxis2.setAutoRangeIncludesZero(false);
        XYPlot xyplot2 = new XYPlot(xydataset2, null, numberaxis2, ren2);
        xyplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        xyplot2.setBackgroundPaint(Color.lightGray);
        xyplot2.setDomainGridlinePaint(Color.white);
        xyplot2.setRangeGridlinePaint(Color.white);
                
		for(int x =0; x < 4 ; x++){
	        ren2.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren2.setSeriesItemLabelsVisible(x, true);
	        ren2.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		}
        
        NumberAxis axis = new NumberAxis("Year");
        axis.setAutoRangeIncludesZero(false);
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(axis);
        plot.setGap(1D);
        plot.add(xyplot1, 2);//66% of page
        plot.add(xyplot2, 1); //33% of page
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("歷年 售價  & 進價成本 變化 ", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
           
		
        return chart;
	}
	
	
	private JFreeChart getChart_costpriceGPchange() {
		
		XYSeries dataBUYFOREIGNCOST = new XYSeries("國外生產價");
		XYSeries dataBUYTWCOST = new XYSeries("台灣生產價");
		XYSeries dataEXPSOLDPRICE = new XYSeries("國外售價");
		XYSeries dataDOMSOLDPRICE = new XYSeries("台灣售價");

		Vector<KeyValue_int_float> vecBUYFOREIGNCOST = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"avgntcost");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYFOREIGNCOST){dataBUYFOREIGNCOST.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		
		//部分品種是台灣採種 例如0786
		Vector<KeyValue_int_float> vecBUYTWCOST = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"avgntcost");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYTWCOST){dataBUYTWCOST.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		

		Vector<KeyValue_int_float> vecEXPSOLDPRICE = db.getKeyValue_int_float_EXPSOLD(panelSearchPcode.selectedpcode,"avgntprice");//將db資料存入vector 
		for (KeyValue_int_float kv : vecEXPSOLDPRICE){dataEXPSOLDPRICE.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data				

		Vector<KeyValue_int_float> vecDOMSOLDPRICE = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode,"avgntprice");//將db資料存入vector 
		for (KeyValue_int_float kv : vecDOMSOLDPRICE){dataDOMSOLDPRICE.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data				

		
		final XYSeriesCollection xyseriescollection1 = new XYSeriesCollection();
		xyseriescollection1.addSeries(dataBUYFOREIGNCOST);
		xyseriescollection1.addSeries(dataEXPSOLDPRICE);
		xyseriescollection1.addSeries(dataBUYTWCOST);
		xyseriescollection1.addSeries(dataDOMSOLDPRICE);
		
		
		XYSeries dataEXPGP = new XYSeries("外銷毛利");
		XYSeries dataINTGP = new XYSeries("綜合毛利");


		Vector<KeyValue_int_float> vecEXPGP = db.getKeyValue_int_float_EXPGP(panelSearchPcode.selectedpcode);//將db資料存入vector
		Vector<KeyValue_int_float> vecINTGP = db.getKeyValue_int_float_INTGP(panelSearchPcode.selectedpcode);//將db資料存入vector
		//**********When it's produced in TW, we need to calculate GP with domestic purchase price*********
		//tbd //combine EXP and DOM cost/price to calculate integrated gp 
		for (KeyValue_int_float kv : vecEXPGP){dataEXPGP.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		for (KeyValue_int_float kv : vecINTGP){dataINTGP.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data			
		
		
		
		final XYSeriesCollection xyseriescollection2 = new XYSeriesCollection();
		xyseriescollection2.addSeries(dataEXPGP);
		xyseriescollection2.addSeries(dataINTGP);
		
		XYDataset xydataset1 = xyseriescollection1;
		XYDataset xydataset2 = xyseriescollection2;
		
		XYLineAndShapeRenderer ren1 = new XYLineAndShapeRenderer();
		ren1.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));   		
        NumberAxis numberaxis1 = new NumberAxis("NT$");
        numberaxis1.setAutoRangeIncludesZero(false);
        XYPlot xyplot1 = new XYPlot(xydataset1, null, numberaxis1, ren1);
        xyplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyplot1.setBackgroundPaint(Color.lightGray);
        xyplot1.setDomainGridlinePaint(Color.white);
        xyplot1.setRangeGridlinePaint(Color.white);
        
        for(int x =0; x < 4 ; x++){
	        ren1.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren1.setSeriesItemLabelsVisible(x, true);
	        ren1.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		} 
        
        
        XYLineAndShapeRenderer ren2 = new XYLineAndShapeRenderer();
		ren2.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));  
        NumberAxis numberaxis2 = new NumberAxis("毛利%");
        numberaxis2.setAutoRangeIncludesZero(false);
        XYPlot xyplot2 = new XYPlot(xydataset2, null, numberaxis2, ren2);
        xyplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        xyplot2.setBackgroundPaint(Color.lightGray);
        xyplot2.setDomainGridlinePaint(Color.white);
        xyplot2.setRangeGridlinePaint(Color.white);
                
		for(int x =0; x < 4 ; x++){
	        ren2.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren2.setSeriesItemLabelsVisible(x, true);
	        ren2.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		}
        
        NumberAxis axis = new NumberAxis("Year");
        axis.setAutoRangeIncludesZero(false);
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(axis);
        plot.setGap(1D);
        plot.add(xyplot1, 2);//66% of page
        plot.add(xyplot2, 1); //33% of page
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("歷年外銷毛利、 售價  & 進價成本  變化 ", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
           
		
        return chart;
	}

	private JFreeChart getJFCchart_3yrMonthlySalesDistribution(final XYDataset dataset){
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setSmallFont(new Font("新細明體", Font.BOLD, 12));
	
		ChartFactory.setChartTheme(standardChartTheme);
		
		
		// create the chart...
		String title = "2年銷售分佈圖\n" + panelSearchPcode.title + "\n";
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // chart
				"年月", // x axis label
				"公斤", // y axis label
				dataset, // data
				true, // 
				true, // 
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

        XYPlot xyplot = (XYPlot)chart.getPlot();
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));
		renderer.setSeriesItemLabelGenerator(0, new XYItemLabelGenerator() { // cost
					public String generateLabel(XYDataset dataset, int series,
							int item) {
						return dataset.getY(series, item).toString();
					}
				});
       
        /*
        if(xyitemrenderer instanceof XYLineAndShapeRenderer)
        {
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyitemrenderer;
            xylineandshaperenderer.setShapesVisible(true);
            xylineandshaperenderer.setShapesFilled(true);
            xylineandshaperenderer.setItemLabelsVisible(true);
        }
        */
        PeriodAxis periodaxis = new PeriodAxis("Date");
        periodaxis.setAutoRangeTimePeriodClass(org.jfree.data.time.Year.class);
        periodaxis.setMajorTickTimePeriodClass(org.jfree.data.time.Year.class);
        periodaxis.setTickMarkOutsideLength(0.0F);
        PeriodAxisLabelInfo aperiodaxislabelinfo[] = new PeriodAxisLabelInfo[2];
        aperiodaxislabelinfo[0] = new PeriodAxisLabelInfo(org.jfree.data.time.Month.class, new SimpleDateFormat("MMM"));
        aperiodaxislabelinfo[1] = new PeriodAxisLabelInfo(org.jfree.data.time.Year.class, new SimpleDateFormat("yyyy"));
        periodaxis.setLabelInfo(aperiodaxislabelinfo);
        xyplot.setDomainAxis(periodaxis);
		
		renderer.setSeriesItemLabelsVisible(0, true);
		
		renderer.setSeriesStroke(0, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		
		xyplot.setRenderer(renderer);		
		return chart;
	}
	
	private XYDataset getJFCdataset_3yrMonthlySalesDistribution() {
		TimeSeries timeseries = new TimeSeries("銷售Kg");
		//TimeSeries timeseries1;
		
		Statement stat = null;
		ResultSet rs = null;		
	
		String sql;
		sql = "Select pcode, level2, pcname, invoice_date, day(invoice_date) as d, month(invoice_date) as m, year(invoice_date) as y, sum(total_weight) as tweight from sao430 where " 
				+ " year(invoice_date) <= "+ YE + " and " 	
				+ " year(invoice_date) >= " + (YE-2) +" and "
				+ " pcode = '" + panelSearchPcode.selectedpcode + "'" 
				+ " group by invoice_date";

		try {
			stat =  (Statement)db.getConnection().createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {	
				int tweight = rs.getInt("tweight");
				int d = rs.getInt("d");
				int m = rs.getInt("m");
				int y = rs.getInt("y");
				
				timeseries.add(new Day(d, m, y), tweight);
			}
			rs.close();
			stat.close();

		} catch (SQLException e) {
			u.debug("Select 3yrMonthlySalesDistribution Exception :" + e.toString());
		}		
		
		//timeseries1 = MovingAverage.createMovingAverage(timeseries, "30 day moving average", 30, 30);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeseries);
        
		return timeseriescollection;
		
	}
	
	private JFreeChart getChart_weightGPchange() {
		
		XYSeries dataBUYFOREIGNKG = new XYSeries("國外採種");
		XYSeries dataBUYTWKG = new XYSeries("台灣採種");
		XYSeries dataEXPSOLDKG = new XYSeries("外銷重量");
		XYSeries dataDOMSOLDKG = new XYSeries("內銷重量");
		
		Vector<KeyValue_int_float> vecBUYFOREIGNKG = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYFOREIGNKG){dataBUYFOREIGNKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data		
		
		Vector<KeyValue_int_float> vecBUYTWKG = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecBUYTWKG){dataBUYTWKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		
		Vector<KeyValue_int_float> vecEXPSOLDKG = db.getKeyValue_int_float_EXPSOLD(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecEXPSOLDKG){dataEXPSOLDKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data

		Vector<KeyValue_int_float> vecDOMSOLDKG = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode,"sumqty");//將db資料存入vector 
		for (KeyValue_int_float kv : vecDOMSOLDKG){dataDOMSOLDKG.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		
		final XYSeriesCollection xyseriescollection1 = new XYSeriesCollection();
		
		xyseriescollection1.addSeries(dataBUYFOREIGNKG);
		xyseriescollection1.addSeries(dataBUYTWKG);
		xyseriescollection1.addSeries(dataEXPSOLDKG);
		xyseriescollection1.addSeries(dataDOMSOLDKG);
		
		
		XYSeries dataEXPGP = new XYSeries("外銷毛利");
		XYSeries dataINTGP = new XYSeries("綜合毛利");


		Vector<KeyValue_int_float> vecEXPGP = db.getKeyValue_int_float_EXPGP(panelSearchPcode.selectedpcode);//將db資料存入vector
		Vector<KeyValue_int_float> vecINTGP = db.getKeyValue_int_float_INTGP(panelSearchPcode.selectedpcode);//將db資料存入vector
		//**********When it's produced in TW, we need to calculate GP with domestic purchase price*********
		//tbd //combine EXP and DOM cost/price to calculate integrated gp 
		for (KeyValue_int_float kv : vecEXPGP){dataEXPGP.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data
		for (KeyValue_int_float kv : vecINTGP){dataINTGP.add((int)kv.key, Math.round(kv.value));	} //資料轉入圖表data			
		
		
		
		final XYSeriesCollection xyseriescollection2 = new XYSeriesCollection();
		xyseriescollection2.addSeries(dataEXPGP);
		xyseriescollection2.addSeries(dataINTGP);
		
		XYDataset xydataset1 = xyseriescollection1;
		XYDataset xydataset2 = xyseriescollection2;
		
		XYLineAndShapeRenderer ren1 = new XYLineAndShapeRenderer();
		ren1.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));   		
        NumberAxis numberaxis1 = new NumberAxis("Kg$");
        numberaxis1.setAutoRangeIncludesZero(false);
        XYPlot xyplot1 = new XYPlot(xydataset1, null, numberaxis1, ren1);
        xyplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyplot1.setBackgroundPaint(Color.lightGray);
        xyplot1.setDomainGridlinePaint(Color.white);
        xyplot1.setRangeGridlinePaint(Color.white);
        
        for(int x =0; x < 4 ; x++){
	        ren1.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren1.setSeriesItemLabelsVisible(x, true);
	        ren1.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		} 
        
        
        XYLineAndShapeRenderer ren2 = new XYLineAndShapeRenderer();
		ren2.setBaseItemLabelFont(new Font("Ariel", Font.PLAIN, 13));  
        NumberAxis numberaxis2 = new NumberAxis("毛利%");
        numberaxis2.setAutoRangeIncludesZero(false);
        XYPlot xyplot2 = new XYPlot(xydataset2, null, numberaxis2, ren2);
        xyplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        xyplot2.setBackgroundPaint(Color.lightGray);
        xyplot2.setDomainGridlinePaint(Color.white);
        xyplot2.setRangeGridlinePaint(Color.white);
                
		for(int x =0; x < 4 ; x++){
	        ren2.setSeriesItemLabelGenerator(x, new XYItemLabelGenerator() { 
				public String generateLabel(XYDataset dataset, int series, int item) {
					return dataset.getY(series, item).toString();
				}
			});
	        ren2.setSeriesItemLabelsVisible(x, true);
	        ren2.setSeriesStroke(x, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		}
        
        NumberAxis axis = new NumberAxis("Year");
        axis.setAutoRangeIncludesZero(false);
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(axis);
        plot.setGap(1D);
        plot.add(xyplot1, 1);//50% of page
        plot.add(xyplot2, 1); //50% of page
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("歷年外銷毛利、 售價  & 進價成本  變化 ", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
           
		
        return chart;
	}	
	
}
