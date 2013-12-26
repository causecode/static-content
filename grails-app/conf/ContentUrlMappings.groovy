/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

class ContentUrlMappings {

    static mappings = {
        "/blog/$id/$sanitizedTitle" {
            controller = "blog"
            action = "show"
            constraints {
                id validator: {  //Used to distinguish other Url like '/blog/edit/1'
                    it.isLong()
                }
            }
        }
        "/page/$id/$sanitizedTitle" {
            controller = "page"
            action = "show"
            constraints {
                id validator: {  //Used to distinguish other Url like '/page/edit/1'
                    it.isLong()
                }
            }
        }
        "/blog" (controller: "blog", action: "list")
        "/blogs" (controller: "blog", action: "list")

        /*"/blog/byTag/$tag" {      // Not working
            controller = "blog"
            action = "list"
            constraints {
                tag validator: {  //Used to distinguish other Url like '/blog/edit/1'
                    !it.isNumber()
                }
            }
        }*/
    }

}
