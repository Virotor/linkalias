package com.lessons.linkalias.exceptions;

import org.springframework.http.HttpStatus;

public class AppErrorImp extends RuntimeException implements AppError {
    protected final String baseLink;
    protected final HttpStatus httpStatus;


    protected AppErrorImp(String baseLink, HttpStatus httpStatus) {
        this.baseLink = baseLink;
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getHTTPStatus() {
        return httpStatus;
    }

    @Override
    public String getMessageError() {
        return toString();
    }
}
