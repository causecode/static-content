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
    }

}
