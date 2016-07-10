package de.summer_projects.webcrawler.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import de.summer_projects.webcrawler.exceptions.TitleIsEmptyException;
import de.summer_projects.webcrawler.modules.*;
import java.io.Console;

import org.jsoup.*;

import org.apache.commons.io.*;

public class SiteLoader {

	public static void callCrawly_SiteLoader() {
		String fullQualifiedSiteTitle = "";
		String localTitle = "";

		Scanner scanner = new Scanner(System.in);
		String userUrl = "";

		/*
		 * clear console
		 */
		// try {
		// Runtime.getRuntime().exec("cls");
		// } catch (IOException e1) {
		// System.err.println("Error at executing cls");
		// }

		/*
		 * start message
		 */
		System.out.println("\n\nWelcome to the Crawly SiteLoader!");
		System.out.println("The SiteLoader will download any website to the directory Crawly is started from.\n");
		System.out.print("Please specify the url of the website you want to download:");
		try {
			userUrl = scanner.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("Please enter a valid url");
		}

		try {
			/*
			 * save entered url as a URL object
			 */
			URL url = new URL(userUrl);

			/*
			 * read site title from the html document
			 * 
			 * if the site has not implicit title specified, crawly will save
			 * the site with the host name
			 */
			fullQualifiedSiteTitle = GetTitle.getTitle(url);
			localTitle = getLocalTitle(fullQualifiedSiteTitle);

			/*
			 * open connection to the specified url
			 */
			URLConnection urlc = url.openConnection();
			System.out.println("Website found at " + urlc.getURL());
			System.out.println("Downloading website...\n");
			String contentType = urlc.getContentType();

			File file = new File(".\\" + localTitle + ".html");

			/*
			 * download the website with FileUtils.copyURLToFile(URL url, File
			 * path)
			 */
			System.out.println("Saving website to " + file.getAbsolutePath());
			FileUtils.copyURLToFile(url, file);

			System.out.println("Crawly has downloaded the website. Terminating...");
			ImageParser.askForReturn();

		} catch (MalformedURLException e) {
			System.err.println("Required URL format: http://www.your-url.com");
			ImageParser.askForReturn();
		} catch (IOException e) {
			System.err.println("IOException occured");
			e.printStackTrace();
			System.exit(0);
		} catch (TitleIsEmptyException e) {
			System.exit(0);
		}
	}

	private static String getLocalTitle(String title) throws TitleIsEmptyException {
		if (title.length() == 0) {
			throw new TitleIsEmptyException();
		} else {
			String res = "";
			char[] a = title.toCharArray();
			for (char c : a) {
				if (c != '-') {
					res += c;
				} else {
					break;
				}
			}
			return res;
		}
	}
}
