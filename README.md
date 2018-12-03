# Content Plugin

A plugin used to manage contents like static pages, menus etc. at one place.

## ChnageLog

See ChangeLog.md file.

## Configurations for this plugin:

```
cc.plugins.content.authorProperty = "username"
cc.plugins.content.contentManagerRole = "ROLE_MANAGER_CONTENT"
cc.plugins.content.default.layout.name = "main"
cc.plugins.content.RoleForMenuItem = ["ROLE_ADMIN","ROLE_CONTENT_MANAGER","ROLE_EMPLOYEE","ROLE_USER"]
```

## Access protect controllers: (according to application need)

```
#!groovy
    '/blog/**' : ['ROLE_MANAGER_ORGS'],
    '/comment/**' : ['ROLE_ADMIN'],
    '/pageLayout/**' : ['ROLE_MANAGER_ORGS'],
    '/faq/**' : ['ROLE_MANAGER_ORGS'],
    '/news/**' : ['ROLE_MANAGER_ORGS'],
    '/menu/**' : ['ROLE_MANAGER_ORGS'],
    '/menuItem/**' : ['ROLE_MANAGER_ORGS'],
    '/page/show/**' : ['IS_AUTHENTICATED_ANONYMOUSLY'],
    '/page/**' : ['ROLE_MANAGER_ORGS'],
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

## Publish plugin

### Local maven
- Run following commands to publish plugin to local maven repository.
```
grails clean
grails compile
grails maven-install
```

Note: Make sure you are using Grails version 2.3.5 and Java version 1.7

### Nexus repository
- Run the following commands to publish the plugin to nexus.
 ```
 grails clean
 grails compile
 grails publish-plugin
 ```

 ### Credentials
 Create a `settings.groovy` file in `~/.grails/` directory if not present and add the following config:
 ```
 // use username and password of nexus repo
 grails.project.repos.ccRepo.username = "foo"
 grails.project.repos.ccRepo.password = "bar"

 grails.project.dependency.authentication = {
   credentials {
     id = "ccRepo"
     username = "foo"
     password = "bar"
   }
 }
 ```

