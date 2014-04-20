import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	public static final int EXPORT = 1;
	public static final int DOMESTIC = 2;
	public int DATASRC = EXPORT; 
	
	KYutil u = new KYutil();
	KYdb db = new KYdb();

	public String title;
	public String selectedpcode;
	private JTextField txfKeyword = new JTextField();
	private DefaultListModel<String> listVegeProd_model = new DefaultListModel<String>();
	public JList<String> listVegeProd = new JList<String>(listVegeProd_model);
	public JScrollPane scpVegeList = new JScrollPane(listVegeProd);
	public JComboBox<String> cbxCrop = new JComboBox<String>(db.getVegeCropVec());
	public JCheckBox chkSort = new JCheckBox("排序");
	public JComboBox<String> cbxSort = new JComboBox<String>();
	
	public PanelYearRange panelyr = new PanelYearRange("統計期間");

	
	private JLabel lblimg = new JLabel("");

	
	private void updateList(){
		String filter;
		if (chkSort.isSelected()){
				
				if (cbxCrop.getSelectedIndex() > 0)  
					filter = (String)cbxCrop.getSelectedItem();
				else
					filter = null;
						
				cbxSort.setEnabled(true);
				
				if (DATASRC == EXPORT){
					switch (cbxSort.getSelectedIndex()){
						case 0: //sales
							db.fillList_exp_prod_orderby_sales(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
							break;
							
						case 1: //weight
							db.fillList_exp_prod_orderby_weight(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
							break;
					}
				}
				else if (DATASRC == DOMESTIC){
					switch (cbxSort.getSelectedIndex()){
					case 0: //sales
						db.fillList_dom_prod_orderby_sales(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
						break;
						
					case 1: //weight
						db.fillList_dom_prod_orderby_weight(listVegeProd, panelyr.getYSint(), panelyr.getYEint(), filter); 
						break;
				}					
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
		JLabel lbl = new JLabel("顯示作物類別");
		
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
				"年銷售總額NT$", 
				"年銷售總重Kg" 
				}));

		panel.add(chkSort);
		panel.add(cbxSort);
		return panel;
	}

	private JComponent compKeyword(){
		JPanel panel = new JPanel();
		JButton btnSearchKeyWord = new JButton("搜尋品種關鍵字");
		txfKeyword.setColumns(10);
		panel.add(txfKeyword);
		panel.add(btnSearchKeyWord);

		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int count = db.fillList_vege_prod_keyword(listVegeProd,txfKeyword.getText());
				if (count == 1){
					listVegeProd.setSelectedIndex(0); //select the 1st item automatically
				}
					
			}
		};
		
		btnSearchKeyWord.addActionListener(actl);
		txfKeyword.addActionListener(actl);
		
		return panel;
	}
	
	public PanelSearchPcode(final int IMAGEMODE, final int SRC) {
		DATASRC = SRC;
		if (IMAGEMODE == SHOWIMAGE) this.setLayout(new GridLayout(3, 0, 0, 0));
		else this.setLayout(new GridLayout(2, 0, 0, 0));
				
		JPanel panelImage = new JPanel();
		JPanel panelFilter = new JPanel();
		JPanel panelList = new JPanel();
		
		if (IMAGEMODE == SHOWIMAGE) this.add(panelImage);
		this.add(panelFilter);
		this.add(panelList);
		
		chkSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chkSort.isSelected())
					cbxSort.setEnabled(true);
				else
					cbxSort.setEnabled(false);
				updateList();
			}
		});	
		
		cbxSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateList();
			}
		});	
		cbxSort.setEnabled(false); //set disabled by default
						
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
					update(); //need to be implemented!!!!!
					//-------------------------------------
				}

			}
		});
		
		listVegeProd.repaint();
	
	}

	public abstract void update();
	
}
