import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.GridLayout;

import java.util.Vector;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PanelEXPForecast extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int EXPSOLDKG = 0;
	private static final int PRODUCTION = 1;
	private static final int BUYTW = 2;
	private static final int BUYFOREIGN = 3;
	private static final int EXPFORECAST = 4;
	private static final int DOMSOLDKG = 5;
	private static final int DOMFORECAST = 6;
	private static final int TRITRADEKG = 7;
	private static final int INVENTORY = 8;
	
	JPanel chartpanel = new JPanel();
	KYdb db = new KYdb();
	
	PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.SHOWIMAGE, PanelSearchPcode.EXPORT){
		private static final long serialVersionUID = 1L;
		public void update() {
			final XYDataset tdataset = getJFCdataset_forecast();
			final JFreeChart tchart = getJFCchart_forecast(tdataset);
			final ChartPanel jfcPanel = new ChartPanel(tchart);

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
			            	case EXPSOLDKG:
		            			title = year + "�~�� " + selectedpcode + "��~���P�����";
		            			final RecordView rvsao430 = new RecordView(db.getResultset_sao430(selectedpcode,year),title);
		            			
		            			rvsao430.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		            			rvsao430.setVisible(true);
		            			
			            		u.debug("EXPSOLDKG");
			            		break;
			            		
			            	case PRODUCTION:
			            		title = year + "�~�� " + selectedpcode + "�~�إͲ��p�e";
			            		final RecordView rvpro130 = new RecordView(db.getResultSet_pro130(selectedpcode,year),title);
			            		rvpro130.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvpro130.setVisible(true);
		            			
			            		u.debug("PRODUCTION");
			            		break;
			            		
			            	case BUYTW:
			            		title = year + "�~�� " + selectedpcode + "�ꤺ�Ͳ����ʬ���";
			            		final RecordView rvpro960 = new RecordView(db.getResultset_pro960(selectedpcode,year),title);
			            		rvpro960.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvpro960.setVisible(true);
			            		u.debug("BUYTW");
			            		break;

			            	case BUYFOREIGN:
			            		title = year + "�~�� " + selectedpcode + "��~�Ͳ����ʬ���";
			            		final RecordView rvpro370 = new RecordView(db.getResultSet_pro370(selectedpcode,year),title);
			            		rvpro370.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvpro370.setVisible(true);
			            		u.debug("BUYFOREIGN");
			            		break;
			            		
			            	case EXPFORECAST:
			            		title = year + "�~�� " + selectedpcode + "��~���w�ʬ���";
			            		final RecordView rvpln145 = new RecordView(db.getResultSet_pln145(selectedpcode,year),title);
			            		rvpln145.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvpln145.setVisible(true);			            		
			            		u.debug("EXPFORECAST");
			            		break;
			            		
			            	case DOMSOLDKG:
			            		title = year + "�~�� " + selectedpcode + "�ꤺ�P�����";
			            		final RecordView rvdom430 = new RecordView(db.getResultset_dom430(selectedpcode,year),title);
			            		rvdom430.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvdom430.setVisible(true);	
			            		u.debug("DOMSOLDKG");
			            		break;
			            		
			            	case DOMFORECAST:
			            		title = year + "�~�� " + selectedpcode + "�ꤺ�w��";
			            		final RecordView rvpln245 = new RecordView(db.getResultSet_pln245(selectedpcode,year),title);
			            		rvpln245.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvpln245.setVisible(true);				            		
			            		
			            		u.debug("DOMFORECAST");
			            		break;
			            		
			            	case TRITRADEKG:
			            		title = year + "�~�� " + selectedpcode + "�ҥ~���";
			            		final RecordView rvsao950 = new RecordView(db.getResultset_sao950(selectedpcode,year),title);
			            		rvsao950.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			            		rvsao950.setVisible(true);	
			            		u.debug("TRITRADEKG");
			            		break;
			            		
			            	case INVENTORY:
			            		u.debug("INVENTORY");
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
	
	public PanelEXPForecast() {
		this.setLayout(new GridLayout(1, 1, 0, 0));
		JSplitPane splitpanel = new JSplitPane();
				
		splitpanel.setDividerLocation(350);
		splitpanel.add(panelSearchPcode, "left");
		splitpanel.add(chartpanel, "right");
		
		this.add(splitpanel);

	}

	private XYDataset getJFCdataset_forecast() {
		XYSeries dataEXPSOLDKG = new XYSeries("�~�P�q");
		XYSeries dataTRITRADEKG = new XYSeries("�ҥ~���");
		XYSeries dataDOMSOLDKG = new XYSeries("���P�q");
		XYSeries dataPRODUCTION = new XYSeries("�p�e�Ͳ�");
		XYSeries dataBUYTW = new XYSeries("�ꤺ����");
		XYSeries dataBUYFOREIGN = new XYSeries("��~����");
		XYSeries dataEXPFORECAST = new XYSeries("��~�w��");
		XYSeries dataDOMFORECAST = new XYSeries("�ꤺ�w��");
		XYSeries dataINVENTORY = new XYSeries("�����w�s");

		Vector<KeyValue_int_float> vecEXPSOLDKG = db.getKeyValue_int_float_EXPSOLD(panelSearchPcode.selectedpcode,"soldkg");//�Ndb��Ʀs�Jvector 
		for (KeyValue_int_float kv : vecEXPSOLDKG){dataEXPSOLDKG.add(kv.key, Math.round(kv.value));	} //�����J�Ϫ�data
		
		Vector<KeyValue_int_float> vecPRODUCTION = db.getKeyValue_int_float_PRODUCTION(panelSearchPcode.selectedpcode);
		for (KeyValue_int_float kv : vecPRODUCTION){dataPRODUCTION.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> vecBUYTW = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"buykg");
		for (KeyValue_int_float kv : vecBUYTW){dataBUYTW.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> vecBUYFOREIGN = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"buykg");
		for (KeyValue_int_float kv : vecBUYFOREIGN){dataBUYFOREIGN.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> vecEXPFORECAST = db.getKeyValue_int_float_EXPFORECAST(panelSearchPcode.selectedpcode);
		for (KeyValue_int_float kv : vecEXPFORECAST){dataEXPFORECAST.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> vecDOMSOLDKG = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode,"soldkg");//�Ndb��Ʀs�Jvector 
		for (KeyValue_int_float kv : vecDOMSOLDKG){dataDOMSOLDKG.add(kv.key, Math.round(kv.value));	} //�����J�Ϫ�data

		Vector<KeyValue_int_float> vecDOMFORECAST = db.getKeyValue_int_float_DOMFORECAST(panelSearchPcode.selectedpcode);
		for (KeyValue_int_float kv : vecDOMFORECAST){dataDOMFORECAST.add(kv.key,  Math.round(kv.value));	}	

		Vector<KeyValue_int_float> vecTRITRADEKG = db.getKeyValue_int_float_TRITRADESOLDKG(panelSearchPcode.selectedpcode);//�Ndb��Ʀs�Jvector 
		for (KeyValue_int_float kv : vecTRITRADEKG){dataTRITRADEKG.add(kv.key, Math.round(kv.value));	} //�����J�Ϫ�data
		
		Vector<KeyValue_int_float> vecINVENTORY = db.getKeyValue_int_float_INVENTORY(panelSearchPcode.selectedpcode);//�Ndb��Ʀs�Jvector 
		for (KeyValue_int_float kv : vecINVENTORY){dataINVENTORY.add(kv.key, Math.round(kv.value));	} //�����J�Ϫ�data
		
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(dataEXPSOLDKG);
		dataset.addSeries(dataPRODUCTION);
		dataset.addSeries(dataBUYTW);
		dataset.addSeries(dataBUYFOREIGN);
		dataset.addSeries(dataEXPFORECAST);
		dataset.addSeries(dataDOMSOLDKG);
		dataset.addSeries(dataDOMFORECAST);
		dataset.addSeries(dataTRITRADEKG);
		dataset.addSeries(dataINVENTORY);
		return dataset;
	}
	
	private JFreeChart getJFCchart_forecast(final XYDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("�s�ө���", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("�s�ө���", Font.BOLD, 15));
		standardChartTheme.setLargeFont(new Font("�s�ө���", Font.BOLD, 15));
		standardChartTheme.setSmallFont(new Font("�s�ө���", Font.BOLD, 12));
		
		ChartFactory.setChartTheme(standardChartTheme);
		
		// create the chart...
		String title = "�~�عw��/�Ͳ�/�P��/���� �����\n" + panelSearchPcode.title + "\n";
		final JFreeChart chart = ChartFactory.createXYLineChart(title, // chart
				"�~��", // x axis label
				"����", // y axis label
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

		renderer.setSeriesItemLabelGenerator(5, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});
		renderer.setSeriesItemLabelGenerator(6, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});

		renderer.setSeriesItemLabelGenerator(7, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});

		renderer.setSeriesItemLabelGenerator(8, new XYItemLabelGenerator() { 
			public String generateLabel(XYDataset dataset, int series, int item) {
				return dataset.getY(series, item).toString() ;
			}
		});

		renderer.setSeriesItemLabelsVisible(0, true);
		renderer.setSeriesItemLabelsVisible(1, true);
		renderer.setSeriesItemLabelsVisible(2, true);
		renderer.setSeriesItemLabelsVisible(3, true);
		renderer.setSeriesItemLabelsVisible(4, true);
		renderer.setSeriesItemLabelsVisible(5, true);
		renderer.setSeriesItemLabelsVisible(6, true);
		renderer.setSeriesItemLabelsVisible(7, true);
		renderer.setSeriesItemLabelsVisible(8, true);
		
		renderer.setSeriesPaint(EXPSOLDKG, Color.RED);
		renderer.setSeriesPaint(PRODUCTION, Color.BLUE);
		renderer.setSeriesPaint(BUYTW, Color.GREEN);
		renderer.setSeriesPaint(BUYFOREIGN, Color.YELLOW);
		renderer.setSeriesPaint(EXPFORECAST, Color.MAGENTA);
		renderer.setSeriesPaint(DOMSOLDKG, Color.BLACK);
		renderer.setSeriesPaint(DOMFORECAST, Color.CYAN);
						
		renderer.setSeriesStroke(EXPSOLDKG, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(PRODUCTION, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(BUYTW, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(BUYFOREIGN, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(EXPFORECAST, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(DOMSOLDKG, new BasicStroke(4f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		renderer.setSeriesStroke(DOMFORECAST, new BasicStroke(4f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		renderer.setSeriesStroke(7, new BasicStroke(4f,	BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND ,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		renderer.setSeriesStroke(8, new BasicStroke(4f,	BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND ,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
				
		plot.setRenderer(renderer);

		return chart;
	}	
	
}