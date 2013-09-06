/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

class InputWidgetTagLib {

    static namespace = "content"

    def renderWidget = { attrs , body ->
        InputWidget inputWidgetInstance = InputWidget.get(attrs.id)
        out << render(template: '/inputWidget/renderWidget', plugin: 'content', model: ['inputWidgetInstance': inputWidgetInstance,
            'inputWidgetValue':attrs.inputWidgetValue].plus(attrs))
    }
}