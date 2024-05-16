package com.lessons.linkalias.exceptions;

public class TTLException extends RuntimeException {

    private final String baseLink;

    public TTLException( String baseLink) {

        this.baseLink = baseLink;
    }

    @Override
    public String toString() {
        return  "Ttl for link " + baseLink;
    }
}
