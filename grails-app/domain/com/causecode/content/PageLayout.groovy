/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import groovy.transform.EqualsAndHashCode

/**
 * Generic domain stores page layout ,used for pages.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Bharti Nagdev
 *
 */
@EqualsAndHashCode
class PageLayout {

    String layoutName

    static constraints = {
    }

    static mapping = {
        table 'cc_content_page_layout'
    }

    @Override
    String toString() {
        layoutName
    }

}
