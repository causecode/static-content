/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

import static com.cc.content.inputWidget.InputWidgetHelpType.*
import static com.cc.content.inputWidget.InputWidgetType.*

class InputWidgetTagLib {

    static namespace = "content"

    /**
     * Renders input widget based on given id.
     * @attr inputWidgetInstance REQUIRED identity of InputWidget domain to render
     */
    def renderWidget = { attrs , body ->
        InputWidget inputWidgetInstance = attrs.remove("inputWidgetInstance")
        List<String> validationTypes = inputWidgetInstance.validation?.tokenize(",")*.trim()
        StringBuilder classes = new StringBuilder(attrs.remove('classes') ?: "")
        validationTypes.each {
            classes.append(" " + it.toString().toLowerCase())
        }
        if(inputWidgetInstance.defaultValue && !attrs.inputWidgetValue) {
            classes.append(" having-default-value")
        }
        classes.append(" inputWidget")
        out << render(template: '/inputWidget/renderWidget', plugin: 'content', model: [
            additionalAttrs: attrs,
            classes: classes.toString(),
            inputWidgetInstance: inputWidgetInstance,
            inputWidgetValue: attrs.remove("inputWidgetValue")
        ])
    }

    /**
     * Renders template used while creating and updating input widget.
     * @attr id REQUIRED identity of InputWidget domain to render
     */
    def widget = { attrs , body ->
        String prefix = attrs.prefix ? attrs.prefix + "." : ""
        InputWidget inputWidgetInstance = InputWidget.get(attrs.id) ?: new InputWidget()

        out << render(template: '/inputWidget/form', plugin: 'content', model: [inputWidgetInstance: inputWidgetInstance,
            prefix: prefix])
    }

    /**
     * Renders HelpType and HelpText based on given id.
     * @attr id REQUIRED identity of InputWidget domain to render
     */
    def widgetHelper = { attrs , body ->
        InputWidget inputWidgetInstance = attrs.inputWidgetInstance

        if(inputWidgetInstance.helpType.equals(BLOCK)) {}
        //out << """ <p class="help-block">$inputWidgetInstance.helpText </p>  """ // Need to insert after Element

        if(inputWidgetInstance.helpType.equals(INLINE))
            out << """ title="$inputWidgetInstance.helpText"  """

        if(inputWidgetInstance.helpType.equals(PLACEHOLDER))
            out << """ placeholder="$inputWidgetInstance.helpText"  """

        if(inputWidgetInstance.helpType.equals(TOOLTIP))
            out << """ title="$inputWidgetInstance.helpText" data-toggle="tooltip" rel="tooltip" """

        if(inputWidgetInstance.helpType.equals(POPOVER))
            out << """ data-toggle="popover" data-placement="right" data-content="$inputWidgetInstance.helpText"
                       data-trigger="hover" rel="popover" """

        pageScope.additionalAttrs?.each { key, value ->
            if(value != "false" && value != false) {
                out << """ $key="$value" """
            }
        }
    }

    /**
     * Renders required field validator based on validation selected.
     * @attr id REQUIRED identity of InputWidget domain to render
     */
    def widgetValidation = { attrs, body ->
        InputWidget inputWidgetInstance = attrs.inputWidgetInstance
        InputWidgetType type = inputWidgetInstance.type

        if(inputWidgetInstance.required) {
            out << """ required="" """
        }
        if(inputWidgetInstance.minChar && type in [TEXT_AREA, TEXT_FIELD]) {
            out << """ minlength="${inputWidgetInstance.minChar}" """
        }
        if(inputWidgetInstance.maxChar && type in [TEXT_AREA, TEXT_FIELD]) {
            out << """ maxlength="${inputWidgetInstance.maxChar}" """
        }
        if(inputWidgetInstance.minValueRange && type in [TEXT_FIELD]) {
            out << """ min="${inputWidgetInstance.minValueRange}" """
        }
        if(inputWidgetInstance.maxValueRange && type in [TEXT_FIELD]) {
            out << """ max="${inputWidgetInstance.maxValueRange}" """
        }
    }
}