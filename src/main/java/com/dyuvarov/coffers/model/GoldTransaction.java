package com.dyuvarov.coffers.model;

import com.dyuvarov.coffers.GoldAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoldTransaction {
    private Long id;

    private LocalDateTime date;

    private long clanId;

    private GoldAction action;

    private int goldBefore;

    private int goldAfter;

    private int goldChange;
}
