import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ibm.icu.util.Calendar;
import java.text.NumberFormat;

public class KYutil {
	public NumberFormat nf = NumberFormat.getCurrencyInstance();
	public String getPcodeFromListItem(String item){
		String pcode = "";
		if (item.contains(",")){
			pcode = item.split(",")[1]; //get the 2nd sub string
		}
		
		return pcode;
	}
	public boolean logicCompare(String logic, float v1, float v2){
		switch (logic){
		case ">=":
			return (v1 >= v2);
		case "<=":
			return (v1 <= v2);
		case ">":
			return (v1 > v2);
		case "<":
			return (v1 < v2);
		case "=":
			return (v1 == v2);
		default:
			return false;
		}
	}
	
	public Vector<String> create10yearVector() {
		Vector<String> vec;
		int ye = Calendar.getInstance().get(Calendar.YEAR);
		vec = createRangeVector(ye-10, 10);
		return vec;
	}
	
	public Vector<String> createRangeVector(int start, int len) {
		Vector<String> vec = new Vector<String>();
		for (int i = start+len; i >= start; i--){
			vec.add(String.valueOf(i));
		}
		return vec;
	}
	
	public String f1decimal(float f) {
		return String.format("%.1f", f);
	}
	
	public String f0decimal(float f) {
		return String.valueOf(Math.round(f));
	}
	
	public void debug(String text) {
		System.out.println(text);
	}

	public void warning(String ws, JFrame mainFrame) {
		JOptionPane.showMessageDialog(mainFrame, ws, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}

	private void debugStringVec(Vector<String> v) {
		for (int i = 0; i < v.size(); i++) {
			debug(v.get(i));
		}
	}
	
	public String capitalize(String input){
		String output;
		output = input.toLowerCase();
		output = output.substring(0, 1).toUpperCase() + output.substring(1);
		
		return output;
	}
}
