package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.UserAddGoldTransaction;

import java.sql.Connection;

/** DAO for "UserAddGoldTransaction" entity */

public interface UserAddGoldTransactionDAO {
    /** Save entity in database */
    boolean save(UserAddGoldTransaction transaction, Connection dbConnection);
}
