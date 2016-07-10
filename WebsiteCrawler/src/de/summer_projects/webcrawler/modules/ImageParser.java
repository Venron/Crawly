package de.summer_projects.webcrawler.modules;

import java.util.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.concurrent.SynchronousQueue;

import javax.imageio.ImageIO;

import org.jsoup.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.summer_projects.webcrawler.application.CrawlyApplication;

/*
 * Give Crawly the link after starting the program, not with the program parameters
 * Let Crawly give the user instructions on what he is doing and what he requires from the user
 * */

public class ImageParser {
	static String path = ImageParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	static String decodedPath = "";
	static String imagePrefix = "";

	public static void callCrawly_ImageParser() {
		String userUrl = "";
		Scanner scanner = new Scanner(System.in);

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
		System.out.println("\n\nWelcome to the Crawly ImageParser!");
		System.out.println("The SiteLoader will download any pictures to the directory Crawly is started from.\n");
		System.out.print("\nCrawl URL:");

		try {
			userUrl = scanner.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("Please enter a url");
			System.exit(0);
		}

		try {
			crawlingAnimator();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		/*
		 * decode url to usable utf-8 format
		 */
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.err.println("Exception at url decoding");
		}

		try {
			/*
			 * open connection to url specified at program start
			 */
			Document doc = Jsoup.connect(userUrl).get();
			/*
			 * select all html elements with <img ...> tag
			 * 
			 * found img elements will be stored in an Elements object with
			 * stores multiple Element objects (like List<Element> Elements =
			 * List<Element>() )
			 */
			Elements images = doc.getElementsByTag("img");

			/*
			 * string list that will hold all the absolute (not relative!) urls
			 * from the images found
			 */
			List<String> absUrls = new ArrayList<String>();

			/*
			 * iterate through the found elements [Element]
			 * 
			 * read the absolute url from each element and store it in the
			 * absUrls list
			 * 
			 * relative url = /data/image
			 * 
			 * absolute url = http://www.url.com/data/image
			 * 
			 * absolute url is required to open a connection to the image url
			 */
			for (Element img : images) {
				absUrls.add(img.absUrl("src"));
			}

			/*
			 * print out the found images
			 */
			if (absUrls.size() == 0) {
				System.out.println("\n\nNo images were found.");
				askForReturn();
			} else {
				System.out.println("\n\n" + absUrls.size() + " images found:\n");
			}
			for (String s : absUrls) {
				System.out.println(s);
			}

			/*
			 * ask the user to specify a image prefix for image storing i.e.
			 * images will be stored as <image-prefix>1.jpg ,
			 * <image-prefix>2.jpg , <image-prefix>3.jpg , ...
			 */
			System.out.println("\nPlease enter the image prefix: ");
			// scanner = new Scanner(System.in);
			imagePrefix = scanner.nextLine();

			/*
			 * display the path the found images will be saved to
			 * 
			 * @REQUIRED UPDATE: let user specify a download path
			 */
			System.out.println("Downloading files to the Crawly directory: " + decodedPath + "\n\n");

			int imgCount = 1;
			int status;
			/*
			 * iterate through the string list, for each string entry (url),
			 * call the downloadImage(int count, URL url) method
			 * 
			 */
			for (String s : absUrls) {
				status = downloadImage(imgCount, new URL(s));
				if (status == 1) {
					/*
					 * present download programm in ONE line, which will be
					 * changed in every loop iteration with the carriage return
					 */
					System.out.print(imgCount + " / " + absUrls.size() + " downloaded.\r");
				} else if (status == -1) {
					/*
					 * display errors
					 */
					System.out.println("Error at downloading image " + imgCount);
				} else {
					/*
					 * display errors
					 */
					System.out.println("Unknown error while downloading image " + imgCount);
				}
				status = 0;
				imgCount++;
			}
			System.out.println("\n\n");

		} catch (IllegalArgumentException e) {
			System.err.println("\nERROR: Required url format: http://www.my-url.com\n\n");
			System.out.println("\n\n\n");
			CrawlyApplication.initCrawlyOuter();
		} catch (IOException e) {
			System.err.println("IOException occured");
		}
	}

	/*
	 * downloadImage(int count, URL imgUrl)
	 * 
	 * this method will download a picture from a given url with the
	 * ImageIO.read() method
	 * 
	 * ImageIO.read() required a URL object as parameter
	 * 
	 * downloaded image will be saved with ImageIO.write(BufferedImage img,
	 * String imgType, File savePath) to the default path, with is the path
	 * where Crawly is started from
	 */
	private static int downloadImage(int count, URL imgUrl) {
		int status = 0;
		BufferedImage img = null;
		try {
			URLConnection connection = imgUrl.openConnection();
			img = ImageIO.read(imgUrl);
		} catch (IOException e) {
			System.err.println("IOException occured");
			status = -1;
		}

		try {
			ImageIO.write(img, "jpg", new File("." + "\\" + imagePrefix + count + ".jpg"));

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (status == -1) {

		} else {
			status = 1;
		}
		return status;
	}

	/*
	 * DEPRECATED
	 */
	private static void usage() {
		System.out.println("Call Crawly with a link like this: http://www.my-url.com");
	}

	private static void crawlingAnimator() throws InterruptedException {
		System.out.println("\n\n");
		char[] loading = { '\\', '|', '/', '-', '\\', '|', '/', '-' };
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j < loading.length; j++) {
				System.out.print("Crawling URL... " + loading[j] + "\r");
				Thread.sleep(100);
			}
		}
		System.out.println();
		System.out.println("Done...");
	}

	public static void askForReturn() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Return to main program? [Y/N]: ");
		char choice = sc.nextLine().charAt(0);
		if (choice == 'Y' || choice == 'y') {
			System.out.println("\n\n");
			CrawlyApplication.initCrawlyOuter();
		} else if (choice == 'N' || choice == 'n') {
			System.out.println("\nTerminating...");
			System.exit(0);
		} else {
			System.out.println("\nInvalid choice. Terminating...");
			System.exit(0);
		}
	}
}
