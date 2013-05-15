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

grails.project.dependency.resolution = {
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
    }

    dependencies {
        runtime 'mysql:mysql-connector-java:5.1.18'
    }

    plugins {
        compile ":taggable:1.0.1"
        runtime ":jquery:1.8.3"
        compile ":jquery-ui:1.8.24"
        runtime ":resources:1.2.RC2"
        compile ":spring-security-core:1.2.7.3"
        build(":tomcat:$grailsVersion", ":release:2.0.3", ":rest-client-builder:1.0.2") {
            export = false
        }
    }
}