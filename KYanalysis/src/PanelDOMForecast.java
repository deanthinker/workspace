import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.util.Vector;

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


public class PanelDOMForecast extends JPanel {

	JPanel chartpanel = new JPanel();
	KYdb db = new KYdb();
	
	PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.SHOWIMAGE, PanelSearchPcode.DOMESTIC){
		private static final long serialVersionUID = 1L;
		public void update() {
			final XYDataset tdataset = getJFCdataset_forecast();
			final JFreeChart tchart = getJFCchart_forecast(tdataset);
			final ChartPanel jfcPanel = new ChartPanel(tchart);

			chartpanel.removeAll();
			chartpanel.revalidate(); // This removes the old chart
			chartpanel.setLayout(new BorderLayout());
			chartpanel.add(jfcPanel);
			chartpanel.repaint(); // This method makes the new chart
			
		}
	};	
	
	public PanelDOMForecast() {
		this.setLayout(new GridLayout(1, 1, 0, 0));
		JSplitPane splitpanel = new JSplitPane();
		JPanel settingpanel = new JPanel();
				
		splitpanel.setDividerLocation(350);
		splitpanel.add(panelSearchPcode, "left");
		splitpanel.add(chartpanel, "right");
		
		this.add(splitpanel);

	}

	private XYDataset getJFCdataset_forecast() {
		XYSeries datasales = new XYSeries("內銷量");
		XYSeries dataproduction = new XYSeries("計畫生產");
		XYSeries databuyTW = new XYSeries("國內採購");
		XYSeries databuyForeign = new XYSeries("國外採購");
		XYSeries dataforecast = new XYSeries("國內預購");

		Vector<KeyValue_int_float> salesvec = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode, "soldkg");//將db資料存入vector 
		for (KeyValue_int_float kv : salesvec){datasales.add(kv.key, Math.round(kv.value));	} //資料轉入圖表data

		Vector<KeyValue_int_float> productionvec = db.getKeyValue_int_float_PRODUCTION(panelSearchPcode.selectedpcode);
		for (KeyValue_int_float kv : productionvec){dataproduction.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyTWvec = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"buykg");
		for (KeyValue_int_float kv : buyTWvec){databuyTW.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyForeignvec = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"buykg");
		for (KeyValue_int_float kv : buyForeignvec){databuyForeign.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> forecastvec = db.getKeyValue_int_float_DOMFORECAST(panelSearchPcode.selectedpcode);
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
		String title = "品種預購/生產/銷售/採購 比較圖\n" + panelSearchPcode.title + "\n";
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
		renderer.setSeriesStroke(0, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(1, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		renderer.setSeriesStroke(2, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(3, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL), true);
		renderer.setSeriesStroke(4, new BasicStroke(3f,	BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL,1.0f, new float[] {6.0f, 6.0f}, 0.0f), true);
		plot.setRenderer(renderer);

		return chart;
	}	
	
}