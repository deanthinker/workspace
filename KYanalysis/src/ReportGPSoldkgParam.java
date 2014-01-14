import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitor;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.AttributedString;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.math.BigDecimal;
import java.util.Date;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.PercentageColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;

import javax.swing.JPanel;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;

import javax.swing.JCheckBox;

import java.awt.Window.Type;

import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.FlowLayout;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.Dialog.ModalityType;



public class ReportGPSoldkgParam extends JDialog {

	JComboBox<String> cbxProductionYS;
	JComboBox<String> cbxProductionYE;
	JComboBox<String> cbx_rangesoldkg_logic;
	JComboBox<String> cbx_gp_logic;
	JComboBox<String> cbx_gp_percent;
	JComboBox<String> cbx_croplist;
	
	private int YE = Calendar.getInstance().get(Calendar.YEAR);
	private int YS = YE - 10;
	
	private final JPanel contentPanel = new JPanel();
	private KYdb db = new KYdb();
	private Connection con;
	private KYutil u = new KYutil();
	private JTextField txf_rangesoldkg;
	
	public static void main(String[] args) {
		try {
			//ReportGPSoldkgParam dialog = new ReportGPSoldkgParam();
			//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ReportGPSoldkgParam() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		con = db.getConnection();
		showUI();
				
	}
	
	private void showUI(){
		setTitle("品種價值分析表");
		setBounds(100, 100, 458, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						build_GPSoldkgParam();
						setVisible(false);
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						dispose();          
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);  
						dispose();          
					}
				});
			}
		}

		contentPanel.setLayout(new GridLayout(2,1, 0, 0));
				
		JPanel panel_2 = new JPanel();
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel label = new JLabel("品種");
		label.setBounds(10, 10, 55, 18);
		panel_2.add(label);
		
		cbx_croplist = new JComboBox(db.getVegeCropVec());
		cbx_croplist.setBounds(47, 9, 123, 21);
		panel_2.add(cbx_croplist);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblgp = new JLabel("毛利GP%");
		lblgp.setBounds(81, 76, 71, 15);
		panel.add(lblgp);
		
		cbx_gp_logic = new JComboBox<String>();
		cbx_gp_logic.setBounds(162, 73, 47, 21);
		panel.add(cbx_gp_logic);
		cbx_gp_logic.setModel(new DefaultComboBoxModel(new String[] { ">=", "<=", "=",">", "<"}));
		
		cbx_gp_percent = new JComboBox<String>();
		cbx_gp_percent.setModel(new DefaultComboBoxModel(new String[] {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"}));
		cbx_gp_percent.setBounds(219, 73, 47, 21);
		panel.add(cbx_gp_percent);
		
		JLabel label_1 = new JLabel("%");
		label_1.setBounds(276, 76, 23, 15);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("銷售/生產統計期間");
		label_2.setBounds(29, 23, 123, 15);
		panel.add(label_2);
		
		cbxProductionYS = new JComboBox<String>(u.create10yearVector());
		cbxProductionYS.setBounds(162, 20, 84, 21);
		panel.add(cbxProductionYS);

		cbxProductionYE = new JComboBox<String>(u.create10yearVector());
		cbxProductionYE.setBounds(256, 20, 81, 21);
		panel.add(cbxProductionYE);

		cbxProductionYS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector tv = new Vector();
				JComboBox<String> combo = (JComboBox) e.getSource();
				int selectedYear = Integer.valueOf((String) combo
						.getSelectedItem());
				// automatically updates YE combo box list
				cbxProductionYE.removeAllItems();
				tv = u.createRangeVector(selectedYear, YE-selectedYear);
				for (int i = 0; i < tv.size(); i++)
					cbxProductionYE.addItem(tv.get(i).toString());
				

			}
		});

		cbxProductionYE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


			}
		});		
		
		JLabel label_3 = new JLabel("期間總銷量");
		label_3.setBounds(71, 51, 82, 15);
		panel.add(label_3);
		
		cbx_rangesoldkg_logic = new JComboBox();
		cbx_rangesoldkg_logic.setModel(new DefaultComboBoxModel(new String[]  { ">=", "<=", "=",">", "<"}));
		cbx_rangesoldkg_logic.setBounds(162, 45, 47, 21);
		panel.add(cbx_rangesoldkg_logic);
		
		JLabel label_4 = new JLabel("公斤");
		label_4.setBounds(276, 51, 35, 15);
		panel.add(label_4);
		
		txf_rangesoldkg = new JTextField();
		txf_rangesoldkg.setText("0");
		txf_rangesoldkg.setBounds(219, 45, 47, 21);
		panel.add(txf_rangesoldkg);
		txf_rangesoldkg.setColumns(10);
	}
	

	private void build_GPSoldkgParam() {
		String subtitle = "";
		String title = "";
		String ys = (String)cbxProductionYS.getSelectedItem();
		String ye = (String)cbxProductionYE.getSelectedItem();
		
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder boldRightStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(14);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);

		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("#").setWidth(4);;
		TextColumnBuilder<String>  colpcode  = col.column("品編", "pcode", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colpname  = col.column("品種名稱", "pname", type.stringType()).setWidth(10);
		TextColumnBuilder<BigDecimal>  colgp  = col.column("近期毛利%", "gp", type.bigDecimalType()).setWidth(6);
		TextColumnBuilder<BigDecimal>  colavggp  = col.column("平均毛利%", "avggp", type.bigDecimalType()).setWidth(6);
		TextColumnBuilder<BigDecimal>  collatestsoldkg = col.column("近期年銷量(Kg)", "soldkg", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  collatestavgntprice  = col.column("近期售價(NT/Kg)", "avgntprice", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colrangeincome  = col.column("期間總營收(萬NT)", "rangeincome", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colrangesoldkg  = col.column("期間總銷量(Kg)", "rangesoldkg", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colavgsoldkg  = col.column("平均年銷量(Kg)", "avgsoldkg", type.bigDecimalType()).setWidth(8);
		
		TextColumnBuilder<BigDecimal> colsort;

		colsort = colgp;

		 title = "所有品種  價值評估表";
		 
		if (cbx_croplist.getSelectedIndex() > 0)
			title = (String)cbx_croplist.getSelectedItem() + " 價值評估表   ";
		
		title = title + "[毛利 " + cbx_gp_logic.getSelectedItem() + cbx_gp_percent.getSelectedItem() + "%" + "]  "
				+ "期間總銷量"+ cbx_rangesoldkg_logic.getSelectedItem() + txf_rangesoldkg.getText() + "公斤]";
				
		subtitle = "期間:" + ys +  " - " + ye;
		
		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow, colpcode, colcrop, colpname, colgp,colavggp,collatestsoldkg,collatestavgntprice,colrangeincome,colrangesoldkg,colavgsoldkg )
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  .sortBy(desc(colsort))
			  
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_GPSoldkgParam())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_GPSoldkgParam() {
		Statement stat = null;
		ResultSet rs = null;
		DRDataSource dataSource = new DRDataSource("pcode", "crop", "pname","gp","avggp","soldkg","avgntprice","rangeincome","rangesoldkg","avgsoldkg");
		float avgntprice = 0;
		float rangeincome =0;
		float rangesoldkg = 0;
		float avgsoldkg = 0;
		float avggp = 0;
		float gp = 0;
		float profit = 0;
		float soldkg = 0;
		int row = 0;
		int count =0;
		String whereyear = null;
		String wherelevel2 = "";
		String whereclause = "";
		String ys = (String)cbxProductionYS.getSelectedItem();
		String ye = (String)cbxProductionYE.getSelectedItem();
		String pcode = "";
		String crop = "";
		String pcname = "";
				
		String sql;		
		
		whereyear = " year >= "+ys+ " and year <= " + ye+ " ";
		
		if (cbx_croplist.getSelectedIndex() > 0)
			sql = "SELECT * from vege_prod where level2 = '" + (String) cbx_croplist.getSelectedItem() + "' order by level2";
		else
			sql = "SELECT * from vege_prod where pcode NOT LIKE 'ZZ%' and pcode NOT LIKE 'FL%'";
		
		//-----------progress bar-------------------
		row = db.countRow2(sql);
	    
		if (whereyear != null)
			whereclause = " where " + whereyear + " and " + wherelevel2;
		else
			whereclause = " where " + whereyear;

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {	 //loop throught all pcodes
				pcode = rs.getString("pcode");
				crop = rs.getString("level2");
				pcname = rs.getString("pcname");
				
				gp = db.getPcodeLatest_gp(pcode);
				if (!u.logicCompare((String)cbx_gp_logic.getSelectedItem(),gp, Float.valueOf((String) cbx_gp_percent.getSelectedItem()))  ) continue;

				rangesoldkg = db.getPcodePeriod_rangesoldkg(pcode, ys, ye);
				if (!u.logicCompare((String)cbx_rangesoldkg_logic.getSelectedItem(),rangesoldkg, Float.valueOf(txf_rangesoldkg.getText()))  ) continue;
				
				avggp = db.getPcodePeriod_avggp(pcode,ys,ye);
				soldkg = db.getPcodeLatest_soldkg(pcode);
				avgntprice = db.getPcodeLatest_avgntprice(pcode);
				rangeincome = db.getPcodePeriod_rangeincome(pcode,ys,ye)/10000; 
				avgsoldkg = rangesoldkg / (Integer.valueOf(ye) - Integer.valueOf(ys) + 1);
								
				dataSource.add(pcode, crop, pcname, 
						new BigDecimal(gp),
						new BigDecimal(avggp),
						new BigDecimal(soldkg),
						new BigDecimal(avgntprice), 
						new BigDecimal(rangeincome), 
						new BigDecimal(rangesoldkg),
						new BigDecimal(avgsoldkg));
			}
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			u.debug("getJRDS_GPSoldkgParam Exception :" + e.toString());
		}
		
		return dataSource;
	}

	
}
