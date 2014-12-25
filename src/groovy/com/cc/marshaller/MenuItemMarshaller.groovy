package com.cc.marshaller

import grails.converters.JSON

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.converters.marshaller.NameAwareMarshaller
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller
import org.codehaus.groovy.grails.web.json.JSONWriter

import com.cc.content.navigation.MenuItem

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
        JSONWriter jsonWriter = converter.getWriter()

        jsonWriter.object()
        jsonWriter.key("id")
                .value(menuItemInstance.id)
        jsonWriter.key("version")
                .value(menuItemInstance.version)
        jsonWriter.key("roles")
        converter.convertAnother(menuItemInstance.roles?.tokenize(",") ?: [])
        jsonWriter.key("title")
                .value(menuItemInstance.title)
        jsonWriter.key("url")
                .value(menuItemInstance.url)

        jsonWriter.key("childItems")
        jsonWriter.array()
        menuItemInstance.childItems.each { childMenuItem ->
            converter.convertAnother(childMenuItem)
        }
        jsonWriter.endArray()

        jsonWriter.key("parent")
                .value(menuItemInstance.parent)
        jsonWriter.endObject()
    }
}