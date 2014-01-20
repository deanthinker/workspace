package importDB;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelFormat {
	public static void main(String[] args) {
		ExcelFormat xlf = new ExcelFormat();
		xlf.openXLS2();
	}
	
	public ExcelFormat(){
		
	}
	
	private void debug(String str){
		System.out.println(str);
	}
	
	
	private int getColIdx(Sheet s, String findStr){
		int col=s.getColumns();
		int idx =-1;
		for (int c=0;c<col;c++){
			if ( findStr.equalsIgnoreCase(s.getCell(c,0).getContents())){
				idx = c;
				break;
			}
		}
		
		return idx;
	}
	
	
	private boolean checkIfkeep(String str){
		boolean  keep = false;
		String[] fields = {
			"年度",
			"作物",
			"品種名",
			"生產編號",
			"實重",
			"發芽率",
			"純度",
			"Aac (+/-)",
			"CGMMV (+/-)",
			"其他病害名稱",
			"(+/-)"
		};		
		
		debug("header:"+str+"\tLen:"+str.length());
		for (int x=0;x<fields.length;x++){
			if (str.equalsIgnoreCase(fields[x])){
				keep = true;
				break;
			}
		}
		
		return keep;
	}
	
	
	private void openXLS1(){
		String filename = "d:/test.xls";
		String outputfile = "d:/temp.xls";
		Workbook read = null;
		Sheet[] rsheets = null;
		
		String[] rsheetnames = null;
		WritableWorkbook write = null;
		WritableSheet[] wsheets = null;
		WritableSheet ws = null;
		int rows = 0; 
		int cols =0;
		int sheets = 0;
		try {
			debug("Load Excel:"+filename + " Create:"+outputfile);
			read = Workbook.getWorkbook(new File(filename));
			write = Workbook.createWorkbook(new File(outputfile));
			debug("Excel Loaded/Created Successfully!");
			
		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//create sheets
		rsheets = read.getSheets();
		
		for (int x=rsheets.length-1;x>=0;x--){
			debug(rsheets[x].getName());
			ws = write.createSheet(rsheets[x].getName(), 0);
		}

		for (int s=0;s<rsheets.length;s++){
			cols = rsheets[s].getColumns();
			rows = rsheets[s].getRows();
			
			debug("sheet:"+rsheets[s].getName() + "\tCols:"+cols+"\tRows:"+rows);
			for (int c=0; c<cols; c++){
				Cell[] cellarr = rsheets[s].getColumn(c);
				
			}
			
		}
		
		
		
		
		
		//release memory
		read.close();
		try {
			write.write();
			write.close();

		} catch (WriteException | IOException e) {
			e.printStackTrace();
		}

		/*
		//print out cell contents
		for (int r=0; r < rows; r++){
			System.out.print("Row"+r+"\t");
			for (int x=0; x < cols; x++){
				//headers[x] = sheet.getCell(x, 0).getContents();
				if (x == cols-1)
					System.out.println(sheet.getCell(x, r).getContents() + "\t");
				else
					System.out.print(sheet.getCell(x, r).getContents() + "\t");
				
			}
		}*/
		
	}	

	private void openXLS2(){
		String filename = "d:/test.xls";
		String outputfile = "d:/temp.xls";
		Workbook read = null;
		Sheet[] rsheets = null;
		
		String[] rsheetnames = null;
		WritableWorkbook write = null;
		WritableSheet[] wsheets = null;
		WritableSheet ws = null;
		int rows = 0; 
		int cols =0;
		int sheets = 0;
		CellView cv = new CellView();
		cv.setHidden(true);
		int start =-1;
		int end = -1;
		
		try {
			debug("Load Excel:"+filename + " Creating: "+outputfile +" ...");
			read = Workbook.getWorkbook(new File(filename));
			write = Workbook.createWorkbook(new File(outputfile), read);
			debug("Excel Loaded/Created Successfully!");
						
			
			//scan sheets
			for (int s=0;s<write.getNumberOfSheets();s++){
				ws = write.getSheet(s);
				cols = ws.getColumns();
				rows = ws.getRows();
				start = getColIdx(ws, "純度");
				end = getColIdx(ws, "成苗率")-1;
				
				debug("sheet:"+ws.getName() + "\tCols:"+cols+"\tRows:"+rows);
				debug("purity:"+ start +"\t gemOK:"+end);
				for (int c=0;c<cols;c++){
					debug("header:"+ws.getCell(c,0).getContents());
					if (checkIfkeep( ws.getCell(c,0).getContents())==false && (c>end || c<start)){
						ws.setColumnView(c, cv); // hide the column
					}
				}
			}			
			
		} catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		
		//--the following code unhide purity
		//we need to locate purity col and the col comes after



		
		
		//release memory
		read.close();
		try {
			write.write();
			write.close();

		} catch (WriteException | IOException e) {
			e.printStackTrace();
		}

		/*
		//print out cell contents
		for (int r=0; r < rows; r++){
			System.out.print("Row"+r+"\t");
			for (int x=0; x < cols; x++){
				//headers[x] = sheet.getCell(x, 0).getContents();
				if (x == cols-1)
					System.out.println(sheet.getCell(x, r).getContents() + "\t");
				else
					System.out.print(sheet.getCell(x, r).getContents() + "\t");
				
			}
		}*/

		
	}	
	
}
