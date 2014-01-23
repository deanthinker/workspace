import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;


public class KYdb {
	private int YEAR_RANGE = 10;
	private int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	private int YS = YE - YEAR_RANGE; // start year
	
	private KYutil u = new KYutil();
	
	static private Connection con;
	
	KYdb(){
		con = getMysqlCon();
	}
	
	KYdb (Connection c){
		con = c;
	}
	
	
	public Connection getConnection(){
		return this.con;
	}

	public boolean isTableExisting(String tablename) {
		Statement stat = null;
		String sql = null;
		ResultSet tmprs = null;
		try {
			stat = con.createStatement();
			/*
			 * create a new table showing avg cost for each variety for each
			 * year
			 */
			sql = "SELECT count(*) FROM information_schema.tables where TABLE_NAME = \'"
					+ tablename + "\'";
			tmprs = stat.executeQuery(sql);
			tmprs.next();

			if (tmprs.getInt(1) == 1) {
				u.debug(tablename + " is exist.");
				tmprs.close();
				stat.close();
				return true;
			} else {
				u.debug(tablename + "doesn't exist.");
				tmprs.close();
				stat.close();
				return false;
			}

		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		}

		return false;
	}
	
	private void dropTable(String tablename) {
		Statement stat = null;
		String sql = null;
		try {
			stat = con.createStatement();
			// delete table
			sql = "DROP TABLE " + tablename;
			stat.executeUpdate(sql);
			debug("table " + tablename + " dropped");
			stat.close();
		} catch (SQLException e) {
			debug("Create Table Exception:" + e.toString());
		}

	}	

