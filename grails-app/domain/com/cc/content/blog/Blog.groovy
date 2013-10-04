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

class Blog extends Content implements Taggable {

    String searchLink() {
        "/blog/$id/$sanitizedTitle"
    }

}