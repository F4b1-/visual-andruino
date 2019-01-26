package it.unibz.mobile.visualandruino.utils;

public class BrickIncorrectOrderException extends RuntimeException {
    public BrickIncorrectOrderException(String errorMessage) {
        super(errorMessage);
    }
}