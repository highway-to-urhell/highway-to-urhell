/*
 * $Id: PrepareOptionsAction.java 471754 2006-11-06 14:55:09Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package examples.options;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 * Perform any tasks and setup any data that
 * must be prepared before the form is displayed.
 *
 * @version $Rev: 471754 $ $Date: 2006-11-06 08:55:09 -0600 (Mon, 06 Nov 2006) $
 */
public class PrepareOptionsAction extends Action {

    // ------------------------------------------------------------ Constructors

    /**
     * Constructor for PrepareOptionsAction.
     */
    public PrepareOptionsAction() {
        super();
    }

    // ---------------------------------------------------------- Action Methods

    /**
     * Process the request and return an <code>ActionForward</code> instance
     * describing where and how control should be forwarded, or
     * <code>null</code>if the response has already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if an exception occurs
     *
     * @return the ActionForward to forward control to
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception {

        /* An array */
        String[] fruits =
            {
                "Strawberry",
                "Apple",
                "Orange",
                "Pear",
                "Mango",
                "Banana",
                "Pineapple" };
        request.setAttribute("fruits", fruits);

        /* Two arrays - one for labels and one for values */
        String[] colors =
            { "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet" };
        request.setAttribute("colors", colors);

        String[] colorCodes =
            {
                "#FF0000",
                "#FFA500",
                "#FFFF00",
                "#00FF00",
                "#0000FF",
                "#4B0082",
                "#EE82EE" };
        request.setAttribute("colorCodes", colorCodes);

        /* A Collection */
        ArrayList colorList = new ArrayList();
        colorList.add("Red");
        colorList.add("Orange");
        colorList.add("Yellow");
        colorList.add("Green");
        colorList.add("Blue");
        colorList.add("Indigo");
        colorList.add("Violet");
        request.setAttribute("colorCollection", colorList);

        /* A Collection of LabelValue beans */
        ArrayList days = new ArrayList();
        days.add(new LabelValueBean("Monday", "1"));
        days.add(new LabelValueBean("Tuesday", "2"));
        days.add(new LabelValueBean("Wednesday", "3"));
        days.add(new LabelValueBean("Thursday", "4"));
        days.add(new LabelValueBean("Friday", "5"));
        days.add(new LabelValueBean("Saturday", "6"));
        days.add(new LabelValueBean("Sunday", "7"));
        request.setAttribute("days", days);

        /* Collection of custom beans */
        ArrayList books = new ArrayList();
        books.add(new BookBean("0596003285", "Programming Jakarta Struts"));
        books.add(new BookBean("1930110502", "Struts in Action"));
        books.add(
            new BookBean("1861007817", "Professional Struts Applications"));
        books.add(new BookBean("0672324725", "Struts Kick Start"));
        books.add(new BookBean("0471213020", "Mastering Jakarta Struts"));
        books.add(new BookBean("1558608621", "The Struts Framework"));
        books.add(new BookBean("0971661901", "Struts Fast Track"));
        request.setAttribute("books", books);

        /* A Map
         *
         * Note: We are using a HashMap which is unsorted - the resulting
         * options could appear in any order. If you want to your options to be
         * in a particular order you should you a SortedMap implementation such
         * as the TreeMap. This behaviour is a feature of the standard Map
         * implementaions and nothing to to with Struts tags.
         *
         * Also, we've used an Integer as the key to demonstrate that the
         * <html:options> and <html:optionsCollection> tags do not require
         * String values for the label and values. If they are passed an object
         * other than a String, they will automatically call the toString()
         * method and use the result.
         */
        HashMap animals = new HashMap();
        animals.put(new Integer(1), "Cat");
        animals.put(new Integer(2), "Dog");
        animals.put(new Integer(3), "Horse");
        animals.put(new Integer(4), "Rabbit");
        animals.put(new Integer(5), "Goldfish");
        request.setAttribute("animals", animals);

        // Forward to form page
        return mapping.findForward("success");

    }

}
