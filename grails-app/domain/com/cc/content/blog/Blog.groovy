/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import org.grails.taggable.Taggable

import com.cc.content.Content
import com.lucastex.grails.fileuploader.UFile

/**
 * Used to store blogs. These domain extends Content implements taggable(Taggable Plugin Required).
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
class Blog extends Content implements Taggable {
    UFile blogImg
    BlogContentType contentType

    static constraints = {
        blogImg nullable:true
        contentType nullable:true
    }

    static final String UFILE_GROUP = "blogImg"
}

enum BlogContentType {
    TINYMCE(1),
    MARKDOWN(2),
    RAWCONTENT(3)
    
    final int id
    BlogContentType(int id) {
        this.id = id
    }
    int getValue() {
        return id
    }
}