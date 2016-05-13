/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.blog

import grails.plugins.taggable.Taggable

import com.causecode.content.Content
import com.lucastex.grails.fileuploader.UFile

/**
 * Used to store blogs. These domain extends Content implements taggable(Taggable Plugin Required).
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
class Blog extends Content implements Taggable {
    UFile blogImg

    static constraints = {
        blogImg nullable:true
    }

    static final String UFILE_GROUP = "blogImg"
}
