/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.marshaller

import grails.converters.JSON

import org.grails.web.converters.exceptions.ConverterException
import org.grails.web.converters.marshaller.ObjectMarshaller
import org.grails.web.json.JSONWriter
import com.causecode.content.blog.Blog

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
                .value(blogInstance.blogImg?.path)
        writer.key("dateCreated")
        converter.convertAnother(blogInstance.dateCreated)
        writer.key("lastUpdated")
        converter.convertAnother(blogInstance.lastUpdated)
        writer.key("publishedDate")
        converter.convertAnother(blogInstance.publishedDate)
        writer.key("type")
                .value(blogInstance.contentType?.name())

        writer.endObject()
    }

}