/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package content

import com.causecode.content.ContentService
import grails.core.GrailsApplication
import grails.plugins.Plugin
import groovy.util.logging.Log4j
import groovy.util.logging.Slf4j

import java.lang.annotation.Annotation

/**
 * This is content grails plugin.
 *
 * Note: Removing the methods without implementation - doWithSpring, doWithApplicationContext, onChange, onConfigChange,
 *       onShutdown and variable license for reference kindly refer previous version.
 */
@Slf4j
class ContentGrailsPlugin extends Plugin {

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
    def organization = [name: 'Causecode Technologies Pvt. Ltd.', url: 'http://causecode.com']
    def developers = [[name: 'Shashank Agrawal', email: 'shashank.agrawal@causecode.com']]
    def issueManagement = [system: 'BITBUCKET', url: 'https://bitbucket.org/causecode/static-content/issues']
    def scm = [url: 'https://bitbucket.org/causecode/static-content']
    def watchedResources = 'file:./grails-app/services/*ContentService.groovy'

    GrailsApplication grailsApplication

    @Override
    void doWithDynamicMethods() {
        log.info '\nConfiguring content plugin ...'

        MetaClass metaClassInstance = grailsApplication.getArtefact('Service', ContentService.name).metaClass

        if (!metaClassInstance.respondsTo(null, 'getShorthandAnnotatedControllers')) {
            Map shorthandAnnotatedControllerMap = [:]
            for (controller in grailsApplication.controllerClasses) {
                Annotation controllerAnnotation = controller.clazz
                        .getAnnotation(com.causecode.annotation.shorthand.ControllerShorthand)
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
                grailsApplication.getDomainClass(SpringSecurityUtils.securityConfig.authority.className).clazz
            }
            metaClassInstance.getAuthorClass {
                grailsApplication
                        .getDomainClass(SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
            }

            log.debug "\nShorthand annotated controller map: ${shorthandAnnotatedControllerMap}"
            log.info '... finished configuring content plugin\n'
        }
    }
}
