package com.dyuvarov.coffers.exception;

import lombok.NoArgsConstructor;

/** Exception to throw when entity can`t be saved */
@NoArgsConstructor
public class EntitySaveException extends RuntimeException {
    public EntitySaveException(String message) {
        super(message);
    }
}
