package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.TaskAddGoldTransaction;

import java.sql.Connection;

public interface TaskAddGoldTransactionDAO {
    boolean save(TaskAddGoldTransaction transaction, Connection dbConnection);
}
