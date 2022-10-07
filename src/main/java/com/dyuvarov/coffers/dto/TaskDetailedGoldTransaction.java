package com.dyuvarov.coffers.dto;

import com.dyuvarov.coffers.model.GoldTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Gold transaction with task information. For gold transaction created by task */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDetailedGoldTransaction extends GoldTransaction {
    private Long taskId;
}
