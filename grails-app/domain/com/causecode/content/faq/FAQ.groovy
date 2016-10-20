/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.faq

import com.causecode.content.Content
import groovy.transform.EqualsAndHashCode

/**
 * These domain extends Content used to store FAQs.
 * @author Shashank Agrawal
 *
 */
@EqualsAndHashCode
class FAQ extends Content {

    static constraints = {
    }

    @Override
    String toString() {
        return "FAQ ($title)"
    }
}
