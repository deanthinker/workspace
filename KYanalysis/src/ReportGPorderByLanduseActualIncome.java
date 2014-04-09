import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.examples.Templates;
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

import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;



public class ReportGPorderByLanduseActualIncome extends JDialog {
	JCheckBox chkMarkGP = null;
	JComboBox<String> cbxMarkGPlogic = null;
	JComboBox<String> cbxMarkGP = null;
	JComboBox<String> cbxProductionYS = null;
	JComboBox<String> cbxProductionYE = null;
	JComboBox<String> cbx_rangesoldkg_logic = null;
	JComboBox<String> cbx_gp_logic = null;
	JComboBox<String> cbx_gp_percent = null;
	JComboBox<String> cbx_croplist = null;
	JRadioButton rad_sortLand, rad_sortProdkg, rad_sortActincome, rad_sortSales, rad_sortLandearn = null;
	JComboBox cbx_newprodyr = null;
	ButtonGroup grp_rad_sort = new ButtonGroup();
	
	
	private int YE = Calendar.getInstance().get(Calendar.YEAR);
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
	public ReportGPorderByLanduseActualIncome() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		con = db.getConnection();
		showUI();
	}
	
	private void showUI(){
		JPanel contentPanel = new JPanel();		
		setTitle("生產價值毛利分析");
		setBounds(100, 100, 458, 403);
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
						build_GPlanduseParam();
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

		contentPanel.setLayout(new GridLayout(3,1, 0, 0));
				
		JPanel panel_top = new JPanel();
		contentPanel.add(panel_top);
		panel_top.setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel panel_top_crop = new JPanel();
		panel_top.add(panel_top_crop);
		panel_top_crop.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_top_crop.add(panel);
				
		JLabel labelCrop = new JLabel("過濾作物");
		panel.add(labelCrop);
		labelCrop.setBounds(10, 10, 55, 18);
		
		cbx_croplist = new JComboBox<String>(db.getVegeCropVec());
		panel.add(cbx_croplist);
		cbx_croplist.setBounds(47, 9, 123, 21);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_top_crop.add(panel_1);
		
		JLabel label_5 = new JLabel("毛利過濾條件");
		panel_1.add(label_5);
		
		cbx_gp_logic = new JComboBox<String>();
		panel_1.add(cbx_gp_logic);
		cbx_gp_logic.setModel(new DefaultComboBoxModel(new String[] { ">=", "<=", "=",">", "<"}));
		
		cbx_gp_percent = new JComboBox<String>();
		panel_1.add(cbx_gp_percent);
		cbx_gp_percent.setModel(new DefaultComboBoxModel(new String[] {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"}));
		
		JLabel label_1 = new JLabel("%");
		panel_1.add(label_1);
		
		JPanel panel_mid = new JPanel();
		contentPanel.add(panel_mid);
		panel_mid.setLayout(new GridLayout(3, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_mid.add(panel_2);
		
		JLabel lblSortBy = new JLabel("排序");
		panel_2.add(lblSortBy);
		
		rad_sortLand = new JRadioButton("生產總面積");
		rad_sortLand.setSelected(true);
		panel_2.add(rad_sortLand);
		grp_rad_sort.add(rad_sortLand);

		rad_sortActincome = new JRadioButton("實收");
		rad_sortActincome.setSelected(true);
		panel_2.add(rad_sortActincome);
		grp_rad_sort.add(rad_sortActincome);

		rad_sortLandearn = new JRadioButton("貢獻");
		rad_sortLandearn.setSelected(true);
		panel_2.add(rad_sortLandearn);
		grp_rad_sort.add(rad_sortLandearn);		
		
		rad_sortSales = new JRadioButton("銷售額");
		rad_sortSales.setSelected(true);
		panel_2.add(rad_sortSales);
		grp_rad_sort.add(rad_sortSales);
		
		rad_sortProdkg = new JRadioButton("生產總重");
		panel_2.add(rad_sortProdkg);
		grp_rad_sort.add(rad_sortProdkg);
		
		JRadioButton rad_sortGP = new JRadioButton("毛利");
		panel_2.add(rad_sortGP);
		grp_rad_sort.add(rad_sortGP);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_mid.add(panel_3);
		
		JLabel lblExcludeNewProduct = new JLabel("首次銷售在");
		panel_3.add(lblExcludeNewProduct);
		
		cbx_newprodyr = new JComboBox();
		panel_3.add(cbx_newprodyr);
		cbx_newprodyr.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		cbx_newprodyr.setSelectedIndex(2);
		
		JLabel lblYear = new JLabel("年內視為新產品");
		panel_3.add(lblYear);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_4.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_mid.add(panel_4);
		
		chkMarkGP = new JCheckBox();
		chkMarkGP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chkMarkGP.isSelected()){
					cbxMarkGPlogic.setEnabled(true);
					cbxMarkGP.setEnabled(true);
				}
				else{
					cbxMarkGPlogic.setEnabled(false);
					cbxMarkGP.setEnabled(false);
				}
			}
		});
		panel_4.add(chkMarkGP);
		
		JLabel label = new JLabel("標記毛利");
		panel_4.add(label);
		
		cbxMarkGPlogic = new JComboBox<String>();
		cbxMarkGPlogic.setModel(new DefaultComboBoxModel(new String[] {">=", "<="}));
		panel_4.add(cbxMarkGPlogic);
		
		cbxMarkGP = new JComboBox<String>();
		cbxMarkGP.setModel(new DefaultComboBoxModel(new String[] {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"}));
		panel_4.add(cbxMarkGP);
		
		JLabel label_3 = new JLabel("%");
		panel_4.add(label_3);

		//default setting  mark <= 40%
		chkMarkGP.setSelected(true);
		cbxMarkGPlogic.setSelectedIndex(1);
		cbxMarkGP.setSelectedIndex(8);		
		
		JPanel panel_bottom = new JPanel();
		contentPanel.add(panel_bottom);
		panel_bottom.setLayout(null);
		
		JLabel label_2 = new JLabel("生產計畫期間");
		label_2.setBounds(10, 23, 82, 15);
		panel_bottom.add(label_2);
		
		cbxProductionYS = new JComboBox<String>(u.create10yearVector());
		cbxProductionYS.setBounds(93, 17, 64, 21);
		panel_bottom.add(cbxProductionYS);
		
				cbxProductionYE = new JComboBox<String>(u.create10yearVector());
				cbxProductionYE.setBounds(171, 17, 69, 21);
				panel_bottom.add(cbxProductionYE);
				
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
						

	}
	

	private void build_GPlanduseParam() {
		String subtitle = "";
		String title = "";
		String ys = (String)cbxProductionYS.getSelectedItem();
		String ye = (String)cbxProductionYE.getSelectedItem();
		
		StyleBuilder boldStyle         = stl.style().bold();
		StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		StyleBuilder titleStyle = stl.style(boldCenteredStyle)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(14);

		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
										.setBorder(stl.pen1Point())
                                        .setBackgroundColor(Color.LIGHT_GRAY);
		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("排名").setWidth(2);
		TextColumnBuilder<String>  colnewp  = col.column("上市", "newp", type.stringType()).setWidth(2);
		TextColumnBuilder<String>  colpcode  = col.column("品編", "pcode", type.stringType()).setWidth(2);
		TextColumnBuilder<String>  colpcname  = col.column("品種名稱", "pcname", type.stringType()).setWidth(4);
		TextColumnBuilder<String>  colcrop  = col.column("作物", "crop", type.stringType()).setWidth(3);
		TextColumnBuilder<BigDecimal>  collanduse  = col.column("占地甲", "landuse", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(2);
		TextColumnBuilder<BigDecimal>  collandperc  = col.column("占地%", "landperc", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(2);		
		TextColumnBuilder<BigDecimal>  colrunper  = col.column("累計%", "runper", type.bigDecimalType())
				.setPattern("#,##0.0")
				.setWidth(2);	
		TextColumnBuilder<BigDecimal>  colprodkg  = col.column("目標Kg", "prodkg", type.bigDecimalType())
			.setPattern("#,##0")	
			.setWidth(3);
		TextColumnBuilder<BigDecimal>  colsales  = col.column("銷售(NT萬)", "sales", type.bigDecimalType())
				.setPattern("#,##0.0")	
				.setWidth(3);
		TextColumnBuilder<BigDecimal>  colactincome  = col.column("實收(NT萬)", "actincome", type.bigDecimalType())
				.setPattern("#,##0.0")	
				.setWidth(3);
		TextColumnBuilder<BigDecimal>  colactincomeper  = col.column("佔比%", "actincomeper", type.bigDecimalType())
				.setPattern("#,##0.0")	
				.setWidth(2);
		TextColumnBuilder<BigDecimal>  colarunper  = col.column("累計%", "arunper", type.bigDecimalType())
				.setPattern("#,##0.0")	
				.setWidth(2);		
		TextColumnBuilder<BigDecimal>  collandearn  = col.column("每甲貢獻", "landearn", type.bigDecimalType())
				.setPattern("#,##0.0")	
				.setWidth(3);
		TextColumnBuilder<String>  collastdeal  = col.column("近期交易", "lastdeal", type.stringType())
			.setHorizontalAlignment(HorizontalAlignment.CENTER)
			.setWidth(3);
		TextColumnBuilder<BigDecimal>  colavgntprice  = col.column("近期售價(NT/Kg)", "avgntprice", type.bigDecimalType())
			.setPattern("#,##0")
			.setWidth(3);
		TextColumnBuilder<BigDecimal>  colavgntcost  = col.column("近期成本(NT/Kg)", "avgntcost", type.bigDecimalType())
			.setPattern("#,##0")
			.setWidth(3);
		TextColumnBuilder<BigDecimal>  colavggp  = col.column("近期毛利%", "avggp", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(2);
		TextColumnBuilder<String>  colmark  = col.column("*", "mark", type.stringType()).setWidth(1);
		
		TextColumnBuilder<BigDecimal> colsort;
		
		if (rad_sortLand.isSelected())
			colsort = collanduse;
		else if (rad_sortProdkg.isSelected())
			colsort = colprodkg;
		else if (rad_sortActincome.isSelected())
			colsort = colactincome;
		else if (rad_sortSales.isSelected())
			colsort = colsales;
		else if (rad_sortLandearn.isSelected())
			colsort = collandearn;
		else
			colsort = colavggp;

		title = "生產價值毛利分析 ";
		
		
		if (cbx_croplist.getSelectedIndex() > 0)
			title = title + " [作物: " + (String)cbx_croplist.getSelectedItem() +"]";

		if (rad_sortLand.isSelected())
			title = title + " [以生產占地排序]";
		else if (rad_sortProdkg.isSelected())
			title = title + " [以生產重量排序]";
		else if (rad_sortActincome.isSelected())
			title = title + " [以實收排序]";
		else if (rad_sortSales.isSelected())
			title = title + " [以銷售額排序]";
		else if (rad_sortLandearn.isSelected())
			title = title + " [以貢獻排序]";
		else
			title = title + " [以毛利排序]";		
		
		
		if (cbx_gp_percent.getSelectedIndex() > 0)
			title = title + "  [毛利 " + cbx_gp_logic.getSelectedItem() + cbx_gp_percent.getSelectedItem() + "%" + "]  ";
				
		subtitle = "生產期間:" + ys +  " - " + ye;

		
		
		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow, colnewp, colpcode, colcrop, colpcname, collanduse, collandperc, colrunper, colprodkg, colsales, colactincome, colactincomeper, colarunper, collandearn, collastdeal, colavgntprice, colavgntcost, colavggp, colmark)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  .sortBy(desc(colsort))
			  
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_GPlanduseParam())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_GPlanduseParam() {
		Statement stat = null;
		ResultSet rs = null;
		DRDataSource dataSource = new DRDataSource("pcode", "newp", "crop", "pcname", "landuse", "landperc", "runper", "prodkg", "sales", "actincome", "actincomeper", "arunper", "landearn", "lastdeal", "avgntprice", "avgntcost", "avggp", "mark");
		float avgntprice = 0;
		float avgntcost = 0;
		float avggp = 0;
		float landuse = 0;
		float landperc = 0;
		float runper = 0;
		float prodkg = 0;
		float sales = 0;
		float actincome = 0;
		float actincomeper = 0;
		float arunper = 0;
		float landearn = 1;
		String mark = "";		
		String lastdeal = "";
		String ys = (String)cbxProductionYS.getSelectedItem();
		String ye = (String)cbxProductionYE.getSelectedItem();
		float rangeActincome = db.getPcodePeriod_rangeActIncomeIntegrated("ALL",ys,ye);
		System.out.println("rangeActincome:"+rangeActincome);
		String pcode = "";
		String crop = "";
		String pcname = "";
		String newp = "";

		String whereyear = " year >= "+ys+ " and year <= " + ye+ " "; //production year range
		String wherelevel2 =  "level2 = '" + (String) cbx_croplist.getSelectedItem() + "'"; //production crop type
		String wheregp = " gp " + cbx_gp_logic.getSelectedItem() + " " + cbx_gp_percent.getSelectedItem();
		String whereclause = "";
		
		if (cbx_croplist.getSelectedIndex() == 0) //all crop
			whereclause = whereyear;
		else
			whereclause = whereyear + " and " + wherelevel2;
		
		String sql =
		"SELECT t1.pcode, t1.level2, t1.pcname, land, perc, prodkg, firstdeal, lastdeal, avgntcost, avgntprice, gp from" 
		
		+" (SELECT pcode, level2, pcname, Format(sland,2) as land, Format( (sland/TT.tland) * 100, 2) as perc, prodkg, year as prodyr"
		+" FROM(SELECT *, sum(landsize) as sland, sum(qty) as prodkg "
		+" FROM market.pro130 where " + whereclause + " group by pcode order by sland desc, prodkg desc) as A,"
		+" (SELECT sum(landsize) as tland from pro130 where " + whereyear + ") as TT) as t1"

		+" left join" 

		+" (Select b.*, firstdeal, lastdeal from integratedgp as b inner join "
		+" (SELECT pcode, min(year) as firstdeal, max(year) as lastdeal FROM integratedgp where gp > 0 and year < 2099 group by pcode) as a"  //year above 2099 is not valid data
		+" on a.pcode = b.pcode and a.lastdeal = b.year ) as t2 "  
		
		+" on t1.pcode = t2.pcode "
		+" where " + wheregp;
		
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {	 //loop throught all pcodes
				pcode = rs.getString("pcode");
				crop = rs.getString("level2");
				pcname = rs.getString("pcname");
				landuse = rs.getFloat("land");
				landperc = rs.getFloat("perc");
				runper = runper + landperc;
				prodkg = rs.getFloat("prodkg");
				avggp = rs.getFloat("gp");
				
				sales = db.getPcodePeriod_rangeincomeIntegrated(pcode,ys,ye);
				actincome = sales * (avggp/100);
				actincomeper = (actincome / rangeActincome)*100;
				arunper = arunper + actincomeper;
				lastdeal = rs.getString("lastdeal");
				avgntprice = rs.getFloat("avgntprice");
				avgntcost = rs.getFloat("avgntcost");
				
				if (landperc != 0)
					landearn = actincome / landuse; //larger = better
				
				System.out.println("landearn:"+landearn + "   actincomeper:"+actincomeper + "  landperc:"+landperc);
				
				if ( YE - rs.getInt("firstdeal") < Integer.valueOf(cbx_newprodyr.getSelectedItem().toString()) )
					newp = "<" + cbx_newprodyr.getSelectedItem().toString() + "Yr";
				else
					newp = rs.getString("firstdeal");
				
				if ( chkMarkGP.isSelected()){
					if (cbxMarkGPlogic.getSelectedItem().equals(">=")){
						if (avggp >= Integer.valueOf(  (String)(cbxMarkGP.getSelectedItem())  ))
							mark = "*";
						else
							mark = "";
					}else{
						if (avggp <= Integer.valueOf(  (String)(cbxMarkGP.getSelectedItem())  ))
							mark = "*";
						else
							mark = "";						
					}
				}					
				dataSource.add(pcode, newp, crop, pcname, 
						new BigDecimal(landuse),
						new BigDecimal(landperc),
						new BigDecimal(runper),
						new BigDecimal(prodkg),
						new BigDecimal(sales),
						new BigDecimal(actincome),
						new BigDecimal(actincomeper),
						new BigDecimal(arunper),
						new BigDecimal(landearn),
						lastdeal, 
						new BigDecimal(avgntprice), 
						new BigDecimal(avgntcost),
						new BigDecimal(avggp), 
						mark);
			}
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			u.debug("getJRDS_GPlanduseParam Exception :" + e.toString());
		}
		
		return dataSource;
	}
}
