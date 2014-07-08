# Content Plugin

## Architecture Overview

```
Content = A generic domain used to store generic fields for storing any content.
Meta = Used for storing meta tags for contents.
ContentMeta = Join class provides meta tags for content.
Blog = These domain is type of Content implements taggable(Taggable Plugin Required).
Comment = Used for storing comment contains reference for nested comment.
BlogComment = Join class provides comments for Blog.
FAQ = These domain is type of Content used to store FAQs.
News = These domain is type of Content used to store News.
PageLayout = Generic domain stores page layout ,used for pages.
Page = These domain is type of Content used to store Page contents and also stores page layout.
InputWidget = A generic domain used to store all information of selected type of input widgets with validations required.
MenuItem = Used to render menu items within menu , also support nested menu items.
Menu = used for rendering menu bar with menu items.
ContentRevision = Used to support old content reference if required.
PageRevision = These domain in type of ContentRevision.
```

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

## Required Modules

1. **validation** in all forms,
2. **prettyprint** for blog show page.

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
