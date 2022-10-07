package com.dyuvarov.coffers.exception;

/** Exception to throw when gold can`t be added because of the limit */
public class BalanceLimitException extends RuntimeException{
    public BalanceLimitException(String message) {
        super(message);
    }
}
