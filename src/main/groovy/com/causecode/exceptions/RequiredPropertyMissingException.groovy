/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.exceptions

/**
 * Thrown when a named property of a specific type is not present in a class.
 * @author Deepen Dhamecha
 */
class RequiredPropertyMissingException extends Exception {

    RequiredPropertyMissingException() {
        super()
    }

    RequiredPropertyMissingException(String message) {
        super(message)
    }

    RequiredPropertyMissingException(String message, Throwable cause) {
        super(message, cause)
    }
}
