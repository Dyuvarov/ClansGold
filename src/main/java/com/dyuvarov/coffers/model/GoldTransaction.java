package com.dyuvarov.coffers.model;

import com.dyuvarov.coffers.GoldAction;
import com.dyuvarov.coffers.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoldTransaction {
    private Long id;

    private LocalDateTime date;

    private long clanId;

    private GoldAction action;

    private int goldBefore;

    private int goldAfter;

    private int goldChange;

    private TransactionStatus status;

    private String errorDescription;

    public GoldTransaction(LocalDateTime date, long clanId, GoldAction action, int goldBefore, int goldAfter, int goldChange) {
        this.date = date;
        this.clanId = clanId;
        this.action = action;
        this.goldBefore = goldBefore;
        this.goldAfter = goldAfter;
        this.goldChange = goldChange;
    }
}
