/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class ContentTagLib {

    static namespace = "content"

    def contentService

    /**
     * Used when current user have content role
     * described by cc.plugins.content.contentMangerRole
     */
    def canEdit = { attrs, body ->
        if(contentService.canEdit()) {
            out << body()
        }
    }

}