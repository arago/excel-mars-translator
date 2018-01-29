package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YAMLToJsonMapper {
	
	public JSONObject toJSON(List<String> yamlLineList){
		JSONObject jOut = new JSONObject(); 
		
		
		int i = 0; 
		Map<Integer,String > variableNameMap = new HashMap<Integer,String>(); 
		Map<Integer,String > variableContentMap = new HashMap<Integer,String>(); 
		Map<Integer,String> variableKeyMap = new HashMap<Integer, String>(); 
		
		for(String line : yamlLineList){
			
			if(isVariableName(line)){
				if(isHeader(line)){
					line = addHeaderLines(jOut, line); 
				}
				else{
					variableNameMap.put(i, line.substring(2,line.length()-1)); 
				}
			}
			
			if(isValue(line)){
				addValue(i, variableContentMap, line);
				
			}
			if(isKey(line)){
				addKey(i, variableKeyMap, line);
			}
			if(isDependency(line)){
				addDependency(i, variableContentMap, line);
			}
			i ++; 
		}
	
		combineValuesAndNames(jOut, variableNameMap, variableContentMap, variableKeyMap);
		return jOut; 
	}

	private void combineValuesAndNames(JSONObject jOut,
			Map<Integer, String> variableNameMap,
			Map<Integer, String> variableContentMap, Map<Integer, String> variableKeyMap) {
		List<Integer> headerLines = new ArrayList<Integer>(variableNameMap.keySet()); 
		Collections.sort(headerLines);
		for (int j = 0; j < headerLines.size(); j++){
			String variableName= variableNameMap.get(headerLines.get(j));
			
			int min = headerLines.get(j) ;
			
			JSONArray contArray = new JSONArray(); 
			JSONArray keyArray = new JSONArray(); 
			
			
			if(j+1 < headerLines.size()){
				int max = headerLines.get(j+1); 
				for (int m : variableContentMap.keySet()){
					if(m>min && m<max){
						contArray.put(variableContentMap.get(m));
					if(variableKeyMap.containsKey(m+1)){
						keyArray.put(variableKeyMap.get(m+1));  
						}
					}
				}
			}else{
				for (int m : variableContentMap.keySet()){
					if(min<m ){
					contArray.put(variableContentMap.get(m)); }
				}
				
			}
			try {
				jOut.put(variableName, contArray);
				if(keyArray!=null ){
					if(keyArray.length()>1)
						jOut.put(variableName+"_key", keyArray); 
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} 
			
			
		}
	}

	private void addDependency(int i, Map<Integer, String> variableContentMap,
			String line) {
		String[] content = line.split("      ID: "); 
		if(content.length > 1){
			variableContentMap.put(i, content[1]);
		}
	}

	private void addValue(int i, Map<Integer, String> variableContentMap,
			String line) {
		String[] content = line.split(" Value: "); 
		if(content.length > 1){
			String value = content[1]; 
			if(value.startsWith("'") && value.endsWith("'")&& value.length()>2)
				value = value.substring(1, value.length()-1); 
			variableContentMap.put(i, value);
		}
	}
	
	private void addKey(int i, Map<Integer, String> variableKeyMap,
			String line) {
		String[] content = line.split(" Key: "); 
		if(content.length > 1){
			String value = content[1]; 
			if(value.startsWith("'") && value.endsWith("'")&& value.length()>2)
				value = value.substring(1, value.length()-1); 
			variableKeyMap.put(i, value);
		}
	}


	private boolean isDependency(String line) {
		return line.startsWith("    UID");
	}

	private String addHeaderLines(JSONObject jOut, String line) {
		line = line.substring(2); 
		String[] variable = line.split(":", 2); 
		try {
			JSONArray jA = new JSONArray(); 
			jA.put(variable[1].substring(1));
			jOut.put(variable[0], jA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return line;
	}

	private boolean isValue(String line) {
		return line.startsWith("    - Value") || line.startsWith("      Value");
	}
	
	private boolean isKey(String line) {
		return line.startsWith("    - Key") || line.startsWith("      Key");
	}

	private boolean isVariableName(String line) {
		return line.startsWith("  ") && !line.startsWith("    ");
	}

	private boolean isHeader(String line) {
		return line.contains(": ");
	}

}
