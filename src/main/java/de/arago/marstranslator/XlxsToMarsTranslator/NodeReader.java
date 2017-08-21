package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.arago.marstranslator.XlxsToMarsTranslator.XlxProcessor.nodeTypes;



public class NodeReader {

	private static final Logger log = LoggerFactory.getLogger(NodeReader.class); 
	private List<Cell> machineHeaderList = new ArrayList<Cell>(); 
	private List<Cell> softwareHeaderList= new ArrayList<Cell>();  
	private List<Cell> resourceHeaderList= new ArrayList<Cell>(); 
	private List<Cell> applicationHeaderList= new ArrayList<Cell>(); 
	private List<Cell> machineHeaderKeyList = new ArrayList<Cell>(); 
	private List<Cell> softwareHeaderKeyList= new ArrayList<Cell>();  
	private List<Cell> resourceHeaderKeyList= new ArrayList<Cell>(); 
	private List<Cell> applicationHeaderKeyList= new ArrayList<Cell>(); 
	
	public List<Node> readListOfNodes(final XSSFWorkbook excel, String type){
		List<Node> nodeList = new ArrayList<Node>(); 
		
		final XSSFSheet sheet = excel.getSheet(type);
		if(sheet==null)
		{
			log.error(type+" sheet not found ! ");
		}
		final int lines = sheet.getLastRowNum();
		log.info("found "+lines+" lines in '"+type+"' ");
			
			final XSSFRow firstRow = sheet.getRow(0);
			
			readHeaderFromRow(type, firstRow); 

		for(int line = 2 ;line<=lines;line++) // skip the first line two lines 
		{
			
			final XSSFRow row = sheet.getRow(line);
			
			Node node =	readNodeFromRow(type, row); 
			if(node!=null)
				nodeList.add(node);
			}
		return nodeList; 
		
	}
	


	private Node readNodeFromRow(String type, XSSFRow row){
		if(trim(row,0)!=null&& (!trim(row,0).isEmpty())){
			switch(nodeTypes.valueOf(type)){
			case Machine: 
				return readMachineFromRow(row); 
			case Software: 
				return readSoftwareFromRow(row); 
			case Resource: 
				return readResourceFromRow(row); 
			case Application: 
				return readApplicationFromRow(row);
			default : 
				return null; 
			}
			
		}
		else {
			return null; 
		}
	}
	
	
	private Node readMachineFromRow( XSSFRow row){
		Machine machine = new Machine(); 
	
		if(row!=null&&row.getCell(0)!=null&&row.getCell(1)!=null&&row.getCell(2)!=null&&row.getCell(3)!=null&&row.getCell(6)!=null&&row.getCell(7)!=null)
		{
			
	
			if(machineHeaderList!=null ){
				machine.setHeaderList(machineHeaderList);
			}
			machine.setId(trim(row,0));
			machine.setNodeName(trim(row,1));
			machine.setCustomerId(trim(row,2));
			machine.setCustomerName(trim(row,2));
			machine.setMachinClass(trim(row,3));
			
			String listOfDependencies = trim(row,4); 
			List<String> dependencies = new ArrayList<String>(){};
			if(listOfDependencies!=null){
				String[] arrachDependencies = listOfDependencies.split("\r?\n"); 
				for(int i = 0; i < arrachDependencies.length; i ++){
					String dependentNode = arrachDependencies[i]; 
					if(!dependentNode.equals("null")){
						dependencies.add(dependentNode);
					
					} 
				}
			}	
			machine.setDependencies(dependencies);
			if(row.getCell(5)!=null&&trim(row,5).isEmpty())
				machine.setAutomationState(trim(row,5));
			
			machine.setOSName(trim(row,6));
			machine.setOSMajorVersion(trim(row,7));
			
			if(row.getCell(8)!=null)
				machine.setOSMinorVersion(trim(row,8));
			
		
			readOptCell(row, machine, machineHeaderList);
		}	
		return machine; 
	}



