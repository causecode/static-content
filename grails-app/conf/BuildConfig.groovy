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

grails.project.repos.ccRepo.url = "https://nexus.causecode.com/repository/maven-releases/"
grails.project.repos.default = "ccRepo"
grails.project.repos.ccRepo.type = "maven"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
		inherits true
		grailsHome()
		grailsPlugins()
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://repo.spring.io/milestone/"
        mavenRepo "https://nexus.causecode.com/repository/maven-releases/"
    }

    dependencies {
    }

    plugins {
        compile (":spring-security-core:2.0-RC3", ":taggable:1.0.1") {
            export = false
        }
        runtime (":resources:1.2.1") {
            export = false
        }
        build (":tomcat:7.0.50", ":release:3.0.1", ":rest-client-builder:2.0.1") {
            export = false
        }
        compile (":hibernate:3.6.10.7") {
            export = false
        }
    }
}
