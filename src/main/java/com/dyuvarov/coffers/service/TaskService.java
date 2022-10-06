package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.GoldAction;
import com.dyuvarov.coffers.dao.JdbcConnectionProvider;
import com.dyuvarov.coffers.dao.TaskAddGoldTransactionDAO;
import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.GoldTransaction;
import com.dyuvarov.coffers.model.Task;
import com.dyuvarov.coffers.model.TaskAddGoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;

@ApplicationScoped
@Log4j
public class TaskService {
    @Inject
    private ClanService clans;

    @Inject
    private JdbcConnectionProvider jdbcConnectionProvider;

    @Inject
    private TaskAddGoldTransactionDAO taskAddGoldTransactionDAO;

    public Task findById(long taskId) {
        if (taskId == 1) {
            return Task.successTack;
        } else if (taskId == 2) {
            return Task.failedTack;
        }
        throw new EntityNotFoundException();
    }

    public void completeTask(long clanId, long taskId) {
        Task task = findById(taskId);

        if (task.isSuccess()) {
            addGoldToClan(clanId, taskId, task.getGold());
        }
    }

    @SneakyThrows
    private void addGoldToClan(long clanId, long taskId, int gold) {
        GoldTransaction goldTransaction = clans.addGold(clanId, gold, GoldAction.TASK);

        Connection connection = jdbcConnectionProvider.getConnection();
        connection.setAutoCommit(false);

        TaskAddGoldTransaction taskTransaction = new TaskAddGoldTransaction(
                taskId,
                goldTransaction.getId()
        );
        try {
            taskAddGoldTransactionDAO.save(taskTransaction, connection);
            connection.commit();
        } catch (Exception e) {
            log.error("Task add gold error", e);
            connection.rollback();
            throw new EntitySaveException(e.getMessage());
        } finally {
            connection.close();
        }
    }
}
