package com.propkaro.rssfeed;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler {
	static String TAG = XmlHandler.class.getSimpleName();
	private RssFeedStructure feedStr = new RssFeedStructure();
	private List<RssFeedStructure> rssList = new ArrayList<RssFeedStructure>();
	private int articlesAdded = 0;
	// Number of articles to download
	private static final int ARTICLES_LIMIT = 2000;
	StringBuffer chars = new StringBuffer();
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		chars = new StringBuffer();

//		if(Utilities.D)Log.v(TAG,"check this method###############");
//		if (qName.equals("media:content")){
//			if (!atts.getValue("url").toString().equalsIgnoreCase("null")) {
//				feedStr.setImgLink(atts.getValue("url").toString());
//			} else {
//				feedStr.setImgLink("");
//			}
//			if(Utilities.D)Log.v(TAG,"hari ****media:content****URL="+atts.getValue("url").toString());
//		} 
//		if (qName.equals("enclosure")) {
//			if (!atts.getValue("url").toString().equalsIgnoreCase("null")) {
//			feedStr.setImgLink(atts.getValue("url").toString());
//			} else {
//				feedStr.setImgLink("");
//			}
//			if(Utilities.D)Log.v(TAG,"hari ****enclosure****URL="+atts.getValue("url").toString());
//		}
//		if (qName.equals("description")) {
//			if (!atts.getValue("src").toString().isEmpty() || !(atts.getValue("src").toString().equals(null))) {
//				feedStr.setImgLink(atts.getValue("src").toString());
//			} else {
//				feedStr.setImgLink("");
//			}
//			if(Utilities.D)Log.v(TAG,"hari ****decription****URL="+atts.getValue("src").toString());
//		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("title")) {
			feedStr.setTitle(chars.toString());
		} else if (localName.equalsIgnoreCase("description")) {
			feedStr.setImgLink(setDescription(chars.toString()));
		} else if (localName.equalsIgnoreCase("pubDate")) {

			feedStr.setPubDate(chars.toString());
		} else if (localName.equalsIgnoreCase("encoded")) {

			feedStr.setEncodedContent(chars.toString());
		} else if (qName.equalsIgnoreCase("media:content")) {
//			feedStr.setImgLink(chars.toString());

		} else if (localName.equalsIgnoreCase("link")) {
			feedStr.setBackurl(chars.toString());
		}
		if (localName.equalsIgnoreCase("item")) {
			rssList.add(feedStr);

			feedStr = new RssFeedStructure();
			articlesAdded++;
			if (articlesAdded >= ARTICLES_LIMIT) {
				throw new SAXException();
			}
		}
	}

	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}
	public String setDescription(String description) {
		String img = "";
		if (description.contains("src=")) {
			img = description.substring(description.indexOf("src="));
			String cleanUp = img.substring(0, img.indexOf(">") + 1);
			img = img.substring(img.indexOf("src=") + 5);
			int indexOf = img.indexOf("'");
			if (indexOf == -1) {
				indexOf = img.indexOf("\"");
			}
			img = img.substring(0, indexOf);
//			setImgLink(img);

			description = description.replace(cleanUp, "");
		}
		return img;
	}

	public List<RssFeedStructure> getLatestArticles(String feedUrl) {
		URL url = null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			url = new URL(feedUrl);
			xr.setContentHandler(this);
			xr.parse(new InputSource(url.openStream()));
		} catch (IOException e) {
		} catch (SAXException e) {

		} catch (ParserConfigurationException e) {

		}

		return rssList;
	}

}