/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.inputWidget


/**
 * This taglib provides tags for rendering comments on blog.
 * @author Laxmi Salunkhe
 */
class InputWidgetService {

    /**
     * Used to create InputWidget instance with given parameters.
     * @param params REQUIRED Map containing parameters required to create InputWidget instance.
     * @return Newly created InputWidget Instance.
     */
    InputWidget create(params) {
        InputWidget inputWidgetInstance = new InputWidget()
        update(params, inputWidgetInstance)
    }

    /**
     * Used to update InputWidget instance with given new parameters.
     * @param inputWidgetInstance REQUIRED InputWidget Instance to be updated.
     * @param params Map containing parameters required to create InputWidget instance.
     * @return Updated InputWidget Instance.
     */
    InputWidget update(params, InputWidget inputWidgetInstance) {
        inputWidgetInstance.properties = params
        inputWidgetInstance.save()
        if(inputWidgetInstance.hasErrors()) {
            log.warn "Error saving inputWidget instance: $inputWidgetInstance.errors"
        }
        return inputWidgetInstance
    }
}