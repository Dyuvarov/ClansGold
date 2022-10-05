package com.dyuvarov.coffers.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntitySaveException extends RuntimeException {
    public EntitySaveException(String message) {
        super(message);
    }
}
