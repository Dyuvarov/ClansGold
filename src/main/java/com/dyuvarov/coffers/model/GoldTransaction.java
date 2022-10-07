package com.dyuvarov.coffers.model;

import com.dyuvarov.coffers.type.GoldAction;
import com.dyuvarov.coffers.type.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;

/** Transaction */
@Getter
@Setter
@NoArgsConstructor
public class GoldTransaction {
    /** transaction id */
    private Long id;

    /** transaction timestamp */
    private LocalDateTime date;

    /** transaction clan id */
    private long clanId;

    /** action which created transaction */
    private GoldAction action;

    /** gold before transaction */
    private int goldBefore;

    /** increasing/decreasing of gold */
    private int goldChange;

    /** transaction status: fail or success */
    private TransactionStatus status;

    /** error message if transaction failed */
    private String errorDescription;

    public GoldTransaction(LocalDateTime date, long clanId, GoldAction action, int goldBefore, int goldChange) {
        this.date = date;
        this.clanId = clanId;
        this.action = action;
        this.goldBefore = goldBefore;
        this.goldChange = goldChange;
    }
}
