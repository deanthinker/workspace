import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.AttributedString;
import java.util.Calendar;
import java.util.Vector;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import net.sf.dynamicreports.examples.Templates;
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

import org.jfree.util.Rotation;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;

import javax.swing.JPanel;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;

import javax.swing.JCheckBox;

import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.FlowLayout;

import javax.swing.border.TitledBorder;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.Insets;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


public class Start {

	private JFrame mainFrame = new JFrame();
	private JList<String> listVegeProd = new JList<String>();
	private JList<String> listProductionVariety = new JList<String>();
	private JList<String> listProductionVarietyDetail = new JList<String>();
	private JSlider slProductionHide = new JSlider();
	
	private DefaultListModel<String> listVegeProd_model = new DefaultListModel<String>();
	private DefaultListModel<String> listProductionVariety_model = new DefaultListModel<String>();
	private DefaultListModel<String> listProductionVarietyDetail_model = new DefaultListModel<String>();
	
	private JLabel lblTotalPcode;
	private int selectedlevel2Index = -1;

	private Connection con = null; // Database objects
		
	private JComboBox<String> cbxProductionYS; // comboBox for production start year
	private JComboBox<String> cbxProductionYE; // comboBox for production end year
	private JComboBox<String> cbxStatYS; // comboBox for production start year
	private JComboBox<String> cbxStatYE; // comboBox for production end year
	private JComboBox<String> cbx_allpcode_class;
	private JComboBox<String> cbx_productionpcode_class;
	private JComboBox<String> cbx_productionpcode_year;
	private JComboBox<String> cbx_stat_rankclass;
	private JComboBox<String> cbx_stat_prodclass;
	private JComboBox<String> cbx_stat_valueyear;
	private JComboBox<String> cbx_production_vege_croplist;
	
	private JRadioButton radStatVariety;
	private JRadioButton radStatCrop;
	private JRadioButton radStatSingleVar;
	private JRadioButton radStatSalesNT;
	private JRadioButton radStatSalesKg;
	private JRadioButton radStatValueProfit;
	private JRadioButton radStatValueGP;
	private JRadioButton radStatValuePrice;
		
	private JPanel panelChartProduction = null;
	public int YEAR_RANGE = 10;
	public int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	public int YS = YE - YEAR_RANGE; // start year
	final int VALUE_TABLE_SHOWALL_PCODE = 1;
	final int VALUE_TABLE_PRODUCTION_PCODE = 2;
	final int MODE_WEIGHT_GP = 1;
	final int MODE_WEIGHT_PRICE = 2;

	Vector<CostPriceData> cost_vec = new Vector<CostPriceData>(0, 0);
	Vector<CostPriceData> price_vec = new Vector<CostPriceData>(0, 0);
	Vector<CostPriceData> TWprice_vec = new Vector<CostPriceData>(0, 0);
	Vector<CostPriceData> outcost_vec = new Vector<CostPriceData>(0, 0);
	Vector<ProductionPercentData> production_percent_vec = new Vector<ProductionPercentData>(0, 0);
	Vector<ProductionVarietyPercentData> production_varietypercent_vec = new Vector<ProductionVarietyPercentData>(0, 0);

	Vector gp_vec = new Vector(0, 0);
	private JTextField tf_singlesearch;
	private KYutil u = new KYutil();
	private KYdb db = new KYdb();
	/**
	 * Launch the application.
	 */	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.mainFrame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Start() {
		//the following thread is necessary for DJNativeBrowser
		Thread t = new Thread(){
		    public void run() { // override Thread's run()
		  	  // this is for Tab 銷售版圖 SWTNativeInterface
		    	//NativeInterface.open();
		  	  
		      //UIUtils.setPreferredLookAndFeel();
		      System.out.println("NativeInterface loading done.");
		    }			
		};
		t.run();
		showUI();
	}



