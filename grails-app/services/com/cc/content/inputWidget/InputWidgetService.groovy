/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

class InputWidgetService {

    InputWidgetService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    InputWidget create(params) {
        InputWidget inputWidgetInstance = new InputWidget(params)
        inputWidgetInstance.save()
        if(inputWidgetInstance.hasErrors()) {
            log.warn "Error saving inputWidget instance: $inputWidgetInstance.errors"
        }
        return inputWidgetInstance
    }
}