	private void readOptCell(XSSFRow row, Node node, List<Cell> headerList) {
		for(Cell headercell : headerList) {
			String currenFreeValue = trim(row,headercell.getCellNumber()); 
			String keyValue = null;
			if(headercell.getKeyRowNumber()>0){
				keyValue = trim(row, headercell.getKeyRowNumber()); 
			}
			if(currenFreeValue!= null && currenFreeValue!= ""  ){
				if(keyValue!= null && keyValue!=""){
					node.getValueList().add(new Cell(headercell.getCellNumber(),currenFreeValue, headercell.getKeyRowNumber(), keyValue ));
					}
				else{
					node.getValueList().add(new Cell(headercell.getCellNumber(),currenFreeValue )); }
				}	
			}
			
	}
	

	
	private Node readSoftwareFromRow( XSSFRow row){
		
			Software software = new Software(); 
			if(row!=null&&row.getCell(0)!=null&&row.getCell(1)!=null&&row.getCell(2)!=null&&row.getCell(3)!=null&&row.getCell(4)!=null)
			{
				software.setId(trim(row,0));
				software.setNodeName(trim(row,1));
				String cid = trim(row,2); 
				software.setCustomerId(cid);
				software.setCustomerName(cid);
				software.setSoftwareClass(trim(row,3));
				software.setSoftwareSubClass(trim(row,4));
				String listOfDependencies = trim(row,5); 
				List<String> dependencies = new ArrayList<String>(){};
				if(listOfDependencies!=null){
					String[] arrachDependencies = listOfDependencies.split("\r?\n"); 
					for(int i = 0; i < arrachDependencies.length; i ++){
						if(!arrachDependencies[i].equals("null"))
							dependencies.add(arrachDependencies[i]); 
					}
				}	
				software.setDependencies(dependencies);
				if(row.getCell(6)!=null&&trim(row,6).isEmpty())
					software.setAutomationState(trim(row,6));
				if(softwareHeaderList!=null ){
					software.setHeaderList(softwareHeaderList);
				}
				
				readOptCell(row, software, softwareHeaderList);
				
			}
		
			return software; 
			
		
	}
	

	private void readHeaderFromRow(String type, XSSFRow firstRow) {
		switch(nodeTypes.valueOf(type)){
		case Machine: 
			readMachineHeaderFromRow(firstRow); 
			break; 
		case Software:
			readSoftwareHeaderFromRow(firstRow);
			break; 
		case Resource: 
			readResourceHeadFromRow(firstRow);
			break; 
		case Application: 
			readApplicationHeadFromRow(firstRow);
			break; 
		}
	
	}

	private boolean isKey(String header){
		if (header.endsWith("_key"))
			return true; 
		return false; 
	}
	
	private void readAndMatchHeader(XSSFRow firstRow, int startCell, List<Cell> headerList, List<Cell> keyList){
		readHeader(firstRow, startCell, headerList, keyList);
		matchHeder(headerList, keyList); 
		
	}



	private void matchHeder(List<Cell> headerList, List<Cell> keyList) {
		if(keyList!=null){
			for(int i = 0; i <keyList.size(); i ++ ){
				Cell keyCell = keyList.get(i); 
				for(int j = 0; j< headerList.size(); j ++ ){
					Cell heaerCell = headerList.get(j); 
					if((heaerCell.getContent()+"_key").equals(keyCell.getContent())){
						heaerCell.setKeyRowNumber(keyCell.getCellNumber());
					}
				}
			}
		}
		
	}



	private void readHeader(XSSFRow firstRow, int startCell,
			List<Cell> headerList, List<Cell> keyList) {
		for(int i = startCell; i < firstRow.getLastCellNum();  i++) {
			if(firstRow.getCell(i)!=null){
				String header = trim(firstRow,i ); 
				if(header!= null && !header.isEmpty()){
					if(isKey(header)){
						keyList.add(new Cell(i, header)); 
					}
					else{
						headerList.add(new Cell(i,header)); 
						}
					}
				}
			}
	}
	
