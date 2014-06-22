package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.domain.WebData;
import io.highway.to.urhell.exception.H2HException;
import io.highway.to.urhell.service.LeechService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class FileSystemService implements LeechService {

	private final static String PATHH2H = "PATH_H2H";
	private final static String WEBXML = "web.xml";

	public FileSystemService() {
		LOG.info("Constructor FileSystemService");
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(FileSystemService.class);
	private List<EntryPathData> listData;
	private String pathWebXml;

	@Override
	public void receiveData(Object incoming) {
		String rootPath = System.getProperty(PATHH2H);
		listData = new ArrayList<EntryPathData>();
		try {
			if (rootPath == null) {
				throw new H2HException("Unknow Variable Path H2h. Please Set "
						+ PATHH2H + " to location application deployment.");
			}
			if (rootPath != null && "".equals(rootPath)) {
				throw new H2HException(
						"Variable Path H2h is empty. Please Set " + PATHH2H
								+ " to location application deployment.");
			}
			// Step 1 search Web.XML
			searchAndParseWebXML(rootPath);
			// Step 2 search static files by categories
			searchStaticFiles(rootPath);

		} catch (H2HException e) {
			LOG.error(e.getMessage());
		}
		LOG.info("complete data for : "
				+ FileSystemService.class.getCanonicalName()
				+ "number elements loaded " + listData.size());
		if (LOG.isDebugEnabled()) {
			Gson gson = new Gson();
			LOG.info(" JSON elements :" + gson.toJson(listData));
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
						listData.add(entry);
					}
				}
			}
		} else {
			EntryPathData entry = new EntryPathData();
			entry.setTypePath(TypePath.STATIC);
			entry.setUri(file.getPath());
			listData.add(entry);
		}

	}

	private void searchAndParseWebXML(String rootPath) {
		File fileRoot = new File(rootPath);
		searchWebXML(fileRoot);
		LOG.info("pathWebXml : " + pathWebXml);
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
		} catch (ParserConfigurationException e) {
			LOG.error("Parse error :"+e.getMessage());
		} catch (SAXException e) {
			LOG.error("Parse error "+e.getMessage());
		} catch (IOException e) {
			LOG.error("Parse error "+e.getMessage());
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
				listData.add(web);
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
				web.setMethodName(elem.getElementsByTagName("filter-name").item(0)
						.getChildNodes().item(0).getNodeValue());
				web.setMethodEntry(elem.getElementsByTagName("filter-class")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setTypePath(TypePath.FILTER);
				for (int j = 0; j < nodeListMapping.getLength(); j++) {
					Element elemMapping = (Element) nodeListMapping.item(j);
					if (elemMapping.getElementsByTagName("filter-name").item(0)
							.getChildNodes().item(0).getNodeValue()
							.equals(web.getMethodName())) {
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
				listData.add(web);
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
				web.setMethodName(elem.getElementsByTagName("servlet-name").item(0)
						.getChildNodes().item(0).getNodeValue());
				web.setMethodEntry(elem.getElementsByTagName("servlet-class")
						.item(0).getChildNodes().item(0).getNodeValue());
				web.setTypePath(TypePath.SERVLET);
				for (int j = 0; j < nodeListMapping.getLength(); j++) {
					Element elemMapping = (Element) nodeListMapping.item(j);
					if (elemMapping.getElementsByTagName("servlet-name")
							.item(0).getChildNodes().item(0).getNodeValue()
							.equals(web.getMethodName())) {
						web.setUri(elemMapping
								.getElementsByTagName("url-pattern").item(0)
								.getChildNodes().item(0).getNodeValue());
					}
				}
				listData.add(web);
			}
		}
		
	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SYSTEM);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}

}
