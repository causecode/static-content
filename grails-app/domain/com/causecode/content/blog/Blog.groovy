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
import groovy.transform.EqualsAndHashCode

/**
 * Used to store blogs. These domain extends Content implements taggable(Taggable Plugin Required).
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
@EqualsAndHashCode
@SuppressWarnings('UnnecessaryTransientModifier')
class Blog extends Content implements Taggable {

    transient numberOfComments
    transient blogImgSrc

    UFile blogImg
    BlogContentType contentType

    static constraints = {
        blogImg nullable: true
        contentType nullable: true
    }

    static final String UFILE_GROUP = 'blogImg'

    @Override
    String toString() {
        return "Blog ($title)($contentType)"
    }
}

@SuppressWarnings('GrailsDomainHasEquals')
enum BlogContentType {
    TINYMCE(1),
    MARKDOWN(2),
    RAWCONTENT(3)
    final int id
    BlogContentType(int id) {
        this.id = id
    }

    @Override
    String toString() {
        return "${this.name()}($id)"
    }
}
