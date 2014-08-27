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
    }

    dependencies {
    }

    plugins {
        compile (":spring-security-core:1.2.7.3", ":taggable:1.0.1") {
            export = false
        }
        compile (":rest-client-builder:2.0.1") {
            export = true
        }
        runtime (":resources:1.2.1") {
            export = false
        }
        build (":tomcat:7.0.50", ":release:3.0.1") {
            export = true
        }

    }
}
