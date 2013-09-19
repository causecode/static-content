/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

class InputWidget {

    String name
    String label
    String widgetKeys
    String widgetValues
    String defaultValue
    String helpText

    InputWidgetType type
    InputWidgetHelpType helpType
    InputWidgetValidation validation

    Date dateCreated
    Date lastUpdated

    static constraints = {
        name blank: true
        label nullable: true
        validation nullable: true
        widgetKeys nullable: true
        widgetValues nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    String toString() {
        return name
    }
}

enum InputWidgetType {
    CHECKBOX, MULTI_SELECT, RADIO, SELECT, TEXT_AREA, TEXT_FIELD
}

enum InputWidgetHelpType {
    BLOCK, INLINE, PLACEHOLDER, POPOVER, TOOLTIP
}

enum InputWidgetValidation {
    EMAIL, INTEGER, DATE, TIME, URL, ZIP_CODE
}