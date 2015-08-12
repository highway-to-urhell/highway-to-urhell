package com.highway2urhell.generator.impl;

import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.domain.FrameworkInformations;
import com.highway2urhell.generator.TheJack;
import com.highway2urhell.service.LeechService;

import java.util.Collection;

/**
 * Be careful, code 3v1l and D1rty
 */
public class HTMLGenerator implements TheJack {

    public String generatePage(Collection<LeechService> collectionService) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset=\"utf-8\"></head><style type=\"text/css\">body{ font-size:18px; color:#FFF; }");
        sb.append(".classh2h {text-shadow:5px 1px 11px #1a45f0} ");
        sb.append(".classname { border:solid 1px #2d2d2d;  text-align:center; background:#575757; padding:50px 10px 20px 20px;  -moz-border-radius: 5px;  -webkit-border-radius: 5px; border-radius: 5px;}");
        sb.append("table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;} ");
        sb.append("table.gridtable tr{	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede ;} ");
        sb.append("table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}");
        sb.append("table.gridtable2 {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#336699;	border-width: 1px;	border-color: #336699;	border-collapse: collapse;} ");
        sb.append("table.gridtable2 tr{	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #336699;	background-color: #dedede ;} ");
        sb.append("table.gridtable2 td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #336699;	background-color: #ffffff;}");
        sb.append("</style><body>)");

        if (collectionService != null) {
            int i = 0;
            for (LeechService leech : collectionService) {
                FrameworkInformations fwk = leech.getFrameworkInformations();
                if(fwk.getListEntryPath() != null && !fwk.getListEntryPath().isEmpty()){
                	sb.append("<div id=\"gibson").append(i).append("\" class=\"classname\">");
                	sb.append("<ul>");
                	sb.append("<p>Framework Informations ").append(fwk.getFrameworkName()).append("</p><br>");
                	sb.append("<p>Framework Version ").append(fwk.getVersion()).append("</p><br>");
                	sb.append("<p>Entries Path : </p><br>");
                    sb.append("<table class=\"gridtable\">");
                    sb.append("<tr><td>Class Name</td><td>Method Name</td><td>Http Method</td><td>Type Path</td><td>Uri Path</td></tr>");
                    for (EntryPathData entry : fwk.getListEntryPath()) {
                        sb.append("<tr>");
                        sb.append("<td><p>").append(entry.getClassName()).append("</p></p></td>");
                        sb.append("<td><p>").append(entry.getMethodName()).append("</p></td>");
                        sb.append("<td><p>").append(entry.getHttpMethod().toString()).append("</p></td>");
                        sb.append("<td><p>").append(entry.getTypePath().toString()).append("</p></td>");
                        sb.append("<td><p>").append(entry.getUri()).append("</p></td>");
                        sb.append("</tr>");
                        if (entry.getListEntryPathData() != null && !entry.getListEntryPathData().isEmpty()) {
                        	sb.append("<tr>");
                        	sb.append("<td colspan=\"5\">");
                        	sb.append("<table class=\"gridtable2\">");
                            sb.append("<tr><td>Key</td><td>Value</td><td>Type</td></tr>");
                            for (EntryPathParam param : entry.getListEntryPathData()) {
                                sb.append("<tr>");
                                sb.append("<td>").append(param.getKey()).append("</td>");
                                sb.append("<td>").append(param.getValue()).append("</td>");
                                sb.append("<td>").append(param.getTypeParam().toString()).append("</td>");
                                sb.append("</tr>");
                            }
                            sb.append("</table>");
                            sb.append("</td>");
                            sb.append("</tr>");
                        
                        }
                    }
                    sb.append("</table>");
                    sb.append("</div>");
                } 
            
            }
        }
        sb.append("</body></html>");
        return sb.toString();
    }

}
