/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

/**
 * @author Shashank Agrawal
 *
 */
class ContentFilters {

    /**
     * Dependency injection for the contentService
     */
    def contentService

    /**
     * Filter to check if current user have authority to view the page or blog instance.
     */
    def filters = {
        all(uri:"/**") {
            before = {
                return true
            }
        }
    }

}