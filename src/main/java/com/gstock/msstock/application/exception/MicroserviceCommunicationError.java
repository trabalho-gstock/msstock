package com.gstock.msstock.application.exception;

import lombok.Getter;

public class MicroserviceCommunicationError extends Exception {

    @Getter
    private Integer status;

    public MicroserviceCommunicationError(String msg, Integer status){
        super(msg);
        this.status = status;
    }
}
