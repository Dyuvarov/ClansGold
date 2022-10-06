package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.GoldAction;
import com.dyuvarov.coffers.dao.JdbcConnectionProvider;
import com.dyuvarov.coffers.dao.UserAddGoldTransactionDAO;
import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.GoldTransaction;
import com.dyuvarov.coffers.model.UserAddGoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;

@ApplicationScoped
@Log4j
public class UserAddGoldService {

    @Inject
    private ClanService clans;

    @Inject
    private UserAddGoldTransactionDAO userAddGoldTransactionDAO;

    @Inject
    private JdbcConnectionProvider jdbcConnectionProvider;

    @SneakyThrows
    public void addGoldToClan(long userId, long clanId, int gold) {
        GoldTransaction goldTransaction= clans.addGold(clanId, gold, GoldAction.USER_ADD);

        Connection connection = jdbcConnectionProvider.getConnection();
        connection.setAutoCommit(false);
        UserAddGoldTransaction userTransaction = new UserAddGoldTransaction(
                userId,
                goldTransaction.getId()
        );
        try {
            userAddGoldTransactionDAO.save(userTransaction, connection);
            connection.commit();
        } catch (Exception e) {
            log.error("User add gold error", e);
            connection.rollback();
            throw new EntitySaveException(e.getMessage());
        } finally {
            connection.close();
        }
    }
}
