package de.arago.marstranslator.XlxsToMarsTranslator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class MARSSchemaValidator implements XSDValidator {
	private SchemaFactory schemaFactory; 
	private File marsSchema; 
	private Schema schema; 
	private Validator validator; 
	
	
	MARSSchemaValidator(int marsVersion) {
		String MarsSchema = ""; 
		if(marsVersion == 2013){
			MarsSchema ="MARS.xsd"; 
		}
		else if(marsVersion == 2015){
			 MarsSchema = "MARS2015.xsd"; 
			
		}
		else {
			 MarsSchema = "MARS2015.xsd"; 
		}	
		 	marsSchema = readSchema(MarsSchema); 
			schemaFactory= SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1"); 
			//v.1.1 to allow multiple entries in xsd 
		try {
			schema = schemaFactory.newSchema(marsSchema);
			
		} catch (SAXException e) {
			e.printStackTrace();
		} 
		validator = schema.newValidator();; 
	}


	private File readSchema(String MarsSchema) {
		File f  = null; 
		try {
		File fp = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		 f = new File(fp.getParentFile()+"/MARS.xsd"); 
		InputStream in = getClass().getResourceAsStream("/"+MarsSchema); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		FileOutputStream fos = new FileOutputStream(f);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		String line = null; 
		while((line = reader.readLine())!=null){
				bw.write(line);
				bw.newLine();
			}
		bw.close();
		reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	
	public String checkForValidationError(String s){
		Source xmlFile = new StreamSource(new StringReader(s));
		try {
			  validator.validate(xmlFile);
			  return null; 
			} catch (SAXException e) {
			  return e.getLocalizedMessage(); 
			} catch (IOException e) {
				e.printStackTrace();
				return e.getLocalizedMessage(); 
			}
	}


	public void validateNodeList(List<Node> nodeList) {
		for(Node node : nodeList){
			String validationError = checkForValidationError(node.getXmlRepresentation()); 
			if(validationError == null){
				node.isValid = true; }
			else{
				node.isValid = false; 
				node.validatonError = validationError; 
			}
			
		}
				
	}


	public boolean validate(String s) {
		if(s== null )
			return false; 
		Source xmlFile = new StreamSource(new StringReader(s));
		try {
			  validator.validate(xmlFile);
			  return true; 
			} catch (SAXException e) {
			  return false;  
			} catch (IOException e) {
				e.printStackTrace();
				return false; 
			}
	}
	
	
}
