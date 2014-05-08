import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.RowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.SwingConstants;


public class Service extends JFrame {
	private static int VIEW = 1;
	private static int ADD = 2;
	private static int EDIT = 3;
	
	private int mode = VIEW;
	final JFrame thisframe = this;
	static private Connection con;
	JComboBox cbxType;
	private JPanel contentPane;
	private JTable table;
	private JTextField txfType;
	private JTextField txfName;
	private JTextField txfZip;
	private JTextField txfAddr;
	private JTextField txfTel1;
	private JTextField txfTel2;
	private JTextField txfMobile;
	private JTextField txfStat;
	private JLabel label_6;
	private JComboBox cbxStat;
	private JLabel label_7;
	private JTextField txfFm1;
	private JTextField txfFm2;
	private JTextField txfFm3;
	private JTextField txfFm6;
	private JTextField txfFm5;
	private JTextField txfFm4;
	private JLabel label_8;
	private JButton btnAdd;
	private JButton btnSave;
	private JButton btnDel;
	private JLabel label_9;
	private JTextField txfSearch;
	private JComboBox cbxSearch;
	private JLabel lblNewLabel_1;
	private JLabel lblTotal;
	private JLabel label_11;
	private JButton btnCancel;
	private JTextArea txaNote;
	
	private MyTableModel model = null;
	private JTextField txfPlp;
	private JTextField txfID;
	private JLabel lblShowHelp;
	ArrayList<String> statList=null;
	ArrayList<String> typeList=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Service frame = new Service();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Service() {
		if (con == null) //shared among all instances; only 1 connection only need to initialize once  
			con = getMysqlCon();
		loadData("");
		showUI();
		
	}
	
