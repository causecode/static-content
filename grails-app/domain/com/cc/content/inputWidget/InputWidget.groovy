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
    String validation

    String noSelectionText
    Integer minChar
    Integer maxChar
    Integer minValueRange
    Integer maxValueRange

    Date dateCreated
    Date lastUpdated

    static constraints = {
        label nullable: true
        validation nullable: true
        widgetKeys nullable: true
        widgetValues nullable: true
        defaultValue nullable: true
        helpText nullable: true
        helpType nullable: true
        noSelectionText nullable: true
        minChar nullable: true
        maxChar nullable: true
        minValueRange nullable: true
        maxValueRange nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    String toString() {
        return name
    }

    boolean isRequired() {
        if(!this.validation) {
            return false
        }
        "REQUIRED" in this.validation.tokenize(",")*.trim()
    }

}

enum InputWidgetType {
    CHECKBOX, MULTI_SELECT, RADIO, SELECT, TEXT_AREA, TEXT_FIELD
}

enum InputWidgetHelpType {
    BLOCK, INLINE, PLACEHOLDER, POPOVER, TOOLTIP
}

enum InputWidgetValidation {
    REQUIRED, EMAIL, INTEGER, TIME, URL, ZIP_CODE
}