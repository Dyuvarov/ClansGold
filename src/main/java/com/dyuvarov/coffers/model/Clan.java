package com.dyuvarov.coffers.model;

import lombok.Getter;
import lombok.Setter;

/** Клан */
@Getter
@Setter
public class Clan {
    /** id клана */
    private Long id;

    /** имя клана */
    private String name; //

    /** текущее количество золота в казне клана */
    private int gold;
}
