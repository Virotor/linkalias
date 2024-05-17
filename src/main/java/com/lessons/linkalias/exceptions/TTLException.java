package com.lessons.linkalias.exceptions;

import org.springframework.http.HttpStatus;

public class TTLException extends AppErrorImp implements  AppError {


    public TTLException( String baseLink) {
        super(baseLink, HttpStatus.REQUEST_TIMEOUT);
    }

    @Override
    public String toString() {
        return  "Timeout for link " + baseLink;
    }

}
