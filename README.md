# Content Plugin

A plugin used to manage contents like static pages, menus etc. at one place.

Configurations for this plugin:

cc.plugins.content.authorProperty = "username"
cc.plugins.content.contentManagerRole = "ROLE_MANAGER_CONTENT"
cc.plugins.content.default.layout.name = "main"

Changes in URLMappings.groovy file:

Remove default url mapping.

Add this: 
<code>
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
</code>
Also add a private method: 
<code>
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
            if(requestedController == resolvedController)   // When no shorthand for controller found
                return params.identity
            else
                return params.actionName
        }
        if(type == "shortened") {
            if(requestedController == resolvedController)   // When no shorthand for controller found
                return false
            else
                return true
        }
    }
</code>
And add a private field as
<code>
    static Map shorthandControllers = [:]
</code>