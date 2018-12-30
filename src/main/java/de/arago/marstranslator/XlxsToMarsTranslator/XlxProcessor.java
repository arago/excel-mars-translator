package de.arago.marstranslator.XlxsToMarsTranslator;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import de.arago.connectit.codegenerator.model.ProcessObject;
//import de.arago.connectit.codegenerator.model.SDFObject;

public class XlxProcessor {

	private String pathToFolder; 
	private String pathToXMLS; 
	private XSSFWorkbook woorkbook;
	private int version; 
	private outputType outputVersion = outputType.XML;  
	
	private List<Node> machineList; 
	private List<Node> softwareList; 
	private List<Node> applicationList; 
	private List<Node> resourceList; 
	private int MARSVersion = 2015; 
	
	private NodeReader nodeReader = new NodeReader(); 
	private DependencieManager depManager ; // = new DependencieManager(machineList, softwareList, applicationList, resourceList) ; 
	//private XSDValidator validator ; 
	
	private enum outputType {XML, JSON}; 
	
	public enum nodeTypes  {Machine, Software, Application, Resource, Dependencies}; 

	
	private static final Logger log = LoggerFactory.getLogger(XlxProcessor.class); 
	
	/*private void setMARSValidator(){
		if(version == 52){
			MARSVersion = 2013;
		}else{
			MARSVersion = 2015; 
		}
		this.validator = new MARSSchemaValidator(MARSVersion); 
	}*/
	
	/*private void setMARSValidator(){
		try
		{
		switch(version){
		case 52: MARSVersion = 2013;
		case 53: MARSVersion = 2015; 
		case 60: MARSVersion = 2015;
		default: MARSVersion = 2015;
			}
		}
		catch (Exception e)
		{
			log.debug("Version is not provided:"+e);
		}
		this.validator = new MARSSchemaValidator(MARSVersion); 
	}*/
	
	public XlxProcessor(String pathToFolder, String pathToXMLS){
		this.pathToFolder = pathToFolder; 
		this.pathToXMLS = pathToXMLS; 
		woorkbook = this.getWorkBookFromFile(); 
		this.version = 53; 
		//setMARSValidator(); 
	}
	
	public XlxProcessor(String pathToFolder, String pathToXMLS, String version){
		this.pathToFolder = pathToFolder; 
		this.pathToXMLS = pathToXMLS; 
		woorkbook = this.getWorkBookFromFile(); 
		try{
			this.version = Integer.parseInt(version); 
		}
		catch (NumberFormatException e){
			log.debug(e.getLocalizedMessage());
			log.debug("Version is set to 5.3");
			this.version =53 ; 
		}
		//setMARSValidator(); 
	}
	
	private XSSFWorkbook getWorkBookFromFile() {

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(pathToXMLS);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		log.info("File Open");
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(fileInputStream);
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
		log.info("Workbook open with #"+workbook.getNumberOfSheets()+" sheets ");
		for(int i=0;i<workbook.getNumberOfSheets();i++)
		{
			log.info("sheet name "+workbook.getSheetName(i));
		}

		return workbook;
	}
	
	public XlxProcessor(String pathToFolder, String pathToXMLS, String version, String outputVersion){
		this.pathToFolder = pathToFolder; 
		this.pathToXMLS = pathToXMLS; 
		woorkbook = this.getWorkBookFromFile(); 
		try{
			this.version = Integer.parseInt(version); 
		}
		catch (NumberFormatException e){
			log.debug(e.getLocalizedMessage());
			log.debug("Version is set to 6.0");
			this.version =60 ; 
		}
		if (outputType.valueOf(outputVersion)!=null) {
			log.debug("Outputtype is set to "+ outputVersion);
			this.outputVersion = outputType.valueOf(outputVersion); 
		}
		//setMARSValidator(); 
	}
	public void readAndWrite(){
	readFromExcle(); 
	checkDependencies(); 
	transformToXML(); 
	//validateNodes(); 
	saveToFile();
	
	showStats();
	}
	
	
	/*private void validateNodes(){
		validator.validateNodeList(machineList); 
		validator.validateNodeList(softwareList);
		validator.validateNodeList(resourceList);
		validator.validateNodeList(applicationList);	
	}*/

	private void showStats() {
		log.info("The following number of Nodes where created"); 
		log.info("Machine: " + machineList.size()); 
		log.info("Software: " + softwareList.size());
		log.info("Resource: "  + resourceList.size());
		log.info("Application: " + applicationList.size());
	}

	private void saveToFile() {
		switch(outputVersion){
		case XML:
			saveListAsXML(machineList, "Machine"); 
			saveListAsXML(softwareList, "Software"); 
			saveListAsXML(resourceList, "Resource"); 
			saveListAsXML(applicationList, "Application"); 
			break; 
		case JSON: 
			saveListAsJSON(machineList, "Machine"); 
			saveListAsJSON(softwareList, "Software"); 
			saveListAsJSON(resourceList, "Resource"); 
			saveListAsJSON(applicationList, "Application"); 
			break; 
		default: 
			log.debug("no output format defined");
			break; 
		}
	}

	private void transformToXML() {
		transformToXML(machineList, "Machine"); 
		transformToXML(softwareList, "Software"); 
		transformToXML(resourceList, "Resource"); 
		transformToXML(applicationList, "Application");
	}

	private void readFromExcle() {
		machineList = nodeReader.readListOfNodes(woorkbook, "Machine"); 
		softwareList = nodeReader.readListOfNodes(woorkbook, "Software"); 
		applicationList = nodeReader.readListOfNodes(woorkbook, "Application"); 
		resourceList = nodeReader.readListOfNodes(woorkbook, "Resource");
	}

	private void checkDependencies() {
		depManager = new DependencieManager(machineList, softwareList, applicationList, resourceList) ; 
		depManager.checkDependencies();
	}
	
	
	
	private void transformToXML(List<Node> nodeList, String type){
		for(Node node : nodeList){
			node.setXMLRepresentation(version);
		}
		
	}
	
	
	
	private void saveListAsXML(List<Node> nodeList, String type){
		int i = 1; 
		for(Node node : nodeList){
			if(node.isValid)
				saveToFile(node.getXmlRepresentation(), type+i, "xml");
			else{
				saveToFile(node.getXmlRepresentation()+"\n\n<!--"+ node.getValidationError() +"-->", "_"+type+i, "xml");
			}
			log.debug("Store "+ type+i +".xml ("   + node.getNodeName()+ ")");
			i ++; 
		}
		
	}
	

	private void saveListAsJSON(List<Node> nodeList, String type){
		int i = 1; 
		for(Node node : nodeList){
			if(node.isValid)
				saveToFile(node.toCURLJSON().toString(), type+i, "json");
			else{
				saveToFile(node.getValidationError() +"\n\n"+node.toCURLJSON().toString(), "_"+type+i, "json");
			}
			log.debug("Store "+ type+i +".json ("   + node.getNodeName()+ ")");
			i ++; 
		}
		
	}
	
	
	
	private void saveToFile(String xml, String name, String type){
		
		File file = new File(pathToFolder+"/"+name+"."+type); 
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			FileOutputStream fs = new FileOutputStream(file);
			OutputStreamWriter ow = new OutputStreamWriter(fs);
			BufferedWriter writer = new BufferedWriter(ow);
			if(xml!=null){
				writer.write(xml);
				writer.flush();
			} else {
				log.debug(name + " is empty");
				}
			
			writer.close();
			fileOutputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	
}
