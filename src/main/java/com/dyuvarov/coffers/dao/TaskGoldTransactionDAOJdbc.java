package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.TaskAddGoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j
@ApplicationScoped
public class TaskGoldTransactionDAOJdbc implements TaskAddGoldTransactionDAO{
    @Override
    public boolean save(TaskAddGoldTransaction transaction, Connection dbConnection) {
        String sql = "INSERT INTO coffers.task_coffer_gold_transaction(task_id, transaction_id) VALUES (?, ?)";
        int insertedRowsCount = 0;
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sql);
            ps.setLong(1, transaction.getTaskId());
            ps.setLong(2, transaction.getTransactionId());

            insertedRowsCount = ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error("Bad task coffer transaction save", sqlException);
            throw new EntitySaveException("Bad task coffer transaction save");
        }
        return insertedRowsCount > 0;
    }
}
