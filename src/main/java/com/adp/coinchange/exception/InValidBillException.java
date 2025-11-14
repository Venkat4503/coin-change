package com.adp.coinchange.exception;

import java.io.Serial;

public class InValidBillException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7999654288161447830L;

    public InValidBillException(final String message) {
        super(message);
    }
}