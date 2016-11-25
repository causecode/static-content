/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

grails.serverURL = 'http://localhost:8080'

grails.resources.modules = {
    validator = {
        resource url: '/js/jquery.validate.js'
    }
}

environments {
    test {
        grails.gorm.autoFlush = true
    }
}

grails.plugin.springsecurity.securityConfigType = "Annotation"

ckeditor = {
    upload = {
        basedir = "/images/"
        overwrite = false
        image = {
            browser = true
            upload = true
            allowed = ['jpg', 'gif', 'jpeg', 'png']
            denied = []
        }
    }
}

grails.app.context = "/"
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
