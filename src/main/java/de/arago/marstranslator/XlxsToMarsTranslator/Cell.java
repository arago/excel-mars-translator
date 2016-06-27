package de.arago.marstranslator.XlxsToMarsTranslator;

public class Cell {
	private int cellNumber; 
	private String content;
	public Cell(){}; 
	public Cell(int cellNumber, String content){
		this.cellNumber = cellNumber; 
		this.content = content; 
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
