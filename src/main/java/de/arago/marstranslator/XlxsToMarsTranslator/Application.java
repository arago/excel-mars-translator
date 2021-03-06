package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.List;

import org.json.JSONObject;

public  class Application extends Node{

	public Application() {
		super("Application");
	}	

	private String applicationClass; 
	private String aaplicationSubClass; 

	public String toXml52() {
		String xml = ""; 
		if(aaplicationSubClass!=null){
			xml+= "<"+aaplicationSubClass ;
			xml+= " xmlns=\"http://mars-o-matic.com\"\n";
		}
		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		xml+= "NodeType=\"Application\""; 
		if(applicationClass!=null){
			xml+= " ApplicationClass=\""+applicationClass+"\""; 
		}
		if(aaplicationSubClass!=null){
			xml+= " ApplicationSubClass=\""+aaplicationSubClass+"\""; 
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
					if(value.getCellNumber()== header.getCellNumber()&& value.getContent()!=null && ! value.getContent().isEmpty()){
						//TODO: fix here. 
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
		if(aaplicationSubClass!=null){
			xml+= "</"+aaplicationSubClass ;
			xml+= ">";
		}
		return xml ; 
	}
	
	public String toXml53() {
		String xml = "<?xml version=\"1.0\" ?>\n"; 
		
		//parameters
		
		if(aaplicationSubClass!=null){
			xml+= "<"+aaplicationSubClass ;
		}
		if(applicationClass!=null){
			xml+= " ApplicationClass=\""+applicationClass+"\"\n"; 
		}
		if(aaplicationSubClass!=null){
			xml+= " ApplicationSubClass=\""+aaplicationSubClass+"\"\n"; 
		}

		
		xml+= "xmlns=\"https://graphit.co/schemas/v2/MARSSchema\"\n";

		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		if(nodeName!=null){
			xml+= "NodeName=\""+nodeName+"\"\n"; 
		}
		xml+= "NodeType=\"Application\"\n"; 
		if(customerId!=null){
			xml+= "CustomerID=\""+customerId.toLowerCase()+"\"\n"; 
		}
		if(customerName!=null){
			xml+= "CustomerName=\""+customerName+"\"\n"; 
		}
		if(automationState!=null && ! automationState.isEmpty()){
			xml+= "AutomationState=\""+automationState+"\""; 
		}
		
		xml+=">\n";
		
		xml+=setOptValues(headerList, valueList);
		
		if(dependencies!=null&& !dependencies.isEmpty()){
			xml+="<Dependencies>\n";
			for(String dependencie : dependencies){
				xml+="<Node ID=\""+dependencie+"\"/>\n"; 
			}
			
			xml+="</Dependencies>\n";
		}
		if(aaplicationSubClass!=null){
		xml+="</"+aaplicationSubClass+">"; 
		}
		return xml; 
	}

	

	public String getApplicationClass() {
		return applicationClass;
	}

	public void setApplicationClass(String applicationClass) {
		this.applicationClass = applicationClass;
	}

	public String getAaplicationSubClass() {
		return aaplicationSubClass;
	}

	public void setAaplicationSubClass(String aaplicationSubClass) {
		this.aaplicationSubClass = aaplicationSubClass;
	}

}
