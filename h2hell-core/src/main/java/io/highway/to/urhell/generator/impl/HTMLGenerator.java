package io.highway.to.urhell.generator.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.service.LeechService;

import java.util.Collection;

/**
 * Be careful, code 3v1l and D1rty
 */
public class HTMLGenerator implements TheJack {

    public String generatePage(Collection<LeechService> collectionService) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset=\"utf-8\"></head><style type=\"text/css\">body{ font-size:18px; color:#FFF; }");
        sb.append(".classh2h {text-shadow:5px 1px 11px #1a45f0}.classname { border:solid 1px #2d2d2d;  text-align:center; background:#575757; padding:50px 10px 20px 20px;  -moz-border-radius: 5px;  -webkit-border-radius: 5px; border-radius: 5px;}table.gridtable {	font-family: verdana,arial,sans-serif;	font-size:11px;	color:#333333;	border-width: 1px;	border-color: #666666;	border-collapse: collapse;} table.gridtable tr{	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #dedede ;} table.gridtable td {	border-width: 1px;	padding: 8px;	border-style: solid;	border-color: #666666;	background-color: #ffffff;}</style><body>)");

        if (collectionService != null) {
            int i = 0;
            for (LeechService leech : collectionService) {
                FrameworkInformations fwk = leech.getFrameworkInformations();

                sb.append("<div id=\"gibson" + i + "\" class=\"classname\">");
                sb.append("<ul>");
                sb.append("<p>Framework Informations " + fwk.getFrameworkName() + "</p><br>");
                sb.append("<p>Framework Version " + fwk.getVersion() + "</p><br>");
                sb.append("<p>Entries Path : </p><br>");
                if (fwk.getListEntryPath() != null && !fwk.getListEntryPath().isEmpty()) {
                    sb.append("<table class=\"gridtable\">");
                    sb.append("<tr><td>Method Name</td><td>Method Entry</td><td>Http Method</td><td>Type Path</td><td>Uri Path</td></tr>");
                    for (EntryPathData entry : fwk.getListEntryPath()) {
                        sb.append("<tr>");
                        sb.append("<td><p>" + entry.getMethodName() + "</p></p></td>");
                        sb.append("<td><p>" + entry.getMethodEntry() + "</p></td>");
                        sb.append("<td><p>" + entry.getHttpMethod().toString() + "</p></td>");
                        sb.append("<td><p>" + entry.getTypePath().toString() + "</p></td>");
                        sb.append("<td><p>" + entry.getUri() + "</p></td>");
                        if (entry.getListEntryPathData() != null && !entry.getListEntryPathData().isEmpty()) {
                            sb.append("<p> Liste Param Entry Path </p> <br>");
                            sb.append("<table>");
                            sb.append("<tr><td>Key</td><td>Value</td><td>Type</td></tr>");
                            for (EntryPathParam param : entry.getListEntryPathData()) {
                                sb.append("<tr>");
                                sb.append("<td>" + param.getKey() + "</td>");
                                sb.append("<td>" + param.getValue() + "</td>");
                                sb.append("<td>" + param.getTypeParam().toString() + "</td>");
                                sb.append("</tr>");
                            }
                            sb.append("</table>");
                        }
                        sb.append("</tr>");
                    }
                    sb.append("</table>");
                } else {
                    sb.append("<p class=\"classh2h\" No Framework detected</p>");
                }

                sb.append("</div>");
            }
        }
        sb.append("</body></html>");
        return sb.toString();
    }

}