	private void readResourceHeadFromRow(XSSFRow firstRow) {
		readAndMatchHeader(firstRow, 6, resourceHeaderList, resourceHeaderKeyList) ; 
	}	
	
	
	private void readMachineHeaderFromRow(XSSFRow firstRow) {
		readAndMatchHeader(firstRow, 9, machineHeaderList, machineHeaderKeyList) ; 
		
	}
			
	private void readSoftwareHeaderFromRow(XSSFRow firstRow) {
		readAndMatchHeader(firstRow, 7, softwareHeaderList, softwareHeaderKeyList) ; 
		
	}	
			
	private void readApplicationHeadFromRow(XSSFRow firstRow) {
		readAndMatchHeader(firstRow, 7, applicationHeaderList, applicationHeaderKeyList) ; 
	}		
	

	
	
	private static String trim(final XSSFRow row,final int num)
	{
		String out="";
		if(row !=null && row.getCell(num)!=null)
		{
			if(row.getCell(num).getCellType()==0){
				out=String.valueOf( Integer.valueOf((int) row.getCell(num).getNumericCellValue()) ); 
			}else{
			out=row.getCell(num).getStringCellValue().trim();
			}
		}
		return out;
	}
	
	
	private Node readApplicationFromRow( XSSFRow row){
	
			Application application = new Application(); 
			if(row!=null&&row.getCell(0)!=null&&row.getCell(1)!=null&&row.getCell(2)!=null&&row.getCell(3)!=null&&row.getCell(4)!=null)
			{
				if(applicationHeaderList!=null){
					application.setHeaderList(applicationHeaderList);
				}
				application.setId(trim(row,0));
				application.setNodeName(trim(row,1));
				application.setCustomerId(trim(row,2));
				application.setCustomerName(trim(row,2));
				application.setApplicationClass(trim(row,3));
				application.setAaplicationSubClass(trim(row,4));
				String listOfDependencies = trim(row,5); 
				List<String> dependencies = new ArrayList<String>(){};
				if(listOfDependencies!=null){
					String[] arrachDependencies = listOfDependencies.split("\r?\n"); 
					for(int i = 0; i < arrachDependencies.length; i ++){
						if(!arrachDependencies[i].equals("null"))
							dependencies.add(arrachDependencies[i]); 
					}
				}	
				application.setDependencies(dependencies);
				if(row.getCell(6)!=null&&trim(row,6).isEmpty())
					application.setAutomationState(trim(row,6));

				readOptCell(row, application, applicationHeaderList);
				
			
			}
			return application; 
			
		
	}
	

	
	private Node readResourceFromRow( XSSFRow row){
		Resource resource = new Resource(); 
			if(row!=null&&row.getCell(0)!=null&&row.getCell(1)!=null&&row.getCell(2)!=null&&row.getCell(3)!=null)
			{
				if(resourceHeaderList!=null ){
					resource.setHeaderList(resourceHeaderList);
				}
				resource.setId(trim(row,0));
				resource.setNodeName(trim(row,1));
				resource.setCustomerId(trim(row,2));
				resource.setCustomerName(trim(row,2));
				resource.setResourceClass(trim(row,3));
				String listOfDependencies = trim(row,4); 
				List<String> dependencies = new ArrayList<String>(){};
				if(listOfDependencies!=null){
					String[] arrachDependencies = listOfDependencies.split("\r?\n"); 
					for(int i = 0; i < arrachDependencies.length; i ++){
						if(!arrachDependencies[i].equals("null"))
							dependencies.add(arrachDependencies[i]); 
					}
				}	
				resource.setDependencies(dependencies);
				if(row.getCell(5)!=null&&trim(row,5).isEmpty())
					resource.setAutomationState(trim(row,5));
				
				readOptCell(row, resource, resourceHeaderList);
				
			}
			return resource; 
			
		
	}
}
