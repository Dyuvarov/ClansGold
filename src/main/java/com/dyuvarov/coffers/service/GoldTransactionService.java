package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.model.GoldTransaction;

import java.sql.Connection;

public interface GoldTransactionService {
    boolean save(GoldTransaction transaction, Connection dbConnection);
}
