import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
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
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;


public class PanelDOMForecast extends JPanel {

	JPanel chartpanel = new JPanel();
	KYdb db = new KYdb();
	
	PanelSearchPcode panelSearchPcode = new PanelSearchPcode(PanelSearchPcode.SHOWIMAGE){
		private static final long serialVersionUID = 1L;
		public void updateChart() {
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
		XYSeries datasales = new XYSeries("���P�q");
		XYSeries dataproduction = new XYSeries("�p�e�Ͳ�");
		XYSeries databuyTW = new XYSeries("�ꤺ����");
		XYSeries databuyForeign = new XYSeries("��~����");
		XYSeries dataforecast = new XYSeries("�ꤺ�w��");

		Vector<KeyValue_int_float> salesvec = db.getKeyValue_int_float_DOMSOLD(panelSearchPcode.selectedpcode, "sumqty");//�Ndb��Ʀs�Jvector 
		for (KeyValue_int_float kv : salesvec){datasales.add(kv.key, Math.round(kv.value));	} //�����J�Ϫ�data

		Vector<KeyValue_int_float> productionvec = db.getKeyValue_int_float_PRODUCTION(panelSearchPcode.selectedpcode);
		for (KeyValue_int_float kv : productionvec){dataproduction.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyTWvec = db.getKeyValue_int_float_BUYTW(panelSearchPcode.selectedpcode,"sumqty");
		for (KeyValue_int_float kv : buyTWvec){databuyTW.add(kv.key,  Math.round(kv.value));	}
		
		Vector<KeyValue_int_float> buyForeignvec = db.getKeyValue_int_float_BUYFOREIGN(panelSearchPcode.selectedpcode,"sumqty");
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