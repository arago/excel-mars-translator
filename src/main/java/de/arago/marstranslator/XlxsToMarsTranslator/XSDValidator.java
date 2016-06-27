package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.List;

public interface XSDValidator {
	public boolean validate(String xml); 
	public String checkForValidationError(String xml); 
	public void validateNodeList(List<Node> nodeList); 
		
	
}
