
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import java.awt.Color;

public class PanelTools extends JPanel {
	private int YEAR_RANGE = 10;
	private int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	private int YS = YE - YEAR_RANGE; // start year
	
	
	private String pcodeTitle = "";
	private KYutil u = new KYutil();
	private KYdb db = new KYdb();
	
	private JTextField txf_keyword;
	JLabel lblimg;
	private String selectedpcode = "";

	JPanel chartpanel = new JPanel();
	private JTextField txfpcode;
	private JTextField txfpath;
	JLabel lblpreview = new JLabel("");
	
	
	private String convertKeyValue2geomapData(Vector<KeyValue_String_float> countrylist){
		String stringdata = "";
		KeyValue_String_float kv;
		for (int i=0;i<countrylist.size();i++){
			kv = countrylist.get(i);
			//u.debug(kv.key+":"+kv.value+"  "+kv.show);
			if (kv.show){
				if (stringdata == "")
					stringdata += "['" + kv.key + "', " + kv.value + "]";
				else
					stringdata += ", ['" + kv.key + "', " + kv.value + "]";
				
			}
		}
		//u.debug("stringdata:"+stringdata);
		return stringdata;
		
	}
	public PanelTools() {
		setLayout(new GridLayout(0, 1, 0, 0));
		//setLayout(new GridLayout(2, 0, 0, 0));
		//setBorder(new TitledBorder(null, "品種清單",TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JSplitPane splitpanel = new JSplitPane();
		JPanel settingpanel = new JPanel();
		settingpanel.setLayout(null);

		
		splitpanel.setDividerLocation(350);
		splitpanel.add(settingpanel, "left");
		splitpanel.add(chartpanel, "right");
		add(splitpanel);

	    chartpanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel label = new JLabel("圖檔目錄位置");
		label.setBounds(25, 10, 72, 15);
		settingpanel.add(label);
		
		
		txf_keyword = new JTextField();
		txf_keyword.setBounds(106, 10, 96, 21);
		settingpanel.add(txf_keyword);
		txf_keyword.setText("D:\\var_pics");
		txf_keyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scanIntoDb(txf_keyword.getText());
			}
		});
		txf_keyword.setColumns(10);
		
	
		JButton btn_search_kw = new JButton("自動插入品種圖");
		btn_search_kw.setBounds(211, 10, 117, 23);
		settingpanel.add(btn_search_kw);
		
		lblimg = new JLabel("");
		lblimg.setBackground(Color.GRAY);
		lblimg.setHorizontalAlignment(SwingConstants.CENTER);
		lblimg.setBounds(10, 41, 329, 253);
		settingpanel.add(lblimg);
		
		JLabel label_1 = new JLabel("\u8F38\u5165\u96FB\u7DE8");
		label_1.setBounds(10, 305, 80, 15);
		settingpanel.add(label_1);
		
		txfpcode = new JTextField();
		txfpcode.setBounds(72, 302, 103, 21);
		settingpanel.add(txfpcode);
		txfpcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectedpcode = txfpcode.getText();
				lblimg.setIcon(db.getImage_by_pcode(selectedpcode));				
			}
		});
		txfpcode.setColumns(10);
		
		JLabel lblImgPath = new JLabel("img path");
		lblImgPath.setBounds(10, 330, 60, 24);
		settingpanel.add(lblImgPath);
		
		txfpath = new JTextField();
		txfpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = new ImageIcon(txfpath.getText());
				lblpreview.setIcon(icon);					
			}
		});
		txfpath.setBounds(73, 331, 160, 21);
		settingpanel.add(txfpath);
		txfpath.setColumns(10);
		
		JButton btnUpdateImg = new JButton("更新品種圖");
		btnUpdateImg.setBounds(246, 331, 93, 23);
		settingpanel.add(btnUpdateImg);
		
		
		lblpreview.setHorizontalAlignment(SwingConstants.CENTER);
		lblpreview.setBackground(Color.GRAY);
		lblpreview.setBounds(10, 365, 329, 237);
		settingpanel.add(lblpreview);
		btnUpdateImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = new ImageIcon(txfpath.getText());
				lblpreview.setIcon(icon);
				update_img(txfpath.getText(), selectedpcode);
			}
		});
		btn_search_kw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scanIntoDb(txf_keyword.getText());
			}
		});
	}
	
	private void update_img(String imgpath, String pcode){
		
		InputStream in = null;  	
		File f = new File(imgpath);
		try {
			in = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.update_vege_prod_img_by_pcode(pcode, in, f.length());
	}
	
	private void scanIntoDb(String imgpath) {
		String fname = "";
		String pcode = "";
		 InputStream in = null;  
         
		int count = 0;
		File dir = new File(imgpath);
		if (dir.exists() && dir.isDirectory()) {

			for (File f : dir.listFiles()) {
				fname = f.getName();
				pcode = db.getPcode_by_pcname(fname);
				if (fname.contains("250") && pcode != "") {// only size of 250 gets inserted to DB
					count++;
					u.debug(count + "\t" + fname + "\t pcode:" + pcode  + "\t len:" + f.length());
					try {
						in = new BufferedInputStream(new FileInputStream(f));
						
						db.update_vege_prod_img_by_pcode(pcode, in, f.length());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				} else
					u.debug(fname + " N/A");

			}

		} else {
			u.debug(imgpath + " does not exist!");
		}

	} 
}
