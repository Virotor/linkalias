package com.lessons.linkalias.exceptions;


import org.springframework.http.HttpStatus;

public class LinkIsResolvedException extends  AppErrorImp implements  AppError {


    public LinkIsResolvedException( String baseLink) {
        super(baseLink, HttpStatus.CONFLICT);
    }

    @Override
    public String toString() {
        return  "This link is already used " + baseLink;
    }

}
