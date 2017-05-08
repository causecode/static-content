# Changelog

## Version 2.6.1
1. Changed PageController to follow restful pattern.
2. Removed gsp pages from grails-app/views/page
 - create.gsp
 - edit.gsp
 - show.gsp
 - list.gsp

## Version 2.6.0

    1. Removed InputWidget classes. These classes include - 
       InputWidget.groovy,
       InputWidgetController.groovy,
       InputWidgetService.groovy,
       InputWidgetTagLib.groovy
       
    2. Removed InputWidget gsp pages from 'views/inputWidget/'.
       _form.gsp
       _renderWidget.gsp
       create.gsp
       edit.gsp
       list.gsp
       show.gsp
       
    3. Removed inputWidget.js file from main/webapp/js/ directory.
       