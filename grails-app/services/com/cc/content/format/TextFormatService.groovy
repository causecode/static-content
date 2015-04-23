/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.format

import grails.plugin.springsecurity.SpringSecurityUtils

class TextFormatService {

     /**
     * To generate available Formats list
     *
     */
    
    List getApplicableFormats() {
        List formats = []

        TextFormat.list().each { textFormatInstance ->
            if (SpringSecurityUtils.ifAnyGranted(textFormatInstance.roles)) {
                formats.add(textFormatInstance)
            }
        }
        return formats
    }

}
