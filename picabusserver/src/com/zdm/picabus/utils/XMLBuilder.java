package com.zdm.picabus.utils;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Utilities class for XML manipulations
 * 
 * @author Daniel Lereya
 * 
 */
public class XMLBuilder {

	private static DocumentBuilder builder = null;
	private static Transformer transformer = null;

	private static DocumentBuilder getDocBilder() {
		if (builder == null) {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			try {
				builder = builderFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return builder;
	}

	private static Transformer getTransformer() {
		if (transformer == null) {
			TransformerFactory transfac = TransformerFactory.newInstance();
			try {
				transformer = transfac.newTransformer();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}
		}
		return transformer;
	}

	/**
	 * 
	 * @param rootName name of the root node
	 * @param description description of this XML (transforms into a comment in root node)
	 * @param childNodes key value pairs of the child nodes <node name, node value>
	 * @return <code>String</code> representing the given data as XML 
	 */
	public static String createXmlDoc(String rootName, String description,
			Map<String, String> childNodes) {
		String xmlString;
		Element childNode;
		Text text;

		// first create a document
		Document doc = getDocBilder().newDocument();

		// create the root element and add it to the document
		Element root = doc.createElement(rootName);
		doc.appendChild(root);

		// add the description to the root element
		if (description != null && !description.isEmpty()) {
			Comment comment = doc.createComment(description);
			root.appendChild(comment);
		}

		for (String childName : childNodes.keySet()) {
			if (childName == null || childName.isEmpty()) {
				continue;
			}
			childNode = doc.createElement(childName);
			root.appendChild(childNode);
			text = doc.createTextNode(childNodes.get(childName));
			childNode.appendChild(text);
		}

		// set up a transformer

		getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
		getTransformer().setOutputProperty(OutputKeys.STANDALONE, "yes");

		// create string from xml tree
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		try {
			getTransformer().transform(source, result);
		} catch (TransformerException e) {
			
			e.printStackTrace();
		}
		xmlString = sw.toString();

		return xmlString;

	}

}
