package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.dao.GoldTransactionDAO;
import com.dyuvarov.coffers.model.GoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;

@ApplicationScoped
public class GoldTransactionServiceImpl implements GoldTransactionService {

    @Inject
    private GoldTransactionDAO goldTransactionDAO;

    @Override
    public boolean save(GoldTransaction transaction, Connection dbConnection) {
        return goldTransactionDAO.create(transaction, dbConnection);
    }
}
