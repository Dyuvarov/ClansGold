package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.UserAddGoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
@Log4j
public class UserAddGoldTransactionDAOJdbc implements UserAddGoldTransactionDAO {

    @Override
    public boolean save(UserAddGoldTransaction transaction, Connection dbConnection) {
        String sql = "INSERT INTO coffers.user_coffer_gold_transaction (user_id, transaction_id) VALUES (?, ?)";
        int insertedRowsCount = 0;
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sql);
            ps.setLong(1, transaction.getUserId());
            ps.setLong(2, transaction.getTransactionId());

            insertedRowsCount = ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error("Bad user coffer transaction save", sqlException);
            throw new EntitySaveException("Bad user coffer transaction save");
        }
        return insertedRowsCount > 0;
    }
}
