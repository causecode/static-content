/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
import com.causecode.marshaller.BlogDomainMarshaller
import com.causecode.marshaller.MenuItemMarshaller
import grails.converters.JSON
import grails.core.GrailsApplication

/**
 * The class is used for App initialization settings.
 */
class ContentBootStrap {

    GrailsApplication grailsApplication

    def init = { servletContext ->
        log.debug 'Content Bootstrap started executing ..'

        JSON.registerObjectMarshaller(new BlogDomainMarshaller())
        JSON.registerObjectMarshaller(new MenuItemMarshaller())

        log.debug 'Content Bootstrap finished executing.'
    }
}
