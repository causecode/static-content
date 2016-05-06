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
import org.grails.web.converters.marshaller.NameAwareMarshaller
import org.grails.web.converters.marshaller.ObjectMarshaller
import org.grails.web.json.JSONWriter

import com.causecode.content.navigation.MenuItem

class MenuItemMarshaller implements ObjectMarshaller<JSON>, NameAwareMarshaller{

    @Override
    String getElementName(Object o) {
        return 'menuItem'
    }

    @Override
    boolean supports(Object object) {
        return object instanceof  MenuItem
    }

    @Override
    void marshalObject(Object object, JSON converter) throws ConverterException {
        MenuItem menuItemInstance = object as MenuItem
        JSONWriter writer = converter.getWriter()

        writer.object()

        writer.key("id")
                .value(menuItemInstance.id)
        writer.key("version")
                .value(menuItemInstance.version)
        writer.key("title")
                .value(menuItemInstance.title)
        writer.key("url")
                .value(menuItemInstance.url)
        writer.key("parent")
                .value(menuItemInstance.parent)

        writer.key("roles")
        converter.convertAnother(menuItemInstance.roles?.tokenize(",") ?: [])
        writer.key("childItems")
        writer.array()
            menuItemInstance.childItems.each { childMenuItem ->
                converter.convertAnother(childMenuItem)
            }
        writer.endArray()

        writer.endObject()
    }
}
