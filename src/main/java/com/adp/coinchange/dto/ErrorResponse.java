package com.adp.coinchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -3009895769001781532L;

    private String uri;
    private String method;
    private String exceptionMessage;
}
