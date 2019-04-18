package com.causecode.content.faq

import com.causecode.content.Content
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * These domain extends Content used to store FAQs.
 * @author Shashank Agrawal
 *
 */
@ToString(includes = ['id'], includePackage = false)
@EqualsAndHashCode
class FAQ extends Content {

    static constraints = {
    }
}
