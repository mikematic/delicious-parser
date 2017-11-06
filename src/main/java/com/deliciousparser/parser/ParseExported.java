package com.deliciousparser.parser;

import java.io.File;
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

public class ParseExported extends AbstractParser{
	
	public String convertToJson(File inputFile){
		String bookmarksJson = "";
		JSONArray bookmarksArr = new JSONArray();
		try{
			Document doc = Jsoup.parse(inputFile, "UTF-8", "http://delicious.com/");
			Elements DTs = doc.select("DT");		
			for (Element DT : DTs){
				Elements link = DT.select("A");
				String title = link.text().toString();
				String url = link.attr("href");
				String savedDate = link.attr("ADD_DATE");
				String[] tags = link.attr("TAGS").split(",");
				String note = DT.select("DD").text();
				
				// Put each bookmark and its artifacts into a JSONObject
				JSONObject bookmarkObj = new JSONObject();
				bookmarkObj.put("title", title);
				bookmarkObj.put("url", url);
				bookmarkObj.put("savedDate", savedDate);
				JSONArray tagsArr = new JSONArray();
				for(String tag : tags){
					tagsArr.add(tag.toLowerCase());
				}				
				bookmarkObj.put("tags", tagsArr);
				bookmarkObj.put("notes", note);
				
				//Check for duplicates and add to the array only if the constructed JSONObject is unique
				if(!isBookmarkInArr(bookmarkObj, bookmarksArr)){
					bookmarksArr.add(bookmarkObj);
				}
				
			}
			// Beautify the final JSONArray 
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			bookmarksJson = gson.toJson(bookmarksArr);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("JSONArray created for " + bookmarksArr.size() + " unique bookmarks\n");
		return bookmarksJson;
		
	}

	public static void main(String[] args){
		ParseExported parseExported = new ParseExported();
		System.out.println(parseExported.convertToJson(new File("/Users/mikematic/Documents/Delicious.Bookmarks/bookmarks-10022017.htm")));
	}

}
