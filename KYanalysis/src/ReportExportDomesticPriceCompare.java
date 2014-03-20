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
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;

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


public class ReportExportDomesticPriceCompare extends JDialog {

	JComboBox<String> cbxProductionYS;
	JComboBox<String> cbx_rangesoldkg_logic;
	
	
	JComboBox<String> cbx_croplist;
	
	private int YE = Calendar.getInstance().get(Calendar.YEAR);
	private int YS = YE - 10;
	
	private final JPanel contentPanel = new JPanel();
	private KYdb db = new KYdb();
	private Connection con;
	private KYutil u = new KYutil();
	private JTextField txf_rangesoldkg;
	


	public ReportExportDomesticPriceCompare() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		con = db.getConnection();
		showUI();
				
	}
	
	private void showUI(){
		setTitle("品種國內外售價比較表");
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
						build_report();
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
		cbx_croplist.setBounds(46, 9, 123, 21);
		panel_2.add(cbx_croplist);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel);
		panel.setLayout(null);
		

		
		JLabel label_2 = new JLabel("銷售/生產統計期間");
		label_2.setBounds(29, 23, 123, 15);
		panel.add(label_2);
		
		cbxProductionYS = new JComboBox<String>(u.create10yearVector());
		cbxProductionYS.setBounds(162, 20, 84, 21);
		panel.add(cbxProductionYS);


			
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
	

	private void build_report() {
		String subtitle = "";
		String title = "";
		String ys = (String)cbxProductionYS.getSelectedItem();

		
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
		TextColumnBuilder<Long>  colexpup  = col.column("外銷年均價NT$", "expup", type.longType()).setWidth(6);
		TextColumnBuilder<Long>  coldomup  = col.column("內銷年均價NT$", "domup", type.longType()).setWidth(6);
		TextColumnBuilder<BigDecimal>  colratio  = col.column("內/外售價倍數", "ratio", type.bigDecimalType()).setWidth(5);
		TextColumnBuilder<Long>  colexpsales = col.column("外銷總額(NT$)", "expsales", type.longType()).setWidth(8);
		TextColumnBuilder<Long>  coldomsales  = col.column("內銷總額(NT$)", "domsales", type.longType()).setWidth(8);
		TextColumnBuilder<Long>  colexpweight  = col.column("外銷總量(Kg)", "expweight", type.longType()).setWidth(8);
		TextColumnBuilder<Long>  coldomweight  = col.column("內銷總量(Kg)", "domweight", type.longType()).setWidth(8);


		 title = "品種國內外售價比較表";
		 
		if (cbx_croplist.getSelectedIndex() > 0)
			title = (String)cbx_croplist.getSelectedItem() + " 國內外售價比較表   ";
		
		title = title + " 期間總銷量"+ cbx_rangesoldkg_logic.getSelectedItem() + txf_rangesoldkg.getText() + "公斤]";
				
		subtitle = "年度:" + ys;
		
		ColumnGroupBuilder itemGroup = grp.group(colcrop);
		itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));		
		
		try {
			report()
				.setColumnTitleStyle(columnTitleStyle)
			  //.setTemplate(Templates.reportTemplate)
				.setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
			  .columns(colrow, colpcode, colcrop, colpname, colexpup, coldomup, colratio, colexpsales, coldomsales, colexpweight, coldomweight )
			  .title(cmp.horizontalList()
					.add(
					cmp.text(title).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
					cmp.text(subtitle).setStyle(titleStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)).newRow()
					  .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))

			  
			  .pageFooter(Templates.footerComponent)
			  .setDataSource(getJRDS_report())
			  
			  .show(false);
			
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource getJRDS_report() {
		DRDataSource dataSource = new DRDataSource("pcode", "crop", "pname", "expup", "domup", "ratio", "expsales", "domsales", "expweight", "domweight");
		float expup = 0;
		float domup = 0;
		float expsales = 0;
		float domsales = 0;
		float expweight = 0;
		float domweight = 0;
		float ratio = 0;
		
		int row = 0;
		int count =0;
		String ys = (String)cbxProductionYS.getSelectedItem();
		String crop;
		if (cbx_croplist.getSelectedIndex() == 0)
			crop = "";
		else
			crop = (String)cbx_croplist.getSelectedItem();
				
		String pcode = "";
		String pcname = "";
		Statement statsao430 = null;
		Statement statdom430 = null;
		ResultSet rssao430 = null;
		ResultSet rsdom430 = null;				
		String sqldom430 = "";
		String sqlsao430 = "";
		
		Vector<PriceData> expPriceVec = db.getResultset_sao430_up_sales_weight(crop,ys);
		Vector<PriceData> domPriceVec = db.getResultset_dom430_up_sales_weight(crop,ys);
		
		u.debug("saosize:"+expPriceVec.size());
		u.debug("domsize:"+domPriceVec.size());
		
		for (int e=0;e<domPriceVec.size();e++){
			pcode = domPriceVec.get(e).pcode;
			crop = domPriceVec.get(e).crop;
			pcname = domPriceVec.get(e).pcname;
			domup = domPriceVec.get(e).up;
			domsales = domPriceVec.get(e).sales;
			domweight = domPriceVec.get(e).weight;
			
			expup = 0; expsales = 0; expweight = 0; ratio = 0;
			for (int d=0;d<expPriceVec.size();d++){
				if (pcode.contentEquals(expPriceVec.get(d).pcode)){
					expup = expPriceVec.get(d).up;
					expsales = expPriceVec.get(d).sales;
					expweight = expPriceVec.get(d).weight;
					break;
				}
			}
			
			if (expup > 0 ) ratio = (domup/expup);
			
			dataSource.add(pcode, crop, pcname, 
					Long.valueOf(Math.round(expup)),
					Long.valueOf(Math.round(domup)),
					new BigDecimal(ratio),
					Long.valueOf(Math.round(expsales)),
					Long.valueOf(Math.round(domsales)), 
					Long.valueOf(Math.round(expweight)), 
					Long.valueOf(Math.round(domweight)));
				
		}
		

	
		return dataSource;
	}

	
}
