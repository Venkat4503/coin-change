package com.adp.coinchange.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 3820247438830389067L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
