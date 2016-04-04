/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import grails.plugins.taggable.TagLink

class BlogService {

    List getAllTags() {
        List tagList = []
        Blog.allTags.each { tagName ->
            def tagListInstance = TagLink.withCriteria(uniqueResult: true) {
                createAlias('tag', 'tagInstance')
                projections {
                    countDistinct 'id'
                    property("tagInstance.name")
                }
                eq('type','blog')
                eq('tagInstance.name', tagName)
            }
            tagList.add(tagListInstance)
        }
        return tagList
    }

}