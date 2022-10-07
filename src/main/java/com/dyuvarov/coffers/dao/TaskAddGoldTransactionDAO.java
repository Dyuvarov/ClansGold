package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.TaskAddGoldTransaction;

import java.sql.Connection;

/** DAO for "TaskAddGoldTransaction" entity */
public interface TaskAddGoldTransactionDAO {

    /** Save entity in database */
    boolean save(TaskAddGoldTransaction transaction, Connection dbConnection);
}
