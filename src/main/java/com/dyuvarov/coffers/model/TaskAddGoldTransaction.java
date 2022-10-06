package com.dyuvarov.coffers.model;

import lombok.Getter;

@Getter
public class TaskAddGoldTransaction {
    private Long id;

    private long taskId;

    private long transactionId;

    public TaskAddGoldTransaction(long taskId, long transactionId) {
        this.taskId = taskId;
        this.transactionId = transactionId;
    }
}
