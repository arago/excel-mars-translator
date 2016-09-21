package de.arago.marstranslator.XlxsToMarsTranslator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelWriter {
	
	private String fileName; 
	private List<String> sheetNames = new ArrayList<String>(); 
	private XSSFWorkbook wb; 
	private static final Logger log = LoggerFactory.getLogger(ExcelWriter.class); 
	private int lastRow; 
	private int lastMachineRow ; 
	private int lastSoftwareRow ; 
	private int lastApplicationRow ; 
	private int lastResoucreRow ;
	
	public ExcelWriter(String fileName){
		this.fileName = fileName; 
		wb = new XSSFWorkbook(); 
		lastRow = 2; 
		lastMachineRow = 2; 
		lastSoftwareRow = 2; 
		lastApplicationRow = 2; 
		lastResoucreRow = 2; 
	}
	
	public void writeToSheet(String sheetName, Map<Integer,String> cellValues, Map<Integer,String> headerValues){
		XSSFSheet sheet; 
		if(sheetNames.contains(sheetName)){
			sheet = wb.getSheet(sheetName); 
			}
		else{
			sheet = wb.createSheet(sheetName); 
			sheetNames.add(sheetName); 
			}
		setRowNumber(headerValues);
		
		
		XSSFRow row = sheet.createRow(lastRow); 
		
		for(int i : cellValues.keySet()){
			XSSFCell cell = row.createCell(i); 
			cell.setCellValue(cellValues.get(i));
		}
		
		
		XSSFRow headerRow = sheet.getRow(0);
		if(headerRow == null); 
			headerRow = sheet.createRow(0); 
		
		for(int j : headerValues.keySet()){
			XSSFCell headerCell = headerRow.createCell(j); 
			headerCell.setCellValue(headerValues.get(j));
		}

		incRowNumber(headerValues);
	}

	private void incRowNumber(Map<Integer, String> headerValues) {
		if(headerValues.containsValue(NodeAttributes.machineClass)){
			lastMachineRow++;   
		} else if(headerValues.containsValue(NodeAttributes.softwareClass)){
			lastSoftwareRow++; 
		}else if(headerValues.containsValue(NodeAttributes.applicationClass)){
			lastApplicationRow++; 
		}else if(headerValues.containsValue(NodeAttributes.resourceClass)){
			 lastResoucreRow++; 
		}
	}

	private void setRowNumber(Map<Integer, String> headerValues) {
		if(headerValues.containsValue(NodeAttributes.machineClass)){
			lastRow = lastMachineRow; 
		} else if(headerValues.containsValue(NodeAttributes.softwareClass)){
			lastRow = lastSoftwareRow;
		}else if(headerValues.containsValue(NodeAttributes.applicationClass)){
			lastRow = lastApplicationRow;
		}else if(headerValues.containsValue(NodeAttributes.resourceClass)){
			lastRow = lastResoucreRow;
		}
	}
	
	
	public void saveWritingsToFile(){
		log.info("Machines written to Excel:" + (lastMachineRow -2));
		log.info("Softwares written to Excel:" + (lastSoftwareRow -2));
		log.info("Resources written to Excel:" + (lastResoucreRow -2));
		log.info("Applications written to Excel:" + (lastApplicationRow -2));
		
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
