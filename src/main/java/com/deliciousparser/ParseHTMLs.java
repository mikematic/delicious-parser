package com.deliciousparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParseHTMLs {
	
	private static Collection<File> getFiles(File directory, FilenameFilter filter, Collection<String> extensions) {
		// List of files
		Collection<File> fileList = new Vector<File>();
		// Get a list of all files&directories in the directory
		File[] files = directory.listFiles();
		if (files == null) {
			System.out.println("FileUtility: Directory: " + directory + " not found!");
			return null;
		}
		// Process each file/directory accordingly
		for (File file : files) {
			if (file.isDirectory()) {
				// Recursively operate on the folder
				fileList.addAll(getFiles(file, filter, extensions));
			} else {
				// Apply filter and add to vector if it passes
				if (filter == null || filter.accept(file, file.getName())) {
					String fileName = file.getName();
					String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
					if (filter == null || extensions.contains(fileExtension.toUpperCase()))
						fileList.add(file);
				}
			}
		}
		return fileList;

	}
	
	private static boolean isBookmarkInArr(JSONObject bookmarkObj, JSONArray bookmarksArr) {
		boolean result = false;
		JSONObject currObj;
		Iterator<JSONObject> iter = bookmarksArr.iterator();
		while (iter.hasNext()) {
			currObj = iter.next();
			if (currObj.get("title").toString().equalsIgnoreCase(bookmarkObj.get("title").toString())
					&& currObj.get("url").toString().equalsIgnoreCase(bookmarkObj.get("url").toString())) {
				System.out.println("Duplicate URL: " + currObj.get("url").toString() + " Skipping...");
				result = true;
				break;
			}
		}
		return result;
	}
	
	public static String convertDeliciousDownloads(String directory){
		String bookmarksJson = "";
		int index = 1;
		Collection<File> fileList = getFiles(new File(directory), null, null);
		JSONArray bookmarksArr = new JSONArray();
		
		for (File file : fileList) {
			try {
				Document doc = Jsoup.parse(file, "UTF-8", "http://delicious.com/");
				Iterator<Element> iter = doc.select("div.articleThumbBlockOuter").iterator();
				Element articleThumbBlockOuter;
				Element articleInfoPan;
				Element articleTitlePan;
				Element tagName;
				Element thumbBriefTxt;

				while (iter.hasNext()) {					
					articleThumbBlockOuter = iter.next();
					articleTitlePan = articleThumbBlockOuter.select("div.articleTitlePan").first();
					articleInfoPan = articleThumbBlockOuter.select("div.articleInfoPan").first();
					tagName = articleThumbBlockOuter.select("ul.tagName").first();
					thumbBriefTxt = articleThumbBlockOuter.select("div.thumbTBriefTxt").first();
					
					String title = articleTitlePan.select("h3").first().getElementsByAttribute("title").first().attr("title");
					String url = articleInfoPan.getElementsByTag("a").first().text();
					String savedDate = articleInfoPan.getElementsByTag("p").get(2).text();
					
					// Put each bookmark and its artifacts into a JSONObject
					JSONObject bookmarkObj = new JSONObject();
					bookmarkObj.put("title", title);
					bookmarkObj.put("url", url);
					bookmarkObj.put("savedDate", savedDate);
					
					// Put the tags for the bookmark in an array and add it to the JSONObject
					JSONArray tagArr = new JSONArray();
					Elements tags = tagName.select("li");
					Iterator<Element> iter2 = tags.iterator();
					while (iter2.hasNext()) {
						Element tag = iter2.next();
						tagArr.add(tag.text().toLowerCase()); //make all tags lower case to make it uniform
					}
					bookmarkObj.put("tags", tagArr);
					
					// If there is a note for the bookmark add it to the JSONObject
					if(thumbBriefTxt.getElementsByTag("p").size() > 1){
						bookmarkObj.put("notes", thumbBriefTxt.getElementsByTag("p").get(1).text());
					}
					
					//Check for duplicates and add to the array only if the constructed JSONObject is unique
					if(!isBookmarkInArr(bookmarkObj, bookmarksArr)){
						bookmarksArr.add(bookmarkObj);
					}
					
				}
				// Beautify the final JSONArray 
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				bookmarksJson = gson.toJson(bookmarksArr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("JSONArray created for " + bookmarksArr.size() + " unique bookmarks\n");
		return bookmarksJson;
	}
	
	public static void main(String[] args)throws Exception{
		if(args.length != 2){
			System.out.println("\nIncorrect parameters. Please check the usage of program.");
			System.out.println("Usage: java -jar deilciousparser.jar <download directory path> <output directory path>");
			System.out.println("       <download directory path>: path to the folder where htmls are downloaded from delicious server");
			System.out.println("       <output directory path>: path to the folder where JSON formated bookmarks is saved.\n");
		}
		else{
			String downloadDir = args[0].toString();
			String outputDir = args[1].toString();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputDir + "/mybookmarks.json")));
			bw.write(convertDeliciousDownloads(downloadDir));
			bw.close();
			System.out.println("JSONArray output to file: " + outputDir + "/mybookmarks.json\n");
			System.out.println("Program has finished. Please review output file and import it to Chromebookmarks using extension import-delicious-bookmarks\n");
		}
				
	}

}
