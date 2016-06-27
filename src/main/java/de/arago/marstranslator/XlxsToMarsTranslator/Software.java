package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Software extends Node {
	
	public Software() {
		super("Software");
	}


	private String softwareClass ; 
	private String softwareSubClass; 
	
	public String toXml52() {
		
		String xml = ""; 
		if(softwareSubClass!=null){
			xml+= "<"+softwareSubClass ;
			xml+= " xmlns=\"http://mars-o-matic.com\"\n";
		}
		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		xml+= "NodeType=\"Software\""; 
		if(softwareClass!=null){
			xml+= " SoftwareClass=\""+softwareClass+"\""; 
		}
		if(softwareSubClass!=null){
			xml+= " SoftwareSubClass=\""+softwareSubClass+"\""; 
		}
		if(nodeName!=null){
			xml+= " NodeName=\""+nodeName+"\""; 
		}
		if(softwareSubClass!=null){
			xml+= ">\n"; 
		
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
		if(softwareSubClass!=null){
			xml+= "</"+softwareSubClass+">";
		}
		
		
		return xml ; 
	}

	@Override
	public String toXml53() {
		String xml = "<?xml version=\"1.0\" ?>\n"; 
		
		//parameters
		
		if(softwareSubClass!=null){
			xml+= "<"+softwareSubClass ;
		}
		if(softwareClass!=null){
			xml+= " SoftwareClass=\""+softwareClass+"\"\n"; 
		}
		if(softwareSubClass!=null){
			xml+= " SoftwareSubClass=\""+softwareSubClass+"\"\n"; 
		}

		
		xml+= "xmlns=\"https://graphit.co/schemas/v2/MARSSchema\"\n";

		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		if(nodeName!=null){
			xml+= "NodeName=\""+nodeName+"\"\n"; 
		}
		xml+= "NodeType=\"Software\"\n"; 
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
				
//				String[] de = dependencie.split("\\.");  // wrong code
//				xml+="<Node ID=\""+de[0]+"\"/>\n";  // wrong code 
				xml+="<Node ID=\""+dependencie+"\"/>\n";// correct code
			}
			
			xml+="</Dependencies>\n";
		}
		if(softwareSubClass!=null){
		xml+="</"+softwareSubClass+">"; 
		}
		return xml; 
	}


	public String getSoftwareClass() {
		return softwareClass;
	}


	public void setSoftwareClass(String softwareClass) {
		this.softwareClass = softwareClass;
	}


	public String getSoftwareSubClass() {
		return softwareSubClass;
	}


	public void setSoftwareSubClass(String softwareSubClass) {
		this.softwareSubClass = softwareSubClass;
	}

}
