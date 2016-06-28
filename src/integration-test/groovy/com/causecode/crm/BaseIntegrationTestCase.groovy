/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 *//*


package com.causecode.crm

import com.causecode.content.PageLayout
import com.causecode.content.blog.Blog
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.Comment
import com.causecode.content.page.Page
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import spock.lang.Specification

import java.util.List as JList

class BaseIntegrationTestCase extends Specification {

    private static Log log = LogFactory.getLog(this)

    def cleanup() {
        log.debug "Cleanup called."
    }

    def setup() {
        log.debug "Setup called."

        JList domains = [Page, Blog, PageLayout, Comment, BlogComment]
        domains.each {
            it.list()*.delete(flush: true)
        }
    }

    Map getContentParams(Integer i) {
        return [
            title: "Targeting Test $i Types and/or Phases", 
            author: "Test User",
            subTitle: "To execute the JUnit integration test $i",
            body: "Grails organises tests by phase and by type. The state of the Grails application."]
    }
}
*/
