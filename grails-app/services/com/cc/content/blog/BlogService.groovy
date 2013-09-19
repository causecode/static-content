/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

class BlogService {

    void addTags(Blog blogInstance, String tags) {
        tags.tokenize(",").each {
            String tag = it.trim() // Taggable plugin make lower case by its own.
            if(!blogInstance.tags.contains(tag)) {
                blogInstance.addTag(tag)
            }
        }
    }

}