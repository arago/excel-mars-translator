package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Machine extends Node {
	
	public Machine() {
		super("Machine");
	}


	private String machinClass= null ; 
	private String OSName= null ; 
	private String OSMajorVersion= null ; 
	private String OSMinorVersion= null ;
	private String hostname= null ; 
	private String ip= null ; 
	



	public String getMachinClass() {
		return machinClass;
	}


	public void setMachinClass(String machinClass) {
		this.machinClass = machinClass;
	}


	public String getOSName() {
		return OSName;
	}


	public void setOSName(String oSName) {
		OSName = oSName;
	}


	public String getOSMajorVersion() {
		return OSMajorVersion;
	}


	public void setOSMajorVersion(String oSMajorVersion) {
		OSMajorVersion = oSMajorVersion;
	}


	public String getOSMinorVersion() {
		return OSMinorVersion;
	}


	public void setOSMinorVersion(String oSMinorVersion) {
		OSMinorVersion = oSMinorVersion;
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	public String toXml52() {
		String xml = ""; 
		if(machinClass!=null){
			xml+= "<"+machinClass ;
			xml+= " xmlns=\"http://mars-o-matic.com\"\n";
		}
		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		xml+= "HasAgentType_WatchMe=\"False\" MachineArchitecture=\"x86_64\"\n"; 
		xml+= "NodeType=\"Machine\""; 
		if(machinClass!=null){
			
			xml+= " MachineClass=\""+machinClass+"\""; 
		}
		if(nodeName!=null){
			xml+= " NodeName=\""+nodeName+"\">\n"; 
		}
		if(dependencies!=null&& !dependencies.isEmpty()){
			xml+="<Dependencies>\n";
			for(String dependencie : dependencies){

				xml+="<Node ID=\""+dependencie+"\"/>\n";  
			}
			
			xml+="</Dependencies>\n";
		}
		if(customerId!=null && customerName!=null){
			xml+= "<CustomerInformation ID=\""+customerId.toLowerCase()+"\" Name=\""+this.getCustomerName()+"\"/>\n"; 
		}
		if(headerList!= null && !headerList.isEmpty()){
			xml+="<Extensions>\n"; 
			for(Cell header : headerList){
				for(Cell value : valueList){
					if(value.getCellNumber()== header.getCellNumber()){
						String[] headerArray = header.getContent().split("\r?\n"); 
						String outerHeader = ""; 
						String innerHeader = ""; 
						if(headerArray.length==2){
							outerHeader =  headerArray[0]; 
							innerHeader = headerArray[1];
							xml+="<"+outerHeader+" "+innerHeader+"=\""+value.getContent()+"\"/>\n"; 
						}
					}
				}
			}
			xml+="</Extensions>\n";	
		}
		
		if(machinClass!=null){
			xml+= "</"+machinClass ;
			xml+= ">";
		}
		return xml; 
	}


	public String toXml53() {
		String xml = "<?xml version=\"1.0\" ?>\n"; 
		
		
		if(machinClass!=null){
			xml+= "<"+machinClass ;
			xml+= " MachineClass=\""+machinClass+"\"\n"; 
		}
		
		xml+= "xmlns=\"https://graphit.co/schemas/v2/MARSSchema\"\n";

		if(id!=null){
			xml+= "ID=\""+id+"\"\n";  
		}
		if(nodeName!=null){
			xml+= "NodeName=\""+nodeName+"\"\n"; 
		}
		xml+= "NodeType=\"Machine\"\n"; 
		if(customerId!=null){
			xml+= "CustomerID=\""+customerId.toLowerCase()+"\"\n"; 
		}
		if(customerName!=null){
			xml+= "CustomerName=\""+customerName+"\"\n"; 
		}
		if(automationState!=null && ! automationState.isEmpty()){
			xml+= "AutomationState=\""+automationState+"\"\n"; 
		}
		if(OSName!=null){
			xml+= "OSName=\""+OSName+"\"\n"; 
		}
		if(OSMajorVersion!=null){
			xml+= "OSMajorVersion=\""+OSMajorVersion+"\"\n"; 
		}
		if(OSMinorVersion!=null && ! OSMinorVersion.isEmpty()){
			xml+= "OSMinorVersion=\""+OSMinorVersion+"\""; 
		}
		xml+=">\n";
		
		for(Cell header : headerList){
			for(Cell value : valueList){
				if(value.getCellNumber()== header.getCellNumber()&& value.getContent()!=null && ! value.getContent().isEmpty()){
					xml+="<"+header.getContent()+"> <Content Value=\""+value.getContent()+"\"/> </"+ header.getContent()+">\n"; 
					checkCellForSpecialHeaders(header.getContent(), value.getContent());
				}
			}
		}
		
		
		if(dependencies!=null&& !dependencies.isEmpty()){
			xml+="<Dependencies>\n";
			for(String dependencie : dependencies){
				xml+="<Node ID=\""+dependencie+"\"/>\n"; 
			}
			
			xml+="</Dependencies>\n";
		}
		if(machinClass!=null){
		xml+="</"+machinClass+">"; 
		}
		

		return xml; 
	}

}
