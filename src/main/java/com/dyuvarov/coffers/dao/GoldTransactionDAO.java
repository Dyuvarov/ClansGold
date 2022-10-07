package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.GoldTransaction;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/** DAO for "GoldTransaction" entity */
public interface GoldTransactionDAO {
    /**
     * Find single clan by id
     * @param id transaction id
     * @param detailed add detailed information depends on transaction action
     */
    Optional<GoldTransaction> findById(long id, boolean detailed);

    /**
     * Find list of transactions with pagination
     * @param pageNumber number of page
     * @param pageSize size of each page
     * @param detailed add detailed information depends on transaction action
     */
    List<? extends GoldTransaction> findAllPageable(int pageNumber, int pageSize, boolean detailed);

    /**
     * Write new transaction in database
     * @param goldTransaction transaction to save
     * @param dbConnection database connection
     */
    boolean create(GoldTransaction goldTransaction, Connection dbConnection);
}
