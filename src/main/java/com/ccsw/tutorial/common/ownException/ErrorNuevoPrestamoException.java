package com.ccsw.tutorial.common.ownException;

public class ErrorNuevoPrestamoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ErrorNuevoPrestamoException(String msg) {
        super(msg);
    }

}
