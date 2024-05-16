package com.lessons.linkalias.exceptions;

import lombok.Getter;

public class LinkNotFoundException extends  RuntimeException{


    private final String baseLink;

    public LinkNotFoundException( String baseLink) {

        this.baseLink = baseLink;
    }

    @Override
    public String toString() {
        return  "Not found base link for short link " + baseLink;
    }
}
