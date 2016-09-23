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
import groovy.transform.ToString

/**
 * These domain extends Content used to store FAQs.
 * @author Shashank Agrawal
 *
 */
@ToString
@EqualsAndHashCode
class FAQ extends Content {

    static constraints = {
    }
}
