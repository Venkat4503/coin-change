package com.adp.coinchange.exception;

import java.io.Serial;

public class InSufficientCoinsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4597823863636942749L;

    public InSufficientCoinsException(final String message) {
        super(message);
    }
}