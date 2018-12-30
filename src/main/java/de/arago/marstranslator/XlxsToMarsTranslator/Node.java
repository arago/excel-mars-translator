package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Node {
	protected String id = null ; 
	protected String nodeName= null ; 
	protected String customerId= null ; 
	protected String customerName= null ; 
	protected List<String> dependencies= null ; 
	protected String automationState= null ;
	protected String xmlRepresentation = null; 
	protected String nodeType = null; 
	protected String sourceCiId = null; 
	protected List<Cell> headerList; 
	protected List<Cell> valueList; 
	protected boolean isValid = true; 
	protected String validatonError = null; 
	
	
	protected boolean isValidEntry(String value) {
		return value!=null && !value.isEmpty();
	}
	
	protected Logger log = LoggerFactory.getLogger(Node.class); 
	
	public String getValidationError(){
		return validatonError; 
	}
	
	public void setValidationError(String validationError){
		this.validatonError = validationError; 
	}
	
	public Node(String nodeType){
		this.nodeType = nodeType; 
		headerList = new ArrayList<Cell>(); 
		valueList = new ArrayList<Cell>(); 
	}
	
	public void setSourceCiId(String sourceCiID) {
		this.sourceCiId = sourceCiID; 
	}
	public String getSourceCiId(){
		return sourceCiId; 
	}
	
	public String getNodeType(){
		return nodeType; 
	}
	
	public void setNodeType(String nodeType){
		this.nodeType= nodeType; 
	}
	
	
	
	public void setXMLRepresentation(int version){
		this.xmlRepresentation = this.toXml(version); 
	}
	
	public String getXmlRepresentation(){
		return xmlRepresentation; 
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId.toLowerCase();
	}
	public List<String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	public String getAutomationState() {
		return automationState;
	}
	public void setAutomationState(String automationState) {
		this.automationState = automationState;
	} 
	
	
	public abstract String toXml53(); 
	
	public abstract String toXml52(); 
	
	protected void checkCellForSpecialHeaders(String header, String value){
		if(header.equals("CustomerName")){
			this.setCustomerName(value);
		}
		if(header.equals("SourceCiId")){
			this.setSourceCiId(value);
		}
	}
	
	
	protected String setOptValues( List<Cell> headerList, List<Cell> valueList) {
		String xmlContent =""; 
		for(Cell header : headerList){
			for(Cell value : valueList){
				if(value.getCellNumber()== header.getCellNumber()&& value.getContent()!=null && ! value.getContent().isEmpty()){
					checkCellForSpecialHeaders(header.getContent(), value.getContent());
					
					
					String[] valueArray = value.getContent().split("\r?\n");
					if(valueArray!=null)
						xmlContent+= "<"+header.getContent()+"> "; 
					
					String[] keyArray = null; 
					if(value.getKeyContent()!=null)
						keyArray = value.getKeyContent().split("\r?\n"); 
					
					for(int i = 0; i <valueArray.length; i ++ ){
						String val = valueArray[i]; 
						// if length ==1 get value j.put(header.getContent(), value)
						// if length > 1 j.put(header.getContent(), valueArray)
						
						if( keyArray== null){
							xmlContent+="<Content Value=\""+val+"\"/> "; 
						}
						else{
							String keyVal = keyArray[i]; 
							xmlContent+= " <Content Value=\""+val+"\"  Key=\""+keyVal+"\"/> "; 
						}
						
						
					}
					

					if(valueArray!=null)
						xmlContent+= "</"+ header.getContent()+">\n"; 
					
					
				}
				
			}
		}
		return xmlContent;
	}
	
	public void addOptValues(JSONObject json, List<Cell> headerList, List<Cell> valueList) {
		for(Cell header : headerList){
			for(Cell value : valueList){
				if(value.getCellNumber()== header.getCellNumber()&& value.getContent()!=null && ! value.getContent().isEmpty()){
					checkCellForSpecialHeaders(header.getContent(), value.getContent());
					
					String[] valueArray = value.getContent().split("\r?\n");

					
//					String[] keyArray = null; 
//					if(value.getKeyContent()!=null)
//						keyArray = value.getKeyContent().split("\r?\n"); 
					if(valueArray.length == 1){
							try {
								json.put('/'+header.getContent(),  valueArray[0]);
							} catch (JSONException e) {
								e.printStackTrace();
							}
					}
					else{
						try {
							json.put('/'+header.getContent(), valueArray);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				}
			}
			
		
}
		
	
	
public JSONObject toCURLJSON(){
		JSONObject j = new JSONObject(); 
/*		try {
			//j.put("ogit/Automation/marsNodeFormalRepresentation", this.getXmlRepresentation());
			j.put("ogit/_owner", this.getCustomerId());
			j.put("ogit/id", this.getSourceCiId()); 
			j.put("ogit/name", this.getNodeName()); 
		//	j.put("ogit/_id", this.getId()); 
			j.put("ogit/Automation/marsNodeType", this.getNodeType()); 

		} catch (JSONException e) {
			e.printStackTrace();
		} */
		return j; 
	}
	
	public String toXml(int version){
		
		switch(version){
		case 53: 
			xmlRepresentation = toXml53(); 
			return xmlRepresentation;  
		case 52: 
			xmlRepresentation = toXml52(); 
			return xmlRepresentation; 
		default: 
			return null; 
		
		}
	}


	public List<Cell> getHeaderList() {
		return headerList;
	}
	


	public void setHeaderList(List<Cell> headerList) {
		this.headerList = new ArrayList<Cell>(); 
		for(Cell headercell : headerList) {
			this.headerList.add(headercell); 
		}

	}



	public List<Cell> getValueList() {
		return valueList;
	}



	public void setValueList(List<Cell> valueList) {
		this.valueList = valueList;
	} 
}