	private void showUI(){
		statList = getStatList();
		typeList = getTypeList();
		
		setTitle("Service Center");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 900, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 864, 171);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		txfType = new JTextField();
		txfType.setBounds(49, 331, 176, 21);
		contentPane.add(txfType);
		txfType.setColumns(10);
		txfType.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
        		if (txfType.getText().length()==0 || txfType.getText().equals(" "))
        			return;
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		for (int idx=0;idx<typeList.size(); idx++){
            			if (typeList.get(idx).contains(txfType.getText())){
            				cbxType.setSelectedIndex(idx);
            			}
            		}
            	}
            }
        });		
		
		
		cbxType = new JComboBox(typeList.toArray());
		cbxType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txfType.setText(cbxType.getSelectedItem().toString());
			}
		});
		cbxType.setFocusable(false);
		cbxType.setBounds(235, 331, 173, 21);
		contentPane.add(cbxType);
		
		JLabel lblNewLabel = new JLabel("\u59D3\u540D");
		lblNewLabel.setBounds(10, 272, 46, 15);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("\u7FA4\u7D44");
		label.setBounds(10, 334, 46, 15);
		contentPane.add(label);
		
		txfName = new JTextField();
		txfName.setColumns(10);
		txfName.setBounds(49, 269, 112, 21);
		contentPane.add(txfName);
		txfName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		if (txfName.getText().length()==0  || txfName.getText().equals(" "))
            			return;
            		int colidx = model.getHeaders().indexOf( "name" );
            		int rowidx = -1;
            		String data = null;
            		
            		for (int r=0; r < model.getRowCount(); r++){
            			data = (String)table.getValueAt(r,colidx);
            			if (data.contains( txfName.getText()) ){
            				rowidx = r;
            				System.out.println("found row:" + rowidx);
            				break;
            			}
            		}
            		
            		if (rowidx >= 0){
            			showFound(rowidx);
            		}
            	}
            	
            }
        });
		
		
		txfZip = new JTextField();
		txfZip.setColumns(10);
		txfZip.setBounds(449, 300, 46, 21);
		contentPane.add(txfZip);
		
		JLabel label_1 = new JLabel("\u5340\u78BC");
		label_1.setBounds(410, 303, 46, 15);
		contentPane.add(label_1);
		
		txfAddr = new JTextField();
		txfAddr.setColumns(10);
		txfAddr.setBounds(48, 300, 352, 21);
		contentPane.add(txfAddr);
		
		JLabel label_2 = new JLabel("\u5730\u5740");
		label_2.setBounds(10, 303, 29, 15);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("\u5E02\u8A711");
		label_3.setBounds(10, 244, 46, 15);
		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("\u5E02\u8A712");
		label_4.setBounds(181, 244, 46, 15);
		contentPane.add(label_4);
		
		txfTel1 = new JTextField();
		txfTel1.setColumns(10);
		txfTel1.setBounds(49, 241, 112, 21);
		contentPane.add(txfTel1);
		txfTel1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
        		if (txfTel1.getText().length()==0 || txfTel1.getText().equals(" "))
        			return;
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		int colidx = model.getHeaders().indexOf( "htel1" );
            		int rowidx = -1;
            		String data = null;
            		for (int r=0; r < model.getRowCount(); r++){
            			data = (String)table.getValueAt(r,colidx);
            			if (data.contains( txfTel1.getText()) ){
            				rowidx = r;
            				System.out.println("found row:" + rowidx);
            				break;
            			}
            		}
            		if (rowidx >= 0){
            			showFound(rowidx);
            		}
            	}
            }
        });		
		
		txfTel2 = new JTextField();
		txfTel2.setColumns(10);
		txfTel2.setBounds(223, 241, 112, 21);
		contentPane.add(txfTel2);
		txfTel2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
		    	if (txfTel2.getText().length()==0  || txfTel2.getText().equals(" "))
        			return;
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		int colidx = model.getHeaders().indexOf( "htel2" );
            		int rowidx = -1;
            		String data = null;
            		for (int r=0; r < model.getRowCount(); r++){
            			data = (String)table.getValueAt(r,colidx);
            			if (data.contains( txfTel2.getText()) ){
            				rowidx = r;
            				System.out.println("found row:" + rowidx);
            				break;
            			}
            		}
            		if (rowidx >= 0){
            			showFound(rowidx);
            		}
            	}
            }
        });	
		
		JLabel label_5 = new JLabel("\u624B\u6A5F");
		label_5.setBounds(10, 216, 46, 15);
		contentPane.add(label_5);
		
		txfMobile = new JTextField();
		txfMobile.setColumns(10);
		txfMobile.setBounds(49, 213, 112, 21);
		contentPane.add(txfMobile);
		
		txfMobile.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
		    	if (txfMobile.getText().length()==0 || txfMobile.getText().equals(" "))
        			return;
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		int colidx = model.getHeaders().indexOf( "mobile" );
            		int rowidx = -1;
            		String data = null;
            		for (int r=0; r < model.getRowCount(); r++){
            			data = (String)table.getValueAt(r,colidx);
            			if (data.contains( txfMobile.getText()) ){
            				rowidx = r;
            				System.out.println("found row:" + rowidx);
            				break;
            			}
            		}
            		if (rowidx >= 0){
            			showFound(rowidx);
            		}
            	}
            }
        });	
		
		
		txfStat = new JTextField();
		txfStat.setColumns(10);
		txfStat.setBounds(49, 362, 112, 21);
		contentPane.add(txfStat);
		txfStat.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
        		if (txfStat.getText().length()==0 || txfStat.getText().equals(" "))
        			return;
            	if (e.getKeyCode() == KeyEvent.VK_F1){
            		for (int idx=0;idx<statList.size(); idx++){
            			if (statList.get(idx).contains(txfStat.getText())){
            				cbxStat.setSelectedIndex(idx);
            			}
            		}
            	}
            }
        });			
		
		label_6 = new JLabel("\u72C0\u614B");
		label_6.setBounds(10, 365, 46, 15);
		contentPane.add(label_6);
		
		cbxStat = new JComboBox(statList.toArray());
		cbxStat.setBounds(174, 362, 115, 21);
		cbxStat.setFocusable(false);
		cbxStat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txfStat.setText(cbxStat.getSelectedItem().toString());
			}
		});		
		contentPane.add(cbxStat);
		
		label_7 = new JLabel("\u5BB6\u5EAD\u6210\u54E1");
		label_7.setBounds(10, 409, 70, 15);
		contentPane.add(label_7);
		
		txfFm1 = new JTextField();
		txfFm1.setColumns(10);
		txfFm1.setBounds(72, 393, 70, 21);
		contentPane.add(txfFm1);
		
		txfFm2 = new JTextField();
		txfFm2.setColumns(10);
		txfFm2.setBounds(152, 393, 70, 21);
		contentPane.add(txfFm2);
		
		txfFm3 = new JTextField();
		txfFm3.setColumns(10);
		txfFm3.setBounds(235, 393, 70, 21);
		contentPane.add(txfFm3);
		
		txfFm6 = new JTextField();
		txfFm6.setColumns(10);
		txfFm6.setBounds(235, 417, 70, 21);
		contentPane.add(txfFm6);
		
		txfFm5 = new JTextField();
		txfFm5.setColumns(10);
		txfFm5.setBounds(152, 417, 70, 21);
		contentPane.add(txfFm5);
		
		txfFm4 = new JTextField();
		txfFm4.setColumns(10);
		txfFm4.setBounds(72, 417, 70, 21);
		contentPane.add(txfFm4);
		
		label_8 = new JLabel("\u5099\u8A3B");
		label_8.setBounds(10, 448, 46, 15);
		contentPane.add(label_8);
		
		btnAdd = new JButton("新增");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enterAddMode();
				
			}
		});
		btnAdd.setBounds(10, 571, 95, 30);
		contentPane.add(btnAdd);
		
		btnSave = new JButton("\u5132\u5B58");
		btnSave.setEnabled(false);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mode == ADD){
					if(fieldFormatOK())
						saveRecord(-1); //add New
					else
						return;
				}
				else if (mode == EDIT){
					if(fieldFormatOK())
						saveRecord( Integer.valueOf(   table.getValueAt(table.getSelectedRow(), 0).toString() ) );
					else
						return;
				}
									            
				loadData("");
				showUI();
				
    			table.scrollRectToVisible(table.getCellRect(table.getRowCount()-1, 0, true));
    			table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
    			readIntoFields();
			}
		});
		btnSave.setBounds(313, 571, 95, 30);
		contentPane.add(btnSave);
		
		btnDel = new JButton("刪除");
		btnDel.setEnabled(false);
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "";
				String id = table.getValueAt( table.getSelectedRow(), 0).toString();
				int n = JOptionPane.showConfirmDialog(   thisframe, "確認刪除資料編號  " + id + " 嗎?",  "確認", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					deleteRecord(id);
										
					loadData("");
					showUI();
				}

			}
		});
		btnDel.setBounds(133, 571, 70, 30);
		contentPane.add(btnDel);
		
		label_9 = new JLabel("\u641C\u5C0B");
		label_9.setBounds(10, 10, 46, 15);
		contentPane.add(label_9);
		
		txfSearch = new JTextField();
		txfSearch.setBounds(40, 7, 144, 21);
		contentPane.add(txfSearch);
		txfSearch.setColumns(10);
		txfSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_ENTER){
            		int colidx = model.getHeaders().indexOf( cbxSearch.getSelectedItem() );
            		int rowidx = -1;
            		String data = null;
            		for (int r=0; r < model.getRowCount(); r++){
            			data = (String)table.getValueAt(r,colidx);
            			if (data.equals( txfSearch.getText()) ){
            				rowidx = r;
            				System.out.println("found row:" + rowidx);
            				break;
            			}
            		}
            		if (rowidx >= 0){
            			table.scrollRectToVisible(table.getCellRect(rowidx, 0, true));
            			table.setRowSelectionInterval(rowidx, rowidx);
            			readIntoFields();
            		}
            		else{
            			popup(" 找不到相符資料");
            			enterViewMode();
            		}
            	}
            }
        });
		
		cbxSearch = new JComboBox(model.getHeaders().toArray());
		cbxSearch.setBounds(194, 7, 95, 21);
		contentPane.add(cbxSearch);
		cbxSearch.setFocusable(false);
		int htel1Idx = model.getHeaders().indexOf("htel1");
		if (htel1Idx >=0) cbxSearch.setSelectedIndex(htel1Idx);
		
		lblNewLabel_1 = new JLabel("\u5408\u8A08");
		lblNewLabel_1.setBounds(620, 10, 29, 15);
		contentPane.add(lblNewLabel_1);
		
		lblTotal = new JLabel("_________");
		lblTotal.setFont(new Font("PMingLiU", Font.PLAIN, 14));
		lblTotal.setHorizontalAlignment(JTextField.RIGHT);
		lblTotal.setBounds(647, 10, 64, 15);
		contentPane.add(lblTotal);
				
		label_11 = new JLabel("\u7B46");
		label_11.setBounds(712, 10, 21, 15);
		contentPane.add(label_11);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(72, 448, 422, 70);
		contentPane.add(scrollPane_1);
		
		txaNote = new JTextArea();
		scrollPane_1.setViewportView(txaNote);
		
		
		lblTotal.setText(  String.valueOf(model.getCount())  );
		table.setModel(model);
		
		btnCancel = new JButton("\u53D6\u6D88");
		btnCancel.setEnabled(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enterViewMode();
			}
		});
		btnCancel.setBounds(424, 571, 87, 30);
		contentPane.add(btnCancel);
		
		JLabel lblPlp = new JLabel("\u4EBA\u53E3");
		lblPlp.setBounds(10, 531, 46, 15);
		contentPane.add(lblPlp);
		
		txfPlp = new JTextField();
		txfPlp.setColumns(10);
		txfPlp.setBounds(49, 528, 46, 21);
		contentPane.add(txfPlp);
		
		JLabel label_10 = new JLabel("\u7DE8\u865F");
		label_10.setBounds(181, 216, 37, 15);
		contentPane.add(label_10);
		
		txfID = new JTextField();
		txfID.setEditable(false);
		txfID.setFocusable(false);
		txfID.setBounds(222, 213, 96, 21);
		contentPane.add(txfID);
		txfID.setColumns(10);
		
		lblShowHelp = new JLabel("\u5728\u624B\u6A5F, \u5E02\u8A71 \u6216 \u59D3\u540D\u6B04\u4F4D, \u6309F1\u53EF\u6AA2\u67E5\u8CC7\u6599\u662F\u5426\u91CD\u8907");
		lblShowHelp.setVerticalAlignment(SwingConstants.TOP);
		lblShowHelp.setForeground(Color.RED);
		lblShowHelp.setFont(new Font("PMingLiU", Font.PLAIN, 16));
		lblShowHelp.setBounds(171, 268, 389, 21);
		contentPane.add(lblShowHelp);
		
		table.getColumnModel().getColumn(1).setMinWidth(50);
		table.getColumnModel().getColumn(2).setMinWidth(60);
		table.getColumnModel().getColumn(3).setMinWidth(30);
		table.getColumnModel().getColumn(4).setMinWidth(50);
		table.getColumnModel().getColumn(5).setMinWidth(30);
		table.getColumnModel().getColumn(7).setMinWidth(130);
		table.getColumnModel().getColumn(8).setMinWidth(80);
		
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		    	if (!table.isVisible())
		    		return;
		    	
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        //EDIT MODE
		        if (me.getClickCount() == 2) {
		        	enterEditMode();
		        }
		        
		        if (me.getClickCount() == 1) {
		        	mode = VIEW;
		            debug("single click: " + 1);
		            btnDel.setEnabled(true);
		            disableFields();
		            readIntoFields();
		        }		        
		    }
		});

		enterViewMode();
		
	}
	
	private boolean fieldFormatOK(){
		if (txfMobile.getText().contains("-")){
			popup("手機格式不得有  '-' 符號! 正確範例: 0928397123");
			txfMobile.requestFocus();
			return false;
		}
		if (txfMobile.getText().length() < 10){
			popup("手機號碼長度太短! 正確範例: 0928397123");
			txfMobile.requestFocus();
			return false;
		}
		if (txfTel1.getText().contains("-")){
			popup("市話1格式不得有  '-' 符號! 正確範例: 5335053");
			txfTel1.requestFocus();
			return false;
		}
		if (txfTel1.getText().startsWith("0")){
			popup("市話1格式不得為0開頭! 正確範例: 5335053");
			txfTel1.requestFocus();
			return false;
		}		
		if (txfTel2.getText().contains("-")){
			popup("市話2格式不得有  '-' 符號! 正確範例: 5335053");
			txfTel2.requestFocus();
			return false;
		}		

		if (txfTel2.getText().startsWith("0")){
			popup("市話2格式不得為0開頭! 正確範例: 5335053");
			txfTel2.requestFocus();
			return false;
		}		
		
		return true;
		
	}
	
	private void saveRecord(int id){
		Statement stat = null;
		ResultSet rs = null;
		String sql = "";
				
		//INSERT INTO main VALUES (val1, val2...)
		if (id == -1){ //add new record
			sql = "INSERT INTO main VALUES (null," 
				+ "'" + txfName.getText() + "',"
				+ "'" + txfTel1.getText() + "',"
				+ "'" + txfTel2.getText() + "',"
				+ "'" + txfMobile.getText() + "',"
				+ "'" + txfStat.getText() + "',"
				+ "'" + txfZip.getText() + "',"
				+ "'" + txfAddr.getText() + "',"
				+ "'" + txfType.getText() + "',"
				+ "'" + txfFm1.getText() + "',"
				+ "'" + txfFm2.getText() + "',"
				+ "'" + txfFm3.getText() + "',"
				+ "'" + txfFm4.getText() + "',"
				+ "'" + txfFm5.getText() + "',"
				+ "'" + txfFm6.getText() + "',"
				+ "'" + txaNote.getText() + "',"
				+ "'" + txfPlp.getText() + "')";
		}
		else{
			sql = "UPDATE service.main SET "
					+ "name='" + txfName.getText() + "',"
					+ "htel1='" + txfTel1.getText() + "',"
					+ "htel2='" + txfTel2.getText() + "',"
					+ "mobile='" + txfMobile.getText() + "',"
					+ "stat='" + txfStat.getText() + "',"
					+ "zip='" + txfZip.getText() + "',"
					+ "addr='" + txfAddr.getText() + "',"
					+ "type='" + txfType.getText() + "',"
					+ "fm1='" + txfFm1.getText() + "',"
					+ "fm2='" + txfFm2.getText() + "',"
					+ "fm3='" + txfFm3.getText() + "',"
					+ "fm4='" + txfFm4.getText() + "',"
					+ "fm5='" + txfFm5.getText() + "',"
					+ "fm6='" + txfFm6.getText() + "',"
					+ "note='" + txaNote.getText() + "',"
					+ "plp='" + txfPlp.getText() + "' "
					+ " WHERE id = "+ id;
		}
			
			debug(sql);

			try {
				stat = con.createStatement();
				stat.executeUpdate(sql);
			}catch (SQLException e) {
				debug("saveRecord INSERT Exception :" + e.toString());
			}
			
					
	}
	private void enterAddMode(){
		mode = ADD;
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAdd.setEnabled(false);
        btnDel.setEnabled(false);
        table.setVisible(false);
        txfSearch.setEnabled(false);
        cbxSearch.setEnabled(false);
        cleanFields();
        enableFields();
        lblShowHelp.setVisible(true);		
	}

	private void enterEditMode(){
		mode = EDIT;
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAdd.setEnabled(false);
        btnDel.setEnabled(false);
        table.setVisible(false);
        txfSearch.setEnabled(false);
        cbxSearch.setEnabled(false);
        readIntoFields();
        enableFields();
        lblShowHelp.setVisible(true);		
	}
	
	private void enterViewMode(){
		mode = VIEW;
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnAdd.setEnabled(true);
        btnDel.setEnabled(false);	
        table.setVisible(true);
        txfSearch.setEnabled(true);
        cbxSearch.setEnabled(true);
        
        cleanFields();
        disableFields();	
        txfMobile.requestFocus();
        lblShowHelp.setVisible(false);

	}
	
	private void readIntoFields(){
		int row = table.getSelectedRow();
		txfID.setText( table.getValueAt(row,0).toString() );
		txfName.setText( table.getValueAt(row,1).toString() );
		txfTel1.setText( table.getValueAt(row,2).toString() );
		txfTel2.setText( table.getValueAt(row,3).toString() );
		txfMobile.setText( table.getValueAt(row,4).toString() );
		txfStat.setText( table.getValueAt(row,5).toString() );
		txfZip.setText( table.getValueAt(row,6).toString() );
		txfAddr.setText( table.getValueAt(row,7).toString() );
		txfType.setText( table.getValueAt(row,8).toString() );
		txfFm1.setText( table.getValueAt(row,9).toString() );
		txfFm2.setText( table.getValueAt(row,10).toString() );
		txfFm3.setText( table.getValueAt(row,11).toString() );
		txfFm4.setText( table.getValueAt(row,12).toString() );
		txfFm5.setText( table.getValueAt(row,13).toString() );
		txfFm6.setText( table.getValueAt(row,14).toString() );
		txaNote.setText( table.getValueAt(row,15).toString() );
		txfPlp.setText( table.getValueAt(row,16).toString() );

		
	}
	private boolean isNumeric(String str)
	{
	  return str.matches("\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}	
	
	private void enableFields(){
		txfMobile.requestFocus();
		txfMobile.setEditable(true);
		txfTel1.setEditable(true);
		txfTel2.setEditable(true);
		txfName.setEditable(true);
		txfAddr.setEditable(true);
		txfZip.setEditable(true);
		txfType.setEditable(true);
		txfStat.setEditable(true);
		txfFm1.setEditable(true);
		txfFm2.setEditable(true);
		txfFm3.setEditable(true);
		txfFm4.setEditable(true);
		txfFm5.setEditable(true);
		txfFm6.setEditable(true);
		txaNote.setEditable(true);
		txfPlp.setEditable(true);
		
		cbxType.setEnabled(true);
		cbxStat.setEnabled(true);
	}

	private void disableFields(){
		txfMobile.requestFocus();
		txfMobile.setEditable(false);
		txfTel1.setEditable(false);
		txfTel2.setEditable(false);
		txfName.setEditable(false);
		txfAddr.setEditable(false);
		txfZip.setEditable(false);
		txfType.setEditable(false);
		txfStat.setEditable(false);
		txfFm1.setEditable(false);
		txfFm2.setEditable(false);
		txfFm3.setEditable(false);
		txfFm4.setEditable(false);
		txfFm5.setEditable(false);
		txfFm6.setEditable(false);
		txaNote.setEditable(false);
		txfPlp.setEditable(false);
		cbxType.setEnabled(false);
		cbxStat.setEnabled(false);
		
		
	}
	
	private void cleanFields(){
		txfMobile.requestFocus();
		txfMobile.setText("");
		txfID.setText("");
		txfTel1.setText("");
		txfTel2.setText("");
		txfName.setText("");
		txfAddr.setText("");
		txfZip.setText("");
		txfType.setText("");
		txfStat.setText("");
		txfFm1.setText( "" );
		txfFm2.setText( "" );
		txfFm3.setText( "");
		txfFm4.setText( "");
		txfFm5.setText( "");
		txfFm6.setText( "");
		txaNote.setText( "");
		txfPlp.setText( "");		
	}
	
	private void deleteRecord(String id){
		Statement stat = null;
		String sql = "DELETE FROM service.main where id = " + id;
		
		debug(sql);

		try {
			stat = con.createStatement();
			stat.executeUpdate(sql);

			stat.close();
		}catch (SQLException e) {
			debug("deleteRecord Exception :" + e.toString());
		}		
		
	}
	private void loadData(String sql){
		Statement stat = null;
		ResultSet rs = null;
		int count = 0;
		if (sql == null || sql == "")
			sql = "SELECT * from service.main";  
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			model = new MyTableModel(rs);
						
		}catch (SQLException e) {
			debug("loadData Exception :" + e.toString());
		}
		
	}
	
	private ArrayList<String> getTypeList(){
		Statement stat = null;
		ResultSet rs = null;
		ArrayList<String> gl = new ArrayList<String>();
		String sql = "";

		sql = "SELECT distinct type from service.main";  
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			while(rs.next()){
				gl.add( rs.getString("type") );
			}
			rs.close();
			stat.close();			
		}catch (SQLException e) {
			debug("loadData Exception :" + e.toString());
		}

		return gl;
	}

	private ArrayList<String> getStatList(){
		Statement stat = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String sql = "";

		sql = "SELECT distinct stat from service.main";  
		
		debug(sql);

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			
			while(rs.next()){
				list.add( rs.getString("stat") );
			}
			rs.close();
			stat.close();
		}catch (SQLException e) {
			debug("loadData Exception :" + e.toString());
		}

		return list;
	}
	
	
	private Connection getMysqlCon() {

		String driver = "com.mysql.jdbc.Driver";
		String serverip = "localhost";
		String dbTable = "service";
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
	
	private void popup(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}
	private void showFound(int rowidx){
		popup("發現類似資料: '" + txfName.getText() + "' "
				+ "\n編號 : " +table.getValueAt(rowidx, 0)  
				+ "\n姓名: " + table.getValueAt(rowidx, 1)
				+ "\n市話1: " + table.getValueAt(rowidx, 2)
				+ "\n市話2: " + table.getValueAt(rowidx, 3)
				+ "\n手機: " + table.getValueAt(rowidx, 4)
				+ "\n地址: " + table.getValueAt(rowidx, 7)
				+ "\n群組: " + table.getValueAt(rowidx, 8)
				);
		
	}
}
