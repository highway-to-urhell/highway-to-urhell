package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.exception.H2HException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileSystemService extends AbstractLeechService {

	private final static String WEBXML = "web.xml";
	public static final String FRAMEWORK_NAME = "SYSTEM";

	private String pathWebXml;

	public FileSystemService() {
		super(FRAMEWORK_NAME);
		setTriggerAtStartup(true);
	}

	@Override
	protected void gatherData(List<EntryPathData> incoming) {
		String rootPath = null;
		try {
			if (rootPath == null) {
				throw new H2HException("Unknow Variable Path H2h. Please Set pathH2h to location application deployment.");
			}
			if ("".equals(rootPath)) {
				throw new H2HException(
						"Variable Path H2h is empty. Please Set pathH2h to location application deployment.");
			}
			// Step 1 search Web.XML
			searchAndParseWebXML(rootPath);
			// Step 2 search static files by categories
			searchStaticFiles(rootPath);

		} catch (H2HException e) {
			LOGGER.error(e.getMessage());
		}
	}

	private void searchStaticFiles(String rootPath) {
		File fileRoot = new File(rootPath);
		searchStatic(fileRoot);
	}

	private void searchStatic(File file) {
		if (file.isDirectory()) {
			if (file.canRead()) {
				for (File tempFile : file.listFiles(new FilterFileH2H())) {
					if (tempFile.isDirectory()) {
						searchStatic(tempFile);
					} else {
						EntryPathData entry = new EntryPathData();
						entry.setTypePath(TypePath.STATIC);
						entry.setUri(tempFile.getPath());
						addEntryPath(entry);
					}
				}
			}
		} else {
			EntryPathData entry = new EntryPathData();
			entry.setTypePath(TypePath.STATIC);
			entry.setUri(file.getPath());
			addEntryPath(entry);
		}

	}

	private void searchAndParseWebXML(String rootPath) {
		File fileRoot = new File(rootPath);
		searchWebXML(fileRoot);
		LOGGER.info("pathWebXml : " + pathWebXml);
		if (pathWebXml != null && !"".equals(pathWebXml)) {
			parseWebXml(pathWebXml);
		}
	}

	private void parseWebXml(String pathWebXml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(pathWebXml));
			extractServlet(document);
			extractFilter(document);
			extractListener(document);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("error while parsing web.xml " + pathWebXml, e);
		}
	}

	private void searchWebXML(File file) {
		if (file.isDirectory()) {
			if (file.canRead()) {
				for (File tempFile : file.listFiles()) {
					if (tempFile.isDirectory()) {
						searchWebXML(tempFile);
					} else {
						if (WEBXML.equals(tempFile.getName().toLowerCase())) {
							pathWebXml = tempFile.getAbsolutePath();
						}
					}
				}
			}
		} else {
			if (WEBXML.equals(file.getName().toLowerCase())) {
				pathWebXml = file.getAbsolutePath();
			}
		}

	}

	private void extractListener(Document document) {
		NodeList nodeListServlet = document.getDocumentElement()
				.getElementsByTagName("listener");
		for (int i = 0; i < nodeListServlet.getLength(); i++) {
			Node node = nodeListServlet.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				EntryPathData web = new EntryPathData();
				Element elem = (Element) node;
				web.setMethodEntry(elem.getElementsByTagName("listener-class")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setTypePath(TypePath.LISTENER);
				addEntryPath(web);
			}
		}

	}

	private void extractFilter(Document document) {
		NodeList nodeListMapping = document.getDocumentElement()
				.getElementsByTagName("filter-mapping");
		NodeList nodeListServlet = document.getDocumentElement()
				.getElementsByTagName("filter");
		for (int i = 0; i < nodeListServlet.getLength(); i++) {
			Node node = nodeListServlet.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				EntryPathData web = new EntryPathData();
				Element elem = (Element) node;
				web.setClassName(elem.getElementsByTagName("filter-name")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setMethodEntry(elem.getElementsByTagName("filter-class")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setTypePath(TypePath.FILTER);
				for (int j = 0; j < nodeListMapping.getLength(); j++) {
					Element elemMapping = (Element) nodeListMapping.item(j);
					if (elemMapping.getElementsByTagName("filter-name").item(0)
							.getChildNodes().item(0).getNodeValue()
							.equals(web.getClassName())) {
						NodeList urlPattern = elemMapping
								.getElementsByTagName("url-pattern");
						if (urlPattern != null && urlPattern.getLength() > 0) {
							web.setUri(urlPattern.item(0).getChildNodes()
									.item(0).getNodeValue());
						} else {
							// Check servlet-Name
							NodeList nameServlet = elemMapping
									.getElementsByTagName("servlet-name");
							if (nameServlet != null
									&& nameServlet.getLength() > 0) {
								web.setUri(nameServlet.item(0).getChildNodes()
										.item(0).getNodeValue());
							}
						}
					}
				}
				addEntryPath(web);
			}
		}

	}

	private void extractServlet(Document document) {
		NodeList nodeListMapping = document.getDocumentElement()
				.getElementsByTagName("servlet-mapping");
		NodeList nodeListServlet = document.getDocumentElement()
				.getElementsByTagName("servlet");
		for (int i = 0; i < nodeListServlet.getLength(); i++) {
			Node node = nodeListServlet.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				EntryPathData web = new EntryPathData();
				Element elem = (Element) node;
				web.setClassName(elem.getElementsByTagName("servlet-name")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setMethodEntry(elem.getElementsByTagName("servlet-class")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setTypePath(TypePath.SERVLET);
				for (int j = 0; j < nodeListMapping.getLength(); j++) {
					Element elemMapping = (Element) nodeListMapping.item(j);
					if (elemMapping.getElementsByTagName("servlet-name")
							.item(0).getChildNodes().item(0).getNodeValue()
							.equals(web.getClassName())) {
						web.setUri(elemMapping
								.getElementsByTagName("url-pattern").item(0)
								.getChildNodes().item(0).getNodeValue());
					}
				}
				addEntryPath(web);
			}
		}

	}

}
