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

/**
 * Used to store blogs. This domain extends Content implements taggable(Taggable Plugin Required).
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
class Blog extends Content implements Taggable {

}