import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;


public abstract class PanelSearchCust extends JPanel {

	public JTextField txfKeyword = new JTextField();
	public JCheckBox chkSort = new JCheckBox("排序");
	public JComboBox<String> cbxSort = new JComboBox<String>();
	public PanelYearRange panelyr = new PanelYearRange("統計期間");
	
	private MyTableModel model;
	private JTable atable = new JTable();
	
	
	public static void main(String[] args) {
		KYdb db = new KYdb();
		try {
			JFrame frame = new JFrame();
			PanelSearchCust pcust = new PanelSearchCust(){
				public void update(){
					
				}
			};
			
			frame.getContentPane().add(pcust);
			frame.setVisible(true);
			frame.setSize(500, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PanelSearchCust() {
		this.setLayout(new GridLayout(2, 0, 0, 0));
		JPanel panelFilter = new JPanel();
		JPanel panelTable = new JPanel();
		this.add(panelFilter);
		this.add(panelTable);

		panelFilter.setLayout(new GridLayout(3, 0, 0, 0));
		
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
		
		panelFilter.add(compKeyword());
		panelFilter.add(panelyr);
		panelFilter.add(compSort());
		
		
		
	}
	
	private void updateList(){
		
	}
	
	private JComponent compKeyword(){
		JPanel panel = new JPanel();
		JButton btnSearchKeyWord = new JButton("搜尋客戶關鍵字");
		txfKeyword.setColumns(10);
		panel.add(txfKeyword);
		panel.add(btnSearchKeyWord);

		
		ActionListener actl = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		};
		
		btnSearchKeyWord.addActionListener(actl);
		txfKeyword.addActionListener(actl);
		
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
	public abstract void update();
}
