package service;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	static private Connection con;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		con = getMysqlCon();
		initialize();
	}

	private Connection getMysqlCon() {


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
			Class.forName(driver); // 閮餃�river
			connection = DriverManager.getConnection(connURL, username, password);
			debug("database connected!");

		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		}// �������qlexception
		catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
		return connection;
	}


	private void debug(String text) {
		System.out.println(text);
	}
	
	private void initialize() {
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 572, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton button = new JButton("Insert SanChu into Main");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repeatCheck();
			}
		});
		button.setBounds(31, 42, 155, 25);
		frame.getContentPane().add(button);
		

	}
	
	
	private void repeatCheck(){
		Statement stat = null;
		ResultSet rscheck = null;

		int id = 0;
		int add=0;
		int count =0;
		
		String name, mobile, type = "";

		try {
			stat = con.createStatement();
			rscheck = stat.executeQuery("Select * from service.sanchu");
		
			while (rscheck.next()){

				name = rscheck.getString("name");
				mobile = rscheck.getString("mobile");
				type = rscheck.getString("type");
				count++;
				debug(count+"\t" + "search:" + name + "\t" + type + "\t" + mobile);						
				if (isNew( name, mobile,  type)){
					add++; 
					System.out.println(add + "\t");
					addToMain(name, mobile, type);
				}
			}
			
			rscheck.close();
			stat.close();
		}catch (SQLException e) {
			debug("repeatCheck Exception :" + e.toString());
		}
		
	}
	
	private boolean isNew(String name,String phone, String type){
		Statement stat = null;
		ResultSet rs = null;
		
		int id = 0;
		String sql = "Select * from service.main where mobile like '%" + phone + "' OR htel1 like '%" + phone + "' OR htel2 like '%" + phone + "' ";
		int count=0;
		String oldtype = "";
		
		String htel1,htel2, mobile = "";
		
		try {
			stat = con.createStatement(ResultSet.TYPE_FORWARD_ONLY ,ResultSet.CONCUR_UPDATABLE);
			rs = stat.executeQuery(sql);
			
			while (rs.next()){
				
				count++;
				name = rs.getString("name");
				htel1 = rs.getString("htel1");
				htel2 = rs.getString("htel2");
				mobile = rs.getString("mobile");
				oldtype =rs.getString("type");
				debug("!!" + count+"\t" + name + "\t" + oldtype + "\t" + htel1 + "\t" + htel2+ "\t" + mobile);
				//rs.updateString("type",type);
				//debug("update:" + type);
				//rs.updateRow();
			}

			
			rs.close();
			stat.close();

		}catch (SQLException e) {
			debug("removeRepeat Exception :" + e.toString());
		}
		
		
		if (count == 0)  return true;
		
		return false;
				
	}
	
	private void addToMain(String name,String mobile, String type){
		Statement stat = null;
		ResultSet rs = null;


		String insert, update = "";
		
		try { //copy table
			stat = con.createStatement();
			if ( !mobile.startsWith("0") )
				mobile = "0"+ mobile;
			
			int idx = type.indexOf("大樓");
			if (idx >= 0){
				type = "大樓:" + type.replace("大樓", "");
			}
			else if (type.contains("大廈")){
				idx = type.indexOf("大廈");
				type = "大樓:" + type.replace("大樓", "");
			}
						
			insert = "INSERT INTO service.main (`type`, `name`, `mobile`, `stat`, note) "
					+ " VALUES ('" 
					+ type + "', " 
					+ "'" + name  + "', "
					+ "'" + mobile + "', "
					+ "'" + "三竹新增"  + "', "
					+ "'" + "服務冊_201306後"+ "') ";
			
			System.out.println(insert);
			stat.executeUpdate(insert);
			
			stat.close();
		} catch (SQLException e) {
			debug("addToMain Exception :" + e.toString());
		}
		
	}
}
