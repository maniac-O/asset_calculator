package com.SH.asset_calculator.exception;

import javax.management.BadAttributeValueExpException;

public class BadArgumentException extends BadAttributeValueExpException {

    /**
     * Constructs a BadAttributeValueExpException using the specified Object to
     * create the toString() value.
     *
     * @param val the inappropriate value.
     */


    public BadArgumentException(String val) {
        super(val);
    }
}
