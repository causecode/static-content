/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.page

import com.cc.content.Content
import com.cc.content.PageLayout

/**
 * These domain extend Content used to store Page contents and also stores page layout.
 * @author Shashank Agrawal
 */
class Page extends Content {

    PageLayout pageLayout

    static constraints = {
        pageLayout nullable:true
    }

}