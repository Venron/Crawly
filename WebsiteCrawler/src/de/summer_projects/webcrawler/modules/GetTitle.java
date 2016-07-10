package de.summer_projects.webcrawler.modules;

import java.io.*;
import java.net.*;

/**
 * This small application takes an URL as argument, downloads that web page and
 * outputs its HTML title to the screen.
 * 
 * Refer to the main() method for how to read a local file instead.
 */
public class GetTitle {
	private static String startTag = "<title>";
	private static String endTag = "</title>";
	private static int startTagLength = startTag.length();
	
	public static String getTitle(URL theURL) {
		BufferedReader bufReader;
		String line;
		boolean foundStartTag = false;
		boolean foundEndTag = false;
		int startIndex, endIndex;
		String title = "";

		try {
			// open file
			bufReader = new BufferedReader(new InputStreamReader(theURL.openStream()));

			// read line by line
			while ((line = bufReader.readLine()) != null && !foundEndTag) {
				// System.out.println(line);

				// search for title start tag (convert to lower case before
				// searhing)
				if (!foundStartTag && (startIndex = line.toLowerCase().indexOf(startTag)) != -1) {
					foundStartTag = true;
				} else {
					// else copy from start of string
					startIndex = -startTagLength;
				}

				// search for title start tag (convert to lower case before
				// searhing)
				if (foundStartTag && (endIndex = line.toLowerCase().indexOf(endTag)) != -1) {
					foundEndTag = true;
				} else {
					// else copy to end of string
					endIndex = line.length();
				}

				// extract title field
				if (foundStartTag || foundEndTag) {
					// System.out.println("foundStartTag="+foundStartTag);
					// System.out.println("foundEndTag="+foundEndTag);
					// System.out.println("startIndex="+startIndex);
					// System.out.println("startTagLength="+startTagLength);
					// System.out.println("endIndex="+endIndex);

					title += line.substring(startIndex + startTagLength, endIndex);
				}
			}

			// close the file when finished
			bufReader.close();

			// output the title
			if (title.length() > 0) {
				System.out.println("Title: " + title);
			} else {
				System.err.println("No title found in document. Will be using host");
				title = theURL.getHost();
			}

		} catch (IOException e) {
			System.out.println("GetTitle.GetTitle - error opening or reading URL: " + e);
		}
		
		return title;
	}

}
