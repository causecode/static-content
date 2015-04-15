/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

class UrlMappings {

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
        "/c/$id/$sanitizedTitle" {
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
        
        "/$controllerName/$actionName?/$identity?/$sanitizedTitle?" {
            controller = {
                return resolveURL(params, applicationContext, "controller")
            }
            action = {
                return resolveURL(params, applicationContext, "action")
            }
            id = {
                return resolveURL(params, applicationContext, "id")
            }
            shortened = {
                return resolveURL(params, applicationContext, "shortened")
            }
        }
    }

}
