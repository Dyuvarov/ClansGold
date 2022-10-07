package com.dyuvarov.coffers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Task */
@Getter
@AllArgsConstructor
public class Task {
    /** task id */
    private Long id;

    /** task status */
    private boolean success;

    /** gold for success */
    private int gold;

    // mocked tasks to not implement additional dao and database tables
    public static final Task successTack;
    public static final Task failedTack;

    static {
        successTack = new Task(1L, true, 1);
        failedTack = new Task(2L, false, 50);
    }
}
