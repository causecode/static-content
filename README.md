# Content Plugin

A plugin used to manage contents like static pages, menus etc. at one place.

## ChnageLog

### Version 1.9.1

#### Breaking change

1. Changed package name of *Menu, MenuItem, Blog, Comment, BlogComment*.

#### Database changes

1. Added new InputWidget domain,
2. Change in structure of menu & menu item.

#### Improvements

1. Bootstrap upgraded to 3.0,
2. Blog/comment UI & code restructured,
3. Added meta tag property in blog show page.

## Configurations for this plugin:

    cc.plugins.content.authorProperty = "username"
    cc.plugins.content.contentManagerRole = "ROLE_MANAGER_CONTENT"
    cc.plugins.content.default.layout.name = "main"
    cc.plugins.content.RoleForMenuItem = ["ROLE_ADMIN","ROLE_CONTENT_MANAGER","ROLE_EMPLOYEE","ROLE_USER"]

## Access protect controllers: (according to application need)

    '/blog/**' : ['ROLE_MANAGER_ORGS'],
    '/pageLayout/**' : ['ROLE_MANAGER_ORGS'],
    '/faq/**' : ['ROLE_MANAGER_ORGS'],
    '/news/**' : ['ROLE_MANAGER_ORGS'],
    '/menu/**' : ['ROLE_MANAGER_ORGS'],
    '/menuItem/**' : ['ROLE_MANAGER_ORGS'],
    '/page/show/**' : ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/page/**' : ['ROLE_MANAGER_ORGS'],

## Changes in URLMappings.groovy file:

Remove default url mapping.

    "/$controller/$action?/$id?"{
        constraints {
            // apply constraints here
        }
    }

Add this:

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

Also add a private method:

    static private String resolveURL(def params, ctx, String type) {
        String requestedController = params.controllerName
        String resolvedController = params.controllerName
        if(!shorthandControllers) {
            shorthandControllers = ctx.contentService.getShorthandAnnotatedControllers()
        }
        shorthandControllers.each { controllername, shorthand ->
            if(shorthand == requestedController) {
                resolvedController = controllername
            }
        }
        if(type == "controller") {
            return resolvedController
        }
        if(type == "action") {
            if(requestedController == resolvedController)   // When no shorthand for controller found
                return params.actionName
            else
                return "show"
        }
        if(type == "id") {
            if(requestedController != resolvedController && params.actionName?.isNumber()) {
                return params.actionName
            }
            return params.identity ?: params.id
        }
        if(type == "shortened") {
            if(requestedController == resolvedController)   // When no shorthand for controller found
                return false
            else
                return true
        }
    }


And add a private field as:

    static Map shorthandControllers = [:]

NOTE : Secure domains

