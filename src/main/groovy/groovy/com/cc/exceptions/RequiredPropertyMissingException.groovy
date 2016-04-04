/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.exceptions
/**
 * Thrown when a named property of a specific type is not present in a class.
 *
 * @author Deepen Dhamecha
 */

public class RequiredPropertyMissingException extends Exception {


    public RequiredPropertyMissingException() {
        super();
    }

    public RequiredPropertyMissingException(String message) {
        super(message);
    }

    public RequiredPropertyMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredPropertyMissingException(Throwable cause) {
        super(cause);
    }
}
