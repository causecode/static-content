import grails.util.Environment

/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.servlet.version = "3.0"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        inherits true
        grailsHome()
        grailsPlugins()
        mavenRepo "http://repo.spring.io/milestone/"
        mavenRepo "http://maven.causecode.com"
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
    }

    plugins {
        compile ":markdown:1.1.1"
        compile (":spring-security-core:2.0-RC4", ":taggable:1.0.1", ":ckeditor:3.6.3.0") {
            export = false
        }
        runtime (":resources:1.2.1", ":hibernate:3.6.10.7") {
            export = false
        }
        build (":tomcat:7.0.50", ":release:3.0.1", ":rest-client-builder:2.0.1") {
            export = false
        }
        if (Environment.isDevelopmentMode()) {
            compile ":codenarc:0.22"
        }
    }
}
