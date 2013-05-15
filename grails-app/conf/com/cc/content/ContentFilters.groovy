/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class ContentFilters {

    def contentService

    def filters = {
        all(controller: '*', action: 'show') {
            before = {
                List restrictedControllerList = ['page', 'blog']
                if(controllerName in restrictedControllerList && params.id) {
                    if(!contentService.isVisible(params.id)) {
                        redirect controller: controllerName, action: "list"
                        return false
                    }
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}