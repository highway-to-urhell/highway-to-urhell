package io.highway.to.urhell.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FilterFileH2H implements FileFilter {

	private List<String> listExtentions;

	public FilterFileH2H() {
		super();
		listExtentions = new ArrayList<String>();
		listExtentions.add("html");
		listExtentions.add("xhtml");
		listExtentions.add("htm");
		listExtentions.add("jsp");
		listExtentions.add("jspx");
		listExtentions.add("jsf");
		listExtentions.add("xml");
		listExtentions.add("txt");
		listExtentions.add("xml");
		listExtentions.add("doc");
		listExtentions.add("docx");
		listExtentions.add("xls");
		listExtentions.add("xlsx");

	}

	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory()) {
			return true;
		} else {
			for (String extension : listExtentions) {
				if (pathname.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
		}
		return false;
	}

}
