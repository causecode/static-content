/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

dataSource = {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "root"
    password = "causecode.11"
}
hibernate = {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

// environment specific settings
environments = {
    development = {
        plugins = {
            runtime "org.grails.plugins:hibernate4"
        }
    }
    test = {
        dataSource = {
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
}

grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
     xml: ['text/xml', 'application/xml'],
     text: 'text/plain',
     js: 'text/javascript',
     rss: 'application/rss+xml',
     atom: 'application/atom+xml',
     css: 'text/css',
     csv: 'text/csv',
     pdf: 'application/pdf',
     rtf: 'application/rtf',
     excel: 'application/vnd.ms-excel',
     ods: 'application/vnd.oasis.opendocument.spreadsheet',
     all: '*/*',
     json: ['application/json','text/json'],
     form: 'application/x-www-form-urlencoded',
     multipartForm: 'multipart/form-data',
]

log4j = {
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

grails.resources.modules = {
    validator = {
        resource url: '/js/jquery.validate.js'
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

environments = {
    test = {
        app = {
            defaultURL = "/ng/app/index.html#"
        }
    }
}

grails.app.context = "/"
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
