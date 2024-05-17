package com.lessons.linkalias.exceptions;

import org.springframework.http.HttpStatus;

public interface AppError{

    HttpStatus getHTTPStatus();

    String getMessageError();
}
