package de.arago.marstranslator.XlxsToMarsTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cell {
	private int keyRowNumber = -1;
	private String keyContent = null; 
	private int cellNumber; 
	private String content;
	public Cell(){}; 
	
	private Logger log = LoggerFactory.getLogger(Node.class);
	
	public Cell(int cellNumber, String content, int keyRowNumber, String keyContent){
		this.cellNumber = cellNumber; 
		this.content = content; 
		this.keyContent = keyContent; 
		this.keyRowNumber = keyRowNumber; 
	}

	public Cell(int cellNumber, String content){
		this.cellNumber = cellNumber; 
		this.content = content; 
	}
	
	public int getKeyRowNumber(){
		return keyRowNumber; 
	}
	
	public String getKeyContent(){
		return keyContent; 
	}
	public void setKeyContent(String keyContent){
		this.keyContent = keyContent; 
	}
	
	public void setKeyRowNumber(int keyRowNumber){
		this.keyRowNumber = keyRowNumber; 
	}

	
	public int getCellNumber() {
		return cellNumber;
	}
	public void setCellNumber(int cellNumber) {
		this.cellNumber = cellNumber;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	} 
	

}
