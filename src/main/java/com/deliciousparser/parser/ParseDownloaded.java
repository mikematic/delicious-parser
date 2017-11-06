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

public class ParseDownloaded extends AbstractParser{
	
	public String convertToJson(File inputFile){
		String bookmarksJson = "";
		int index = 1;
		Collection<File> fileList = getFiles(inputFile, null, null);
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

}
