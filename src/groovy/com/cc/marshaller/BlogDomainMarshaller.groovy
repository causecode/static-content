/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.marshaller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller
import org.codehaus.groovy.grails.web.json.JSONWriter
import com.cc.content.blog.Blog

class BlogDomainMarshaller implements ObjectMarshaller<JSON> {

    @Override
    boolean supports(Object object) {
        return object instanceof Blog
    }

    @Override
    void marshalObject(Object object, JSON converter) throws ConverterException {
        Blog blogInstance = object as Blog
        String author = blogInstance.resolveAuthor()
        JSONWriter writer = converter.getWriter()

        writer.object()

        writer.key("id")
                .value(blogInstance.id)
        writer.key("title")
                .value(blogInstance.title)
        writer.key("subTitle")
                .value(blogInstance.subTitle)
        writer.key("body")
                .value(blogInstance.body)
        writer.key("author")
                .value(author)
        writer.key("publish")
                .value(blogInstance.publish)
        writer.key("blogImgSrc")
                .value(blogInstance.blogImgSrc)
        writer.key("dateCreated")
        converter.convertAnother(blogInstance.dateCreated)
        writer.key("lastUpdated")
        converter.convertAnother(blogInstance.lastUpdated)

        writer.endObject()
    }

}