package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.arago.marstranslator.XlxsToMarsTranslator.XlxProcessor.nodeTypes;

public class NodeWriter {
	private ExcelWriter writer; 
	private Map<Integer,String> machineHeader =new HashMap<Integer, String>();
	private Map<Integer,String> softwareHeader =new HashMap<Integer, String>();
	private Map<Integer,String> applicationHeader =new HashMap<Integer, String>();
	private Map<Integer,String> resourceHeader =new HashMap<Integer, String>();
	private int j =0;
	private int jMachine=0; 
	private int jSoftware=0; 
	private int jResource=0; 
	private int jApplication=0; 
	
	
	
	private void setRow(int j ){
		this.j = j; 
	}
	
	public NodeWriter(String path){
		writer = new ExcelWriter(path); 
		initHeaders(); 
	}


	private void initHeaders() {
		initMachineHeader(); 
		
		initSoftwareHeader(); 
		
		initResourceHeader(); 
		
		
		initApplicationHeader();
	}


	private void initApplicationHeader() {
		applicationHeader.put(0, NodeAttributes.node_ID ); 
		applicationHeader.put(1, NodeAttributes.nodeName ); 
		applicationHeader.put(2, NodeAttributes.customerId ); 
		applicationHeader.put(3, NodeAttributes.applicationClass ); 
		applicationHeader.put(4, NodeAttributes.applicationSubClass ); 
		applicationHeader.put(5, NodeAttributes.dependencies ); 
		applicationHeader.put(6, NodeAttributes.automationState);
	}


	private void initResourceHeader() {
		resourceHeader.put(0, NodeAttributes.node_ID); 
		resourceHeader.put(1, NodeAttributes.nodeName); 
		resourceHeader.put(2, NodeAttributes.customerId); 
		resourceHeader.put(3, NodeAttributes.resourceClass); 
		resourceHeader.put(4, NodeAttributes.dependencies); 
		resourceHeader.put(5, NodeAttributes.automationState);
	}


	private void initSoftwareHeader() {
		softwareHeader.put(0, NodeAttributes.node_ID ); 
		softwareHeader.put(1, NodeAttributes.nodeName ); 
		softwareHeader.put(2, NodeAttributes.customerId ); 
		softwareHeader.put(3, NodeAttributes.softwareClass ); 
		softwareHeader.put(4, NodeAttributes.softwareSubClass ); 
		softwareHeader.put(5, NodeAttributes.dependencies ); 
		softwareHeader.put(6, NodeAttributes.automationState);
	}


	private void initMachineHeader() {
		machineHeader.put(0,NodeAttributes.node_ID  ); 
		machineHeader.put(1,NodeAttributes.nodeName ); 
		machineHeader.put(2,NodeAttributes.customerId ); 
		machineHeader.put(3,NodeAttributes.machineClass ); 
		machineHeader.put(4,NodeAttributes.dependencies );
		machineHeader.put(5,NodeAttributes.automationState ); 
		machineHeader.put(6,NodeAttributes.oSName ); 
		machineHeader.put(7,NodeAttributes.oSMajorVersion); 
		machineHeader.put(8,NodeAttributes.oSMinorVersion); 
		machineHeader.put(9,NodeAttributes.hostname ); 
		machineHeader.put(10, NodeAttributes.ip); 
		machineHeader.put(11,NodeAttributes.sourceCiID);
	}
	
	
	public void saveFile(){
		writer.saveWritingsToFile(); 
	}
	
	
	
