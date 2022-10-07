package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.dao.GoldTransactionDAO;
import com.dyuvarov.coffers.model.GoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.sql.Connection;
import java.util.List;

@ApplicationScoped
public class GoldTransactionService {

    @Inject
    private GoldTransactionDAO goldTransactionDAO;

    public boolean save(GoldTransaction transaction, Connection dbConnection) {
        return goldTransactionDAO.create(transaction, dbConnection);
    }

    public GoldTransaction findById(long id, boolean detailed) {
        return goldTransactionDAO.findById(id, detailed).orElseThrow(EntityNotFoundException::new);
    }

    public List<? extends GoldTransaction> findAllPageable(Integer pageNumber, Integer pageSize, boolean detailed) {
        pageNumber = pageNumber==null ? 0 : pageNumber;
        pageSize = pageSize==null ? 10 : pageSize;
        return goldTransactionDAO.findAllPageable(pageNumber, pageSize, detailed);
    }
}
