/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.iframe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {

    private String url;

    public Scraper(String url) {
        this.url = url;
    }

    public String scrape() throws IOException {
        URL u = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));

        String inputLine;
        StringBuffer b = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            b.append(inputLine);
        }

        in.close();

        String base = getBase(url);
        String result;

        if (base != null) {
            result = setBase(b.toString(), base);
        } else {
            result = b.toString();
        }

        return result;
    }

    private static String getBase(String url) {
        try {
            URL u = new URL(url);
            StringBuffer b = new StringBuffer();
            b.append(u.getProtocol());
            b.append("://");
            b.append(u.getHost());
            if (u.getPort() != -1) {
                b.append(":");
                b.append(u.getPort());
            }
            System.out.println(b.toString());
            return b.toString();
        } catch (MalformedURLException mfue) {
            return null;
        }
    }

    private static String setBase(String content, String base) {
        // remove base tag if it exists
        Pattern basePattern = Pattern.compile("<base.*?/>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher baseMatcher = basePattern.matcher(content);
        if (baseMatcher.find()) {
            // base is already set
            System.out.println("found base");
            return content;
        }
        int baseIndex = content.indexOf("</head>");

        String newContent = content.substring(0, baseIndex);
        newContent = newContent + "<base href=\"" + base + "\"/></head>" + content.substring(baseIndex + 7);
        // add new base tag
        // Pattern headPattern = Pattern.compile("<head.*>(.*?)</head>",
        // Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        // Matcher headMatcher = headPattern.matcher(content);
        //
        // if (headMatcher.find()) {
        // StringBuffer newHead = new StringBuffer();
        // newHead.append("<head>\n");
        // newHead.append("<base href=\"");
        // newHead.append(base);
        // newHead.append("\" />\n");
        // newHead.append(headMatcher.group(1));
        // newHead.append("\n");
        // newHead.append("</head>\n");
        // System.out.println(newHead.toString());
        // content = headMatcher.replaceFirst(newHead.toString());
        // }

        return newContent;
    }
}