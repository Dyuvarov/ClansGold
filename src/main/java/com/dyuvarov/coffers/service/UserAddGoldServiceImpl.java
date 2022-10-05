package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.GoldAction;
import com.dyuvarov.coffers.TransactionStatus;
import com.dyuvarov.coffers.dao.JdbcConnectionProvider;
import com.dyuvarov.coffers.dao.UserAddGoldTransactionDAO;
import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.Clan;
import com.dyuvarov.coffers.model.GoldTransaction;
import com.dyuvarov.coffers.model.UserAddGoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.time.LocalDateTime;

@ApplicationScoped
public class UserAddGoldServiceImpl implements UserAddGoldService{

    @Inject
    private ClanService clans;

    @Inject
    private GoldTransactionService transactionService;

    @Inject
    private UserAddGoldTransactionDAO userAddGoldTransactionDAO;

    @Inject
    private JdbcConnectionProvider jdbcConnectionProvider;

    @SneakyThrows
    @Override
    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = clans.get(clanId).orElseThrow(EntityNotFoundException::new);
        int oldGold = clan.getGold();
        int newGold = oldGold + gold;
        clan.setGold(newGold);

        Connection connection = jdbcConnectionProvider.getConnection();
        connection.setAutoCommit(false);

        GoldTransaction goldTransaction = new GoldTransaction(
                LocalDateTime.now(),
                clanId,
                GoldAction.USER_ADD,
                oldGold,
                newGold,
                gold
        );
        goldTransaction.setStatus(TransactionStatus.ACCEPTED);
        try {
            transactionService.save(goldTransaction, connection);

            UserAddGoldTransaction userTransaction = new UserAddGoldTransaction(
                    userId,
                    goldTransaction.getId()
            );

            userAddGoldTransactionDAO.save(userTransaction, connection);
            clans.update(clan, connection);
        } catch (Exception e) {
            connection.rollback();
            goldTransaction.setStatus(TransactionStatus.FAILED);
            goldTransaction.setErrorDescription(e.getMessage());
            goldTransaction.setGoldAfter(oldGold);
            transactionService.save(goldTransaction, connection);
            throw new EntitySaveException(e.getMessage());
        } finally {
            connection.commit();
            connection.close();
        }
    }
}
