package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.List;

import org.json.JSONException;

public class YamlToExcelProcessor {
	
	private String pathToFiles; 
	private NodeWriter writer; 
	private YamlFileReader yR = new YamlFileReader(); 
	private JSONToNodeMapper ma2pp = new JSONToNodeMapper(); 
	private YAMLToJsonMapper mapper = new YAMLToJsonMapper(); 
	
	
	public YamlToExcelProcessor(String pathToFiles, String pathToExcleFile){
		this.pathToFiles = pathToFiles; 
		 writer = new NodeWriter(pathToExcleFile); 
	}
	
	public void processe(){
		List<List<String>> in = yR.readListOfFilesFromFolder(pathToFiles); 
		for(List<String> ins : in ){
			try {
				writer.writeNode(ma2pp.toNode(mapper.toJSON(ins)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		writer.saveFile();
	}

}
