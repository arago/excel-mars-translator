package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.arago.marstranslator.XlxsToMarsTranslator.XlxProcessor.nodeTypes;

public class JSONToNodeMapper {
	
	private List<String> nodeAttributeList = new ArrayList<String>(); 

	JSONToNodeMapper(){
		nodeAttributeList.add(NodeAttributes.applicationClass); 
		nodeAttributeList.add(NodeAttributes.applicationSubClass); 
		nodeAttributeList.add(NodeAttributes.automationState); 
		nodeAttributeList.add(NodeAttributes.ctime); 
		nodeAttributeList.add(NodeAttributes.customerId); 
		nodeAttributeList.add(NodeAttributes.customerName); 
		nodeAttributeList.add(NodeAttributes.dependencies); 
		nodeAttributeList.add(NodeAttributes.dependencies); 
		nodeAttributeList.add(NodeAttributes.hostname); 
		nodeAttributeList.add(NodeAttributes.machineClass); 
		nodeAttributeList.add(NodeAttributes.mtime); 
		nodeAttributeList.add(NodeAttributes.nodeID); 
		nodeAttributeList.add(NodeAttributes.nodeName); 
		nodeAttributeList.add(NodeAttributes.oSMajorVersion); 
		nodeAttributeList.add(NodeAttributes.oSMinorVersion); 
		nodeAttributeList.add(NodeAttributes.oSName); 
		nodeAttributeList.add(NodeAttributes.resourceClass); 
		nodeAttributeList.add(NodeAttributes.softwareClass); 
		nodeAttributeList.add(NodeAttributes.softwareSubClass); 
		nodeAttributeList.add(NodeAttributes.sourceCiID); 
		nodeAttributeList.add(NodeAttributes.uid); 
		nodeAttributeList.add(NodeAttributes.xmlns); 
		nodeAttributeList.add(NodeAttributes.node_ID); 
		nodeAttributeList.add(NodeAttributes.nodeType); 
	}
	
	
	Node toNode(JSONObject jo) throws JSONException{
		Node node = null; 
		String nodeType = checkAndGet(jo,  "NodeType");
		if(nodeType != null)
			node = getNode(node, nodeType, jo);
		return node; 
	}

	private String checkAndGet(JSONObject jo,  String string)
			throws JSONException {
		if(jo.has(string)){
			JSONArray jA = (JSONArray) jo.get(string); 
			return  (String) jA.get(0); }
		return null; 
	}

	private Node getNode(Node node, String nodeType, JSONObject jo) throws JSONException {
		node = setMandatoryAttributes(node, nodeType, jo);
		setOptAttributes(node, jo);
		return node;
	}


	private void setOptAttributes(Node node, JSONObject jo)
			throws JSONException {
		List<Cell> headerList = new ArrayList<Cell>(); ;
		List<Cell> valueList = new ArrayList<Cell>();
		int cellNumb = 12; 
		@SuppressWarnings("unchecked")
		Iterator<String> keys = jo.keys(); 
		while (keys.hasNext()){
			String key = (String) keys.next(); 
			if(!nodeAttributeList.contains(key)){
				Cell headerCell = new Cell(cellNumb, key); 
				JSONArray jA = (JSONArray) jo.get(key); 
				String value =""; 
				for(int k = 0; k < jA.length(); k ++){
					 value+= (String) jA.get(k);
					 if( (k+1)<jA.length()){
						 value+="\n"; 
						 }
				}
				Cell valueCell = new Cell(cellNumb, value); 
				headerList.add(headerCell); 
				valueList.add(valueCell); 
				cellNumb ++; 
			}
			
		}
		node.setHeaderList(headerList);
		node.setValueList(valueList);
	}

	private Node setMandatoryAttributes(Node node, String nodeType,
			JSONObject jo) throws JSONException {
		if(nodeType.equals(nodeTypes.Machine.toString())){
			Machine mnode = new Machine(); 
			mnode.setHostname(checkAndGet(jo,NodeAttributes.hostname));
			mnode.setOSMajorVersion(checkAndGet(jo,NodeAttributes.oSMajorVersion));
			mnode.setOSMinorVersion(checkAndGet(jo,NodeAttributes.oSMinorVersion));
			mnode.setOSName(checkAndGet(jo,NodeAttributes.oSName));
			mnode.setMachinClass(checkAndGet(jo,NodeAttributes.machineClass));
			node = mnode; 
		}
		else if (nodeType.equals(nodeTypes.Software.toString())){
			Software snode = new Software(); 
			snode.setSoftwareClass(checkAndGet(jo,NodeAttributes.softwareClass));
			snode.setSoftwareSubClass(checkAndGet(jo,NodeAttributes.softwareSubClass));
			node = snode; 
		}
		else if (nodeType.equals(nodeTypes.Resource.toString())){
			Resource rnode = new Resource(); 
			rnode.setResourceClass(checkAndGet(jo,NodeAttributes.resourceClass));
			node = rnode; 
		}
		else if (nodeType.equals(nodeTypes.Application.toString())){
			Application anode = new Application(); 
			anode.setApplicationClass(checkAndGet(jo,NodeAttributes.applicationClass));
			anode.setAaplicationSubClass(checkAndGet(jo,NodeAttributes.applicationSubClass));
			node = anode; 
		}
		node.setAutomationState(checkAndGet(jo, NodeAttributes.automationState));
		node.setCustomerId(checkAndGet(jo, NodeAttributes.customerId));
		node.setCustomerName(checkAndGet(jo, NodeAttributes.customerName));
		node.setId(checkAndGet(jo, NodeAttributes.nodeID));
		node.setNodeName(checkAndGet(jo, NodeAttributes.nodeName));
		node.setNodeType(nodeType);
		node.setSourceCiId(checkAndGet(jo,NodeAttributes.sourceCiID));
		if(jo.has(NodeAttributes.dependencies)){
			List<String> depList = new ArrayList<String>();
			JSONArray deps = (JSONArray) jo.get(NodeAttributes.dependencies);
			for(int dn = 0; dn < deps.length() ; dn ++ ){
				depList.add((String) deps.get(dn));
			}
			node.setDependencies(depList); 
		} 
		return node;
	}

}