	protected JComponent makeProductionChartPanel() {
		panelChartProduction = new JPanel();

		final DefaultPieDataset tdataset = getJFCdataset_production();
		final JFreeChart tchart = getJFCchart_production(tdataset);
		ChartPanel jfcPanel = new ChartPanel(tchart);

		panelChartProduction.setLayout(new BorderLayout());
		panelChartProduction.add(jfcPanel);
		return panelChartProduction;
	}

	
	protected JComponent makeCostPriceGPSettingSearchPanel() {	
		JPanel thispanel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		
		JButton btn_allpcode = new JButton("所有品種清單");
		JLabel lbl_productionpcode = new JLabel("年度生產品種清單");
		new JButton("作物別");
		
		final JLabel lblvalue = new JLabel("關鍵字");
		JButton btn_singlepcode = new JButton("篩選");

		cbx_allpcode_class = new JComboBox(db.getVegeCropVec());
		cbx_productionpcode_year = new JComboBox(u.create10yearVector());
		cbx_productionpcode_class = new  JComboBox(db.getVegeCropVec());
		
		thispanel.setBorder(new TitledBorder(null, "篩選",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		thispanel.setLayout(new GridLayout(4, 1, 0, 0));
		
		tf_singlesearch = new JTextField();

		btn_singlepcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_listVegeProd_singleProd(tf_singlesearch.getText());
			}
		});		

		
		btn_allpcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbx_allpcode_class.setSelectedIndex(0);
				update_listVegeProd_AllProd(false);
			}
		});			

		

		cbx_allpcode_class.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_listVegeProd_AllProd(true);
			}
		});		

			
		cbx_productionpcode_year.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbx_productionpcode_class.setSelectedIndex(0);
				update_listVegeProd_ProductionProd(true, false);
			}
		});	

		cbx_productionpcode_class.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				update_listVegeProd_ProductionProd(true, true);
			}
		});	
		
		
		tf_singlesearch.setColumns(6);
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		panel1.add(btn_allpcode);
		panel1.add(cbx_allpcode_class);
		panel3.add(lbl_productionpcode);
		panel3.add(cbx_productionpcode_year);
		panel3.add(cbx_productionpcode_class);
		
		panel2.add(lblvalue);		
		panel2.add(tf_singlesearch);
		panel2.add(btn_singlepcode);

		JLabel lblhideweight1 = new JLabel("隱藏");
		String[] arr1to5 = {"0","1","2","3","4","5"};
		final JComboBox cbx_hideweight = new JComboBox(arr1to5);
		cbx_hideweight.setSelectedIndex(0);
		cbx_hideweight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cbx_hideweight.getSelectedIndex();
			}
		});	
		
		JLabel lblhideweight2 = new JLabel("公斤以下的資訊");
		
		panel4.add(lblhideweight1);
		panel4.add(cbx_hideweight);
		panel4.add(lblhideweight2);
		
		thispanel.add(panel1);
		thispanel.add(panel3);
		thispanel.add(panel2);
		thispanel.add(panel4);
		
		
		return thispanel;
	}
	

	

	protected JComponent makeCostPriceGPSettingPanel() {
		// consists of setting, search, list
		JPanel thispanel = new JPanel();
		thispanel.setBorder(new TitledBorder(null, "設定",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		thispanel.setLayout(new GridLayout(3, 1, 0, 0));
		


		return thispanel;
	}
/*
	protected JComponent makeCostPriceGPTabPanel() {
		JSplitPane splitpanel = new JSplitPane();
		splitpanel.setDividerLocation(350);
		splitpanel.add(makeCostPriceGPSettingPanel(), "left");
		splitpanel.add(makeCostPriceGPChartPanel(), "right");
		return splitpanel;
	}
*/
	
	protected JComponent makeStatisticsTabPanel() {
		JSplitPane splitpanel = new JSplitPane();
		splitpanel.setDividerLocation(350);
		splitpanel.add(makeStatSettingPanel(), "left");
		splitpanel.add(makeStatChartPanel(), "right");
		return splitpanel;
	}
		
	protected JComponent makeProductionTabPanel() {
		JSplitPane panel = new JSplitPane();
		panel.add(makeProductionSettingPanel(), "left");
		panel.add(makeProductionChartPanel(), "right");
		update_listProductionVariety(); // must be the last line (after listProductionVariety is updated)
		return panel;
	}

	protected JComponent makeMapTabPanel() {
		JSplitPane splitpanel = new JSplitPane();
		splitpanel.setDividerLocation(170);
		splitpanel.add(new JPanel(), "left");
		splitpanel.add(new PanelBrowser(), "right");
		return splitpanel;
	}

		protected JComponent makeStatSettingPanel() {
		final ButtonGroup buttonGroup1 = new ButtonGroup();
		final ButtonGroup buttonGroup2 = new ButtonGroup();
		final ButtonGroup buttonGroup3 = new ButtonGroup();
		
		JPanel settingpanel = new JPanel();
		//settingpanel.setBorder(new TitledBorder(null, "設定",TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_sales = new JPanel();
		JPanel panel_other_value1 = new JPanel();
		JPanel panel_other_value2 = new JPanel();
		JPanel panel_valueopt = new JPanel();
		
		JPanel settingpanelrank = new JPanel();
		settingpanelrank.setBorder(new TitledBorder(null, "排行榜",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));		
		
		JPanel panel_yrange = new JPanel();
		//panel_yrange.setBorder(new TitledBorder(null, "排序",TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_class = new JPanel();
		//panel_class.setBorder(new TitledBorder(null, "分析標的",TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel settingpanelother = new JPanel();
		settingpanelother.setBorder(new TitledBorder(null, "品種價值表",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));			
		
		cbx_stat_rankclass = new JComboBox(db.getVegeCropVec());
		cbx_stat_prodclass = new JComboBox(db.getVegeCropVec());
		
		JLabel lblyrange = new JLabel("統計期間");
		cbxStatYS = new JComboBox(u.create10yearVector());
		cbxStatYE = new JComboBox(u.create10yearVector());

		cbxStatYS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector tv = new Vector();
				JComboBox combo = (JComboBox) e.getSource();
				int selectedYear = Integer.valueOf((String) combo
						.getSelectedItem());
				// automatically updates YE combo box list
				cbxStatYE.removeAllItems();
				tv = u.createRangeVector(selectedYear, YE-selectedYear);
				for (int i = 0; i < tv.size(); i++)
					cbxStatYE.addItem(tv.get(i).toString());

			}
		});


		panel_yrange.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));

		
		panel_yrange.add(lblyrange);
		panel_yrange.add(cbxStatYS);
		panel_yrange.add(cbxStatYE);
		
		settingpanel.setLayout(new GridLayout(3, 1, 0, 0));
		settingpanelrank.setLayout(new GridLayout(3, 1, 0, 0));
		//settingpanelother.setLayout(new GridLayout(2, 1, 0, 0));
		
		settingpanelrank.add(panel_yrange);
		settingpanelrank.add(panel_class);
		settingpanelrank.add(panel_sales);
		
		settingpanel.add(settingpanelrank);
		settingpanel.add(settingpanelother);
		radStatSalesNT = new JRadioButton("銷售額NT");
		radStatSalesKg = new JRadioButton("銷售量Kg");
		radStatSalesNT.setSelected(true);
		buttonGroup2.add(radStatSalesNT);
		buttonGroup2.add(radStatSalesKg);
		
		JLabel lblNewLabel_1 = new JLabel("排序");
		panel_sales.add(lblNewLabel_1);
		panel_sales.add(radStatSalesNT);
		panel_sales.add(radStatSalesKg);
		
		
		radStatVariety = new JRadioButton("品種別");
		radStatVariety.setSelected(true);
		radStatVariety.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbx_stat_rankclass.setEnabled(false);
			}
		});
		
		JLabel lblNewLabel = new JLabel("分析標的");
		panel_class.add(lblNewLabel);
		buttonGroup1.add(radStatVariety);
		panel_class.add(radStatVariety);
		
		radStatCrop = new JRadioButton("作物別");
		radStatCrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbx_stat_rankclass.setEnabled(false);
			}
		});
		buttonGroup1.add(radStatCrop);
		panel_class.add(radStatCrop);
		
		radStatSingleVar = new JRadioButton("單一作物");
		radStatSingleVar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbx_stat_rankclass.setEnabled(true);
			}
		});
		buttonGroup1.add(radStatSingleVar);
		panel_class.add(radStatSingleVar);
		
		cbx_stat_rankclass.setEnabled(false);
		panel_class.add(cbx_stat_rankclass);
	
		
		final JCheckBox chkseed_num = new JCheckBox("顯示組合號");
		panel_sales.add(chkseed_num);

		
		JButton btnStatSalesRank = new JButton("顯示");
		btnStatSalesRank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chkseed_num.isSelected())
					buildStatSalesRankseed_num();
				else
					buildStatSalesRank();
			}
		});
				
		panel_sales.add(btnStatSalesRank);
		
		JLabel lblStatVarietyQL_LQ = new JLabel("分析標的");
		JButton btnStatVarietyQL_LQ = new JButton("顯示");
		btnStatVarietyQL_LQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buildStatVarietyQL_LQ();
			}
		});
		
		settingpanelother.setLayout(new GridLayout(2, 1, 0, 0));

		
		JLabel lblStatValueOrder = new JLabel("排序");
		radStatValueProfit = new JRadioButton("每甲利益");
		radStatValueGP = new JRadioButton("毛利");
		radStatValuePrice = new JRadioButton("售價");
		buttonGroup3.add(radStatValueProfit);
		buttonGroup3.add(radStatValueGP);
		buttonGroup3.add(radStatValuePrice);
		radStatValueProfit.setSelected(true);

		String[] arr1to5 = {"歷年","1年內","2年內","3年內","4年內","5年內"};
		cbx_stat_valueyear = new JComboBox(arr1to5);
		cbx_stat_valueyear.setSelectedIndex(5);
			
		panel_other_value1.add(lblStatVarietyQL_LQ);
		panel_other_value1.add(cbx_stat_prodclass);	
		panel_other_value1.add(cbx_stat_valueyear);
		
		panel_valueopt.add(lblStatValueOrder);
		panel_valueopt.add(radStatValueProfit);
		panel_valueopt.add(radStatValueGP);
		panel_valueopt.add(radStatValuePrice);
		
		panel_other_value1.add(panel_valueopt);
		panel_other_value1.add(btnStatVarietyQL_LQ);
		
		settingpanelother.add(panel_other_value1);
		panel_other_value2.setLayout(new GridLayout(0, 2, 0, 0));
		
		settingpanelother.add(panel_other_value2);
		
		JPanel buttonPanel = new JPanel();
		settingpanel.add(buttonPanel);
		buttonPanel.setLayout(new GridLayout(4, 2, 0, 0));
		
		JButton btnGPlanduseActincome = new JButton("生產價值(占地+實收)");
		btnGPlanduseActincome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ReportGPorderByLanduseActualIncome win = new ReportGPorderByLanduseActualIncome();
							win.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		buttonPanel.add(btnGPlanduseActincome);
		
		JButton btnGPActualIncomeLanduse = new JButton("銷售排名(實收 +占地)");
		btnGPActualIncomeLanduse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ReportGPorderByActualIncomeLanduse win = new ReportGPorderByActualIncomeLanduse();
					win.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
		});
		buttonPanel.add(btnGPActualIncomeLanduse);
		

		
		JButton btnAvgGPWeightReport = new JButton("近期/平均  毛利/重量 排名"); 
		buttonPanel.add(btnAvgGPWeightReport);
		btnAvgGPWeightReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ReportGPSoldkgParam win = new ReportGPSoldkgParam();
							win.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		JButton btnForeignDomesticPriceCompareReport = new JButton("品種國內外售價比較表");
		buttonPanel.add(btnForeignDomesticPriceCompareReport);
		btnForeignDomesticPriceCompareReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ReportExportDomesticPriceCompare win = new ReportExportDomesticPriceCompare();
							win.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});




		
		
		return settingpanel;
	}
	
	protected JComponent makeStatChartPanel() {
		JPanel thispanel = new JPanel();
		return thispanel;
		
	}
	protected JComponent makeProductionVarietySettingListPanel() {
		JPanel thispanel = new JPanel();
		thispanel.setBorder(new TitledBorder(null, "作物別品種清單",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		thispanel.setLayout(new GridLayout(1,1));
		listProductionVarietyDetail = new JList(listProductionVarietyDetail_model);
		JScrollPane scp = new JScrollPane(listProductionVarietyDetail);

		listProductionVarietyDetail
		.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
		thispanel.add(scp);
		return thispanel;
	}

	protected JComponent makeProductionSettingSliderPanel() {
		JPanel thispanel = new JPanel();
		final JLabel lblvalue = new JLabel();
		thispanel.setBorder(new TitledBorder(null, "隱藏圖表資料 < %",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_thispanel = new GridBagLayout();
		gbl_thispanel.columnWidths = new int[] {250, 30, 0};
		gbl_thispanel.rowHeights = new int[]{67, 0};
		gbl_thispanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_thispanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		thispanel.setLayout(gbl_thispanel);
		
		
		slProductionHide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblvalue.setText(String.valueOf(slProductionHide.getValue()) + " %");
			}
		});
		
				slProductionHide.setMinorTickSpacing(1);
				slProductionHide.setMajorTickSpacing(10);
				slProductionHide.setValue(1); //default hide data < 1%
				slProductionHide.setPaintTicks(true);
				slProductionHide.setPaintLabels(true);
				slProductionHide.setBorder(null);
					
				GridBagConstraints gbc_slProductionHide = new GridBagConstraints();
				gbc_slProductionHide.fill = GridBagConstraints.BOTH;
				gbc_slProductionHide.insets = new Insets(0, 0, 0, 5);
				gbc_slProductionHide.gridx = 0;
				gbc_slProductionHide.gridy = 0;
				thispanel.add(slProductionHide, gbc_slProductionHide);
	
		thispanel.add(lblvalue);
		
		return thispanel;
	}
	
	
	protected JComponent makeProductionSettingListPanel() {
		JPanel thispanel = new JPanel();
		thispanel.setBorder(new TitledBorder(null, "顯示作物別明細",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		thispanel.setLayout(new GridLayout(1,1));
		listProductionVariety = new JList(listProductionVariety_model);
		JScrollPane scp = new JScrollPane(listProductionVariety);

		listProductionVariety.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;

						JList tmpl = (JList) e.getSource();
						if (tmpl.isSelectionEmpty()) {
							lblTotalPcode.setText("Nothing selected.");
						} else {
							selectedlevel2Index = tmpl.getSelectedIndex();
							lblTotalPcode.setText(tmpl.getSelectedValue()
									.toString());
							//refreshProductionChart();
							insertProductionChart_variety();
							//DO NOT run update_listProductionVariety()!! LOOP!!
							update_listProductionVarietyDetail();
							
						}
					}
				});

		listProductionVariety
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		thispanel.add(scp);
		
		return thispanel;
	}

	protected JComponent makeProductionSettingPanel() {
		JPanel settingpanel = new JPanel();
		settingpanel.setBorder(new TitledBorder(null, "Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settingpanel.setLayout(new GridLayout(5, 1, 0, 0));

		settingpanel.add(makeProductionSettingSliderPanel());
		
		JPanel panel_period = new JPanel();
		panel_period.setBorder(new TitledBorder(null, "選擇特定期間",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_year = new JPanel();
		JPanel panel_yrange = new JPanel();
		panel_period.setLayout(new GridLayout(0, 2, 0, 0));
		panel_period.add(panel_year);
		panel_period.add(panel_yrange);		

		JLabel lblyrange = new JLabel("Period");
		JLabel lblyear = new JLabel("Year");

		cbxProductionYS = new JComboBox(u.create10yearVector());
		cbxProductionYE = new JComboBox(u.create10yearVector());

		cbxProductionYS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector tv = new Vector();
				JComboBox combo = (JComboBox) e.getSource();
				int selectedYear = Integer.valueOf((String) combo
						.getSelectedItem());
				// automatically updates YE combo box list
				cbxProductionYE.removeAllItems();
				tv = u.createRangeVector(selectedYear, YE-selectedYear);
				for (int i = 0; i < tv.size(); i++)
					cbxProductionYE.addItem(tv.get(i).toString());
				
				selectedlevel2Index = -1; //reset to avoid chart display error; 
										 //the previously selected item may not be available in the new dataset (index out of range) 
				refreshProductionChart();
				update_listProductionVariety(); // must be the last line (after listProductionVariety is updated)
			}
		});

		cbxProductionYE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				selectedlevel2Index = -1; //reset to avoid chart display error; 
				 //the previously selected item may not be available in the new dataset (index out of range) 
				refreshProductionChart();
				update_listProductionVariety(); // must be the last line (after listProductionVariety is updated)
			}
		});


		panel_yrange.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		panel_year.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		//panel_yrange.setLayout(new GridLayout(1, 3, 0, 0));
		//panel_year.setLayout(new GridLayout(1, 2, 0, 0));
		
		panel_yrange.add(lblyrange);
		panel_yrange.add(cbxProductionYS);
		panel_yrange.add(cbxProductionYE);
		
		panel_year.add(lblyear);

		
		JButton btnProdRatio = new JButton("顯示所有作物占比");
		btnProdRatio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedlevel2Index = -1; //reset to avoid chart display error; 
				 //the previously selected item may not be available in the new dataset (index out of range) 				
				refreshProductionChart();
				update_listProductionVariety(); // must be the last line (after listProductionVariety is updated)
			}
		});
		
		JPanel panelreport = new JPanel();
		
		JButton btnStatVarietyQL_LQ = new JButton("報表:品種價值表");
		btnStatVarietyQL_LQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buildProductionVarietyQL_LQ();
			}
		});
				
		panelreport.setLayout(new GridLayout(0, 2, 0, 0));
		panelreport.add(btnStatVarietyQL_LQ);
		
		JPanel panel_btn = new JPanel();
		panel_btn.setLayout(new GridLayout(2, 1, 0, 0));
		panel_btn.add(btnProdRatio);
		panel_btn.add(panelreport);
		
		cbx_production_vege_croplist = new JComboBox<String>(db.getVegeCropVec());
		panelreport.add(cbx_production_vege_croplist);
		
		settingpanel.add(panel_period);
		
		settingpanel.add(panel_btn);
		settingpanel.add(makeProductionSettingListPanel());
		settingpanel.add(makeProductionVarietySettingListPanel());

		return settingpanel;
	}

	
	protected JComponent makeCostPriceModePanel() {
		JPanel settingpanel = new JPanel();
				
		return settingpanel;
	}

	private void showUI() {
		
		this.con = db.getConnection();
		/*
		ProductReview pr = new ProductReview();
		pr.setVisible(true);
		*/
		
		mainFrame.setTitle("Known-You Product Analysis v1.2");
		mainFrame.setBounds(10, 10, 1348, 640);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblTotalPcode = new JLabel("Total PCODE:");
		mainFrame.getContentPane().add(lblTotalPcode);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("外銷預購/供給", null, new PanelEXPForecast(), null);
		tabbedPane.addTab("內銷預購/供給", null, new PanelDOMForecast(), null);
		tabbedPane.addTab("進價/售價/毛利", null, new PanelProductAnalysis(),	null);
		tabbedPane.addTab("外銷客戶分析", null, new PanelCustAnalysis(PanelCustAnalysis.EXPORT), null);
		tabbedPane.addTab("內銷客戶分析", null, new PanelCustAnalysis(PanelCustAnalysis.DOMESTIC), null);
		tabbedPane.addTab("生產計劃", null, makeProductionTabPanel(), null);
		tabbedPane.addTab("統計排名", null, makeStatisticsTabPanel(), null);
		
		//disable this tab to make run faster
		//tabbedPane.addTab("銷售版圖", null, new PanelBrowser(), null);
		
		//tabbedPane.addTab("----", null, new PanelInventory(), null);

		mainFrame.getContentPane().add(tabbedPane);
		PanelTools panelTools = new PanelTools();
		tabbedPane.addTab("其他", null, panelTools, null);
	}

	private JFreeChart getJFCchart_production_variety(PieDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 15));
		ChartFactory.setChartTheme(standardChartTheme);
		String ys, ye;

		String title = "";
		String total = "";

		ys = (String) cbxProductionYS.getSelectedItem();
		ye = (String) cbxProductionYE.getSelectedItem();
		total = u.f1decimal(getTotalProductionVarietyLandSize(ys,ye));
		

			title = production_percent_vec.get(selectedlevel2Index).level2 + " " + ys + " - " + ye + "期間生產計劃品種占比\n 合計" +total+ "甲";


		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				false, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setCircular(false);
		
		//plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		plot.setLabelGenerator(new ProductionVarietyLabel(slProductionHide.getValue()));
		
				plot.setIgnoreZeroValues(true);
		plot.setNoDataMessage("No data available");

		// plot.setForegroundAlpha(0.8f);

		return chart;

	}

	private JFreeChart getJFCchart_production(PieDataset dataset) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("TW");
		standardChartTheme.setExtraLargeFont(new Font("新細明體", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("新細明體", Font.BOLD, 15));
		standardChartTheme.setLargeFont(new Font("新細明體", Font.BOLD, 15));
		ChartFactory.setChartTheme(standardChartTheme);
		String ys, ye;
		String title = "";
		String total = "";

			ys = (String) cbxProductionYS.getSelectedItem();
			ye = (String) cbxProductionYE.getSelectedItem();
			total = u.f1decimal(getTotalProductionLandSize(ys,ye));
			title = ys + " - " + ye + "期間生產計劃作物占比\n 合計" +total+ "甲";

		JFreeChart chart = ChartFactory.createPieChart(title, // chart title
				dataset, // data
				false, // include legend
				true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();
		//plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {1}甲 ({2})"));
		plot.setLabelGenerator(new ProductionVarietyLabel(slProductionHide.getValue()));   

        plot.setDirection(Rotation.ANTICLOCKWISE);
			
		plot.setCircular(false);
		plot.setLabelGap(0.05);
		
		plot.setNoDataMessage("No data available");

		// plot.setForegroundAlpha(0.8f);
        
		return chart;
	}
	
	
	//---------------------------MOST IMPORTANT --------------------------------------------------
	//Search single variety
	
	//single year
	//year range
	
	//Top 10 income by Variety
	//Top 10 landuse by Variety
	//Top 5 income by crop type
	//top 5 landuse by crop type
	
	private void update_listVegeProd_ProductionProd(boolean filterYear, boolean filterClass) {

		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		String year = (String)cbx_productionpcode_year.getSelectedItem();
		String croptype = (String)cbx_productionpcode_class.getSelectedItem();
		
		//ys = (String) cbxProductionYear.getSelectedItem();
		
		if (filterYear == true && filterClass == false )
			sql = "SELECT count(*), level2, pcname, pcode, sum(landsize) as subsize, sum(qty) as subqty FROM market.pro130 where year = '"+ year  +"' group by pcode order by level2, subsize desc ";
		else if (filterYear == true && filterClass == true && cbx_productionpcode_class.getSelectedIndex() != 0)
			sql = "SELECT count(*), level2, pcname, pcode, sum(landsize) as subsize, sum(qty) as subqty FROM market.pro130 where year = '"+ year  +"' and level2 = '"+ croptype  +"' group by pcode order by level2, subsize desc ";
		else
			sql = "SELECT count(*), level2, pcname, pcode, sum(landsize) as subsize, sum(qty) as subqty FROM market.pro130 where year = '"+ year  +"' group by pcode order by level2, subsize desc ";
		listVegeProd_model.removeAllElements();
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				listVegeProd_model.addElement(
						 rs.getString("pcode") + "    , "
						+rs.getString("level2") + " , " 
						+rs.getString("pcname") + " , "
						+u.f1decimal(rs.getFloat("subsize"))+"甲"+" "
						+u.f1decimal(rs.getFloat("subqty"))+"Kg"
						);
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("update_listVegeProd_ProductionProd Exception :" + e.toString());
		}


	}	
	
	private void update_listVegeProd_singleProd(String keyw) {
		ResultSet rs = null;
		Statement stat = null;
		
		String sql = "Select level2, pcode, pcname from vege_prod where level2 =  '"+ keyw + "' or pcode = '"+ keyw + "' or pcname like '" + keyw + "' order by pcode";
		listVegeProd_model.removeAllElements();
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				listVegeProd_model.addElement(
						 rs.getString("pcode") + "     , "
						+rs.getString("level2") + "     , " 
						+rs.getString("pcname") 
						);
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {u.debug("search single pcode Exception :"	+ e.toString());}

		listVegeProd.setModel(listVegeProd_model);
		
		listVegeProd.repaint();
	}	

	
	
	private void update_listVegeProd_AllProd(boolean filtercrop){
		ResultSet rs = null;
		Statement stat = null;
		String sql = "";
		if (filtercrop==false || cbx_allpcode_class.getSelectedIndex()==0) 
			sql = "Select level2, pcode, pcname from vege_prod order by pcode";
		else
			sql = "Select level2, pcode, pcname from vege_prod where level2 = '" + 
					(String)cbx_allpcode_class.getSelectedItem() + "' order by pcode";
		
		listVegeProd_model.removeAllElements();
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				listVegeProd_model.addElement(
						 rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") 
						);
			}
		}catch (SQLException e) {u.debug("search single pcode Exception :"	+ e.toString());}

		listVegeProd.setModel(listVegeProd_model);
		listVegeProd.repaint();		
	}
	
	private void update_listProductionVariety() {
		listProductionVariety_model.removeAllElements();
		for (int i = 0; i < production_percent_vec.size(); i++) {
			listProductionVariety_model.addElement(production_percent_vec
					.get(i).level2
					+ "; "
					+ production_percent_vec.get(i).landuse
					+ "甲; "
					+ u.f1decimal(production_percent_vec.get(i).percent)+ "%"
					+ "; "
					+ u.f1decimal(production_percent_vec.get(i).weight)+ "Kg"); 
		}
		
		listProductionVariety.setModel(listProductionVariety_model);
		listProductionVariety.repaint();
	}

	private void update_listProductionVarietyDetail() {
		listProductionVarietyDetail_model.removeAllElements();
		for (int i = 0; i < production_varietypercent_vec.size(); i++) {
			listProductionVarietyDetail_model.addElement(production_varietypercent_vec.get(i).pcname
					+ "; "
					+ production_varietypercent_vec.get(i).landuse
					+ "甲; "
					+ u.f1decimal(production_varietypercent_vec.get(i).percent)+ "%"
					+ "; "
					+ u.f1decimal(production_varietypercent_vec.get(i).weight)+ "Kg"); 
		}
		
		listProductionVarietyDetail.setModel(listProductionVarietyDetail_model);
		listProductionVarietyDetail.repaint();
	}
	
	/*
	
	private CostPriceData getCostPriceData(ResultSet rs) {
		CostPriceData cp = new CostPriceData();
		try {
			cp.pcode = rs.getString("pcode");
			cp.avgntcost = rs.getFloat("avgntcost");
			cp.avgntprice = rs.getFloat("avgntprice");
			cp.buykg = rs.getFloat("buykg");
			cp.soldkg = rs.getFloat("soldkg");
			cp.gp = rs.getFloat("gp");
			cp.year = rs.getInt("year");
		} catch (SQLException e) {
			u.debug("rs-->store vector exception :" + e.toString());
		}
		return cp;
	}

	private CostPriceData getOutCostData(ResultSet rs) {
		CostPriceData cp = new CostPriceData();
		try {
			cp.pcode = rs.getString("pcode");
			cp.avgntcost = rs.getFloat("avgntcost");
			cp.buykg = rs.getFloat("sumqty");
			cp.year = rs.getInt("year");
		} catch (SQLException e) {
			u.debug("rs-->store vector exception :" + e.toString());
		}
		return cp;
	}
*/
	private DefaultPieDataset getJFCdataset_production_variety() {

		Statement stat = null;
		ResultSet rs = null;
		String pcname = "";
		float landsize = 0;
		float totalsize = 0;
		float subqty =0;
		float percent =0;
		String sql = "";
		String ys = "", ye = "";
		
		//SELECT * FROM market.pro130 where year = 2011 and level2 = '西瓜'  order by landsize desc

			ys = (String) cbxProductionYS.getSelectedItem();
			ye = (String) cbxProductionYE.getSelectedItem();
			sql = "SELECT *, sum(landsize) as subsize, sum(qty) as subqty FROM market.pro130 where year >= " + ys
					+ " and year <= " + ye
					+ " and level2 = '" + production_percent_vec.get(selectedlevel2Index).level2 +"'"
					+ " group by pcode"
					+ " order by subsize desc";

		totalsize = getTotalProductionVarietyLandSize(ys, ye);

		u.debug(sql);

		DefaultPieDataset dataset = new DefaultPieDataset();

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			production_varietypercent_vec.removeAllElements();
			while (rs.next()) {
				pcname = rs.getString("pcname");
				landsize = rs.getFloat("subsize");
				percent = (landsize / totalsize) * 100;
				subqty = rs.getFloat("subqty");
				
				production_varietypercent_vec.add(new ProductionVarietyPercentData(pcname,landsize, percent,subqty));
				dataset.setValue(pcname, landsize);
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("Select vege_cost_price_year Exception :" + e.toString());
		}

		return dataset;
	}
	
	private DefaultPieDataset getJFCdataset_production() {

		Statement stat = null;
		ResultSet rs = null;
		String crop = "";
		float landsize = 0;
		float totalsize = 0;
		float subqty =0;
		float percent =0;
		String sql = "";
		String ys = "", ye = "";


			ys = (String) cbxProductionYS.getSelectedItem();
			ye = (String) cbxProductionYE.getSelectedItem();
			sql = "SELECT distinct level2, sum(landsize) as leveltotalsize, sum(qty) as levelqty FROM market.pro130 where year >= "
					+ ys
					+ " and year <= "
					+ ye
					+ " group by level2  order by leveltotalsize desc";


		totalsize = getTotalProductionLandSize(ys, ye);

		u.debug(sql);

		DefaultPieDataset dataset = new DefaultPieDataset();

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			production_percent_vec.removeAllElements();
			while (rs.next()) {
				crop = rs.getString("level2");
				landsize = rs.getFloat("leveltotalsize");
				
				percent = (landsize / totalsize) * 100;
				subqty = rs.getFloat("levelqty");
				
				production_percent_vec.add(new ProductionPercentData (crop, landsize, percent, subqty));
				dataset.setValue(crop, landsize);
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getJFCdataset_production Exception :" + e.toString());
		}

		return dataset;
	}

	private void insertProductionChart_variety() {
		final DefaultPieDataset tdataset = getJFCdataset_production_variety();
		final JFreeChart tchart = getJFCchart_production_variety(tdataset);
		ChartPanel jfcPanel = new ChartPanel(tchart);

		panelChartProduction.removeAll();
		panelChartProduction.setLayout(new BorderLayout());
		panelChartProduction.add(jfcPanel);
		panelChartProduction.validate();
		panelChartProduction.repaint();
		
	}	
	
	private void refreshProductionChart() {
		listProductionVarietyDetail_model.removeAllElements(); //clean out the list to avoid confusion
		
		final DefaultPieDataset tdataset = getJFCdataset_production();
		final JFreeChart tchart = getJFCchart_production(tdataset);
		ChartPanel jfcPanel = new ChartPanel(tchart);

		panelChartProduction.removeAll();
		panelChartProduction.setLayout(new BorderLayout());
		panelChartProduction.add(jfcPanel);
		panelChartProduction.validate();
		panelChartProduction.repaint();
		
	}

	
	
	private float getTotalProductionVarietyLandSize(String ys, String ye) {
		Statement stat = null;
		ResultSet rs = null;
		String sqltotal = "";
		int totalsize = 0;


			sqltotal = "SELECT sum(landsize) as totalsize FROM market.pro130 where year >= " + ys
					+ " and year <= " + ye
					+ " and level2 = '" + production_percent_vec.get(selectedlevel2Index).level2 +"'";
	

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqltotal);
			while (rs.next()) {
				totalsize = rs.getInt("totalsize");
			}
		} catch (SQLException e) {
			u.debug("SELECT totalsize Exception :" + e.toString());
		}

		return totalsize;
	}

	
	private float getTotalProductionLandSize(String ys, String ye) {
		Statement stat = null;
		ResultSet rs = null;
		String sqltotal = "";
		float totalsize = 0;


			sqltotal = "SELECT sum(landsize) as totalsize FROM market.pro130 where year >= "
					+ ys + " and year <= " + ye;
		

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqltotal);
			while (rs.next()) {
				totalsize = rs.getInt("totalsize");
			}
		} catch (SQLException e) {
			u.debug("SELECT totalsize Exception :" + e.toString());
		}

		return totalsize;
	}


	private void buildProductionVarietyQL_LQ() {
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder boldRightStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
		                                    .setBorder(stl.pen1Point())
		                                    .setBackgroundColor(Color.LIGHT_GRAY);
		StyleBuilder titleStyle        = stl.style(boldCenteredStyle)
		                                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
		                                    .setFontSize(15);
		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("#").setWidth(4);
		TextColumnBuilder<String>  colpcode  = col.column("品編", "pcode", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colpname  = col.column("品種名稱", "pname", type.stringType()).setWidth(10);
		TextColumnBuilder<BigDecimal>  colqty  = col.column("規劃產出[Kg]", "qty", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  collandsize  = col.column("規劃用地[甲]", "landsize", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colsoldkg = col.column("近期銷量(Kg)", "soldkg", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colavgntprice  = col.column("近期售價(NT/Kg)", "avgntprice", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colql  = col.column("每甲產能(Kg)", "ql", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colgp  = col.column("毛利%", "gp", type.bigDecimalType()).setWidth(6);
		TextColumnBuilder<BigDecimal>  colincome  = col.column("營收(NT萬)", "income", type.bigDecimalType()).setWidth(12);
		TextColumnBuilder<BigDecimal>  colprofit  = col.column("毛利X營收(NT萬)", "profit", type.bigDecimalType()).setWidth(12);

		
		if (radStatValueProfit.isSelected()) {
		} else if (radStatValueGP.isSelected()) {
		} else if (radStatValuePrice.isSelected()) {
		} else {
		}
		
		
		String title = "生產計劃 品種  價值評估";
		String subtitle = "生產計劃期間:" + cbxProductionYS.getSelectedItem() + " - " + cbxProductionYE.getSelectedItem();
		
		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow,colpcode, colcrop, colpname, colqty, collandsize, colsoldkg, colavgntprice, colql,colgp,colincome,colprofit)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  //.sortBy(desc(colsort))
			  .groupBy(itemGroup)

			  	.subtotalsAtGroupFooter(itemGroup, sbt.sum(colqty).setLabel("小計").setLabelStyle(boldRightStyle))
			  	.subtotalsAtGroupFooter(itemGroup, sbt.sum(collandsize).setLabel("小計").setLabelStyle(boldRightStyle))
			  	.subtotalsAtGroupFooter(itemGroup, sbt.sum(colincome).setLabel("小計").setLabelStyle(boldRightStyle))
			  	.subtotalsAtGroupFooter(itemGroup, sbt.sum(colprofit).setLabel("小計").setLabelStyle(boldRightStyle))
			  	
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_ProductionVarietyQL_LQ())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private JRDataSource getJRDS_ProductionVarietyQL_LQ() {
		Statement statlevel2 = null;
		Statement statvars = null;
		ResultSet rslevel2 = null;
		ResultSet rsvars = null;
		String sqllevel2 = ""; //getting crop type list
		String sqlvars = ""; //getting variety list of specific a crop type 
		String whereyear = "";
		String wherelevel2 = "";
		float avgntprice = 0;
		float income = 0;
		float gp = 0;
		float profit = 0;
		float soldkg = 0;
		DRDataSource dataSource = new DRDataSource("pcode", "crop", "pname","qty",
                "landsize","soldkg","avgntprice","ql","gp","income","profit");
		
		
		whereyear = " year >= " + cbxProductionYS.getSelectedItem() + " and year <= " + cbxProductionYE.getSelectedItem() + " ";
		
		if (cbx_production_vege_croplist.getSelectedIndex()==0)
			sqllevel2 = "select distinct level2 from pro130 where " + whereyear; //get distinct crop list of the year
		else
			sqllevel2 = "select '" + (String)cbx_production_vege_croplist.getSelectedItem() + "' as level2";
		
	
		try {
			statlevel2 = con.createStatement();
			rslevel2 = statlevel2.executeQuery(sqllevel2);
			
			//loop each crop type
			while(rslevel2.next()){
				wherelevel2 = " level2 = '" + rslevel2.getString("level2") + "' ";
				sqlvars = "select pcode, level2, pcname, sqty, slandsize, sqty/slandsize as ql, slandsize/sqty as lq, year"
						+ " from ("
								+ " Select *, sum(qty) as sqty, sum(landsize) as slandsize  "
								+ " from pro130 where " 
								+ whereyear + " and "
								+ wherelevel2
								+ " group by pcode) as t1  "
								+ " order by slandsize desc";
				/*
				sqlvars = "select pcode, level2, pcname, qty, landsize, "
						+ "qty/landsize as ql, "
						+ "landsize/qty as lq, "
						+ "year, season "
						+ " from pro130 "
						+ " where "
						+ whereyear
						+ " and "
						+ wherelevel2
						+ " group by pcode"
						+ " order by landsize desc"; //以占地排序
				*/
				
				u.debug(sqlvars);	

				statvars = con.createStatement();
				rsvars = statvars.executeQuery(sqlvars);
				
				//loop each variety
				while(rsvars.next()){
					avgntprice = db.getPcodeLatest_avgntprice(rsvars.getString("pcode"));
					income = (avgntprice * rsvars.getFloat("sqty")) / 10000;
					soldkg = db.getPcodeLatest_soldkg(rsvars.getString("pcode"));
					gp = db.getPcodeLatest_gp(rsvars.getString("pcode"));
					profit = income * (gp/100) ;
					
					dataSource.add(rsvars.getString("pcode"),
							rsvars.getString("level2"),
							rsvars.getString("pcname"),
							new BigDecimal(rsvars.getFloat("sqty")),
							new BigDecimal(rsvars.getFloat("slandsize")),
							new BigDecimal(soldkg),
							new BigDecimal(u.f1decimal(avgntprice)),
							new BigDecimal(rsvars.getFloat("ql")),
							new BigDecimal(gp),
							new BigDecimal(income),
							new BigDecimal(profit));
					//u.debug(rsvars.getString("level2")+", "+rsvars.getString("pcode"));
				}
			}
			if (statvars != null) statvars.close();
			if (rsvars != null) rsvars.close();
			if (statlevel2 != null) statlevel2.close();
			if (rslevel2 != null) rslevel2.close();
		} 
		catch (SQLException x) {
			System.out.println("getJRDS_ProductionVarietyQL_LQ Exception :" + x.toString());
		}		
		
		return dataSource;
	}
	
	private void buildStatVarietyQL_LQ() {
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(15);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);

		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("#").setWidth(4);;
		TextColumnBuilder<String>  colpcode  = col.column("品編", "pcode", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(5);
		TextColumnBuilder<String>  colpname  = col.column("品種名稱", "pname", type.stringType()).setWidth(10);
		TextColumnBuilder<String>  colql_ref  = col.column("參考期:產量:占地", "ref", type.stringType()).setWidth(15);
		//TextColumnBuilder<BigDecimal>  colqty  = col.column("當時規劃[Kg]", "qty", type.bigDecimalType()).setWidth(8);
		//TextColumnBuilder<BigDecimal>  collandsize  = col.column("當時用地[甲]", "landsize", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colsoldkg = col.column("近期銷量(Kg)", "soldkg", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colavgntprice  = col.column("近期售價(NT/Kg)", "avgntprice", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colql  = col.column("每甲產能(Kg)", "ql", type.bigDecimalType()).setWidth(8);
		TextColumnBuilder<BigDecimal>  colgp  = col.column("毛利%", "gp", type.bigDecimalType()).setWidth(6);
		TextColumnBuilder<BigDecimal>  colincome  = col.column("每甲收入(NT萬)", "income", type.bigDecimalType()).setWidth(12);
		TextColumnBuilder<BigDecimal>  colprofit  = col.column("毛利X收入(NT萬)", "profit", type.bigDecimalType()).setWidth(12);
		
		
		TextColumnBuilder<BigDecimal> colsort;
		if (radStatValueProfit.isSelected())
			colsort = colprofit;
		else if (radStatValueGP.isSelected())
			colsort = colgp;
		else if (radStatValuePrice.isSelected())
			colsort = colavgntprice;
		else
			colsort = colprofit;
		
		
		String title = "所有品種  價值評估";
		String subtitle = "";

		if (cbx_stat_valueyear.getSelectedIndex() == 0)
			subtitle = "期間:" + YS + " - " + YE;
		else
			subtitle = "期間:" + (YE-cbx_stat_valueyear.getSelectedIndex()) + " - " + YE;
		
		if (cbx_stat_prodclass.getSelectedIndex() > 0)
			title = (String) cbx_stat_prodclass.getSelectedItem() + " 品種價值評估 ";

		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow, colpcode, colcrop, colpname, colql_ref, colsoldkg, colql, colavgntprice,colgp,colincome,colprofit)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  .sortBy(desc(colsort))
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_StatVarietyQL_LQ())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_StatVarietyQL_LQ() {
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		DRDataSource dataSource = new DRDataSource("pcode", "crop", "pname","ref","soldkg","avgntprice","ql","gp","income","profit");
		float avgntprice = 0;
		float income = 0;
		float gp = 0;
		float profit = 0;
		float soldkg = 0;
		String whereyear = null;
		String wherelevel2 = "";
		String whereclause = "";
		if (cbx_stat_valueyear.getSelectedIndex() > 0)
			whereyear = " year >= " + String.valueOf(YE-cbx_stat_valueyear.getSelectedIndex()) + " and year <= " + String.valueOf(YE) + " ";
		else
			whereyear = " year >= " + String.valueOf(YS) + " and year <= " + String.valueOf(YE) + " ";

		if (cbx_stat_prodclass.getSelectedIndex() > 0){
			wherelevel2 = " t1.level2 = '" + (String) cbx_stat_prodclass.getSelectedItem() + "' ";
			if (whereyear != null)
				wherelevel2 = " and " + wherelevel2;
		}
			
		whereclause = " where " + whereyear + wherelevel2;
		
		sql = "select t1.pcode, t1.level2, t1.pcname, qty, landsize, "
				+ "qty/landsize as ql, "
				+ "landsize/qty as lq, "
				+ "max(concat(t1.year,season)) as yearseason, "
				+ "t1.year, season "
				+ " from pro130 as t1 "
				+ whereclause
				+ "group by pcode";	
		u.debug(sql);
	
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				avgntprice = db.getPcodeLatest_avgntprice(rs.getString("pcode"));
				income = (avgntprice * rs.getFloat("ql")) / 10000;
				gp = db.getPcodeLatest_gp(rs.getString("pcode"));
				soldkg = db.getPcodeLatest_soldkg(rs.getString("pcode"));
				profit = income * (gp/100) ;
				
				dataSource.add(rs.getString("pcode"),
						rs.getString("level2"),
						rs.getString("pcname"),
							rs.getString("yearseason")+" : "
							+u.f1decimal(new BigDecimal(rs.getFloat("qty")).floatValue())	+" : " 
							+u.f1decimal(new BigDecimal(rs.getFloat("landsize")).floatValue()),
							
						new BigDecimal(soldkg),
						new BigDecimal(u.f1decimal(avgntprice)),
						new BigDecimal(rs.getFloat("ql")),
						new BigDecimal(gp),
						new BigDecimal(income),
						new BigDecimal(profit));
			}
			stat.close();
			rs.close();
		} 
		catch (SQLException x) {
			System.out.println("getJRDS_StatVarietyQL_LQ Exception :" + x.toString());
		}
		
		return dataSource;
	}

	
	private void buildStatSalesRankseed_num() {
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(15);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);
		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("#").setWidth(4);
		TextColumnBuilder<Integer>  colorder  = col.column("排名", "rank", type.integerType()).setWidth(10);
		TextColumnBuilder<String>  colpcode  = col.column("品種編號", "pcode", type.stringType()).setWidth(10);
		TextColumnBuilder<String>  colseed_num  = col.column("組合號", "seed_num", type.stringType()).setWidth(15);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(10);
		TextColumnBuilder<String>  colpname  = col.column("品種名稱", "pname", type.stringType()).setWidth(30);
		TextColumnBuilder<Integer>  colsoldkg  = col.column("近期銷量[Kg]", "soldkg", type.integerType()).setWidth(10);
		TextColumnBuilder<Integer>  colsales  = col.column("營收[NT萬]", "sales", type.integerType()).setWidth(15);
		PercentageColumnBuilder    colsalesPer = col.percentageColumn("總營收佔比 [%]", colsales).setWidth(10);
		String title = "品種 [銷售金額] 排行表";
		String subtitle = "年度:"+(String)cbxStatYS.getSelectedItem() + "~"+(String)cbxStatYE.getSelectedItem() ;
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
			  .columns(colrow,colorder,colpcode,colseed_num,colcrop,colpname,colsoldkg,colsales,colsalesPer)
			  //.groupBy(colcrop)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))			  
			  .pageFooter(Templates.footerComponent)
			  
			  .setDataSource(getJRDS_StatSalesRankseed_num())
			  .show(false);
		} catch (DRException e) {
			e.printStackTrace();
		}
	}
	
	private JRDataSource getJRDS_StatSalesRankseed_num() {
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		DRDataSource dataSource = new DRDataSource("rank", "pcode", "seed_num", "crop", "pname","soldkg","sales");

		if (radStatSalesNT.isSelected()){
		sql = "SELECT vcp.pcode, level2, pcname, vp.seed_num, count(*) as c, "
				+ "sum(soldkg) as tsoldkg, sum(buykg) as tbuykg, "
				+ "sum(avgntprice*soldkg) as tsales, "
				+ "sum(avgntcost*buykg) as tpur, avg(gp) "
				+ "from vege_cost_price_year as vcp, vege_prod as vp where "
				+ "vcp.pcode = vp.pcode and "
				+ "year >= " + (String)cbxStatYS.getSelectedItem() + " and "
				+ "year <=" + (String)cbxStatYE.getSelectedItem()
				+" group by pcode order by tsales desc";	
		}
		else{
		sql = "SELECT vcp.pcode, level2, pcname, vp.seed_num, count(*) as c, "
				+ "sum(soldkg) as tsoldkg, sum(buykg) as tbuykg, "
				+ "sum(avgntprice*soldkg) as tsales, "
				+ "sum(avgntcost*buykg) as tpur, avg(gp) "
				+ "from vege_cost_price_year as vcp, vege_prod as vp where "
				+ "vcp.pcode = vp.pcode and "
				+ "year >= " + (String)cbxStatYS.getSelectedItem() + " and "
				+ "year <=" + (String)cbxStatYE.getSelectedItem()
				+" group by pcode order by tsoldkg desc";	
		}
		
		int rank = 1;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				dataSource.add(
						rank,
						rs.getString("pcode"),
						rs.getString("seed_num"),
						rs.getString("level2"),
						rs.getString("pcname"),
						rs.getInt("tsoldkg"),
						rs.getInt("tsales")/10000);
				rank++;
			}
			stat.close();
			rs.close();
		} 
		catch (SQLException x) {
			System.out.println("getJRDS_StatSalesRankseed_num Exception :" + x.toString());
		}
		
		return dataSource;
	}
	
	
	private void buildStatSalesRank() {
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(15);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);
		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("#").setWidth(4);
		TextColumnBuilder<Integer>  colorder  = col.column("排名", "rank", type.integerType()).setWidth(10);
		TextColumnBuilder<String>  colpcode  = col.column("品種編號", "pcode", type.stringType()).setWidth(10);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(10);
		TextColumnBuilder<String>  colpname  = col.column("品種名稱", "pname", type.stringType()).setWidth(30);
		TextColumnBuilder<Integer>  colsoldkg  = col.column("近期銷量[Kg]", "soldkg", type.integerType()).setWidth(15);
		TextColumnBuilder<Integer>  colsales  = col.column("營收[NT萬]", "sales", type.integerType()).setWidth(15);
		PercentageColumnBuilder    colsalesPer = col.percentageColumn("總營收佔比 [%]", colsales).setWidth(10);
		String title = "品種 [銷售金額] 排行表";
		String subtitle = "年度:"+(String)cbxStatYS.getSelectedItem() + "~"+(String)cbxStatYE.getSelectedItem() ;
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
			  .columns(colrow,colorder,colpcode,colcrop,colpname,colsoldkg,colsales,colsalesPer)
			  //.groupBy(colcrop)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))			  
			  .pageFooter(Templates.footerComponent)
			  
			  .setDataSource(getJRDS_StatSalesRank())
			  .show(false);
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_StatSalesRank() {
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		DRDataSource dataSource = new DRDataSource("rank", "pcode", "crop", "pname","soldkg","sales");

		if (radStatSalesNT.isSelected()){
		sql = "SELECT vcp.pcode, level2, pcname, count(*) as c, "
				+ "sum(soldkg) as tsoldkg, sum(buykg) as tbuykg, "
				+ "sum(avgntprice*soldkg) as tsales, "
				+ "sum(avgntcost*buykg) as tpur, avg(gp) "
				+ "from vege_cost_price_year as vcp, vege_prod as vp where "
				+ "vcp.pcode = vp.pcode and "
				+ "year >= " + (String)cbxStatYS.getSelectedItem() + " and "
				+ "year <=" + (String)cbxStatYE.getSelectedItem()
				+" group by pcode order by tsales desc";	
		}
		else{
		sql = "SELECT vcp.pcode, level2, pcname, count(*) as c, "
				+ "sum(soldkg) as tsoldkg, sum(buykg) as tbuykg, "
				+ "sum(avgntprice*soldkg) as tsales, "
				+ "sum(avgntcost*buykg) as tpur, avg(gp) "
				+ "from vege_cost_price_year as vcp, vege_prod as vp where "
				+ "vcp.pcode = vp.pcode and "
				+ "year >= " + (String)cbxStatYS.getSelectedItem() + " and "
				+ "year <=" + (String)cbxStatYE.getSelectedItem()
				+" group by pcode order by tsoldkg desc";	
		}
		
		int rank = 1;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				dataSource.add(
						rank,
						rs.getString("pcode"),
						rs.getString("level2"),
						rs.getString("pcname"),
						rs.getInt("tsoldkg"),
						rs.getInt("tsales")/10000);
				rank++;
			}
			stat.close();
			rs.close();
		} 
		catch (SQLException x) {
			System.out.println("getJRDS_StatSalesRank Exception :" + x.toString());
		}
		
		return dataSource;
	}
	
	
	static class ProductionVarietyLabel implements PieSectionLabelGenerator {
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
				if (Float.parseFloat(percent) >= this.percent) {//show ones > 1%
					result = title + "(" + landuse + "甲)" + "("+ percent +"%)"; 
				}
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

		ProductionVarietyLabel(int percent) {
			this.percent = percent;
		}

		ProductionVarietyLabel() {
			this.percent = 1;
		}
	}
}

/**
 * A custom label generator (returns null for one item as a test).
 */

