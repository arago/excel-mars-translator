package de.arago.marstranslator.XlxsToMarsTranslator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class YamlFileReader {
	
	public String readFile(String path){
		
		List<String> file;
		try {
			file = Files.readAllLines(Paths.get(path));
			String yamlFile = ""; 
			for(String line : file){
				yamlFile+= line + "\n"; 
			}
			return yamlFile; 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null; 
	}
	
	
	public List<String> readFileAsList(String path){
		try {
			return Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null; 
	}
	
	
	public List<List<String>> readListOfFilesFromFolder(String folderpath){
		List<List<String>> fileList = new ArrayList(); 
		File tempFile = new File(folderpath);
		if (tempFile!=null && tempFile.listFiles()!=null )
		for(File file : tempFile.listFiles()){
			if(file.isDirectory())
			{
				fileList.addAll(readListOfFilesFromFolder(file.getAbsolutePath())); 
			}
			else{
				if(file.isFile()&&!file.isHidden()){
					if(file.getName().endsWith(".yaml"))
						fileList.add(readFileAsList(file.getAbsolutePath())); 
				}
			}
			
		}
		return fileList; 
		
	}
	
	public List<String> readFilesFromFolder(String folderpath){
		List<String> fileList = new ArrayList<String>(); 
		File tempFile = new File(folderpath); 
		for(File file : tempFile.listFiles()){
			if(file.isDirectory())
			{
				fileList.addAll(readFilesFromFolder(file.getAbsolutePath())); 
			}
			else{
				if(file.isFile()&&!file.isHidden()){
				String fileContent = readFile(file.getAbsolutePath()) ; 
				if (fileContent!=null) 
					fileList.add(fileContent); 
				}
			}
			
		}
		return fileList; 
	}
	
	

}
