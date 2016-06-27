package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.List;

import de.arago.marstranslator.XlxsToMarsTranslator.XlxProcessor.nodeTypes;

public class DependencieManager {
	
	private List<Node> machineList; 
	private List<Node> softwareList; 
	private List<Node> applicationList; 
	private List<Node> resourceList; 
	
	DependencieManager(List<Node> machineList,List<Node> softwareList,  List<Node> applicationList, List<Node> resourceList){
		super(); 
		this.machineList = machineList; 
		this.softwareList = softwareList; 
		this.applicationList = applicationList; 
		this.resourceList = resourceList; 
	}
	
	public void checkDependencies(){
		checkDefaultNodesChildes(softwareList, "Software"); 
		checkDefaultNodesChildes(resourceList, "Resource"); 
		checkDefaultNodesChildes(applicationList, "Application"); 
		checkDefaultNodesParents(resourceList, "Resource"); 
		checkDefaultNodesParents(softwareList, "Software"); 
		checkDefaultNodesParents(machineList, "Machine");  
		
		//Check dependencies for all 
		checkDependencies(machineList, "Machine");
		checkDependencies(softwareList, "Software");
		checkDependencies(resourceList, "Resource");
		checkDependencies(applicationList, "Application");
		
		//remove empty dependencies (read - cleanup) 
		removeEmptyDependencies(machineList, "Machine"); 
		removeEmptyDependencies(softwareList, "Software"); 
		removeEmptyDependencies(resourceList, "Resource"); 
		removeEmptyDependencies(applicationList, "Application");
	}

