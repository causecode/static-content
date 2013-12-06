# Content Plugin

A plugin used to manage contents like static pages, menus etc. at one place.

## ChangeLog

See ChangeLog.md file.

## Configurations for this plugin:

```
#!groovy
cc.plugins.content.authorProperty = "username"
cc.plugins.content.blog.list.max = 10
cc.plugins.content.contentManagerRole = "ROLE_CONTENT_MANAGER,ROLE_EMPLOYEE"
cc.plugins.content.breadcrumbs.baseMap = ['/': 'Home']
```

## Changes in URLMappings.groovy file:

Remove default url mapping.

```
#!groovy
"/$controller/$action?/$id?"{
    constraints {
        // apply constraints here
    }
}
```

Add this:

```
#!groovy
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
```

Also add a private method:

```
#!groovy
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
```

And add a private field as:

```
static Map shorthandControllers = [:]
```

NOTE : Secure domains
