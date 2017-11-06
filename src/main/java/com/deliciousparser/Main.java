package com.deliciousparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.deliciousparser.parser.ParseDownloaded;
import com.deliciousparser.parser.ParseExported;

public class Main {
	
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("\nIncorrect parameters. Please check the usage of program.");
			System.out.println("Usage: java -jar deilciousparser.jar <input directory/file path> <output directory path>");
			System.out.println("       <input directory/file path>: path to the folder where htmls are downloaded from delicious server <or> path to file exported using delicious api");
			System.out.println("       <output directory path>: path to the folder where JSON formated bookmarks is saved.\n");
		}
		else{
			File inputFile = new File (args[0].toString());
			String outputDir = args[1].toString();
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputDir + "/mybookmarks.json")));
				// Manual downloading of bookmarks using wget uses a directory structure
				if(inputFile.isDirectory()){
					ParseDownloaded parseDownloaded = new ParseDownloaded();
					bw.write(parseDownloaded.convertToJson(inputFile));
					bw.close();
				}
				// Single file is generated if using the delicious Exporting api
				else{
					ParseExported parseExported = new ParseExported();
					bw.write(parseExported.convertToJson(inputFile));
					bw.close();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			System.out.println("JSONArray output to file: " + outputDir + "/mybookmarks.json\n");
			System.out.println("Program has finished. Please review output file and import it to Chromebookmarks using extension import-delicious-bookmarks\n");
		}
	}

}
