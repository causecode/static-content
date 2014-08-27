/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

import com.cc.marshaller.BlogDomainMarshaller
import grails.converters.JSON

class ContentBootStrap {

    def grailsApplication

    def init = { servletContext ->
        log.debug "Content Bootstrap started executing .."

        JSON.registerObjectMarshaller(new BlogDomainMarshaller())

        log.debug "Content Bootstrap finished executing."
    }

}