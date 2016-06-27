package de.arago.marstranslator.XlxsToMarsTranslator;

import junit.framework.TestCase;

public class MARSSchmeaValidatorTest extends TestCase {
	private MARSSchemaValidator val = new MARSSchemaValidator(2015); 
	private String s; 
	
	public void test(){
		s = "<?xml version='1.0' ?> <WebDevelopment ApplicationClass='Development' ApplicationSubClass='WebDevelopment' xmlns='https://graphit.co/schemas/v2/MARSSchema' ID='Example:Model:Application:WebDevelopment' NodeName='WebDevApplicationNode1' NodeType='Application' CustomerID='examplemodel.de' CustomerName='examplemodel.de'><Dependencies><Node ID='Example:Model:Resource:AppServer1'/></Dependencies></WebDevelopment>"; 
		System.out.println(val.checkForValidationError(s));
		assertTrue(val.validate(s)); 
		
	}
}
