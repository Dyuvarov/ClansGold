package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.UserAddGoldTransaction;

import java.sql.Connection;

public interface UserAddGoldTransactionDAO {
    boolean save(UserAddGoldTransaction transaction, Connection dbConnection);
}
