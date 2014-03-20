import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Calendar;


public class PanelYearRange extends JPanel {

	private int YEAR_RANGE = 10;
	private int YE = Calendar.getInstance().get(Calendar.YEAR); // end year
	private JComboBox<String> cbxYS;
	private JComboBox<String> cbxYE;
	private JLabel lblyrange;
	
	public PanelYearRange(String title) {
		lblyrange = new JLabel(title);
		cbxYS = new JComboBox<String>(create10yearVector());
		cbxYE = new JComboBox<String>(create10yearVector());

		cbxYS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<String> tv = new Vector<String>();
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				int selectedYear = Integer.valueOf((String) combo
						.getSelectedItem());
				// automatically updates YE combo box list
				cbxYE.removeAllItems();
				tv = createRangeVector(selectedYear, YE-selectedYear);
				for (int i = 0; i < tv.size(); i++)
					cbxYE.addItem(tv.get(i).toString());
			}
		});
		
		
		this.add(lblyrange);
		this.add(cbxYS);
		this.add(cbxYE);
		
	}

	
	private Vector<String> create10yearVector() {
		Vector<String> vec;
		int ye = Calendar.getInstance().get(Calendar.YEAR);
		vec = createRangeVector(ye-10, 10);
		return vec;
	}
	
	private Vector<String> createRangeVector(int start, int len) {
		Vector<String> vec = new Vector<String>();
		for (int i = start+len; i >= start; i--){
			vec.add(String.valueOf(i));
		}
		return vec;
	}
	public String getYS(){
		return (String)cbxYS.getSelectedItem();		
	}
	
	public String getYE(){
		return (String)cbxYE.getSelectedItem();
	}
	
	public int getYSint(){
		return Integer.valueOf((String)cbxYS.getSelectedItem());		
	}
	
	public int getYEint(){
		return Integer.valueOf((String)cbxYE.getSelectedItem());	
	}
	

}
