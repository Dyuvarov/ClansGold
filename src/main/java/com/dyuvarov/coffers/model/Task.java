package com.dyuvarov.coffers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Task {
    private Long id;

    private boolean success;

    private int gold;

    public static final Task successTack;
    public static final Task failedTack;

    static {
        successTack = new Task(1L, true, 1);
        failedTack = new Task(2L, false, 50);
    }
}