	public Vector<String> getVarVec() {
		Statement stat = null;
		ResultSet rs = null;
		Vector<String> temp = new Vector<String>();
		String sql = "SELECT DISTINCT pcode from vege_prod where pcode NOT LIKE 'ZZ%' and pcode NOT LIKE 'FL%'";
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				temp.add(rs.getString("pcode"));
			}
			rs.close();
			stat.close();

		} catch (SQLException e) {
			debug("getVegeCropList Exception :" + e.toString());
		}

		return temp;
	}		
	
	
	public Vector<String> getVegeCropVec() {
		Statement stat = null;
		ResultSet rs = null;
		Vector<String> temp = new Vector<String>();
		String sql = "SELECT DISTINCT level2 from vege_prod where pcode NOT LIKE 'ZZ%' and pcode NOT LIKE 'FL%'";
		//debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			temp.add("所有作物");
			while (rs.next()) {
				temp.add(rs.getString("level2"));
			}
			rs.close();
			stat.close();

		} catch (SQLException e) {
			debug("getVegeCropList Exception :" + e.toString());
		}

		return temp;
	}		
	
	public float getPcodeLatest_avgntprice(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		float avgntprice =0;
		String sql = "SELECT * FROM market.variety_price_year where "
				+ "pcode = '" + pcode + "' "
				+ " order by year desc";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				avgntprice = rs.getFloat("avgntprice"); //return 1st record
			}else{
				avgntprice = 0;
			}
			rs.close();
			stat.close();

		}catch (SQLException e) {
			debug("getPcodeLatest_avgntprice Exception :" + e.toString());
		}
		return avgntprice;
	}

	public float getPcodeLatest_soldkg(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		String sql = "SELECT * FROM vege_cost_price_year where "
				+ "pcode = '" + pcode + "' "
				+ " order by year desc";
		
		//debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("soldkg"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodeLatest_soldqty Exception :" + e.toString());
		}
		return soldkg;
	}	

	public float getPcodePeriod_rangesoldkg(String pcode, String ys, String ye){ //期間總銷量
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		String sql = "SELECT sum(soldkg) as totalsoldkg FROM vege_cost_price_year where "
				+ " pcode = '" + pcode + "' and"
				+ " year >= " + ys + " and "
				+ " year <= " + ye
				+ " group by pcode";
		//debug(sql);
		//should only return 1 record
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("totalsoldkg"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodeLatest_soldqty Exception :" + e.toString());
		}
		return soldkg;
	}		

	public float getPcodePeriod_rangeincome(String pcode, String ys, String ye){ //期間總銷量
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		String sql = "SELECT sum(sumntprice) as totalincome FROM variety_price_year where "
				+ " pcode = '" + pcode + "' and"
				+ " year >= " + ys + " and "
				+ " year <= " + ye
				+ " group by pcode";
		//debug(sql);
		//should only return 1 record
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("totalincome"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodeLatest_soldqty Exception :" + e.toString());
		}
		return soldkg;
	}		
	
	public float getPcodePeriod_rangebuykgTW(String pcode, String ys, String ye){ //期間總採購量
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		
		String sql = "SELECT pcode, year, sum(intostock_qty) as sumqty from ( " 
				   + "SELECT pcode, level2, intostock_qty, year(supply_date) as year "
				   + " FROM market.pro960 where pcode = '" + pcode + "') as t1 where "
				   + " year >= '" + ys + "' and year <='" + ye + "'" 
				   + " group by pcode";
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("sumqty"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodePeriod_rangebuykgTW Exception :" + e.toString());
		}
		return soldkg;
	}		
	
	
	public float getPcodePeriod_rangebuykg(String pcode, String ys, String ye){ //期間總採購量
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		String sql = "SELECT sum(buykg) as totalbuykg FROM vege_cost_price_year where "
				+ " pcode = '" + pcode + "' and"
				+ " year >= " + ys + " and "
				+ " year <= " + ye
				+ " group by pcode";
		//debug(sql);
		//should only return 1 record
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("totalbuykg"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodeLatest_soldqty Exception :" + e.toString());
		}
		return soldkg;
	}		

	public float getPcodePeriod_rangelanduse(String pcode, String ys, String ye){//期間總占地
		Statement stat = null;
		ResultSet rs = null;
		float soldkg =0;
		String sql = "SELECT sum(landsize) as totallanduse FROM pro130 where "
				+ " pcode = '" + pcode + "' and"
				+ " year >= " + ys + " and "
				+ " year <= " + ye
				+ " group by pcode";
		//debug(sql);
		//should only return 1 record
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				soldkg = rs.getFloat("totallanduse"); //return 1st record
			}else{
				soldkg = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodeLatest_soldqty Exception :" + e.toString());
		}
		return soldkg;
	}			
	
	public boolean getPcodeLatest_buyFromPRO370(String pcode){
		//check the latest purchase source, pro340(RBU) or pro960(Taiwan)
		//determine the GP with the cost from the source of the latest purchase record
		
		boolean buyfrom370 = true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date datepro370 = null;
		Date datepro960 = null;
		try {
			datepro370 = sdf.parse("01/01/1910");
			datepro960 = sdf.parse("01/01/1910"); //default to ancient time
		} catch (ParseException e1) {e1.printStackTrace();} 
		
		String sqlpro370 = "SELECT max(invoice_date) as mdate FROM pro370 where pcode = '"+pcode+"' group by pcode";
		String sqlpro960 = "SELECT max(supply_date) as mdate FROM pro960 where pcode = '"+pcode+"' group by pcode";
		
		Statement stat = null;
		ResultSet rs = null;

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sqlpro370);
			if (rs.next()) { 
				datepro370 = rs.getDate("mdate"); //return 1st record
			}
			rs.close();
			stat.close();
			
			stat = con.createStatement();
			rs = stat.executeQuery(sqlpro960);
			if (rs.next()) { 
				datepro960 = rs.getDate("mdate"); //return 1st record
			}
			rs.close();
			stat.close();			

			buyfrom370 = datepro370.after(datepro960);
			
		}catch (SQLException e) {
			debug("getPcodeLatest_buyFromPRO370 Exception :" + e.toString());
		}
		
		return buyfrom370;	
	}

	public float getPcodePeriod_avggp(String pcode, String ys, String ye){ //期間總採購量
		Statement stat = null;
		ResultSet rs = null;
		float totalgp =0;
		float avggp =0;
		int intys = Integer.valueOf(ys);
		int intye = Integer.valueOf(ye);
		String sql;
		

		if (getPcodeLatest_buyFromPRO370(pcode))
			sql = "select tp, tc, (((tp-tc)/tp)*100) as avggp "
						+ "from (select "
						+ "sum(avgntprice) as tp, "
						+ "sum(avgntcost) as tc "
						+ "from vege_cost_price_year where "
						+ "pcode = '"+pcode+"' and "
						+ "year >= "+ys+" and "
						+ "year <= "+ye+" "
						+ "group by pcode) t1";
		else //from pro960
			sql = "select tp, tc, (((tp-tc)/tp)*100) as avggp "
					+ "from (select "
					+ "sum(avgntprice) as tp, "
					+ "sum(avgntcost) as tc "
					+ "from vege_cost_price_yearpro960out where "
					+ "pcode = '"+pcode+"' and "
					+ "year >= "+ys+" and "
					+ "year <= "+ye+" "
					+ "group by pcode) t1";			
		
		//debug(sql);
		//should only return 1 record
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				avggp = rs.getFloat("avggp"); //return 1st record
			}else{
				avggp = 0;
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getPcodePeriod_avggp Exception :" + e.toString());
		}
		return avggp;
	}		
	
	public float getPcodeLatest_gp(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		float gp =0;
		
		String sql = "SELECT * FROM vege_cost_price_year where "
				+ "pcode = '" + pcode + "' "
				+ " order by year desc";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next() && gp==0) {
				gp = rs.getFloat("gp"); //return 1st record non-zero
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {debug("getPcodeLatest_avgntprice Exception :" + e.toString());	}
		
		if (gp == 0){ //if gp is not found in vege_cost_price_year, try to find it in vege_cost_price_yearpro960out
			sql = "SELECT * FROM vege_cost_price_yearpro960out where "
					+ "pcode = '" + pcode + "' "
					+ " order by year desc";
			try {
				stat = con.createStatement();
				rs = stat.executeQuery(sql);
				while (rs.next() && gp==0) {
					gp = rs.getFloat("gp"); //return 1st record non-zero
				}
				rs.close();
				stat.close();
			}catch (SQLException e) {debug("getPcodeLatest_gp Exception :" + e.toString());	}			
		}
		return gp;
	}	
	
	public void update_vege_prod_img_by_pcode(String pcode, InputStream in, long filelen){
		Statement stat = null;
		ResultSet rs = null;
		String sql= "select * from vege_prod where pcode = '" +pcode + "'"; //must select all primary keys
		debug(sql);
		
		try {
			//con.setAutoCommit(false);
			stat = con.createStatement(ResultSet.TYPE_FORWARD_ONLY ,ResultSet.CONCUR_UPDATABLE);
			
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				//if (rs.getString("pcode") == pcode) {
					
					rs.updateBinaryStream("imgdata", in, in.available());
					
					rs.updateRow();
					
					in.close();
					break;

				//}
			}
			
			//con.commit();
			rs.close();
			stat.close();
			
		}catch (SQLException | IOException e) {
			debug("update_vege_prod_img_by_pcode Exception :" + e.toString());
		}		
		
		/*
		try {
			if (!imgExist){
				con.setAutoCommit(false);
				pstat = con.prepareStatement(sqlupdate);
				pstat.setBinaryStream(1, in, in.available());
				pstat.setString(2, pcode);
				pstat.executeUpdate();
				con.commit();
			}
			in.close();
			pstat.close();
			con.close();

		}catch (SQLException | IOException e) {
			debug("update_vege_prod_img_by_pcode Exception :" + e.toString());
		}
		
		 */

	}
	
	public ImageIcon getImage_by_pcode(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		ImageIcon icon = null;
		String sql = "Select pcode, imgdata from vege_prod where pcode = '" + pcode +"'";
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				byte[] imgdata = rs.getBytes("imgdata") ;
				if (imgdata != null){
					Image img = Toolkit.getDefaultToolkit().createImage(imgdata);
					icon =new ImageIcon(img);
				}
			}
		
		}catch (SQLException e) {
			debug("getImage_by_pcode Exception :" + e.toString());
		}
			
		
		return icon;
	}
	
	public String getPcode_by_pcname(String kw){
		String pcode = "";
		Statement stat = null;
		ResultSet rs = null;
		float avgntcost = 0;
		String sql = "SELECT * FROM vege_prod";
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				if (kw.contains(rs.getString("pcname"))){
					pcode = rs.getString("pcode");
					break;
				}
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("get_pcode_by_pcname Exception :" + e.toString());
		}
		
		return pcode;
	}
		
	
	
	public ResultSet getResultset_vege_prod(){
		Statement stat = null;
		ResultSet rs = null;
		float avgntcost = 0;
		String sql = "SELECT * FROM vege_prod";
		

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			//stat.close();
		}catch (SQLException e) {
			debug("getResultset_vege_prod Exception :" + e.toString());
		}
		
		return rs;
	}

	public void fillList_vege_prod_orderby_sales(JList list, int ys, int ye, String level2){
		ResultSet rs = null;
		Statement stat = null;
		String sql;
		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		float sales = 0;
		if (level2 != null){
			sql = "Select pcode, level2, pcname, sum(total_weight) as sweight, sum(usincome) as susincome , sum(ntincome) as sntincome " 
					+" from (SELECT *, (total_weight * (unit_price * toUSrate)) as usincome, (total_weight * (unit_price * toNTrate)) as ntincome FROM market.sao430"
					+" WHERE year(invoice_date) >= '"+ ys+ "' and "
					+" year(invoice_date) <= '"+ ye +"' and "
					+" level2 = '" + level2 +"' ) as t1"
					+" Group by pcode "
					+" Order by sntincome desc";
		}
		else{
			sql = "Select pcode, level2, pcname, sum(total_weight) as sweight, sum(usincome) as susincome , sum(ntincome) as sntincome " 
					+" from (SELECT *, (total_weight * (unit_price * toUSrate)) as usincome, (total_weight * (unit_price * toNTrate)) as ntincome FROM market.sao430"
					+" WHERE year(invoice_date) >= '"+ ys+ "' and "
					+" year(invoice_date) <= '"+ ye +"') as t1 "
					+" Group by pcode "
					+" Order by sntincome desc";
		}	
					
		u.debug(sql);
			
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				sales = rs.getFloat("sntincome")/10000;
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "   ,"
						+rs.getString("level2") + "   ," 
						+rs.getString("pcname") + "   ,"
						+"("+  u.nf.format(sales)  +"萬)"
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod_orderby_sales :"	+ e.toString());}

		list.repaint();		
	}
	
	public void fillList_vege_prod_orderby_weight(JList list, int ys, int ye, String level2){
		ResultSet rs = null;
		Statement stat = null;
		String sql;
		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		if (level2 != null){
			sql = "SELECT pcode, level2, pcname, sum(total_weight) as sqty FROM market.sao430 where "
				+ " pcode not like 'ZZ%' and pcode not like 'FL%' and pcode not like 'U%' and pcode not like 'S%' and "
				+ " year(invoice_date) >= '"+ys+"' and "
				+ " year(invoice_date) <= '"+ye+"' and "
				+ " level2 = '" + level2 + "'"
				+ " group by pcode "
				+ " order by sqty desc"; 
		}
		else{
			sql = "SELECT pcode, level2, pcname, sum(total_weight) as sqty FROM market.sao430 where "
					+ " pcode not like 'ZZ%' and pcode not like 'FL%' and pcode not like 'U%' and pcode not like 'S%' and "
					+ " year(invoice_date) >= '"+ys+"' and "
					+ " year(invoice_date) <= '"+ye+"' "
					+ " group by pcode "
					+ " order by sqty desc"; 
		}	
					
		u.debug(sql);
			
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") + "     ,"
						+"(售重"+rs.getString("sqty")+"Kg)"
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod_orderby_weight :"	+ e.toString());}

		list.repaint();		
	}
	
	public void fillList_vege_prod_orderby_weight(JList list, int ys, int ye){
		ResultSet rs = null;
		Statement stat = null;

		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		
		String sql = "SELECT pcode, level2, pcname, invoice_date, sum(total_weight) as sqty FROM market.sao430 where "
				+ " pcode not like 'ZZ%' and pcode not like 'FL%' and pcode not like 'U%' and pcode not like 'S%' and"
				+ " invoice_date > '"+ys+"' and "
				+ " invoice_date < '"+ye+"' "
				+ " group by pcode "
				+ " order by sqty desc"; 

					
		u.debug(sql);
			
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") + "     ,"
						+"(銷售"+rs.getString("sqty")+"Kg)"
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod_orderby_weight :"	+ e.toString());}

		list.repaint();		
	}	
	
	public void fillList_vege_prod_orderby_forecast(JList list, int ys, int ye, String level2){
		ResultSet rs = null;
		Statement stat = null;
		String sql;
		int o = 0;
		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		
		String clause = "SELECT pcode, level2code, pcname, sum(planqty) as sqty "
				+ " FROM market.pln145 where "
				+ " pcode not like 'ZZ%' and pcode not like 'FL%' and pcode not like 'U%' and pcode not like 'S%' and "
				+ " planyear >= "+ys+" and "
				+ " planyear <= "+ye+" group by pcode";
		
		if (level2 != null){
			sql = "SELECT t1.pcode, v.pcname, v.level2, t1.sqty from (" + clause +") as t1, vege_prod as v where "
				+ " t1.pcode = v.pcode and"
				+ " v.level2 = '" + level2 + "'"
				+ " order by sqty desc";
		}
		else{
			sql = "SELECT t1.pcode, v.pcname, v.level2, t1.sqty from (" + clause +") as t1, vege_prod as v where "
					+ " t1.pcode = v.pcode "
					+ " order by sqty desc";
		}
		u.debug("ttt " + sql);
			
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") + "     ,"
						+"(預購"+rs.getString("sqty")+"Kg)"
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod_orderby_forecast :"	+ e.toString());}

		list.repaint();		
	}
	
	public void fillList_vege_prod_orderby_forecast(JList list, int ys, int ye){
		ResultSet rs = null;
		Statement stat = null;

		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		
		String clause = "SELECT pcode, level2code, pcname, sum(planqty) as sqty "
				+ " FROM market.pln145 where "
				+ " pcode not like 'ZZ%' and pcode not like 'FL%' and pcode not like 'U%' and pcode not like 'S%' and "
				+ " planyear >= "+ys+" and "
				+ " planyear <= "+ye+" group by pcode";
		
		String sql = "SELECT t1.pcode, v.pcname, v.level2, t1.sqty from (" + clause +") as t1, vege_prod as v where "
				+ " t1.pcode = v.pcode "
				+ " order by sqty desc";
					
		u.debug("ttt " + sql);
			
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") + "     ,"
						+"("+rs.getString("sqty")+"Kg)"
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod_orderby_forecast :"	+ e.toString());}

		list.repaint();		
	}

	
	public void fillList_vege_prod(JList list, boolean filter, String level2){
		ResultSet rs = null;
		Statement stat = null;
		String sql = "";
		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
		
		if (filter)
			sql = "Select level2, pcode, pcname from vege_prod where level2 = '" + level2 + "' order by pcode";
		else
			sql = "Select level2, pcode, pcname from vege_prod order by pcode";
		
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank +  "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") 
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod :"	+ e.toString());}

		//list.setModel(listVegeProd_model);
		list.repaint();		
	}

	public void fillList_vege_prod_keyword(JList list, String kw){
		ResultSet rs = null;
		Statement stat = null;
		String sql = "Select level2, pcode, pcname from vege_prod where level2 =  '"+ kw + "' or pcode = '"+ kw + "' or pcname like '" + kw + "' order by pcode";
		DefaultListModel<String> listmodel = (DefaultListModel<String>) list.getModel();
					
		listmodel.removeAllElements();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int rank=0;
			while (rs.next()) {
				rank++;
				listmodel.addElement(
						rank + "   ,"
						+rs.getString("pcode") + "     ,"
						+rs.getString("level2") + "     ," 
						+rs.getString("pcname") 
						);
			}
		}catch (SQLException e) {u.debug("fillList_vege_prod :"	+ e.toString());}

		//list.setModel(listVegeProd_model);
		list.repaint();		
	}	

	public ResultSet getResultset_sao950(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		float avgntcost = 0;
		String sql = "SELECT pename, dealer, if(inexp = 'I', '買進', '賣出') as inexp, profitcenter, target, weight as Kg, (up) as USup, "
				+ " CONCAT('$', FORMAT((up * twrate), 0)) as NTup, "
				+ " CONCAT('$', FORMAT((up * twrate * weight), 0)) as TotalSales " 
 				+ " FROM market.sao950  where "
				+ " pcode = " + pcode + " and " 
				+ " invoice_date >= '" + year + "' and invoice_date <= '"+(Integer.valueOf(year)+1)+ "'"; 
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_sao430 Exception :" + e.toString());
		}
		
		return rs;
	}	
	
	public ResultSet getResultset_sao430(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		float avgntcost = 0;
		String sql = "SELECT pcode, level2, pcname, invoice_date, custcode, dest_country, "
				+ " total_weight as qty, "
				+ " CONCAT('$', FORMAT(((unit_price * toNTrate)), 0)) as NTpricePerKg " 
				+ " FROM market.sao430  where "
				+ " pcode = " + pcode + " and " 
				+ " invoice_date >= '" + year + "' and invoice_date <= '"+(Integer.valueOf(year)+1)+ "'"; 
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_sao430 Exception :" + e.toString());
		}
		
		return rs;
	}
	
	
	public ResultSet getResultset_customer_all(String filter){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		
		if (filter == null)
			sql = "SELECT distinct custcode, cust_name, cust_country from sao430";
		else
			sql = "SELECT distinct custcode, cust_name, cust_country from sao430 where " + filter;
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_allcustomers Exception :" + e.toString());
		}
		
		return rs;		
	}
	
	public ResultSet getResultset_CustVarietySales(String ys, String ye, String custcode, String crop){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		
		//return a list of variety sales
		sql = "SELECT pcode, level2, pcname, format(tweight,1) as 'soldkg', format(tsales/10000,1) as 'sales' from " 
				+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
				+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
				+" where custcode = '" + custcode + "' and level2 = '" + crop + "' and "
				+" year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
				+ " group by pcode ) as t2 order by tsales desc";
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_CustVarietySales Exception :" + e.toString());
		}
		
		return rs;	
	}
	
	
	public ResultSet getResultset_CustSales(String ys, String ye, String custcode, String pcode, String crop){
		//ys and ye are mandatory
		//5 cases
		//1(2012,2013, "ABC", null, "all") get ABC sales group by crop figure between year 2012~2013
		//2(2012,2013, "ABC", null, null) get ABC sales figure between year 2012~2013
		//3(2012,2013, "ABC", null, "西瓜") get ABC watermelon sales figure between year 2012~2013
		//4(2012,2013, null, null, "西瓜") get all watermelon sales figure between year 2012~2013
		//5(2012,2013, null, "0786", null) sales of 0786 between year 2012~2013
		//6(2012,2013, null, null, null) total sales between year 2012~2013
				
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		String pcodefilter = "";
		String cropfilter = "";
		String custcodefilter = "";
		String groupby = "group by custcode"; //case 2
		String select = "level2 ";
		String totalSales = "";
		String custSales = "";
		
		if (custcode!=null)	custcodefilter = " custcode = '" + custcode + "' and ";
		if (crop!=null && crop.equalsIgnoreCase("all")==false)	
			cropfilter = " level2 = '" + crop + "' and ";
		if (pcode!=null)	pcodefilter = " pcode = '" + pcode + "' and ";

		if (custcode!=null && pcode==null && crop.equalsIgnoreCase("all")){
			groupby = "group by custcode, level2 "; //case 1
			select = "level2, ";
		}
		if (custcode!=null && pcode==null && crop==null){ //case 2
			groupby = "group by custcode ";
			select = "custcode, cust_name, ";
		}
			
		if (custcode==null && pcode==null && crop!=null){ //case 4
			groupby = "";
			select = "level2, ";
		}
		if (custcode==null && pcode!=null && crop==null){ //case 5
			groupby = "";
			select = "pcode, level2, pcname, ";
		}
		if (custcode==null && pcode==null && crop==null){ //case 6
			groupby = "";
			select = "";
		}
		
		totalSales = getYearRangeSales(ys,ye);
		if (custcode!=null)
			custSales = getYearRangeSalesByCust(ys,ye,custcode);
		
		debug("totalSales:"+totalSales + "\t custSales:"+custSales);
		BigInteger t = new BigInteger("100");
		
		sql = "SELECT "+ select + " format(tweight,1) as '銷售重(Kg)', format(tsales/10000,1) as '銷售額(萬)', round((tsales/"+custSales +")*100) as '銷售佔比%' from " 
				+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
				+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
				+" where " + pcodefilter + cropfilter + custcodefilter
				+" year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
				+ groupby + " ) as t2 order by tsales desc";
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_CustSales Exception :" + e.toString());
		}
		
		return rs;			
	}
	
	public String getYearRangeSalesByCust(String ys, String ye, String custcode){
		Statement stat = null;
		ResultSet rs = null;
		String sql = null;
		String sales ="";
		
		sql = "SELECT format(tweight,1) as soldkg, format(tsales,1) as sales from " 
				+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
				+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
				+" where custcode = '" +custcode + "' and "
				+" year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
				+  " ) as t2 ";
		debug(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				sales = rs.getString("sales").replace(",", "");
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getYearRangeSalesByCust Exception :" + e.toString());
		}
		
		return sales;
	}

	public String getYearRangeSales(String ys, String ye){
		Statement stat = null;
		ResultSet rs = null;
		String sql = null;
		String sales ="";
		
		
		sql = "SELECT format(tweight,1) as soldkg, format(tsales,1) as sales from " 
				+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
				+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
				+" where year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
				+  " ) as t2 ";
		debug(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				sales = rs.getString("sales").replace(",", "");
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getYearRangeSales Exception :" + e.toString());
		}
		
		return sales;
	}
	
	
	public ResultSet getResultset_customer_pcode(String pcode, String ys, String ye, String filter, String order){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		if (filter == null) filter = ""; //make it a zero length so that can be added to the string
		
		if (pcode.length()==0){
			sql = "SELECT custcode, cust_name, format(tsales/10000,1) as '銷售額(萬)', format(tweight,1) as '銷售重(Kg)' from " 
					+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
					+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
					+" where " + filter
					+" year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
					+" group by custcode) as t2 "
					+" order by "+order+" desc ";
		}
		else{
			sql = "SELECT custcode, cust_name, format(tsales/10000,1) as '銷售額(萬)', format(tweight,1) as '銷售重(Kg)', format(tsales/tweight,1) as '平均單價' from " 
					+" (SELECT *, sum(total_weight) as tweight, sum(total) as tsales from " 
					+" (SELECT *, year(invoice_date) as year, (unit_price * toNTrate * total_pack) as total from sao430 " 
					+" where " + filter
					+" pcode = '"+pcode+"' and  year(invoice_date) >= "+ys+" and year(invoice_date) <= "+ye+") as t1  "
					+" group by custcode) as t2 "
					+" order by "+order+" desc ";
		}
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_customer_pcode Exception :" + e.toString());
		}
		
		return rs;		
	}
	
	
	public ResultSet getResultset_dom430(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;

		
		String sql = "SELECT pcode, shipdate, custname, packtype, packprice, actlqty, actlweight, (packprice * actlqty)/actlweight as NTup , class"
				+ " FROM market.dom430  where "
				+ " pcode = " + pcode + " and " 
				+ " shipdate >= '" + year + "' and shipdate <= '"+(Integer.valueOf(year)+1)+ "'"; 

		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_dom430 Exception :" + e.toString());
		}
		
		return rs;
	}		
	public Vector<KeyValue_int_float> getKeyValue_int_float_EXPFORECAST(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		Float planqty = null;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT pcode, pcname, planyear, planqty, sum(planqty) as splanqty from pln145 "
				+ " where pcode = '" +pcode +"' "
				+ " group by planyear";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("planyear");
				planqty = rs.getFloat("splanqty");
				data.add(new KeyValue_int_float(year, planqty ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_EXPforecast Exception :" + e.toString());
		}
		
		return data;
	}

	public Vector<KeyValue_int_float> getKeyValue_int_float_DOMFORECAST(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		Float planqty = null;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT pcode, planyear, planqty, sum(planqty) as splanqty from pln245 "
				+ " where pcode = '" +pcode +"' "
				+ " group by planyear";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("planyear");
				planqty = rs.getFloat("splanqty");
				data.add(new KeyValue_int_float(year, planqty ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_DOMforecast Exception :" + e.toString());
		}
		
		return data;
	}	
	
	public float PcodeLatestInventory(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = YE;
		float tweight = 0;
		
		String sql = "SELECT distinct pcode, reserve_std_qty, avai_std_qty, "
				+ " (reserve_std_qty+avai_std_qty) as tweight, "
				+ " '" + YE + "' as year "
				+ " from market.inv340 where pcode = '" +pcode + "'";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				tweight = rs.getFloat("tweight");
			}
			
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("PcodeLatestInventory Exception :" + e.toString());
		}
		
		return tweight;
		
	}

	public Vector<KeyValue_int_float> getKeyValue_int_float_INVENTORY(String pcode){ //期末庫存
		Statement stat = null;
		ResultSet rs = null;
		int year = YE;
		float soldkg = 0;
		float buykg = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		float curr = PcodeLatestInventory(pcode);
		
		//calculate inventory of last 5 years
		/*
		 * say 2013 year-end stock is 50kg
		 *    100kg was sold in 2013
		 *    130kg was produced in 2013
		 * then 2012 year-end stock was
		 * 50 + 100 - 130 = 20kg
		 * 
		 */
		data.add(new KeyValue_int_float(year, curr)); //add current year
		year--;
		for (int i=year ; i >= (year - 4); i--){
			soldkg = getPcodePeriod_rangesoldkg(pcode, String.valueOf(i+1), String.valueOf(i+1));
			buykg = getPcodePeriod_rangebuykg(pcode, String.valueOf(i+1), String.valueOf(i+1))
					+ getPcodePeriod_rangebuykgTW(pcode, String.valueOf(i+1), String.valueOf(i+1));
			curr = curr + soldkg - buykg;
			u.debug("year:"+i+"\tsoldkg:"+soldkg+"\tbuykg:"+buykg+"\tcurr:"+curr);
			data.add(new KeyValue_int_float(i, curr)); //add current year
		}
			
		
		return data;
	}	
	
	public Vector<KeyValue_int_float> getKeyValue_int_float_TRITRADESOLDKG(String pcode){ //境外交易
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		Float tweight = null;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT pcode, pename, level2, sum(weight) as tweight, year(invoice_date) as year FROM market.sao950 "
				+ " where pcode = '" +pcode +"' "
				+ " and inexp = 'E' "
				+ " group by year";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				tweight = rs.getFloat("tweight");
				data.add(new KeyValue_int_float(year, tweight ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_TriTradeSoldtweight Exception :" + e.toString());
		}
		
		return data;
	}
	
	
	public Vector<KeyValue_int_float> getKeyValue_int_float_EXPSOLD(String pcode, String field){
		//Support 3 queries: sumqty, sumntprice, avgntprice
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float fieldValue = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT * from variety_price_year"
				+ " where pcode = '" +pcode +"' ";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				fieldValue = rs.getFloat(field);
				data.add(new KeyValue_int_float(year, fieldValue ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_EXPSOLD Exception :" + e.toString());
		}
		
		return data;
	}

	public Vector<KeyValue_int_float> getKeyValue_int_float_DOMSOLD(String pcode, String field){
		//support 3 queries: avgntprice, sumqty, sumntsales
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float fieldValue = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "Select pcode, pcname, level2, iyear, sumntsales/sumqty as avgntprice, sumqty, sumntsales "
		+ " from ( Select *, year(shipdate) as iyear, sum(actlweight) as sumqty, sum(sales) as sumntsales"
		+ " from (SELECT *, (packprice * actlqty) as sales" 
		+ " from dom430 where pcode = '" + pcode +"'"
		+ " ) as t1 group by pcode, iyear) as t2";		
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("iyear");
				if (Math.abs(YE - year) <= 10){
					fieldValue = rs.getFloat(field);
					data.add(new KeyValue_int_float(year, fieldValue ));
				}
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_DOMSOLD Exception :" + e.toString());
		}
		
		return data;
	}
	
	
	public String getHTMLtable_String_float_country_weight(String pcode, String ys, String ye){
		String htmlTable="";
		String header = "<table  border=1>";
		String footer = "</table>";
		String row = "<tr> <td>客戶代碼</td><td>客戶名稱(Kg)</td> <td>地區</td><td>國家</td> <td>銷售日 </td> <td>總重Kg </td> </tr>";
		
		ResultSet rs = getResultSet_String_float_country_weight(pcode, ys, ye);
		try {
			while (rs.next()) {
				row += "<tr>"+
						"<td>" + rs.getString("custcode") + "</td>" +
						"<td>" + rs.getString("cust_name") + "</td>" +
						"<td>" + rs.getString("region") + "</td>" +
					  "<td>" + rs.getString("dest_country") + "</td>" +
					  "<td>" + rs.getString("invoice_date") + "</td>" +
					  "<td>" + rs.getString("total_weight") + "</td>" +
					  "<tr>";
				
			}
			htmlTable = header + row + footer;
			
			rs.getStatement().close();
			rs.close();
		} catch (SQLException e) {
			u.debug("getHTMLtable_String_float_country_weight Exception :" + e.toString());
		}
		
		return htmlTable;
	}
	
	public ResultSet getResultSet_pln145(String pcode, String ys){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT pcode, pcname, region, planyear, planq, if(class = 'E', '增購', '預購') as class, planqty "
				+ " from pln145 where planyear = "+ ys + " and pcode = '" + pcode + "'"
				+ " order by planq";

		u.debug(sql);
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			u.debug("getResultSet_pln145 Exception :" + e.toString());
		}
		
		return rs;		
		
	}
	
	public ResultSet getResultSet_pln245(String pcode, String ys){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT pcode, agentid, planyear, planq, if(class = 'E', '增購', '預購') as class, planqty "
				+ " from pln245 where planyear = "+ ys + " and pcode = '" + pcode + "'"
				+ " order by planq";

		u.debug(sql);
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			u.debug("getResultSet_pln245 Exception :" + e.toString());
		}
		
		return rs;		
		
	}	 
	
	public ResultSet getResultSet_pro130(String pcode, String ys){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT pcode, level2, pcname, estd_year, estd_season, site, qty, status, landsize, origin "
				+ " FROM market.pro130 where estd_year = "+ ys + " and pcode = '" + pcode + "';";
		u.debug(sql);
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			u.debug("getResultSet_pro130 Exception :" + e.toString());
		}
		
		return rs;		
	}

	public ResultSet getResultSet_pro370(String pcode, String ys){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT pcode, level2, pcname, suppliercode, invoice_date, invoice_qty, (unit_price * rate) as NTup, (invoice_qty * unit_price * rate) as totalcost, purchase_type "
					+ "FROM market.pro370 WHERE year(invoice_date) = "+ys+" and pcode = '"  + pcode +"'";
		u.debug(sql);
		
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			u.debug("getResultSet_pro370 Exception :" + e.toString());
		}
		
		return rs;		
	}	
	
	public ResultSet getResultSet_String_float_country_weight(String pcode, String ys, String ye){
		Statement stat = null;
		ResultSet rs = null;

		String sql = "";
		
		sql = "SELECT * FROM market.sao430 "
					+ " where pcode = " + pcode + " and "
					+ " year(invoice_date) >= '"+ ys + "' and year(invoice_date) <='" + ye + "'"
					+ " order by total_weight desc";
		
		u.debug("getResultSet_String_float_country_weight: "+ sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			u.debug("getResultSet_String_float_country_weight Exception :" + e.toString());
		}
		
		return rs;
	}
	public Vector<KeyValue_String_float> getKeyValue_String_float_country_weight(String pcode, String ys, String ye){
		Vector<KeyValue_String_float> data = new Vector<KeyValue_String_float>();
		Vector<KeyValue_String_float> countrylist = getKeyValue_String_float_countries();
		
		Statement stat = null;
		ResultSet rs = null;
		String country = "";
		String tmp;
		KeyValue_String_float tmpkv;
		int foundIndex=0;
		float weight = 0;
		int idx = -1;
		boolean show = true;
		String sql = "";
		sql = "SELECT pcode, level2, pcname, tyear, dest_country, "
				+ "sum(total_weight) as sweight, sum(NTtotal) as sNTtotal, sum(UStotal) as sUStotal "
				+ "FROM (SELECT *, "
					+ "year(invoice_date) as tyear, "
					+ "(unit_price * total_weight * toNTrate) as NTtotal, "
					+ "(unit_price * total_weight * toUSrate) as UStotal "
					+ " FROM market.sao430) as t1 "
					+ " where pcode = " + pcode + " and "
					+ " tyear >= "+ ys + " and tyear <=" + ye
					+ " group by pcode, dest_country order by sweight desc";
		u.debug(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			while (rs.next()) {
				country = rs.getString("dest_country");
				tmp = country.toUpperCase();
				if (tmp.contains("ST."))
					tmp.replace("S.T", "SAINT. ");
				
				show = false;
				
				if (tmp.contains("USA") || tmp.contains("U.S.A") || tmp.contains("U. S. A.") || tmp.contains("GUAM") || tmp.contains("HAWAII")){
					country = "United States";
					show = true;
				}
				else if (tmp.contains("HOLLAND") || tmp.contains("NETHERLAND")){
					country = "Netherlands"; show = true;
				}
				else if (tmp.contains("KOREA")){
					country = "South Korea"; show = true;
				}
				else if (tmp.contains("HONG KONG")||tmp.contains("XIAMEN")||tmp.contains("GUANGZHOU")){
					country = "China"; show = true;
				}
				else if (tmp.contains("TAIPEI") || tmp.contains("台") || tmp.contains("彰化") || tmp.contains("新北")||tmp.contains("TAINAN")){
					country = "Taiwan";	show = true;
				}
				else if (tmp.contains("VIET-NAM")){
					country = "Vietnam"; show = true;
				}
				else if (tmp.contains("ITALTY")){
					country = "Italy"; show = true;
				}
				else if (tmp.contains("ZELAND")){
					country = "New Zealand"; show = true;
				}
				else if (tmp.contains("MYNMAR")){
					country = "Myanmar"; show = true;
				}
				else if (tmp.contains("COTE")||tmp.contains("D'IVOIRE") ||tmp.contains("IVORY")){
					country = "Ivory Coast"; show = true;
				}
				else if (tmp.contains("UAE")||tmp.contains("U.A.E.") ||tmp.contains("U. A. E.") || tmp.contains("DUBAI")){
					country = "United Arab Emirates"; show = true;
				}
				else if (tmp.contains("OKINAWA")){
					country = "Japan"; show = true;
				}
				else if (tmp.contains(".LUCIA") || tmp.contains("ST. LUCIA")){
					country = "Saint Lucia"; show = true;
				}
				else if (tmp.contains("SOLOMON")){
					country = "Solomon Islands"; show = true;
				}
				else if (tmp.contains("NEVIS")){
					country = "Saint Kitts and Nevis"; show = true;
				}				
				else if (tmp.contains("CALEDONI")){
					country = "New Caledonia"; show = true;
				}					
				else if (tmp.contains("MICRONESIA")){
					country = "Miconasia"; show = true;
				}					
				else if (tmp.contains("SAIPAN")){
					country = "Saipan"; show = true;
				}					
				else if (tmp.contains("TRINIDAD")){
					country = "Trinidad"; show = true;
				}
				else if (tmp.contains("AGRENTINA")){
					country = "Argentina"; show = true;
				}
				else if (tmp.contains("VIENNA")){
					country = "Austria"; show = true;
				}
				else{
					
					for (int i = 0; i < countrylist.size(); i++) {
						if (tmp.contains(countrylist.get(i).key.toUpperCase())){
							show = true;
							country = countrylist.get(i).key;
							break;
						}
					}
				}
				weight = rs.getFloat("sweight");
				
				//if the country already exists in the data, then increment the weight;
				foundIndex = findKeyExist(country, data);
				if (foundIndex >= 0){
					tmpkv = data.get(foundIndex);
					tmpkv.value = tmpkv.value + weight; //add weight instead of adding new data (cuz redundant data won't show on map)
					data.set(foundIndex, tmpkv);
				}
				else
					data.add(new KeyValue_String_float(country, weight, show));
				
				if (show == false)
					u.debug("skip: "+country + "   weight:"+weight);
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_String_float_country_weight Exception :" + e.toString());
		}		
		u.debug("size:"+data.size());
		return data;
	}
	
	public float get_domestic_pcode_year_soldkg(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		float weight = 0;
		try {
			sql = "Select pcode, pcname, level2, iyear, tsales/tweight as avgntprice, tweight, tsales " +
					" from ( Select *, year(shipdate) as iyear, sum(actlweight) as tweight, sum(sales) as tsales "+ 
					" from (SELECT *, ((packprice * actlqty)/actlweight) as up, (packprice * actlqty) as sales "+ 
					" from dom430 where pcode = '" + pcode + "' and year(shipdate) = '" + year + "' ) as t1 group by pcode, iyear) as t2";
			
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				weight = rs.getFloat("tweight");
			}

			rs.close();
			stat.close();

		} catch (SQLException e) {
			u.debug("Select get_domestic_pcode_year_weight Exception :"
					+ e.toString());
		}
		
		return weight;			
	}
	
	public float get_domestic_pcode_year_sales(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
		float sales = 0;
		try {
			sql = "Select pcode, pcname, level2, iyear, tsales/tweight as avgntprice, tweight, tsales " +
					" from ( Select *, year(shipdate) as iyear, sum(actlweight) as tweight, sum(sales) as tsales "+ 
					" from (SELECT *, ((packprice * actlqty)/actlweight) as up, (packprice * actlqty) as sales "+ 
					" from dom430 where pcode = '" + pcode + "' and year(shipdate) = '" + year + "' ) as t1 group by pcode, iyear) as t2";
			debug(sql);
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				sales = rs.getFloat("tsales");
			}

			rs.close();
			stat.close();

		} catch (SQLException e) {
			u.debug("Select get_domestic_pcode_year_sales Exception :"
					+ e.toString());
		}
		
		return sales;	
	} 
	
	private int findKeyExist(String search, Vector<KeyValue_String_float> v){
		int index = -1;
		for (int i=0;i<v.size();i++){
			if (v.get(i).key == search ){
				index = i;
				break;
			}
		}
		return index;
	}
			
	public Vector<KeyValue_String_float> getKeyValue_String_float_countries(){
		Statement stat = null;
		ResultSet rs = null;
		String name = "";
		float qty = 0;
		Vector<KeyValue_String_float> data = new Vector<KeyValue_String_float>();
		
		String sql = "SELECT * from world.country";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				name = rs.getString("Name");
				data.add(new KeyValue_String_float(name, qty, true ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_String_float_countries() Exception :" + e.toString());
		}
		
		return data;
	}	

	public Vector<KeyValue_int_float> getKeyValue_int_float_EXPGP(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float gp = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT * from vege_cost_price_year"
				+ " where pcode = '" +pcode +"' ";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				gp = rs.getFloat("gp");
				data.add(new KeyValue_int_float(year, gp ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_GP Exception :" + e.toString());
		}
		
		return data;
	}	

	public Vector<KeyValue_int_float> getKeyValue_int_float_INTGP(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float gp = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT * from integratedgp"
				+ " where pcode = '" +pcode +"' ";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				gp = rs.getFloat("gp");
				data.add(new KeyValue_int_float(year, gp ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_INTGP Exception :" + e.toString());
		}
		
		return data;
	}		
	
	public Vector<KeyValue_int_float> getKeyValue_int_float_BUYFOREIGN(String pcode, String field){
		//Support 3 queries: sumqty, sumntcost, avgntcost
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float fieldValue = 0;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		String sql = "SELECT * from variety_cost_year"
				+ " where pcode = '" +pcode +"' ";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				fieldValue = rs.getFloat(field);
				data.add(new KeyValue_int_float(year, fieldValue ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_BUYFOREIGN Exception :" + e.toString());
		}
		
		return data;
	}
	
	public Vector<KeyValue_int_float> getKeyValue_int_float_BUYTW(String pcode, String field){
		//Support 3 queries: sumqty, sumntcost, avgntcost
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		float fieldValue = 0;

		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();

		
		String sql = "SELECT *, sumntcost/sumqty as avgntcost from ("
				   + "SELECT pcode, year, sum(total_pay) as sumntcost, sum(intostock_qty) as sumqty from ( " 
				   + "SELECT pcode, level2, total_pay, intostock_qty, year(supply_date) as year "
				   + " FROM market.pro960 where pcode = '" + pcode + "') as t1  " 
				   + " group by year ) as t2";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("year");
				fieldValue = rs.getFloat(field);
				data.add(new KeyValue_int_float(year, fieldValue ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_BUYTW Exception :" + e.toString());
		}
		
		return data;
	}	
	
	public Vector<KeyValue_int_float> getKeyValue_int_float_PRODUCTION(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		int year = 0;
		Float planqty = null;
		Vector<KeyValue_int_float> data = new Vector<KeyValue_int_float>();
		
		//我們採用estd_year因為需要知道這些數量是供應到哪一年
		//此數字會跟生產計劃裡的qty不同, 因為生產計劃顯示的是當年規畫要生產的用地
		String sql = "SELECT pcode, level2, pcname, estd_year, sum(qty) as sqty FROM market.pro130 "
				+ " where pcode = '" +pcode +"' "
		 		+ " group by estd_year";
		
		u.debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);

			while (rs.next()) {
				year = rs.getInt("estd_year");
				planqty = rs.getFloat("sqty");
				data.add(new KeyValue_int_float(year, planqty ));
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			u.debug("getKeyValue_int_float_production Exception :" + e.toString());
		}
		
		return data;
	}			
	

	public Vector<PriceData> getResultset_sao430_up_sales_weight(String level2, String year){
		Statement stat = null;
		ResultSet rs = null;	
		Vector<PriceData> priceVec = new Vector<PriceData>();
		
		String sql ="Select *, (sales/tweight) as up, (@sumsales :=@sumsales + sales) as rs, (@sumweight := @sumweight + tweight) as rw from " 
				+ " (SELECT *, sum(unit_price * toNTrate * total_weight) as sales, sum(total_weight) as tweight from sao430, (Select @sumsales := 0, @sumweight := 0) r"  
				+ " where level2 like '%"+level2+"%' and year(invoice_date) = '"+year+"' "
				+ " group by pcode) as t1"
				+ " order by sales desc;";
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			
			while(rs.next()){
				/*
				u.debug(rs.getString("pcode") + "\t" 
						+ rs.getString("level2") + "\t"
						+ rs.getString("pcname") + "\t"
						+ rs.getString("up") + "\t"
						+ rs.getString("sales") + "\t"
						+ rs.getString("rs") + "\t"
						+ rs.getString("tweight") + "\t"
						+ rs.getString("rw") + "\t"
						);
						*/
				priceVec.add(new PriceData(
						rs.getString("pcode"),
						rs.getString("level2"),
						rs.getString("pcname"),
						rs.getFloat("up"),
						rs.getFloat("sales"),
						rs.getFloat("tweight")
						));
			}
			
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getResultset_sao430_up_sales_weight Exception :" + e.toString());
		}
		return priceVec;
	}	
	
	public Vector<PriceData> getResultset_dom430_up_sales_weight(String level2, String year){
		Statement stat = null;
		ResultSet rs = null;	
		Vector<PriceData> priceVec = new Vector<PriceData>();
		
		String sql ="Select *, (sales/tweight) as up, (@sumsales :=@sumsales + sales) as rs, (@sumweight := @sumweight + tweight) as rw from " 
					+ " (SELECT *, sum(packprice * actlqty) as sales, sum(actlweight) as tweight from dom430, (Select @sumsales := 0, @sumweight := 0) r"  
					+ " where level2 like '%"+level2+"%' and year(shipdate) = '"+year+"' "
					+ " group by pcode) as t1"
					+ " order by sales desc;";
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			//  ------debug------
			while(rs.next()){
				/*
				u.debug(rs.getString("pcode") + "\t" 
						+ rs.getString("level2") + "\t"
						+ rs.getString("pcname") + "\t"
						+ rs.getString("up") + "\t"
						+ rs.getString("sales") + "\t"
						+ rs.getString("rs") + "\t"
						+ rs.getString("tweight") + "\t"
						+ rs.getString("rw") + "\t"
						);
						*/
				priceVec.add(new PriceData(
						rs.getString("pcode"),
						rs.getString("level2"),
						rs.getString("pcname"),
						rs.getFloat("up"),
						rs.getFloat("sales"),
						rs.getFloat("tweight")
						));
				
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("getResultset_dom430_up_sales_weight Exception :" + e.toString());
		}
		return priceVec;
	}
	
	public ResultSet getResultset_pro340(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT pcode, level2, pcname, invoice_date, suppliercode, invoice_qty, "
				+ "curr, rate, unit_price as USpricePerKg,  "
				+ " CONCAT('$', FORMAT((unit_price * rate), 0)) as NTpricePerKg "
				+ "FROM market.pro370  where "
				+ " pcode = " + pcode + " and " 
				+ " invoice_date >= '" + year + "' and invoice_date <= '"+(Integer.valueOf(year)+1)+ "'"; 
						
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		}catch (SQLException e) {
			debug("getResultset_pro340 Exception :" + e.toString());
		}
		
		return rs;
	}	
	
	public ResultSet getResultset_pro960(String pcode, String year){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "SELECT p.pcode, p.level2, v.pcname, supply_date, suppliername, intostock_qty, up as NTpricePerKg "
				+ "FROM pro960 as p, vege_prod as v where "
				+ " p.pcode = v.pcode and "
				+ " p.pcode = " + pcode + " and " 
				+ " supply_date >= '" + year + "' and supply_date <= '"+(Integer.valueOf(year)+1)+ "'"; 
						
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			//stat.close();
		}catch (SQLException e) {
			debug("getResultset_pro960 Exception :" + e.toString());
		}
		
		return rs;
	}	
	
	public float getPcodeLatest_avgntcost(String pcode){
		Statement stat = null;
		ResultSet rs = null;
		float avgntcost = 0;
		String sql = "SELECT * FROM market.variety_cost_year where "
				+ "pcode = '" + pcode + "' "
				+ " order by year desc";
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if (rs.next()) { 
				avgntcost =  rs.getFloat("avgntcost"); //return 1st record
			}else{
				avgntcost =  0;
			}
			rs.close();
			stat.close();

		}catch (SQLException e) {
			debug("getPcodeLatest_avgntcost Exception :" + e.toString());
		}
		
		return avgntcost;
			
	}	

	public long countRow(String sql) { // the SQL must be something like SELECT
		// count(pcode) from xxxx where xxx =
		// xxx
		long count = 0;
		Statement st;
		ResultSet rstmp = null;

		try {
			st = con.createStatement();
			rstmp = st.executeQuery(sql);
			rstmp.next();
			count = rstmp.getLong(1);
			rstmp.close();
			st.close();
			return count;
		} catch (SQLException e) {
			u.debug("countRow Exception :" + e.toString());
		}

		return count;
	}

	
	public int countRow2(String sql) { // the SQL must be something like SELECT
		int count = 0;
		Statement st;
		ResultSet rstmp = null;

		try {
			st = con.createStatement();
			rstmp = st.executeQuery(sql);
			if (rstmp.last())
				count = rstmp.getRow();
			else
				count = 0;
			
			rstmp.close();
			st.close();
		} catch (SQLException e) {
			u.debug("countRow Exception :" + e.toString());
		}

		return count;
	}

	
	private Connection getMysqlCon() {
		//debug("xxxx"+Calendar.getInstance().get(Calendar.YEAR));
		/*
		 * String driver = "com.mysql.jdbc.Driver"; String serverip =
		 * tf_serverip.getText(); String dbTable = tf_dbName.getText(); String
		 * username = tf_user.getText(); String password =
		 * tf_password.getText();
		 */

		String driver = "com.mysql.jdbc.Driver";
		String serverip = "localhost";
		String dbTable = "market";
		String username = "root";
		String password = "1234";
		Connection connection = null;

		String connURL = "jdbc:mysql://" + serverip + "/" + dbTable
				+ "?useUnicode=true&characterEncoding=utf-8";
		debug("loading database");
		debug(connURL);
		debug("user:" + username + "  password:" + password);

		try {
			Class.forName(driver); // 註冊driver
			connection = DriverManager.getConnection(connURL, username, password);
			debug("database connected!");

		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		}// 有可能會產生sqlexception
		catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
		return connection;
	}


	private void debug(String text) {
		System.out.println(text);
	}

}


