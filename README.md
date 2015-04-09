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

## Required Modules

1. **validation** in all forms,
2. **prettyprint** for blog show page.


## Development

## Adding Angular Files

To install angular based project copy following configurations from plugin **GruntFile.js** to app **GruntFile.js**.

> Copy symlinks anf contentInstall task to app **GruntFile.js**.
> Check grunt plugin dependencies Ex. angular-ui-tinymce plugin. Add them if missing.
> Check Application module dependency injection from **app.js** file.
> Then run command: `grunt contentInstall` to install templates.
> Include all Angular Models and Controllers in application **index.html** file.


## Architecture Overview

### Domains

- **Content** :
A generic domain used to store generic fields for storing any content.
- **Meta** : 
Used for storing meta tags for contents.
- **ContentMeta** : 
Join class provides meta tags for content.
- **Blog** : 
These domain is type of Content implements taggable(`Taggable` Plugin Required).
- **Comment** : 
Used for storing comment contains reference for nested comment.
- **BlogComment** : 
Join class provides comments for Blog.
- **FAQ** : 
These domain is type of Content used to store FAQs.
- **News** : 
These domain is type of Content used to store News.
- **PageLayout** : 
Generic domain stores page layout ,used for pages.
- **Page** : 
These domain is type of Content used to store Page contents and also stores page layout.
- **InputWidget** : 
A generic domain used to store all information of selected type of input widgets with validations required.
- **MenuItem** : 
Used to render menu items within menu , also support nested menu items.
- **Menu** : 
Used for rendering menu bar with menu items.
- **ContentRevision** : 
Used to support old content reference if required.
- **PageRevision** : 
These domain in type of ContentRevision.

### Controllers

- **Blog**, **PageLayout**, **Page**, **News**, **FAQ**, **InputWidget**, **Menu** : 
Provides default CRUD end point for Content Manager.
- **Meta** : 
Provides end point to `delete` any meta information of content for Content Manager.
- **Blog** : 
Provides End point for `comment` on any blog for all.
- **Comment** : 
Provides end point for `delete` comment for Content Manager.
- **FAQ** : 
Provides default CRUD end point for Content Manager.
- **News** : 
Provides end point to `link` news information of type content for Content Manager.
- **MenuItem** : 
Provides end point to `reorder` menu items for Content Manager.
- **ContentRevision** : 
Provides end point to `show`, `load` and `delete` content revision for Content Manager.

### Utility Classes

- **ControllerShorthand** : 
Java Class Provides annotation used to mark any controller's short hand value for creating link. 
Example: 'c' for content controller
- **SanitizedTitle** : 
Java Class Provides annotation used to mark a domain field as sanitizable value used in generating link.
- **ContentService** : 
    - Provides methods which check is current logged-in user have authority to view the current `Content` instance.
    - Provides method which resolves given `Content` author.
    - Provides method which checks current logged-in user has role Content Manager or not.
    - Provides create `ContentRevision` method.
    - Provides create link method which return user friendly URL on basis of attributes passed.
- **FriendlyUrlService** : 
    - Provides method which transforms the text passed as an argument to a text without spaces.
    - Provides method which converts all accent characters to ASCII characters.
- **MenuItemService** : 
    - Provides methods which reorders menu items.
    - Provides methods which fixes ordering of menu items to default.

## Detailed Documentation

### Changes in URLMappings.groovy file:

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

### Use of Sanitized Title Annotation

This annotation is used to mark a domain field as sanitizable value used in generating link.
**Example**: URLMapping for User controller will use fullName field to generate link as mentioned in above URLMapping 
section. ```"/user/{actionName}/{identity}?/{fullName}?"```

```
Class User {
    String firstName
    String lastName
    @SanitizedTitle
    String fullName
    String email
    ...
}
```

### Use of Controller Shorthand Annotation

URLMapping for User controller will use controller's short hand value to generate link as mentioned in above URLMapping section.
**Example**: Link ```"/u/{actionName}/{identity}?/{fullName}?"```
```
@ControllerShorthand(value = "u")
Class UserController {
    ...
}
```

### Use of Menu-MenuItems Feature

- Create `Menu` with the help of CRUD screens.
- Edit `Menu` and add menu items to menu.
- `MenuItems` position can be changed after creating.
- Use Menu TagLib to render menu item as follows

```
<com:menu id="{id}" menuCss="{menuCss}" menuTemplate="{menuTemplate}" />
Options:
    - id: (Required) Menu Id.
    - menuCss: (Optional) Custom menu CSS.
    - menuTemplate: (Optional) Custom menu template.
```

### Use of Blog Feature

- Create `Blog` with the help of CRUD screens.
- `Blog` provides comment end point , Use `Comment` and nested comment tagLib for rendering comments as follows:
```
<content:comment commentInstance="{commentInstance}" />
<content:nestedComment commentInstance="{commentInstance}" />
Options:
    - commentInstance: (Required).
```

- Provides search link tagLib which generates link to blog page.
```
<a href="<content:searchLink id='${instanceId }' />">
    Instance Title
</a>
Options:
    - id: (Required) Blog Id.
```

NOTE : Secure domains
