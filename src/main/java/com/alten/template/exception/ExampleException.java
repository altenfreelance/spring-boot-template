package com.alten.template.exception;

public class ExampleException extends Throwable {
    public ExampleException(String test) {
        super("This is an example exception " + test);
    }
}
