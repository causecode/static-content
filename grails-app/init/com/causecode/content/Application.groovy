package com.causecode.content

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

/**
 * This is the entry point of the com.causecode.content.Application.
 */
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(com.causecode.content.Application, args)
    }
}