	protected void checkDefaultNodesChildes(List<Node> nodeList, String type) {
		switch(nodeTypes.valueOf(type)){
		case Software: 
			checkDefaultSoftwareNodesChildes(nodeList); 
			break; 
		case Resource: 
			checkDefaultResourceNodesChildes(nodeList); 
			break; 
		case Application: 
			checkDefaultApplicationNodesChildes(nodeList); 
			break;
		default:
			break; 
		
		}
	}

	
	private void checkDefaultResourceNodesParents(List<Node> nodeList) {
		List<Node> newNodeList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty())
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+P")){
					int number = 1; 
					for(Node relatedNode : applicationList){
						Resource refrenceResource = (Resource ) node; 
						String newId = refrenceResource.getId()+number; 
						Resource newResource = new Resource(); 
						newResource.setHeaderList(refrenceResource.getHeaderList());
						newResource.setValueList(refrenceResource.getValueList());
						newResource.setAutomationState(refrenceResource.getAutomationState());
						newResource.setCustomerId(refrenceResource.getCustomerId());
						newResource.setCustomerName(refrenceResource.getCustomerName());
						newResource.setId(newId);
						newResource.setResourceClass(refrenceResource.getResourceClass());
						newResource.setNodeName(refrenceResource.getNodeName()+number);
						List<String> refDep = refrenceResource.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.remove(dependencie); 
						newDep.add(relatedNode.getId());
						newResource.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						newNodeList.add(newResource);
						number++; 
					}
				}
			}
		}
		resourceList.addAll(newNodeList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(resourceList);
		for(Node finalNode : finalNodeList){
			if(finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(dependencie != null && dependencie.startsWith("+P")){
					resourceList.remove(finalNode); 
				} 
				
			}
		}
		
	}

	private void checkDefaultSoftwareNodesParents(List<Node> nodeList) {
		List<Node> newNodeList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty())
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+P")){
					int number = 1; 
					for(Node relatedNode : resourceList){
						Software refrenceSoftware = (Software ) node; 
						String newId = refrenceSoftware.getId()+number; 
						Software newSoftware = new Software(); 
						newSoftware.setHeaderList(refrenceSoftware.getHeaderList());
						newSoftware.setValueList(refrenceSoftware.getValueList());
						newSoftware.setAutomationState(refrenceSoftware.getAutomationState());
						newSoftware.setCustomerId(refrenceSoftware.getCustomerId());
						newSoftware.setCustomerName(refrenceSoftware.getCustomerName());
						newSoftware.setId(newId);
						newSoftware.setSoftwareClass(refrenceSoftware.getSoftwareClass());
						newSoftware.setSoftwareSubClass(refrenceSoftware.getSoftwareSubClass());
						newSoftware.setNodeName(refrenceSoftware.getNodeName()+number);
						List<String> refDep = refrenceSoftware.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.remove(dependencie); 
						newDep.add(relatedNode.getId());
						newSoftware.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						newNodeList.add(newSoftware);
						number++; 
					}
					
					 
				}
			}
		}
		
		
		softwareList.addAll(newNodeList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(softwareList);
		for(Node finalNode : finalNodeList){
			if(finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(dependencie!= null && dependencie.startsWith("+P")){
					softwareList.remove(finalNode); 
				} 
				
			}
		}
		
	}

	
	private void checkDefaultResourceNodesChildes(List<Node> nodeList) {
		List<Node> newNodeList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty())
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+C")){
					int number = 1; 
					for(Node relatedNode : softwareList){
						Resource refrenceResource = (Resource ) node; 
						String newId = refrenceResource.getId()+number; 
						Resource newResource = new Resource();
						newResource.setHeaderList(refrenceResource.getHeaderList());
						newResource.setValueList(refrenceResource.getValueList());
						newResource.setAutomationState(refrenceResource.getAutomationState());
						newResource.setCustomerId(refrenceResource.getCustomerId());
						newResource.setCustomerName(refrenceResource.getCustomerName());
						newResource.setId(newId);
						newResource.setResourceClass(refrenceResource.getResourceClass());
						newResource.setNodeName(refrenceResource.getNodeName()+number);
						List<String> refDep = refrenceResource.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.remove(dependencie); 
						newDep.add(relatedNode.getId());
						newResource.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						newNodeList.add(newResource);
						number++; 
					}
				}
			}
		}
		resourceList.addAll(newNodeList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(resourceList);
		for(Node finalNode : finalNodeList){
			if(finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(dependencie!=null && dependencie.startsWith("+C")){
					resourceList.remove(finalNode); 
				} 
				
			}
		}
	}

	private void checkDefaultSoftwareNodesChildes(List<Node> nodeList) {
		List<Node> newNodeList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty())
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+C")){
					int number = 1; 
					for(Node relatedNode : machineList){
						Software refrenceSoftware = (Software ) node; 
						String newId = refrenceSoftware.getId()+number; 
						Software newSoftware = new Software(); 
						newSoftware.setHeaderList(refrenceSoftware.getHeaderList());
						newSoftware.setValueList(refrenceSoftware.getValueList());
						newSoftware.setAutomationState(refrenceSoftware.getAutomationState());
						newSoftware.setCustomerId(refrenceSoftware.getCustomerId());
						newSoftware.setCustomerName(refrenceSoftware.getCustomerName());
						newSoftware.setId(newId);
						newSoftware.setSoftwareClass(refrenceSoftware.getSoftwareClass());
						newSoftware.setSoftwareSubClass(refrenceSoftware.getSoftwareSubClass());
						newSoftware.setNodeName(refrenceSoftware.getNodeName()+number);
						List<String> refDep = refrenceSoftware.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.add(relatedNode.getId());
						newDep.remove(dependencie); 
						newSoftware.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						newNodeList.add(newSoftware);
						number++; 
					}
				}
			}
		}
		softwareList.addAll(newNodeList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(softwareList);
		for(Node finalNode : finalNodeList){
			if(finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(dependencie != null && dependencie.startsWith("+C")){
					softwareList.remove(finalNode); 
				} 
			}
		}
	}

	private void checkDefaultApplicationNodesChildes(List<Node> nodeList) {
		List<Node> newNodeList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty() )
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+")){
					int number = 1; 
					for(Node relatedNode : resourceList){
						Application referenceApplication = (Application) node; 
						String newId = referenceApplication.getId()+number; 
						Application newApplication = new Application(); 
						newApplication.setHeaderList(referenceApplication.getHeaderList());
						newApplication.setValueList(referenceApplication.getValueList());
						newApplication.setAutomationState(referenceApplication.getAutomationState());
						newApplication.setCustomerId(referenceApplication.getCustomerId());
						newApplication.setCustomerName(referenceApplication.getCustomerName());
						newApplication.setId(newId);
						newApplication.setApplicationClass(referenceApplication.getApplicationClass());
						newApplication.setAaplicationSubClass(referenceApplication.getAaplicationSubClass());
						newApplication.setNodeName(referenceApplication.getNodeName()+number);
						List<String> refDep = referenceApplication.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.remove(dependencie); 
						newDep.add(relatedNode.getId());
						
						newApplication.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						newNodeList.add(newApplication);
						number++; 
					}
				}
			}
		}
		applicationList.addAll(newNodeList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(applicationList);
		for(Node finalNode : finalNodeList){
			if( finalNode!=null && finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(finalNode.getDependencies()!=null)
				if(dependencie != null && dependencie.startsWith("+")){
					applicationList.remove(finalNode); 
				} 
				
			}
		}
		
	}
	
	private void checkDefaultMachineNodesParents(List<Node> nodeList) {
		List<Node> newMachineList = new ArrayList<Node>(); 
		for(Node node : nodeList){
			if(node.getDependencies()!=null && !node.getDependencies().isEmpty())
			for(int i = 0 ; i < node.getDependencies().size(); i++){
				String dependencie = node.getDependencies().get(i); 
				if(dependencie!=null && dependencie.startsWith("+")){
					int number = 1; 
					for(Node relatedNode : softwareList){
						Machine referenceMachine = (Machine) node; 
						String newId = referenceMachine.getId()+number; 
						Machine newMachine = new Machine(); 
						newMachine.setHeaderList(referenceMachine.getHeaderList());
						newMachine.setValueList(referenceMachine.getValueList());
						newMachine.setAutomationState(referenceMachine.getAutomationState());
						newMachine.setCustomerId(referenceMachine.getCustomerId());
						newMachine.setCustomerName(referenceMachine.getCustomerName());
						newMachine.setId(newId);
						newMachine.setMachinClass(referenceMachine.getMachinClass());
						newMachine.setNodeName(referenceMachine.getNodeName()+number);
						newMachine.setOSMajorVersion(referenceMachine.getOSMajorVersion());
						newMachine.setOSMinorVersion(referenceMachine.getOSMinorVersion());
						newMachine.setOSName(referenceMachine.getOSName());
						List<String> refDep = referenceMachine.getDependencies(); 
						List<String> newDep = new ArrayList<String>(); 
						newDep.addAll(refDep); 
						newDep.remove(dependencie); 
						newDep.add(relatedNode.getId());
						
						newMachine.setDependencies(newDep);
						List<String> currDepList = relatedNode.getDependencies();
						if(currDepList == null){
							currDepList = new ArrayList<String>(); 
						}
						currDepList.add(newId);
						if(referenceMachine.getIp()!=null)
							newMachine.setIp(referenceMachine.getIp());
						if(referenceMachine.getHostname()!=null)
							newMachine.setHostname(referenceMachine.getHostname());
						newMachineList.add(newMachine); 
						number++; 
					}
				}
			}
		}
		machineList.addAll(newMachineList);
		List<Node> finalNodeList = new ArrayList<Node>();
		finalNodeList.addAll(machineList);
		for(Node finalNode : finalNodeList){
			if(finalNode.getDependencies()!=null)
			for(String dependencie : finalNode.getDependencies()){
				if(dependencie != null && dependencie.startsWith("+")){
					resourceList.remove(finalNode); 
				} 
				
			}
		}
	}
	

	
	protected void removeEmptyDependencies(List<Node> nodeList, String type) {
		for(Node node : nodeList){
			List<String> depList = node.getDependencies(); 
			List<String> newDepList = new ArrayList<String>(); 
			if(depList!=null){
				newDepList.addAll(depList); 
				for(int i = 0; i < depList.size(); i ++  ){
					depList.get(i); 
					if( depList.get(i)==null || depList.get(i).isEmpty() ){
						newDepList.remove(i); 
					}
				}
			}
			node.setDependencies(newDepList);
		}
		
	}

	protected void checkDefaultNodesParents(List<Node> nodeList, String type) {
		switch(nodeTypes.valueOf(type)){
		case Machine: 
			checkDefaultMachineNodesParents(nodeList); 
			break; 
		case Software: 
			checkDefaultSoftwareNodesParents(nodeList); 
			break; 
		case Resource: 
			checkDefaultResourceNodesParents(nodeList); 
			break;
		default:
			break; 
		
		}
	}
	
	protected void checkDependencies(List<Node> nodeList, String type){
		for(int i = 0; i < nodeList.size(); i ++){
			List<String> dependencies = new ArrayList<String>(); 
			if(nodeList.get(i)!= null && nodeList.get(i).getDependencies()!= null){
			List<String> dependencieList = nodeList.get(i).getDependencies(); 
			
			for(String dependencie : dependencieList){
				dependencies.add(dependencie);
			}
			for(String dependencie : dependencieList){
				if(type.equals("Machine")){
					if(dependencie!=null && dependencie.equals("all")){
						for(int j = 0 ; j < softwareList.size(); j ++){
							dependencies.add(softwareList.get(j).getId());
							if(nodeList==null)
							{softwareList.get(j).setDependencies(new ArrayList<String>()); }
								if(softwareList.get(j).getDependencies()!=null){
									
									softwareList.get(j).getDependencies().add(nodeList.get(i).getId()); 
										
							
							
							}
						}
						dependencies.remove(dependencie);
					}
					
				}else if(type.equals("Software")){
					if(dependencie!=null && dependencie.equals("all")){
						
						for(int j  = 0; j < resourceList.size(); j ++ ){
							dependencies.add(resourceList.get(j).getId()); 
							if(nodeList==null)
							{resourceList.get(j).setDependencies(new ArrayList<String>());  }
								if(resourceList.get(j).getDependencies()!=null){
									
									resourceList.get(j).getDependencies().add(nodeList.get(i).getId()); 
										
								
							}
						}
						for(int j  = 0; j < machineList.size(); j ++ ){
							dependencies.add(machineList.get(j).getId()); 
							if(nodeList==null)
							{machineList.get(j).setDependencies(new ArrayList<String>());  }
								if(machineList.get(j).getDependencies()!=null){
									
									machineList.get(j).getDependencies().add(nodeList.get(i).getId()); 
								}
								
							
						}
						dependencies.remove(dependencie);
					}
					
				}
				else if(type.equals("Resource")){
					if(dependencie!=null && dependencie.equals("all")){
						for(int j = 0 ; j < softwareList.size(); j ++){
							dependencies.add(softwareList.get(j).getId());
							if(nodeList==null)
							{softwareList.get(j).setDependencies(new ArrayList<String>()); }
								if(softwareList.get(j).getDependencies()!=null){
									softwareList.get(j).getDependencies().add(nodeList.get(i).getId()); 
								}
							
						}
						for(int j = 0 ; j < applicationList.size(); j ++){
							dependencies.add(applicationList.get(j).getId());
							if(nodeList==null)
							{applicationList.get(j).setDependencies(new ArrayList<String>()); }
								if(applicationList.get(j).getDependencies()!=null){
									
									applicationList.get(j).getDependencies().add(nodeList.get(i).getId()); 
										
								
							}
						}
						dependencies.remove(dependencie);
					}
				}
				else if(type.equals("Application")){
					if(dependencie!=null && dependencie.equals("all")){
						for(int j  = 0; j < resourceList.size(); j ++ ){
							dependencies.add(resourceList.get(j).getId()); 
							if(nodeList==null)
							{resourceList.get(j).setDependencies(new ArrayList<String>());}
								if(resourceList.get(j).getDependencies()!=null){
									 
									resourceList.get(j).getDependencies().add(nodeList.get(i).getId()); 
										
								
							}
						}
						dependencies.remove(dependencie);
					}
				}
			}
			nodeList.get(i).setDependencies(dependencies);
		}
		}
	}
	
	
}
