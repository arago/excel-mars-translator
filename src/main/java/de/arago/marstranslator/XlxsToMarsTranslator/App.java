package de.arago.marstranslator.XlxsToMarsTranslator;

import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 */
public class App 
{
	//private static final Logger log = LoggerFactory.getLogger(App.class);  
    public static void main( String[] args )
    {
      BasicConfigurator.configure();
      System.out.println("Enter full path to folder to store the JSON files: ");
      Scanner scanner = new Scanner(System.in);
      String pathToStore = scanner.nextLine();
      System.out.println("Enter full path to the Excel file: ");
      String pathToRead = scanner.nextLine();
      XlxProcessor xlxProcessor = new XlxProcessor(pathToStore, pathToRead, "60", "JSON"); 
      xlxProcessor.readAndWrite();
      scanner.close();
      /* if(args.length==4){
    	   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1], args[2], args[3]); 
           xlxProcessor.readAndWrite();
       }
       else if(args.length==3){
    	   if (args[0].equals("toExcel")) {
           	   YamlToExcelProcessor yamlToExcleProcessor = new YamlToExcelProcessor(args[1], args[2]);
           	   yamlToExcleProcessor.processe();
           	   } 
    	   else if (args[2].equals("JSON")){
    		   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1], "60", args[2]); 
    		   xlxProcessor.readAndWriteJSON();
    	   }
       }
//       else if(args.length==2){
//   	   XlxProcessor xlxProcessor = new XlxProcessor(args[0], args[1]); 
//           xlxProcessor.readAndWrite();}
    else if (args.length==1){
    	   log.error("Please enter the full path where to store the output XML/ JSON files and the full path to the source excle file, only one input was given ");
       }
       else{
    	   log.error("Please enter the full path where to store the XML/ JSON files and the full path to the source excle file");
       }
    
       
    */
    }
    
    
}