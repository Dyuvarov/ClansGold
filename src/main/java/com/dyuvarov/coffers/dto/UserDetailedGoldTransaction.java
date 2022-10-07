package com.dyuvarov.coffers.dto;

import com.dyuvarov.coffers.model.GoldTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Gold transaction with user information. For gold transaction created by user  */
@Getter
@Setter
@AllArgsConstructor
public class UserDetailedGoldTransaction extends GoldTransaction {
    private Long userId;
}
