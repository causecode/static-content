/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

/**
 * This class is used for mapping requests to controllers and views.
 */
class ContentUrlMappings {

    private static final String SHOW = 'show'
    private static final String BLOG = 'blog'
    private static final String LIST = 'list'

    static mappings = {
        "/blog/$id/$sanitizedTitle" {
            controller = BLOG
            action = SHOW
            constraints {
                id validator: {  //Used to distinguish other Url like '/blog/edit/1'
                    it.isLong()
                }
            }
        }
        "/c/$id/$sanitizedTitle" {
            controller = 'page'
            action = SHOW
            constraints {
                id validator: {  //Used to distinguish other Url like '/page/edit/1'
                    it.isLong()
                }
            }
        }
        "/blog" (controller: BLOG, action: LIST)
        "/blogs" (controller: BLOG, action: LIST)

        /*"/blog/byTag/$tag" {      // Not working
            controller = 'blog'
            action = 'list'
            constraints {
                tag validator: {  //Used to distinguish other Url like '/blog/edit/1'
                    !it.isNumber()
                }
            }
        }*/
    }
}
