/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

import java.lang.annotation.Annotation

import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.ContentService

class ContentGrailsPlugin {

    def version = "1.9"
    def grailsVersion = "2.1 > *"
    def groupId = "com.cc.plugins"
    def dependsOn = ["hibernate": "2.1 > *"]
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Content Plugin"
    def author = "CauseCode"
    def authorEmail = ""
    def description = '''\
A plugin used to manage contents like static pages, menus etc. at one place.
Also provides shortened and user friendly urls.
'''
    def documentation = "http://grails.org/plugin/content"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    //    def license = "APACHE"

    def organization = [ name: "CauseCode Technologies Pvt. Ltd.", url: "http://causecode.com" ]

    //    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    //    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
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
        MetaClass metaClassInstance = application.getServiceClass(ContentService.class.name)?.metaClass
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
            println "\nShorthand annotated controller map: $shorthandAnnotatedControllerMap"
            println ""
            metaClassInstance.getShorthandAnnotatedControllers {
                return shorthandAnnotatedControllerMap
            }
        }
    }

}