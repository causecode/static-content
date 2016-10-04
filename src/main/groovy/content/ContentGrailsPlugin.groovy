/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package content

import grails.core.GrailsApplication
import grails.plugins.Plugin

import java.lang.annotation.Annotation

/**
 * This is content grails plugin.
 */
class ContentGrailsPlugin extends Plugin {

    def version = '2.5.3'
    def groupId = 'com.cc.plugins'
    def grailsVersion = '3.1.4 > *'
    def pluginExcludes = [
        'grails-app/views/error.gsp',
        '.gitmodules',
        'src/main/templates/*',
        'src/main/webapp/ng/app/app.js',
        'src/main/webapp/ng/app/index.html',
        'src/main/webapp/ng/Gruntfile.js',
        'src/main/webapp/ng/bower.json',
        'src/main/webapp/ng/package.json'
    ]

    def title = 'Content Plugin'
    def author = 'CauseCode'
    def authorEmail = ''
    def description = '''A plugin used to manage contents like static pages, menus etc. at one place.
                         Also provides shortened and user friendly urls.'''
    def profiles = ['web']
    def documentation = 'http://grails.org/plugin/content'

    // def license = 'APACHE'

    def organization = [ name: 'Causecode Technologies Pvt. Ltd.', url: 'http://causecode.com' ]
    def developers = [ [ name: 'Shashank Agrawal', email: 'shashank.agrawal@causecode.com' ]]
    def issueManagement = [ system: 'BITBUCKET', url: 'https://bitbucket.org/causecode/static-content/issues' ]
    def scm = [ url: 'https://bitbucket.org/causecode/static-content' ]
    def watchedResources = 'file:./grails-app/services/*ContentService.groovy'

    GrailsApplication grailsApplication

    /*Closure doWithSpring() { {->
            // TODO Implement runtime spring config (optional)
        }
    }*/

    void doWithDynamicMethods(ctx) {
        // TODO Implement registering dynamic methods to classes (optional)
        /*def ctx = grailsApplication.mainContext
        println '\nConfiguring content plugin ...'

        println '... finished configuring content plugin\n'*/
        def application = ctx.grailsApplication
        MetaClass metaClassInstance = application.getServiceClass(ContentService.name).metaClass

        if (!metaClassInstance.respondsTo(null, 'getShorthandAnnotatedControllers')) {
            Map shorthandAnnotatedControllerMap = [:]
            for (controller in application.controllerClasses) {
                Annotation controllerAnnotation = controller.clazz.getAnnotation(ControllerShorthand)
                if (controllerAnnotation) {  // Searching for shorthand for grails controller
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
            //println "\nShorthand annotated controller map: $shorthandAnnotatedControllerMap"
            //println ""
        }
    }

    /*void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.

    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }*/
}
