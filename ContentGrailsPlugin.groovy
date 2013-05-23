import java.lang.annotation.Annotation

import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.ContentService

/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

class ContentGrailsPlugin {

    def version = "1.7"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = ["hibernate": "2.1 > *"]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    //def loadAfter = ['hibernate']
    // TODO Fill in these fields
    def title = "Content Plugin" // Headline display name of the plugin
    def author = "CauseCode"
    def authorEmail = ""
    def description = '''\
A plugin used to manage contents like static pages, menus etc. at one place.
'''
    //def loadBefore = ['grails-plugin-url-mappings']

    def documentation = "http://grails.org/plugin/content"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    //    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    //    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    //    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    //    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    //    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def watchedResources = "file:./grails-app/services/*ContentService.groovy"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        println "\nConfiguring content plugin ..."
        addServiceMethod(ctx)
        println "... finished configuring content plugin\n"
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        if (event.source && application.isServiceClass(event.source)) {
            addServiceMethod(event.ctx)
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
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
            println "\nShorthand annotated controller map: $shorthandAnnotatedControllerMap \n"
            metaClassInstance.getShorthandAnnotatedControllers {
                return shorthandAnnotatedControllerMap
            }
        }
    }
}