package importDB;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.ScrollPaneConstants;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement;

import javax.swing.AbstractAction;
import javax.swing.Action;

import java.awt.Font; 

public class MainLoad extends JFrame {
	

	private JPanel contentPane;
	private JTextField tf_xlsfilename;
	private JTextField tf_serverip;
	private JTextField tf_user;
	private JTextField tf_dbName;
	
	  private Connection con = null; //Database objects 
	  //連接object 
	  private Statement stat = null; 
	  private Statement tmpstat = null;
	  //執行,傳入之sql為完整字串 
	  private ResultSet rs = null; 
	  //結果集 
	  private PreparedStatement pst = null; 
	  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
	  //先利用?來做標示 
	  

	  private JTextField tf_password;
	  private JTextField tf_startyear;
	  private JTextField tf_endyear;
	  
	/**
	 * Launch the application.
	 */

	private void openXLS(){
		String filename = tf_xlsfilename.getText();
		Workbook workbook = null;
		Sheet sheet = null;
		int rows, cols =0;
		try {
			debug("Excel:"+filename);
			workbook = Workbook.getWorkbook(new File(filename));
			debug("Excel Loaded Successfully!");
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		debug("ttt");
		sheet = workbook.getSheet(0);
		rows = sheet.getRows();
		cols = sheet.getColumns();
		debug("total rows:" + rows + " col:"+cols);
		
		
		if (rows > 0){
			for (int r=0; r < rows; r++){
				System.out.print("Row"+r+"\t");
				for (int x=0; x < cols; x++){
					//headers[x] = sheet.getCell(x, 0).getContents();
					if (x == cols-1)
						System.out.println(sheet.getCell(x, r).getContents() + "\t");
					else
						System.out.print(sheet.getCell(x, r).getContents() + "\t");
				}
			}
		}

		
		
	}

 
		private void setMysqlCon(){
			/*
			  String driver = "com.mysql.jdbc.Driver";
			  String serverip = tf_serverip.getText();
			  String dbTable = tf_dbName.getText();
			  String username = tf_user.getText();
			  String password = tf_password.getText();
			 */

			  String driver = "com.mysql.jdbc.Driver";
			  String serverip = "localhost";
			  String dbTable = "market";
			  String username = "root";
			  String password = "1234";
			
			  String connURL = "jdbc:mysql://" + serverip + "/" + dbTable + "?useUnicode=true&characterEncoding=Big5";
			  debug("loading database");
			  debug(connURL);
			  debug("user:"+username + "  password:"+password);
			  
			
		    try { 
		      Class.forName(driver);    //註冊driver
		      con = DriverManager.getConnection(connURL,username,password);
		      debug("database connected!");
		      
		    } 
		    catch(ClassNotFoundException e) 
		    { 
		      System.out.println("DriverClassNotFound :"+e.toString()); 
		    }//有可能會產生sqlexception 
		    catch(SQLException x) { 
		      System.out.println("Exception :"+x.toString()); 
		    } 		
		}
 
 
	  private void Close() 
	  { 
	    try 
	    { 
	      if(rs!=null) 
	      { 
	        rs.close(); 
	        rs = null; 
	      } 
	      if(stat!=null) 
	      { 
	        stat.close(); 
	        stat = null; 
	      } 
	      if(pst!=null) 
	      { 
	        pst.close(); 
	        pst = null; 
	      } 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	  } 	
	
	/**
	 * Create the frame.
	 */
	public MainLoad() {
		setMysqlCon();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 792, 683);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tf_xlsfilename = new JTextField();
		tf_xlsfilename.setText("D:\\KY Excel\\watermelon_datasheet20131028.xls");
		tf_xlsfilename.setBounds(109, 487, 272, 21);
		contentPane.add(tf_xlsfilename);
		tf_xlsfilename.setColumns(10);
		
		tf_serverip = new JTextField();
		tf_serverip.setText("localhost");
		tf_serverip.setBounds(109, 519, 145, 21);
		contentPane.add(tf_serverip);
		tf_serverip.setColumns(10);
		
		tf_user = new JTextField();
		tf_user.setText("root");
		tf_user.setBounds(109, 582, 145, 21);
		contentPane.add(tf_user);
		tf_user.setColumns(10);
		
		JLabel lblExcelFileName = new JLabel("Excel File Name");
		lblExcelFileName.setBounds(10, 490, 89, 15);
		contentPane.add(lblExcelFileName);
		
		JLabel lblDbServerIp = new JLabel("DB Server IP");
		lblDbServerIp.setBounds(10, 522, 89, 15);
		contentPane.add(lblDbServerIp);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 585, 89, 15);
		contentPane.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 616, 89, 15);
		contentPane.add(lblPassword);
		
		
		JButton btn_openxls = new JButton("Open File");
		btn_openxls.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("openning excel file");
				openXLS();
			}
		});
		btn_openxls.setBounds(391, 486, 117, 21);
		contentPane.add(btn_openxls);
		
		JLabel lblDbTable = new JLabel("DB Name");
		lblDbTable.setBounds(10, 553, 89, 15);
		contentPane.add(lblDbTable);
		
		tf_dbName = new JTextField();
		tf_dbName.setText("market");
		tf_dbName.setColumns(10);
		tf_dbName.setBounds(109, 550, 145, 21);
		contentPane.add(tf_dbName);
		
		tf_password = new JTextField();
		tf_password.setText("1234");
		tf_password.setBounds(109, 613, 145, 21);
		contentPane.add(tf_password);
		tf_password.setColumns(10);
		
		JButton btnCreateVarietyCostYear = new JButton("Create Variety Cost Year");
		btnCreateVarietyCostYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createVarietyCostYear();
			}
		});
		btnCreateVarietyCostYear.setBounds(22, 77, 180, 23);
		contentPane.add(btnCreateVarietyCostYear);
		
		JButton btnVarietyPriceYear = new JButton("Create Variety Price Year");
		btnVarietyPriceYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createVarietyPriceYear();
			}
		});
		btnVarietyPriceYear.setBounds(22, 110, 180, 23);
		contentPane.add(btnVarietyPriceYear);
		
		tf_startyear = new JTextField();
		tf_startyear.setText("2003");
		tf_startyear.setBounds(203, 78, 49, 21);
		contentPane.add(tf_startyear);
		tf_startyear.setColumns(10);
		
		tf_endyear = new JTextField();
		tf_endyear.setText("2014");
		tf_endyear.setColumns(10);
		tf_endyear.setBounds(262, 78, 49, 21);
		contentPane.add(tf_endyear);
		
		JButton btnCreateAvgCostPriceYear = new JButton("Create Avg Cost PriceYear");
		btnCreateAvgCostPriceYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createTable_vege_cost_price_year();
			}
		});
		btnCreateAvgCostPriceYear.setBounds(22, 44, 307, 23);
		contentPane.add(btnCreateAvgCostPriceYear);
		
		JButton btnFromproToInsource = new JButton("PRO960 to in out source");
		btnFromproToInsource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createPRO960toInsourceOutsource();
			}
		});
		btnFromproToInsource.setBounds(22, 178, 307, 23);
		contentPane.add(btnFromproToInsource);
		
		JButton btnAll = new JButton("ALL");
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createALL();
			}
		});
		btnAll.setBounds(22, 227, 232, 86);
		contentPane.add(btnAll);
		
		JButton btnCreateProInsource = new JButton("Create PRO960 insource cost year");
		btnCreateProInsource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPRO960toInsourceCostYear("in");
				createPRO960toInsourceCostYear("out");
			}
		});
		btnCreateProInsource.setBounds(22, 145, 307, 23);
		contentPane.add(btnCreateProInsource);
		
		JButton btnFixGp = new JButton("Fix 0% GP");
		btnFixGp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fixGP();
			}
		});
		btnFixGp.setBounds(357, 44, 99, 23);
		contentPane.add(btnFixGp);
		
		JButton btnCreateAvgCost = new JButton("Create vege_cost_price_yearPRO960out");
		btnCreateAvgCost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTable_vege_cost_price_yearPRO960outNEW();
			}
		});
		btnCreateAvgCost.setBounds(22, 11, 307, 23);
		contentPane.add(btnCreateAvgCost);
		
		JButton button = new JButton("Fix 0% GP");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fixGPwithPRO960();
			}
		});
		button.setBounds(357, 11, 99, 23);
		contentPane.add(button);
		
		JLabel lblSamplesAreIgnored = new JLabel("samples are ignored (see code comments)");
		lblSamplesAreIgnored.setBounds(208, 114, 248, 15);
		contentPane.add(lblSamplesAreIgnored);
		
		JButton btnCreateIntegratedGp = new JButton("Create Integrated GP");
		btnCreateIntegratedGp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createIntegratedGP();
			}
		});
		btnCreateIntegratedGp.setBounds(270, 227, 186, 35);
		contentPane.add(btnCreateIntegratedGp);
	}
	
	
	private void createIntegratedGP(){
		Statement stat = null;
		ResultSet rs = null;
		Statement domstat = null;
		ResultSet domrs = null;

		String sql = "";

		try { //copy table
			stat = con.createStatement();
			rs = stat.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'integratedGP'");
			
			rs.next();
			if (rs.getInt(1) == 1){
				stat.executeUpdate("DROP TABLE integratedGP");
				stat.executeUpdate("CREATE TABLE integratedGP LIKE vege_cost_price_year");			
				stat.executeUpdate("INSERT integratedGP SELECT * from vege_cost_price_year");
			}
			else{
				stat.executeUpdate("DELETE from integratedGP");
				stat.executeUpdate("INSERT integratedGP SELECT * from vege_cost_price_year");
			}

			
		} catch (SQLException e) {
			debug("create_integratedGP CREATE TABLE Exception :" + e.toString());
		}
		
	
		String pcode = "";
		String year = "";
		double expsoldkg = 0;
		double expbuykg = 0;
		double expavgntprice = 0;
		double expavgntcost = 0;
		double expntsales = 0;
		double expntcost = 0;
		double domsoldkg = 0;
		double dombuykg = 0;
		double domavgntprice = 0;
		double domavgntcost = 0;
		double domntsales = 0;
		double domntcost = 0;
		double itgsoldkg = 0;
		double itgbuykg = 0;
		double itgavgntprice = 0;
		double itgavgntcost = 0;
		double itgntsales = 0;
		double itgntcost = 0;
		double itggp = 0;
		
		double row = countRow("SELECT count(*) from integratedGP");
		double count = 0;
		debug("Integrate GP...");
		
		try{ //integrate domestic cost price into the new table
			stat = con.createStatement(
				    ResultSet.TYPE_FORWARD_ONLY,
				    ResultSet.CONCUR_UPDATABLE,
				    ResultSet.HOLD_CURSORS_OVER_COMMIT
				   );
			rs = stat.executeQuery("SELECT * from integratedGP");
			while (rs.next()) {
				count ++;
				debug(""+count + " / " + row);				
				pcode = rs.getString("pcode");
				year = rs.getString("year");
				expsoldkg = rs.getDouble("soldkg");
				expbuykg = rs.getDouble("buykg");
				expavgntprice = rs.getDouble("avgntprice");
				expavgntcost = rs.getDouble("avgntcost");
				rs.getDouble("gp");
				expntsales = expsoldkg * expavgntprice;
				expntcost = expbuykg * expavgntcost;
				
				domstat = con.createStatement();
				sql = "SELECT * from vege_cost_price_yearpro960out where pcode = '" + pcode + "' and year = '" + year + "'";
				domrs = domstat.executeQuery(sql);
				
				while(domrs.next()){ // only 1 record should exist
					domsoldkg = domrs.getDouble("soldkg");
					dombuykg = domrs.getDouble("buykg");
					domavgntprice = domrs.getDouble("avgntprice");
					domavgntcost = domrs.getDouble("avgntcost");
					domntsales = domsoldkg * domavgntprice;
					domntcost = dombuykg * domavgntcost;
					itgsoldkg = expsoldkg + domsoldkg;
					itgbuykg = expbuykg + dombuykg;
					itgntsales = expntsales + domntsales;
					itgntcost = expntcost + domntcost;
					
					
					if (itgntsales==0 || itgsoldkg ==0)
						itgavgntprice = 0;
					else
						itgavgntprice = itgntsales / itgsoldkg;
					
					if (itgntcost==0 || itgbuykg ==0)
						itgavgntcost = 0;
					else
						itgavgntcost = itgntcost / itgbuykg;
					
					if (itgavgntprice==0 || itgavgntcost==0)
						itggp = 0;
					else
						itggp = (itgavgntprice - itgavgntcost) / itgavgntprice * 100;
					
					//debug("EXP skg:"+expsoldkg+" bkg:" +expbuykg + " avgp:"+expavgntprice+ " avgc:"+expavgntcost+ " s:"+expntsales+ " c:"+expntcost+" gp:"+expgp);
					//debug("DOM skg:"+domsoldkg+" bkg:" +dombuykg + " avgp:"+domavgntprice+ " avgc:"+domavgntcost+ " s:"+domntsales+ " c:"+domntcost+" gp:"+domgp);
					debug("ITG pcode:"+pcode+" yr:"+year + " skg:"+itgsoldkg+" bkg:" +itgbuykg + " avgp:"+itgavgntprice+ " avgc:"+itgavgntcost+ " s:"+itgntsales+ " c:"+itgntcost+ " gp:"+itggp);
					rs.updateDouble("soldkg", itgsoldkg);
					rs.updateDouble("buykg", itgbuykg);
					rs.updateDouble("avgntprice", itgavgntprice);
					rs.updateDouble("avgntcost", itgavgntcost);
					rs.updateDouble("gp", itggp);
	
					rs.updateRow();
				}	
			}		
		
		} catch (SQLException e) {
			debug("create_integratedGP integrate Exception :" + e.toString());
		}		
		
		try{
			rs.close();
			stat.close();
		} catch (SQLException e) {
			debug("Integrate UPDATE Exception :" + e.toString());
		}
		
		//

	}
	
	private long pcodeyear_exist_in_vege_cost_price_year(String table,String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		long id = -1;
		
		try { 
			stat = con.createStatement();
			rs = stat.executeQuery("SELECT * from "+table+" where pcode = '"+pcode + "' and year = '" + year + "' " );
			while (rs.next()){
				id = rs.getLong("id");
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			debug("pcodeyear_exist_in_vege_cost_price_year Exception :" + e.toString());
		}		
		return id;
	}
	
	private void fixGP(){
		//when gp == 0; there are 2 possibilities
		//1. no soldkg  (no sale; keep gp 0)
		//2. no buykg (no buy; solution: use previous year cost to calculate gp)
		//DO NOT UDPATE avgntcost BECAUSE IT WILL AFFECT OTHER STATISTICS
		Statement pcodelistStat = null;
		ResultSet pcodelistrs = null;
		
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";

		float previous_avgntcost =0;
		float gp = 0;
		
		String sqlpcodelist = "SELECT distinct pcode from vege_cost_price_year";
		debug(sqlpcodelist);
		try { //get pcode list
			pcodelistStat = con.createStatement();
			pcodelistrs = pcodelistStat.executeQuery(sqlpcodelist);
		} catch (SQLException e) {
			debug("get pcodelist Exception :" + e.toString());
		}

		try { // loop pcode
			while (pcodelistrs.next()) {
				sql = "SELECT * from vege_cost_price_year where pcode = \'" + pcodelistrs.getString("pcode") + "\' order by year";
				//sql = "SELECT * from vege_cost_price_year where pcode = '0117' order by year";
				debug(sql);

				stat = con.createStatement(
					    ResultSet.TYPE_FORWARD_ONLY,
					    ResultSet.CONCUR_UPDATABLE,
					    ResultSet.HOLD_CURSORS_OVER_COMMIT
					   );
				rs = stat.executeQuery(sql);
				previous_avgntcost = 0; //must reset to 0 for each variety
				while (rs.next()) {
					gp = rs.getInt("gp");
					debug(rs.getString("pcode")+" " + rs.getString("year")+ " gp:"+gp+"  soldkg:" + rs.getFloat("soldkg") + "  avgcost:"+ rs.getFloat("avgntcost") +"   prec:" + previous_avgntcost); 
					if (gp==0  && rs.getFloat("soldkg") > 0 && rs.getFloat("avgntprice") > 0 && previous_avgntcost > 0 ){ //if there's sales
						//avgntprice MUST be >0 to avoid infinity error
						gp = ((rs.getFloat("avgntprice") - previous_avgntcost)/rs.getFloat("avgntprice") * 100 );
						rs.updateFloat("gp", gp);
						rs.updateRow();
					}
					else{
						previous_avgntcost = rs.getInt("avgntcost");
					}
				}
			}
			pcodelistStat.close();
			pcodelistrs.close();
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			debug("loop pcodelist Exception :" + e.toString());
		}		
		
/*
		try {
			stat = con.createStatement(
				    ResultSet.TYPE_FORWARD_ONLY,
				    ResultSet.CONCUR_UPDATABLE,
				    ResultSet.HOLD_CURSORS_OVER_COMMIT
				   );
			rs = stat.executeQuery(sql);
			//when gp == 0; there are 2 possibilities
			//1. no soldkg  (no sale; keep gp 0)
			//2. no buykg (no buy; solution: use previous year cost to calculate gp)
			while (rs.next()) {
				gp = rs.getInt("gp");
				//debug(rs.getString("year")+ "  "+rs.getString("pcname")+"  gp:"+gp+"  soldkg:" + rs.getFloat("soldkg") + "  avgcost:"+ rs.getFloat("avgntcost") +"   prec:" + previous_avgntcost); 
				if (gp==0  && rs.getFloat("soldkg") > 0 && previous_avgntcost > 0 ){ //if there's sales
					gp = ((rs.getFloat("avgntprice") - previous_avgntcost)/rs.getFloat("avgntprice") * 100 );
					rs.updateFloat("gp", gp);
					rs.updateRow();
				}
				else{
					previous_avgntcost = rs.getInt("avgntcost");
				}
			}						
			rs.close();
			stat.close();
		} catch (SQLException e) {
			debug("getJFCdataset_weightGP_GPchange Exception :" + e.toString());
		}
*/
	}

	
	private void fixGPwithPRO960(){ //TBD
		//vege_cost_price_year only records imports and do not take "inland outsource" into consideration
		//for instance papaya RedLady is largely produced in TW only
		
		Statement pcodelistStat = null;
		ResultSet pcodelistrs = null;
		
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";

		float previous_avgntcost =0;
		float gp = 0;
		
		String sqlpcodelist = "SELECT distinct pcode from pro960";
		debug(sqlpcodelist);
		try { //get pcode list
			pcodelistStat = con.createStatement();
			pcodelistrs = pcodelistStat.executeQuery(sqlpcodelist);
		} catch (SQLException e) {
			debug("get pcodelist Exception :" + e.toString());
		}	
		
		try { // loop pcode
			while (pcodelistrs.next()) {
				sql = "SELECT * from vege_cost_price_yearpro960out where pcode = \'" + pcodelistrs.getString("pcode") + "\' order by year";
				//sql = "SELECT * from vege_cost_price_yearpro960out where pcode = '0117' order by year";
				debug(sql);

				stat = con.createStatement(
					    ResultSet.TYPE_FORWARD_ONLY,
					    ResultSet.CONCUR_UPDATABLE,
					    ResultSet.HOLD_CURSORS_OVER_COMMIT
					   );
				rs = stat.executeQuery(sql);
				previous_avgntcost = 0; //must reset to 0 for each variety
				while (rs.next()) {
					gp = rs.getInt("gp");
					debug(rs.getString("pcode")+" " + rs.getString("year")+ " gp:"+gp+"  soldkg:" + rs.getFloat("soldkg") + "  avgcost:"+ rs.getFloat("avgntcost") +"   prec:" + previous_avgntcost); 
					if (gp==0  && rs.getFloat("soldkg") > 0 && rs.getFloat("avgntprice") > 0 && previous_avgntcost > 0 ){ //if there's sales
						//avgntprice MUST be >0 to avoid infinity error
						gp = ((rs.getFloat("avgntprice") - previous_avgntcost)/rs.getFloat("avgntprice") * 100 );
						rs.updateFloat("gp", gp);
						rs.updateRow();
					}
					else{
						previous_avgntcost = rs.getInt("avgntcost");
					}
				}
			}
			pcodelistStat.close();
			pcodelistrs.close();
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			debug("loop pcodelist Exception :" + e.toString());
		}				
		
		
		
	}	
	
	
	public void debug(String text){
		System.out.println(text);
	}

	
	private void createALL(){
		createVarietyCostYear();
		createVarietyPriceYear();
		createTable_vege_cost_price_year();
		createPRO960toInsourceOutsource();
		createPRO960toInsourceCostYear("in");
		createPRO960toInsourceCostYear("out");
		createTable_vege_cost_price_yearPRO960outNEW();
		createIntegratedGP();
	}
	
	private void createPRO960toInsourceCostYear(String tbsource){
		ResultSet rs = null;
		int sy = Integer.parseInt(tf_startyear.getText());
		int ey = Integer.parseInt(tf_endyear.getText());
		int year = sy;
		int nextyear = sy+1;
		
		String sql = "";
		
		if (isTableExisting("pro960_"+tbsource+"_cost_year"))
			dropTable("pro960_"+tbsource+"_cost_year");

		try {
			  stat = con.createStatement();

		      //create new table
		      sql = "CREATE TABLE pro960_"+tbsource+"_cost_year (" +
		    			  "pcode		VARCHAR(20)" +
		    			  ",count		INT" +
		    			  ",firstdeal	DATE" +
		    			  ",lastdeal 	DATE"+
		    			  ",sumqty	DECIMAL(15,2)"+
		    			  ",sumntcost  DECIMAL(15,2)"+
		    			  ",avgntcost  DECIMAL(15,2)"+
		    			  ",year	INT"+	
		    			  ")";	    	  
		      stat.executeUpdate(sql);
		      debug(sql);
		    } 
		    catch(SQLException e)   {debug("CreateDB "+tbsource+"Exception :" + e.toString());  } 
		    finally  { Close();  } 	
			
			try  { 
		      stat = con.createStatement();
		      for (year=sy; year < ey; year++){
		  		sql = "Select pcode, count(pcode) as deals, min(supply_date) as firstdeal, max(supply_date) AS lastdeal, sum(intostock_qty) as iqty, sum(total_pay) as ntcost from pro960"+tbsource+" where supply_date >\'"+ year +"\' and supply_date < \'"+ nextyear +"\' group by pcode order by pcode    ";
		    	debug (sql);
	      
			      rs = stat.executeQuery(sql);
		    	  //debug("pcode\tcount\tfirstdeal\tlastdeal\tsumqty\tsumcost\t");	      
		    	  debug(""+year);
			      
		    	  while(rs.next()){
				      tmpstat = con.createStatement();
				      sql = "INSERT into pro960_"+tbsource+"_cost_year VALUES (?,?,?,?,?,?,?,?)";
				      PreparedStatement preparedStmt = con.prepareStatement(sql);
				      preparedStmt.setString (1, rs.getString(1));
				      preparedStmt.setInt(2,rs.getInt(2));
				      preparedStmt.setDate(3,rs.getDate(3));
				      preparedStmt.setDate(4,rs.getDate(4));
				      preparedStmt.setFloat(5, rs.getFloat(5));
				      preparedStmt.setFloat(6, rs.getFloat(6));
				      if (rs.getFloat(5) != 0)
				    		preparedStmt.setFloat(7, rs.getFloat(6)/rs.getFloat(5));
				      else
				    	    preparedStmt.setFloat(7,0);
				      
				      preparedStmt.setInt(8, year);
				      preparedStmt.execute();
				      //debug(rs.getString(1) + "\t"+rs.getInt(2) + "\t"+rs.getDate(3) + "\t"+rs.getString(4) + "\t"+rs.getFloat(5) + "\t" + rs.getFloat(6));
		    	  }
			      nextyear++;
		      }
		      
		    } 
		    catch(SQLException e)   { debug("Insert or Select Exception :" + e.toString());  } 
		    finally  { Close();  } 				
	}

	
	
	private void createPRO960toInsourceOutsource(){
		String sql = "";
   
		if (isTableExisting("pro960in"))
			dropTable("pro960in");
		if (isTableExisting("pro960out"))
			dropTable("pro960out");
  	      
		try  { 
			stat = con.createStatement();
			sql = "CREATE TABLE pro960in SELECT * FROM pro960 where up = 0"; //if unit price is 0, the seed is produced in our own farm
			stat.executeUpdate(sql);
			debug(sql);
			stat = con.createStatement();
			sql = "CREATE TABLE pro960out SELECT * FROM pro960 where up > 0"; //if unit price is > 0, the seed is out sourced
			stat.executeUpdate(sql);
			debug(sql);
		} 
		
	    catch(SQLException e)   { debug("CREATE TABLE pro960in pro960out Exception :" + e.toString());  } 
	    finally  { Close();  }	
	}
	
	public void createVarietyCostYear(){
		ResultSet rs = null;
		int sy = Integer.parseInt(tf_startyear.getText());
		int ey = Integer.parseInt(tf_endyear.getText());
		int year = sy;
		int nextyear = sy+1;
		
		String sql = "";
		
		if (isTableExisting("variety_cost_year"))
			dropTable("variety_cost_year");
      	    
		try {
		  stat = con.createStatement();

	      //create new table
	      sql = "CREATE TABLE variety_cost_year (" +
	    			  "pcode		VARCHAR(20)" +
	    			  ",count		INT" +
	    			  ",firstdeal	DATE" +
	    			  ",lastdeal 	DATE"+
	    			  ",sumqty	DECIMAL(15,2)"+
	    			  ",sumntcost  DECIMAL(15,2)"+
	    			  ",avgntcost  DECIMAL(15,2)"+
	    			  ",year	INT"+	
	    			  ")";	    	  
	      stat.executeUpdate(sql);
	      debug("table created");
	    } 
	    catch(SQLException e)   { System.out.println("CreateDB Exception :" + e.toString());  } 
	    finally  { Close();  } 	
		
		try  { 
	      stat = con.createStatement();
	      for (year=sy; year < ey; year++){
	    	  /* 
	    	   * find out total qty and total cost for each variety for each year
	    	  */
	    	  sql = "Select pcode, count(pcode) as deals, min(invoice_date) as firstdeal, max(invoice_date) AS lastdeal, sum(invoice_qty) as iqty, sum(invoice_cost*rate) as ntcost from pro370 where invoice_date > \'"+ year +"\' and invoice_date < \'"+ nextyear +"\' group by pcode order by pcode    ";     
	    	  //debug (sql);
      
		      rs = stat.executeQuery(sql);
		      
	    	  //debug("pcode\tcount\tfirstdeal\tlastdeal\tsumqty\tsumcost\t");	      
	    	  debug(""+year);
		      
	    	  while(rs.next()){
			      tmpstat = con.createStatement();
			      sql = "INSERT into variety_cost_year VALUES (?,?,?,?,?,?,?,?)";
			      PreparedStatement preparedStmt = con.prepareStatement(sql);
			      preparedStmt.setString (1, rs.getString(1));
			      preparedStmt.setInt(2,rs.getInt(2));
			      preparedStmt.setDate(3,rs.getDate(3));
			      preparedStmt.setDate(4,rs.getDate(4));
			      preparedStmt.setFloat(5, rs.getFloat(5));
			      preparedStmt.setFloat(6, rs.getFloat(6));
			      if (rs.getFloat(5) != 0)
			    		preparedStmt.setFloat(7, rs.getFloat(6)/rs.getFloat(5));
			      else
			    	    preparedStmt.setFloat(7,0);
			      
			      preparedStmt.setInt(8, year);
			      preparedStmt.execute();
			      //debug(rs.getString(1) + "\t"+rs.getInt(2) + "\t"+rs.getDate(3) + "\t"+rs.getString(4) + "\t"+rs.getFloat(5) + "\t" + rs.getFloat(6));
	    	  }
		      nextyear++;
	      }
	      
	    } 
	    catch(SQLException e)   { System.out.println("Insert or Select Exception :" + e.toString());  } 
	    finally  { Close();  } 		
	}

	
	public void dropTable(String tablename){
		String sql = null;
		try  { 
		stat = con.createStatement();
		//delete table
		sql = "DROP TABLE " + tablename;
   		stat.executeUpdate(sql);
   		debug("table "+tablename+" dropped");
		}
	    catch(SQLException e)   { debug("Create Table Exception:" + e.toString());  } 
	    finally  { Close();  }		
	}
	
	public boolean isTableExisting(String tablename){
		String sql = null;
		ResultSet tmprs = null;
		try { 
		      stat = con.createStatement();	    
		      /*
		       * create a new table showing avg cost for each variety for each year 
		      */
		      sql = "SELECT count(*) FROM information_schema.tables where TABLE_NAME = \'" + tablename + "\'";
		      tmprs = stat.executeQuery(sql);
		      tmprs.next();
		      
		      if (tmprs.getInt(1) == 1){
		    	  debug(tablename + " is exist.");
		    	  tmprs.close();
		    	  return true;
		      }
		      else{
		    	  debug(tablename + "doesn't exist.");
		    	  tmprs.close();
		    	  return false;
		      }
		      
		    }
		    catch(SQLException e)   { System.out.println("CreateDB Exception :" + e.toString());  } 
			finally  {Close();} 		      
		return false;
	}

	
	
	
	private long countRow(String sql){ //the SQL must be something like SELECT count(pcode) from xxxx where xxx = xxx 
		long count = 0;
		Statement st;
		ResultSet rstmp = null;
		try {
			st = con.createStatement();
			rstmp = st.executeQuery(sql);
			rstmp.next();
			count = rstmp.getLong(1);
			rstmp.close();
			return count;
		}
		catch(SQLException e) { debug("countRow Exception :" + e.toString());  } 
			
		return count;
	}

	private void test(){
		ResultSet rscost = null;
		String sql;
		float avgntcost = 0;
		String buykg = "";
		String pcode = "0135";
		String year = "2012";
		try {
		  sql = "SELECT * FROM variety_cost_year where pcode = '" + pcode +"' and year = '" + year +"'";
		  stat = con.createStatement();
		  rscost = stat.executeQuery(sql);
		  rscost.next();
		  avgntcost = rscost.getFloat("avgntcost");
		  buykg = rscost.getString("sumqty");
		}
		  catch(SQLException e) { debug("CreateDB Exception :" + e.toString());  } 
		  finally  { Close();}
		  
		  debug (""+avgntcost + " // " + buykg);
	}
	
	float getDOM430_avgntprice(String pcode, String year){
		float soldkg = 0;
		float tsales = 0;
		float avgntprice = 0;
		ResultSet rs = null;
		Statement stat = null;
		String sql;
		try {
			sql = "Select pcode, pcname, level2, iyear, tsales/tweight as avgntprice, tweight, tsales " +
					" from ( Select *, year(shipdate) as iyear, sum(actlweight) as tweight, sum(sales) as tsales "+ 
					" from (SELECT *, ((packprice * actlqty)/actlweight) as up, (packprice * actlqty) as sales "+ 
					" from dom430 where pcode = '" + pcode + "' and year(shipdate) = "+ year + ") as t1 group by pcode, iyear) as t2";

			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				soldkg = rs.getFloat("tweight");
				tsales = rs.getFloat("tsales");
				
				if (soldkg > 0)
					avgntprice = tsales / soldkg;
				else
					avgntprice = 0;
								
			}
			rs.close();
			stat.close();

		} catch (SQLException e) {
			debug("getDOM430_avgntprice Exception :" 	+ e.toString());
		}			
		
		return avgntprice;
	}
	
	public void createTable_vege_cost_price_yearPRO960outNEW(){
		String sql = null;
		ResultSet rs = null;
		Statement stat = null;
		Statement statinsert = null;
		
		String pcode = "";
		String year = "";
		float soldkg = 0;
		float buykg = 0;
		float avgntprice = 0;
		float avgntcost = 0;
		long id = -1;
		

	    if (isTableExisting("pro960_out_cost_year") == false || isTableExisting("variety_price_year")==false ){
	    	debug("either pro960_out_cost_year or variety_price_year do not exist! program terminated.");
	    	return;
	    }

	    if (isTableExisting("vege_cost_price_yearPRO960out"))
	    	dropTable("vege_cost_price_yearPRO960out");
	    
		
		//create table
		debug("creating vege_cost_price_yearPRO960out...");
		
	      sql = "CREATE TABLE vege_cost_price_yearPRO960out (" +
	      "id INT NOT NULL AUTO_INCREMENT,"+
	      "pcode varchar(20) DEFAULT NULL," +
		  "soldkg decimal(15,2) DEFAULT NULL," +
		  "buykg decimal(15,2) DEFAULT NULL," +
		  "avgntprice decimal(15,2) DEFAULT NULL," +
		  "avgntcost decimal(15,2) DEFAULT NULL," +
		  "gp decimal(25,6) DEFAULT NULL," +
		  "year int(11) DEFAULT NULL," +
		  "PRIMARY KEY (`id`)" +
		  ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
	      
		  try {
			  stat = con.createStatement();
			  stat.executeUpdate(sql);
			  debug("vege_cost_price_yearPRO960out table created");
		  }
		  catch(SQLException e) { debug("CreateDB vege_cost_price_yearPRO960out :" + e.toString());  } 

		 

			
		  try {
			  //list out the sales record
			  sql = "Select pcode, pcname, level2, iyear, tsales/soldkg as avgntprice, soldkg, tsales " +
						" from ( Select *, year(shipdate) as iyear, sum(actlweight) as soldkg, sum(sales) as tsales "+ 
						" from (SELECT *, ((packprice * actlqty)/actlweight) as up, (packprice * actlqty) as sales "+ 
						" from dom430) as t1 group by pcode, iyear) as t2";
				
			  stat = con.createStatement();
			  rs = stat.executeQuery(sql);
			  debug(sql);
			  //insert the sales record in year base
			  while(rs.next()){
				  pcode = rs.getString("pcode");
				  year = rs.getString("iyear");
				  soldkg = rs.getFloat("soldkg");
				  rs.getFloat("tsales");
				  avgntprice = rs.getFloat("avgntprice");
				  sql = "INSERT into vege_cost_price_yearPRO960out VALUES(NULL,'"+pcode +"',"
						  + soldkg+ "," 
						  + "0," 
						  + Float.toString(avgntprice) + "," 
						  + "0," 
						  + "0," 
						  + year + ")";				  
				  //debug(sql);
				  statinsert = con.createStatement();
				  statinsert.executeUpdate(sql);

			  }
		  }  catch(SQLException e) { debug("CreateDB Exception :" + e.toString());  }
	  
		  try{
			 
			  //fill out the cost fields in the record with the data extracted from pro960 (domestic purchase) 
			  sql = "SELECT *, tcost/buykg as avgntcost from ("
					  +"SELECT pcode, year, sum(intostock_qty) as buykg, sum(cost) as tcost from ("
					  +"SELECT pcode, year(supply_date) as year, intostock_qty, up, (intostock_qty * up) as cost FROM pro960 where up > 0"
				  	  +") as t1 group by pcode,year) as t2 order by pcode";	
			  
			  stat = con.createStatement();
			  rs = stat.executeQuery(sql);			  
			  while(rs.next()){
				  pcode = rs.getString("pcode");
				  year = rs.getString("year"); 
				  buykg = rs.getFloat("buykg");
				  rs.getFloat("tcost");
				  avgntcost = rs.getFloat("avgntcost");
				  id = pcodeyear_exist_in_vege_cost_price_year("vege_cost_price_yearPRO960out",pcode,year);
				  debug("id:"+id + "\tpcode:"+pcode+"\tyear:"+year);
				  if (id >= 0){ //if exist then simply update the record
					  sql = "UPDATE vege_cost_price_yearPRO960out SET "
					  		+ " `buykg`="+buykg + ", "
					  		+ " `avgntcost`="+avgntcost + ", "
					  		+ " `gp`= (avgntprice-"+avgntcost + ")/avgntprice "
					  		+" WHERE `id`='"+ id  +"'";
					  debug("update id:"+id+"   sql:"+sql);
					  statinsert = con.createStatement();
					  statinsert.executeUpdate(sql);
					  
				  }else{
					  sql = "INSERT into vege_cost_price_yearPRO960out VALUES(NULL,'"+pcode +"',"
							  + "0," 
							  + buykg +"," 
							  + "0," 
							  + Float.toString(avgntcost) + ","
							  + "0," 
							  + year + ")";				  
					  debug(sql);
					  statinsert = con.createStatement();
					  statinsert.executeUpdate(sql);					  
				  }
					  
			  }
			  
			  
		  }  catch(SQLException e) { debug("CreateDB Exception :" + e.toString());  } 
		  

	}	

	public void createTable_vege_cost_price_year(){
		String sql = null;
		ResultSet rscost = null;
		ResultSet rsprice = null;
		String pcode = null;
		String year = null;
		long count =0;
		float avgntcost = 0;
		float avgntprice = 0;
		String soldkg = "";
		String buykg = "";
		float gp = 0;
		float totalRecord = 0;
		float currRecord =0;
		Statement statprice = null;
		Statement statcost = null;
		
	    if (isTableExisting("variety_cost_year") == false || isTableExisting("variety_price_year")==false ){
	    	debug("either variety_cost_year or variety_price_year do not exist! program terminated.");
	    	return;
	    }

	    if (isTableExisting("vege_cost_price_year"))
	    	dropTable("vege_cost_price_year");
	    
		
		//create table
		debug("creating vege_cost_price_year...");
		
	      sql = "CREATE TABLE vege_cost_price_year (" +
	      "id INT NOT NULL AUTO_INCREMENT,"+
	      "pcode varchar(20) DEFAULT NULL," +
		  "soldkg decimal(15,2) DEFAULT NULL," +
		  "buykg decimal(15,2) DEFAULT NULL," +
		  "avgntprice decimal(15,2) DEFAULT NULL," +
		  "avgntcost decimal(15,2) DEFAULT NULL," +
		  "gp decimal(25,6) DEFAULT NULL," +
		  "year int(11) DEFAULT NULL," +
		  "PRIMARY KEY (`id`)" +
		  ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
	      
		  try {
			  stat = con.createStatement();
			  stat.executeUpdate(sql);
			  stat.close();
			   debug("vege_cost_price_year table created");
		  }
		  catch(SQLException e) { debug("CreateDB Exception :" + e.toString());  } 
		  finally  { Close();}

		  try {
			  
			  sql = "SELECT * FROM variety_price_year";
			  //sql = "SELECT * FROM variety_price_year where pcode = '2361'";
			  totalRecord = countRow("SELECT count(*) FROM variety_price_year");
			  
			  statprice = con.createStatement();
			  rsprice = statprice.executeQuery(sql);

			  while (rsprice.next()){
				  pcode = rsprice.getString("pcode");
				  year = rsprice.getString("year");
				  avgntprice = rsprice.getFloat("avgntprice");
				  soldkg = rsprice.getString("sumqty");
				  
				  sql = "SELECT count(*) FROM variety_cost_year where pcode = '" + pcode +"' and year = '" + year +"'";  
				  count = countRow(sql);
				  //debug(sql);
				  if (count > 1){
					  debug("ERR! there should never be > 1 record");
					  return;
				  }
				  else if (count == 0){
					  avgntcost = 0;
					  buykg = "0";
					  gp = 0;
				  }
				  else if (count == 1){
					  //this is the correct outcome
					  sql = "SELECT * FROM variety_cost_year where pcode = '" + pcode +"' and year = '" + year +"'";
					  statcost = con.createStatement();
					  rscost = statcost.executeQuery(sql);
					  //debug(sql);
					  while(rscost.next()){
						  avgntcost = rscost.getFloat("avgntcost");
						  buykg = rscost.getString("sumqty");
						  if (avgntcost > 0 && avgntprice > 0)
							  gp = (avgntprice - avgntcost) / avgntprice * 100;
						  else
							  gp = 0;
					  }
					  statcost.close();
					  rscost.close();
				  }
				  sql = "INSERT into vege_cost_price_year VALUES(NULL,'"+pcode +"',"
				  											  	  + soldkg+ "," 
				  											  	  + buykg + "," 
				  											  	  + Float.toString(avgntprice) + "," 
				  											  	  + Float.toString(avgntcost) + "," 
				  											  	  + Float.toString(gp) + "," 
				  											  	  + year + ")";
				  //debug(sql);
				  stat = con.createStatement();
				  stat.executeUpdate(sql);
				  currRecord++;
				  debug("Progress:"+currRecord+"/"+totalRecord + "("+   Math.round(currRecord/totalRecord * 100) + "%)");
				  stat.close();
			  }
			  statprice.close();
			  rsprice.close();
		  }
		  catch(SQLException e) { debug("vege_cost_price_year data insertion Exception :" + e.toString());  } 
		  finally  { Close();}

		  
		  //insert table
		  
	}
	
	public void createVarietyPriceYear(){
		ResultSet rs = null;
		int sy = Integer.parseInt(tf_startyear.getText());
		int ey = Integer.parseInt(tf_endyear.getText());
		int year = sy;
		int nextyear = sy+1;
		
		String sql = "";
		
		if (isTableExisting("variety_price_year"))
			dropTable("variety_price_year");      
	    
		try {
		  stat = con.createStatement();

	      //create new table
	      sql = "CREATE TABLE variety_price_year (" +
	    			  "pcode		VARCHAR(20)" +
	    			  ",count		INT" +
	    			  ",firstdeal	DATE" +
	    			  ",lastdeal 	DATE"+
	    			  ",sumqty	DECIMAL(15,2)"+
	    			  ",sumntprice  DECIMAL(15,2)"+
	    			  ",avgntprice  DECIMAL(15,2)"+
	    			  ",year	INT"+	
	    			  ")";	    	  
	      stat.executeUpdate(sql);
	      debug("table created");
	    } 
	    catch(SQLException e)   { System.out.println("CreateDB Exception :" + e.toString());  } 
	    finally  { Close();  } 	
		
		try  { 
	      stat = con.createStatement();
	      for (year=sy; year < ey; year++){
	    	  /* 
	    	   * find out total qty and total selling price for each variety for each year
	    	  */
	    	  /* CONSIDER TO REMOVE this old comment
	    	  sql = "Select pcode, "
	    	  		+ "count(pcode) as deals, "
	    	  		+ "min(invoice_date) as firstdeal, "
	    	  		+ "max(invoice_date) AS lastdeal,  "
	    	  		+ "sum(total_weight) as iqty, "
	    	  		+ "sum(unit_price * rate * total_weight) as nttotal "
	    	  		+ " from sao430 where "
	    	  		+ " invoice_date > \'"+ year 
	    	  		+ "\' and invoice_date < \'"+ nextyear 
	    	  		+"\' group by pcode order by pcode";
	    	 */

	    	  
	    	  sql = "Select pcode, count(pcode) as deals, min(invoice_date) as firstdeal, "
	    	  		+ " max(invoice_date) AS lastdeal,"
	    	  		+ " sum(total_weight) as iqty, "
	    	  		+ " sum(unit_price * toNTrate * total_weight) as nttotal  "
	    	  			+ " from (SELECT * FROM sao430 where "
		    	  		+ " invoice_date > \'"+ year + "' and " 
		    	  		+ " invoice_date < \'"+ nextyear + "' and " 
	    	  			+ " unit_price > 0 and " ////DO NOT COUNT SAMPLES ****
	    	  			+ " total_weight > 0.05) as t1 " //DO NOT COUNT SAMPLES ****
	    	  			+ " group by pcode "
	    	  			+ " order by pcode";
	    	  
	    	  //debug (sql);
      
		      rs = stat.executeQuery(sql);
		      
	    	  //debug("pcode\tcount\tfirstdeal\tlastdeal\tsumqty\tsumcost\t");	      
	    	  debug(""+year);
		      
	    	  while(rs.next()){
			      tmpstat = con.createStatement();
			      sql = "INSERT into variety_price_year VALUES (?,?,?,?,?,?,?,?)";
			      PreparedStatement preparedStmt = con.prepareStatement(sql);
			      preparedStmt.setString (1, rs.getString(1));
			      preparedStmt.setInt(2,rs.getInt(2));
			      preparedStmt.setDate(3,rs.getDate(3));
			      preparedStmt.setDate(4,rs.getDate(4));
			      preparedStmt.setFloat(5, rs.getFloat(5));
			      preparedStmt.setFloat(6, rs.getFloat(6));
			      if (rs.getFloat(5) != 0)
			    		preparedStmt.setFloat(7, rs.getFloat(6)/rs.getFloat(5));
			      else
			    	    preparedStmt.setFloat(7,0);
			      
			      preparedStmt.setInt(8, year);
			      preparedStmt.execute();
			      //debug(rs.getString(1) + "\t"+rs.getInt(2) + "\t"+rs.getDate(3) + "\t"+rs.getString(4) + "\t"+rs.getFloat(5) + "\t" + rs.getFloat(6));
	    	  }
		      nextyear++;
	      }
	      
	    } 
	    catch(SQLException e)   { System.out.println("InsertDB Exception :" + e.toString());  } 
	    finally  { Close();  } 		
	}
	
	
	
	public static void main(String[] args) {
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainLoad frame = new MainLoad();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
		MainLoad parentFrame = new MainLoad();
		parentFrame.setVisible(true);
		
	}
}
