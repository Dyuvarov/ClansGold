package com.dyuvarov.coffers.model;

import lombok.Getter;
import lombok.Setter;

/** Clan */
@Getter
@Setter
public class Clan {
    /** clan id */
    private Long id;

    /** clan name */
    private String name; //

    /** current gold */
    private int gold;
}
