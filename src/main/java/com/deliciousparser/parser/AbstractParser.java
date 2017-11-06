package com.deliciousparser.parser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class AbstractParser {

	public static Collection<File> getFiles(File directory, FilenameFilter filter, Collection<String> extensions) {
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

	public static boolean isBookmarkInArr(JSONObject bookmarkObj, JSONArray bookmarksArr) {
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
	
	public abstract String convertToJson(File inputFile);
	
}
