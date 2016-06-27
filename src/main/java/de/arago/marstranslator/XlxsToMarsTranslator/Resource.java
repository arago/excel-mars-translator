package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Resource extends Node {
	private String resourceClass; 
	
	public Resource(){super("Resource"); }
//	public Resource(Resource node){
//		super("Resource"); 
//		this.setHeaderList(node.getHeaderList());
//		this.setValueList(node.getValueList());
//		this.setAutomationState(node.getAutomationState());
//		this.setCustomerId(node.getCustomerId());
//		this.setCustomerName(node.getCustomerName());
//		this.setId(node.getId());
//		this.setResourceClass(node.getResourceClass());
//		this.setNodeName(node.getNodeName());
//		this.setDependencies(node.getDependencies()); 
//	}; 
	
	public String getResourceClass() {
		return resourceClass;
	}



	public void setResourceClass(String resourceClass) {
		this.resourceClass = resourceClass;
	}

	public String toXml52() {
		
		String xml = ""; 
		if(resourceClass!=null){
			xml+= "<"+resourceClass ;
			xml+= " xmlns=\"http://mars-o-matic.com\"\n";
		}
		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		xml+= "NodeType=\"Resource\""; 
		if(resourceClass!=null){
			xml+= " ResourceClass=\""+resourceClass+"\""; 
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
		
		
		if(resourceClass!=null){
			xml+= "</"+resourceClass+">" ;
		}
		
		
	
		
		return xml ; 
	}

	@Override
	public String toXml53() {
		String xml = "<?xml version=\"1.0\" ?>\n"; 
		
		//parameters
		
		if(resourceClass!=null){
			xml+= "<"+resourceClass ;
		}
		if(resourceClass!=null){
			xml+= " ResourceClass=\""+resourceClass+"\"\n"; 
		}
		
		
		xml+= "xmlns=\"https://graphit.co/schemas/v2/MARSSchema\"\n";

		if(id!=null){
			xml+= "ID=\""+id+"\"\n"; 
		}
		if(nodeName!=null){
			xml+= "NodeName=\""+nodeName+"\"\n"; 
		}
		xml+= "NodeType=\"Resource\"\n"; 
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
				xml+="<Node ID=\""+dependencie+"\"/>\n"; 
			}
			
			xml+="</Dependencies>\n";
		}
		if(resourceClass!=null){
		xml+="</"+resourceClass+">"; 
		}
		return xml; 
	}
	
	
}