	public void writeNodes(List<Node> nodeList){
		for(Node node : nodeList)
			writeNode(node);
	}
	
	
	public void writeNode(Node node ){
		Map<Integer, String> cellValues = new HashMap<Integer, String>();
		setRow(0);
  
		Map<Integer, String> headerValues ; 
		
		String depString = ""; 
		
		for(int kk = 0; kk < node.getDependencies().size(); kk ++){
			String dep = node.getDependencies().get(kk); 
			depString += dep; 
			if((kk+1)<node.getDependencies().size() )		
				depString+="\n"; 
			
		}

		setStandardValues(node, cellValues); 
		
		if(node.getNodeType().equals(nodeTypes.Machine.toString())){
			Machine mnode = (Machine) node;  
			headerValues = machineHeader; 
			setDefMachineValues(node, cellValues, depString, mnode ); 
			if(j < jMachine){
				j = jMachine; 
			}
		}else if(node.getNodeType().equals(nodeTypes.Software.toString())){
			Software snode = (Software) node; 
			headerValues = softwareHeader; 
			setDefSoftwareValues(node, cellValues, depString, snode); 
			if(j < jSoftware){
				j = jSoftware; 
			}
		}else if(node.getNodeType().equals(nodeTypes.Resource.toString())){
			Resource rnode = (Resource) node; 
			headerValues = resourceHeader; 
			setDefResourceValues(node, cellValues, depString, rnode); 
			if(j < jResource){
				j = jResource; 
			}
		}else if(node.getNodeType().equals(nodeTypes.Application.toString())){
			Application anode = (Application) node; 
			headerValues = applicationHeader; 
			setDefApplicationValues(node, cellValues, depString, anode); 
			if(j < jApplication){
				j = jApplication; 
			}
		}
		else{
			headerValues = null; 
		}
		
		
		
		List<Cell> headerCellList = node.getHeaderList(); 
		for(Cell headerCell : headerCellList){
			String optHeader = headerCell.getContent(); 
			if(headerValues.containsValue(optHeader)){
				for(int l = 0; l < headerValues.size(); l ++ ){
					if(headerValues.get(l).equals(optHeader)){
						for(Cell c : node.getValueList()){
							if(c.getCellNumber() == headerCell.getCellNumber()){
								cellValues.put(l, c.getContent()); 
							}
						}
					}
				}
			}else{
				headerValues.put(j, optHeader);
				for(Cell c : node.getValueList()){
					if(c.getCellNumber() == headerCell.getCellNumber()){
						cellValues.put(j, c.getContent()); 
						setRow(j+1);
					}
				}
			}
			
		}
		
		if(node.getNodeType().equals(nodeTypes.Machine.toString())){
			jMachine = j ; 
		}else if(node.getNodeType().equals(nodeTypes.Software.toString())){
			jSoftware = j; 
		}else if(node.getNodeType().equals(nodeTypes.Resource.toString())){
			jResource = j; 
		}else if(node.getNodeType().equals(nodeTypes.Application.toString())){
			jApplication = j; 
		}
		
		writer.writeToSheet(node.getNodeType(), cellValues, headerValues );
	}


	private void setStandardValues(Node node, Map<Integer, String> cellValues) {
		cellValues.put(0, node.getId() ); 
		cellValues.put(1, node.getNodeName() ); 
		cellValues.put(2, node.getCustomerId() );
	}


	private void setDefApplicationValues(Node node,
			Map<Integer, String> cellValues, String depString, Application anode) {
		
		cellValues.put(3, anode.getApplicationClass()); 
		cellValues.put(4, anode.getAaplicationSubClass()); 
		cellValues.put(5, depString); 
		cellValues.put(6, node.getAutomationState() ); 
		setRow(7);
	}


	private void setDefResourceValues(Node node,
			Map<Integer, String> cellValues, String depString, Resource rnode) {
		cellValues.put(3, rnode.getResourceClass() ); 
		cellValues.put(4, depString); 
		cellValues.put(5, node.getAutomationState() );
		setRow(6);
	}


	private void setDefSoftwareValues(Node node,
			Map<Integer, String> cellValues, String depString, Software snode) {
		cellValues.put(3, snode.getSoftwareClass() ); 
		cellValues.put(4, snode.getSoftwareSubClass() ); 
		cellValues.put(5, depString); 
		cellValues.put(6, node.getAutomationState() ); 
		setRow(7);
	}


	private void setDefMachineValues(Node node,
			Map<Integer, String> cellValues, String depString, Machine mnode) {
		cellValues.put(3, mnode.getMachinClass() ); 
		cellValues.put(4, depString); 
		cellValues.put(5, node.getAutomationState() );
		cellValues.put(6, mnode.getOSName() );
		cellValues.put(7, mnode.getOSMajorVersion() );
		cellValues.put(8, mnode.getOSMinorVersion() );
		cellValues.put(9, mnode.getHostname() );
		cellValues.put(10, mnode.getIp() );
		cellValues.put(11, mnode.getSourceCiId() );
		setRow(12);
		j = 12;
	}

}
