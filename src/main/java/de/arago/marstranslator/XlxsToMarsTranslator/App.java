package de.arago.marstranslator.XlxsToMarsTranslator;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class App 
{
	private static final Logger log = LoggerFactory.getLogger(App.class);  
    public static void main( String[] args )
    {
       BasicConfigurator.configure();
       if(args.length==4){
    	   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1], args[2], args[3]); 
           xlxProcessor.readAndWrite();
       }
       else if(args.length==3){
    	   if (args[0].equals("toExcel")) {
           	   YamlToExcelProcessor yamlToExcleProcessor = new YamlToExcelProcessor(args[1], args[2]);
           	   yamlToExcleProcessor.processe();
           	   } 
    	   else{
    	   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1], args[2] ); 
           xlxProcessor.readAndWrite();
    	   }
       }
       else if(args.length==2){
    	   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1]); 
           xlxProcessor.readAndWrite();
       }else if (args.length==1){
    	   log.error("Please enter the full path where to store the xml files and the full path to the source excle file, only one input was given ");
       }
       else{
    	   log.error("Please enter the full path where to store the xml files and the full path to the source excle file");
       }
    }
}