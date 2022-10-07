package com.dyuvarov.coffers.model;

import lombok.Getter;

/** Additional info about add gold by user */
@Getter
public class UserAddGoldTransaction {
    private Long id;

    private long userId;

    private long transactionId;

    public UserAddGoldTransaction(long userId, long transactionId) {
        this.userId = userId;
        this.transactionId = transactionId;
    }
}
