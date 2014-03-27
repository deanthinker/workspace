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



public class ReportGPorderBySales extends JDialog {

	JComboBox<String> cbxProductionYS = null;
	JComboBox<String> cbxProductionYE = null;
	JComboBox<String> cbx_rangesoldkg_logic = null;
	JComboBox<String> cbx_gp_logic = null;
	JComboBox<String> cbx_gp_percent = null;
	JComboBox<String> cbx_croplist = null;
	JRadioButton rad_sortSales, rad_sortSoldkg, rad_sortGP = null;
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
	public ReportGPorderBySales() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		con = db.getConnection();
		showUI();
	}
	
	private void showUI(){
		JPanel contentPanel = new JPanel();		
		setTitle("�P��ƦW��Q���R");
		setBounds(100, 100, 458, 355);
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
				
		JLabel labelCrop = new JLabel("�L�o�@��");
		panel.add(labelCrop);
		labelCrop.setBounds(10, 10, 55, 18);
		
		cbx_croplist = new JComboBox<String>(db.getVegeCropVec());
		panel.add(cbx_croplist);
		cbx_croplist.setBounds(47, 9, 123, 21);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_top_crop.add(panel_1);
		
		JLabel label_5 = new JLabel("��Q�L�o����");
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
		panel_mid.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_mid.add(panel_2);
		
		JLabel lblSortBy = new JLabel("�Ƨ�");
		panel_2.add(lblSortBy);
		
		rad_sortSales = new JRadioButton("�P����B");
		rad_sortSales.setSelected(true);
		panel_2.add(rad_sortSales);
		grp_rad_sort.add(rad_sortSales);
		
		rad_sortSoldkg = new JRadioButton("�P���`��");
		panel_2.add(rad_sortSoldkg);
		grp_rad_sort.add(rad_sortSoldkg);
		
		rad_sortGP = new JRadioButton("��Q");
		panel_2.add(rad_sortGP);
		grp_rad_sort.add(rad_sortGP);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_mid.add(panel_3);
		
		JLabel lblExcludeNewProduct = new JLabel("�����P��b");
		panel_3.add(lblExcludeNewProduct);
		
		cbx_newprodyr = new JComboBox();
		panel_3.add(cbx_newprodyr);
		cbx_newprodyr.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		cbx_newprodyr.setSelectedIndex(2);
		
		JLabel lblYear = new JLabel("�~�������s���~");
		panel_3.add(lblYear);
		
		
		JPanel panel_bottom = new JPanel();
		contentPanel.add(panel_bottom);
		panel_bottom.setLayout(null);
		
		JLabel label_2 = new JLabel("�P��έp����");
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

		TextColumnBuilder<Integer>  colrow  = col.reportRowNumberColumn("�ƦW").setWidth(3);
		TextColumnBuilder<String>  colnewp  = col.column("�W��", "newp", type.stringType()).setWidth(3);
		TextColumnBuilder<String>  colpcode  = col.column("�~�s", "pcode", type.stringType()).setWidth(4);
		TextColumnBuilder<String>  colpcname  = col.column("�~�ئW��", "pcname", type.stringType()).setWidth(6);
		TextColumnBuilder<String>  colcrop  = col.column("�@��", "crop", type.stringType()).setWidth(4);
		TextColumnBuilder<BigDecimal>  colsales  = col.column("�P��(NT�U)", "sales", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(4);
		TextColumnBuilder<BigDecimal>  colsalesper  = col.column("�e��%", "sper", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(3);		
		TextColumnBuilder<BigDecimal>  colrunper  = col.column("�֭p%", "runper", type.bigDecimalType())
				.setPattern("#,##0.0")
				.setWidth(3);	
		TextColumnBuilder<BigDecimal>  colsoldkg  = col.column("�P��Kg", "soldkg", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(4);
		TextColumnBuilder<String>  collastdeal  = col.column("������", "lastdeal", type.stringType())
			.setHorizontalAlignment(HorizontalAlignment.CENTER)
			.setWidth(3);
		TextColumnBuilder<BigDecimal>  colavgntprice  = col.column("������(NT/Kg)", "avgntprice", type.bigDecimalType())
			.setPattern("#,##0")
			.setWidth(4);
		TextColumnBuilder<BigDecimal>  colavgntcost  = col.column("�������(NT/Kg)", "avgntcost", type.bigDecimalType())
			.setPattern("#,##0")
			.setWidth(4);
		TextColumnBuilder<BigDecimal>  colavggp  = col.column("�����Q%", "avggp", type.bigDecimalType())
			.setPattern("#,##0.0")
			.setWidth(3);
		/*
		TextColumnBuilder<BigDecimal> colsort;
		
		if (rad_sortSales.isSelected())
			colsort = colsales;
		else if (rad_sortSoldkg.isSelected())
			colsort = colsoldkg;
		else
			colsort = colavggp;
		 */
		title = "�Ͳ����Ȥ�Q���R ";
		
		
		if (cbx_croplist.getSelectedIndex() > 0)
			title = title + " [�@��: " + (String)cbx_croplist.getSelectedItem() +"]";
		
		if (rad_sortSales.isSelected())
			title = title + " [�H�P���B�Ƨ�]";
		else if (rad_sortSoldkg.isSelected())
			title = title + " [�H�P��q�Ƨ�]";
		else
			title = title + " [�H��Q�Ƨ�]";
		
		if (cbx_gp_percent.getSelectedIndex() > 0)
			title = title + "  [��Q " + cbx_gp_logic.getSelectedItem() + cbx_gp_percent.getSelectedItem() + "%" + "]  ";
				
		subtitle = "�P��έp����:" + ys +  " - " + ye;

		
		
		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow, colnewp, colpcode, colcrop, colpcname, colsales, colsalesper, colrunper, colsoldkg, collastdeal, colavgntprice, colavgntcost, colavggp)
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
			  
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_GPsalesParam())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_GPsalesParam() {
		Statement stat = null;
		ResultSet rs = null;
		DRDataSource dataSource = new DRDataSource("pcode", "newp", "crop", "pcname", "sales", "sper", "runper", "soldkg", "lastdeal", "avgntprice", "avgntcost", "avggp");
		float avgntprice = 0;
		float avgntcost = 0;
		float avggp = 0;
		float sales = 0;
		float salesper = 0;
		float runper = 0;
		float soldkg = 0;
		String lastdeal = "";
		String ys = (String)cbxProductionYS.getSelectedItem();
		String ye = (String)cbxProductionYE.getSelectedItem();
		String pcode = "";
		String crop = "";
		String pcname = "";
		String newp = "";

		String whereyear = " year >= "+ys+ " and year <= " + ye+ " "; //production year range
		
		String wherelevel2 ="";  
		if (cbx_croplist.getSelectedIndex()!=0) //the first item does not apply filter
			wherelevel2 = " and level2 = '" + (String) cbx_croplist.getSelectedItem() + "'"; //production crop type

		String wheregp = " gp " + cbx_gp_logic.getSelectedItem() + " " + cbx_gp_percent.getSelectedItem();
		String whereclause = "";
		if (cbx_croplist.getSelectedIndex() == 0) //all crop
			whereclause = whereyear;
		else
			whereclause = whereyear + " and " + wherelevel2;

		String orderclause ="";		
		if (rad_sortSales.isSelected())
			orderclause = " ts";
		else if (rad_sortSoldkg.isSelected())
			orderclause = " skg";
		else
			orderclause = " gp";
		
		String sql =
				"SELECT *, (@running := @running + sper) FROM "
				+"( "
				+"	SELECT v.pcode, level2, pcname, firstdeal, skg, bkg, ts/10000 as ts, tc/10000 as tc, (ts/t2.totalsales)*100 as sper, lastdeal, (ts/skg) as avgprice, (tc/bkg) as avgcost, (((ts/skg)-(tc/bkg))/(ts/skg)*100) as gp from "
				+"	( "
				+"		(SELECT I.pcode, sum(soldkg) as skg, sum(buykg) as bkg, sum(tsales) as ts, sum(tcost) as tc FROM integratedgp as I, vege_prod as V where I.pcode = V.pcode " + wherelevel2 + " and " + whereyear + " group by pcode )as t1,"
				+"		(SELECT sum(tsales) as totalsales FROM market.integratedgp where " + whereyear + ") as t2, "
				+"		(SELECT pcode, min(year)  as firstdeal, max(year) as lastdeal FROM market.integratedgp where year < 2099 group by pcode ) as t3, "
				+"		vege_prod as v"
				+"	) "
				+"	WHERE t1.pcode = v.pcode and t1.pcode = t3.pcode" 
				+") as TA, (select @running:=0) as TB where " + wheregp + " order by " + orderclause + " desc ";
		
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {	 //loop throught all pcodes
				pcode = rs.getString("pcode");
				crop = rs.getString("level2");
				pcname = rs.getString("pcname");
				sales = rs.getFloat("ts");
				salesper = rs.getFloat("sper");
				runper = runper + salesper; 
				soldkg = rs.getFloat("skg");
				lastdeal = rs.getString("lastdeal");
				avgntprice = rs.getFloat("avgprice");
				avgntcost = rs.getFloat("avgcost");
				avggp = rs.getFloat("gp");
				
				if ( YE - rs.getInt("firstdeal") < Integer.valueOf(cbx_newprodyr.getSelectedItem().toString()) )
					newp = "<" + cbx_newprodyr.getSelectedItem().toString() + "Yr";
				else
					newp = rs.getString("firstdeal");
				
				dataSource.add(pcode, newp, crop, pcname, 
						new BigDecimal(sales),
						new BigDecimal(salesper),
						new BigDecimal(runper),
						new BigDecimal(soldkg),
						lastdeal, 
						new BigDecimal(avgntprice), 
						new BigDecimal(avgntcost),
						new BigDecimal(avggp));
			}
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			u.debug("getJRDS_GPlanduseParam Exception :" + e.toString());
		}
		
		return dataSource;
	}
}
