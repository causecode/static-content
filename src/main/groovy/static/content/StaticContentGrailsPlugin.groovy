/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package static.content

import java.lang.annotation.Annotation

import grails.plugin.springsecurity.SpringSecurityUtils

import com.causecode.annotation.shorthand.ControllerShorthand
import com.causecode.content.ContentService

class ContentGrailsPlugin {

    def version = "2.4.4"
    def groupId = "com.cc.plugins"
    def grailsVersion = "2.1 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        ".gitmodules",
        "src/templates/*",
        "web-app/ng/app/app.js",
        "web-app/ng/app/index.html",
        "web-app/ng/Gruntfile.js",
        "web-app/ng/bower.json",
        "web-app/ng/package.json"
    ]

    def title = "Content Plugin"
    def author = "CauseCode"
    def authorEmail = ""
    def description = '''\
A plugin used to manage contents like static pages, menus etc. at one place.
Also provides shortened and user friendly urls.
'''
    def documentation = "http://grails.org/plugin/content"

    //    def license = "APACHE"

    def organization = [ name: "Causecode Technologies Pvt. Ltd.", url: "http://causecode.com" ]

    def developers = [ [ name: "Shashank Agrawal", email: "shashank.agrawal@causecode.com" ]]

    def issueManagement = [ system: "BITBUCKET", url: "https://bitbucket.org/causecode/static-content/issues" ]

    def scm = [ url: "https://bitbucket.org/causecode/static-content" ]

    def watchedResources = "file:./grails-app/services/*ContentService.groovy"

    def doWithDynamicMethods = { ctx ->
        println "\nConfiguring content plugin ..."
        addServiceMethod(ctx)
        println "... finished configuring content plugin\n"
    }

    def onChange = { event ->
        if (event.source && application.isServiceClass(event.source)) {
            addServiceMethod(event.ctx)
        }
    }

    private void addServiceMethod(ctx) {
        def application = ctx.grailsApplication
        MetaClass metaClassInstance = application.getServiceClass(ContentService.class.name).metaClass

        if (!metaClassInstance.respondsTo(null, 'getShorthandAnnotatedControllers')) {
            Map shorthandAnnotatedControllerMap = [:]
            for(controller in application.controllerClasses) {
                Annotation controllerAnnotation = controller.clazz.getAnnotation(ControllerShorthand.class)
                if(controllerAnnotation) {  // Searching for shorthand for grails controller
                    String actualName = controller.name
                    String camelCaseName = actualName.replace(actualName.charAt(0), actualName.charAt(0).toLowerCase())
                    shorthandAnnotatedControllerMap.put(camelCaseName, controllerAnnotation.value())
                }
            }
            metaClassInstance.getShorthandAnnotatedControllers {
                return shorthandAnnotatedControllerMap
            }
            metaClassInstance.getRoleClass {
                application.getDomainClass(SpringSecurityUtils.securityConfig.authority.className).clazz
            }
            metaClassInstance.getAuthorClass {
                application.getDomainClass(SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
            }
            println "\nShorthand annotated controller map: $shorthandAnnotatedControllerMap"
            println ""
        }
    }

}
