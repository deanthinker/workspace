import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public abstract class PanelSearchPcode extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SHOWIMAGE = 1;
	public static final int NOIMAGE = 0;
	
	KYutil u = new KYutil();
	KYdb db = new KYdb();

	public String title;
	public String selectedpcode;
	private JTextField txfKeyword = new JTextField();
	private DefaultListModel<String> listVegeProd_model = new DefaultListModel<String>();
	public JList<String> listVegeProd = new JList<String>(listVegeProd_model);
	public JScrollPane scpVegeList = new JScrollPane(listVegeProd);
	public JComboBox<String> cbxCrop = new JComboBox<String>(db.getVegeCropVec());
	public JComboBox<String> cbxYear = new JComboBox<String>(u.create10yearVector());		
	public JCheckBox chkSort = new JCheckBox("�Ƨ�");
	public JComboBox<String> cbxSort = new JComboBox<String>();
	
	public PanelYearRange panelyr = new PanelYearRange("�έp����");

	
	private JLabel lblimg = new JLabel("");

	
	private void updateList(){
		String filter;
		if (chkSort.isSelected()){
				
				if (cbxCrop.getSelectedIndex() > 0)  
					filter = (String)cbxCrop.getSelectedItem();
				else
					filter = null;
						
				cbxSort.setEnabled(true);
				
				switch (cbxSort.getSelectedIndex()){
					case 0: //sales
						db.fillList_vege_prod_orderby_sales(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
						break;
						
					case 1: //weight
						db.fillList_vege_prod_orderby_weight(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
						break;
				}
				
		}
		else{
				if (cbxCrop.getSelectedIndex()==0) 
					db.fillList_vege_prod(listVegeProd,false, null);
				else
					db.fillList_vege_prod(listVegeProd,true, (String)cbxCrop.getSelectedItem());
		}
		
		listVegeProd.repaint();
		txfKeyword.setText("");
	}
		
	private JComponent compCrop(){
		JPanel panel = new JPanel();
		JLabel lbl = new JLabel("��ܧ@�����O");
		
		cbxCrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkSort.setSelected(false);
				cbxSort.setEnabled(false);
				if (cbxCrop.getSelectedIndex()==0) 
					db.fillList_vege_prod(listVegeProd,false, null);
				else
					db.fillList_vege_prod(listVegeProd,true, (String)cbxCrop.getSelectedItem());
				
				listVegeProd.repaint();
				txfKeyword.setText("");
			}
		});	
				
		panel.add(lbl);
		panel.add(cbxCrop);
		return panel;
	}
	
	private JComponent compSort(){
		JPanel panel = new JPanel();

		cbxSort.setModel(new DefaultComboBoxModel<String>(new String[] 
				{
				"�~�P���`�BNT$", 
				"�`�P���`��Kg" 
				}));

		panel.add(chkSort);
		panel.add(cbxSort);
		return panel;
	}

	private JComponent compKeyword(){
		JPanel panel = new JPanel();
		JButton btnSearchKeyWord = new JButton("�j�M����r");
		txfKeyword.setColumns(10);
		panel.add(txfKeyword);
		panel.add(btnSearchKeyWord);

		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.fillList_vege_prod_keyword(listVegeProd,txfKeyword.getText());
			}
		};
		
		btnSearchKeyWord.addActionListener(actl);
		txfKeyword.addActionListener(actl);
		
		return panel;
	}
	
	public PanelSearchPcode(final int IMAGEMODE) {
		if (IMAGEMODE == SHOWIMAGE) this.setLayout(new GridLayout(3, 0, 0, 0));
		else this.setLayout(new GridLayout(2, 0, 0, 0));
				
		JPanel panelImage = new JPanel();
		JPanel panelFilter = new JPanel();
		JPanel panelList = new JPanel();
		
		if (IMAGEMODE == SHOWIMAGE) this.add(panelImage);
		this.add(panelFilter);
		this.add(panelList);

		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		};	
		
		
		chkSort.addActionListener(actl);
		cbxSort.addActionListener(actl);
						
		if (IMAGEMODE == SHOWIMAGE) panelImage.add(lblimg);
		if (IMAGEMODE == SHOWIMAGE) lblimg.setVerticalAlignment(SwingConstants.TOP);

		panelList.add(scpVegeList);
		
		
		panelFilter.setLayout(new GridLayout(4, 0, 0, 0));
		panelFilter.add(compKeyword());
		panelFilter.add(panelyr);
		panelFilter.add(compCrop());
		panelFilter.add(compSort());
		
		listVegeProd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scpVegeList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		db.fillList_vege_prod(listVegeProd,false, null);
		listVegeProd.setSelectedIndex(0);		
		listVegeProd.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;

				JList theList = (JList) e.getSource();
				if (theList.isSelectionEmpty()) {
				} 
				else {
					title = theList.getSelectedValue().toString();
					selectedpcode = u.getPcodeFromListItem(title);
					
					if (IMAGEMODE == SHOWIMAGE) lblimg.setIcon(db.getImage_by_pcode(selectedpcode));
					//------------IMPORTANT----------------
					updateChart(); //need to be implemented!!!!!
					//-------------------------------------
				}

			}
		});
		
		listVegeProd.repaint();
	
	}

	public abstract void updateChart();
	
}
