/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class PageLayout {

    String layoutName

    static constraints = {
    }

    static mapping = {
        table "cc_content_page_layout"
    }

    @Override
    public String toString() {
        layoutName
    }

}