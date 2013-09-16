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

    /**
     * Renders input widget based on given id.
     * @attr id REQUIRED identity of InputWidget domain to render
     */
    def renderWidget = { attrs , body ->
        InputWidget inputWidgetInstance = InputWidget.get(attrs.remove("id"))

        out << render(template: '/inputWidget/renderWidget', plugin: 'content', model: [
            attrs: attrs,
            inputWidgetInstance: inputWidgetInstance,
            inputWidgetValue: attrs.remove("inputWidgetValue")
        ])
    }
}