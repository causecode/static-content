/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

/**
 * This class is used for mapping requests to controllers and views.
 *
 * Note: Removing implementation of "/blog/byTag/$tag" closure as its no being used, kindly refer previous version if
 *       needed.
 */
class ContentUrlMappings {

    static mappings = {
        "/blog/$id/$sanitizedTitle" {
            controller = 'blog'
            action = 'show'
            constraints {
                id validator: {  //Used to distinguish other Url like '/blog/edit/1'
                    it.isLong()
                }
            }
        }

        "/c/$id/$sanitizedTitle" {
            controller = 'page'
            action = 'show'
            constraints {
                id validator: {  //Used to distinguish other Url like '/page/edit/1'
                    it.isLong()
                }
            }
        }

        "/blog" (controller: 'blog', action: 'list')
        "/blogs" (controller: 'blog', action: 'list')
    }
}
