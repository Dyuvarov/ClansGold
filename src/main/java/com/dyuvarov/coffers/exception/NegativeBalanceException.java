package com.dyuvarov.coffers.exception;

/** Exception to throw when there is not enough gold for transaction */
public class NegativeBalanceException extends RuntimeException{
    public NegativeBalanceException(String message) {
        super(message);
    }
}
