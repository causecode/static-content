/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.page

import com.causecode.content.ContentRevision
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * These domain extends ContentRevision.
 * @author Shashank Agrawal
 *
 */
@ToString(includes = ['id'], includePackage = false)
@EqualsAndHashCode
class PageRevision extends ContentRevision {
}
