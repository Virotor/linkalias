package com.lessons.linkalias.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends  AppErrorImp implements  AppError{


    public LinkNotFoundException( String baseLink) {
        super(baseLink, HttpStatus.NOT_FOUND);
    }

    @Override
    public String toString() {
        return  "Not found base link for short link " + baseLink;
    }

}
