package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.type.GoldAction;
import com.dyuvarov.coffers.type.TransactionStatus;
import com.dyuvarov.coffers.dao.ClanDAO;
import com.dyuvarov.coffers.dao.JdbcConnectionProvider;
import com.dyuvarov.coffers.exception.BalanceLimitException;
import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.exception.NegativeBalanceException;
import com.dyuvarov.coffers.model.Clan;
import com.dyuvarov.coffers.model.GoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
@Log4j
public class ClanService {

    @Inject
    private ClanDAO clanDAO;

    @Inject
    private GoldTransactionService transactionService;

    @Inject
    private JdbcConnectionProvider jdbcConnectionProvider;

    /** Find single clan by id */
    public Optional<Clan> get(long clanId) {
        return clanDAO.findById(clanId);
    }

    /**
     * Update single clan info in database
     * @param clan object to be saved
     * @param dbConnection connection to database
     * @return true on save success, else false
     */
    public boolean update(Clan clan, Connection dbConnection) {
        return clanDAO.update(clan, dbConnection);
    }

    /** add gold in clan`s  coffer*/
    @SneakyThrows
    public synchronized GoldTransaction addGold(long clanId, int gold, GoldAction action) {
        Connection connection = jdbcConnectionProvider.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        Clan clan = get(clanId).orElseThrow(EntityNotFoundException::new);
        int oldGold = clan.getGold();
        int newGold = oldGold + gold;
        clan.setGold(newGold);

        GoldTransaction goldTransaction = new GoldTransaction(
                LocalDateTime.now(),
                clanId,
                action,
                oldGold,
                gold
        );
        if (newGold < 0) { //negative count of gold
            goldTransaction.setStatus(TransactionStatus.FAILED);
            goldTransaction.setErrorDescription("Balance can`t be less than 0");
            try{
                transactionService.save(goldTransaction, connection);
                connection.commit();
            } catch (Exception e) {
                log.error("Add gold error", e);
            } finally {
                connection.close();
            }
            throw new NegativeBalanceException("Balance can`t be less than 0");
        } else if (gold > 0 && Integer.MAX_VALUE - gold < oldGold) { // more than max gold
            goldTransaction.setStatus(TransactionStatus.FAILED);
            goldTransaction.setErrorDescription("Balance can`t be bigger than " + Integer.MAX_VALUE);
            try{
                transactionService.save(goldTransaction, connection);
                connection.commit();
            } catch (Exception e) {
                log.error("Add gold error", e);
            } finally {
                connection.close();
            }
            throw new BalanceLimitException("Balance can`t be bigger than " + Integer.MAX_VALUE);
        } else {
            goldTransaction.setStatus(TransactionStatus.ACCEPTED);
            try {
                transactionService.save(goldTransaction, connection);
                update(clan, connection);
                connection.commit();
            } catch (Exception e) {
                log.error("Add gold error", e);
                connection.rollback(); //rollback if update failed
                goldTransaction.setStatus(TransactionStatus.FAILED);
                goldTransaction.setErrorDescription(e.getMessage());
                transactionService.save(goldTransaction, connection);
                connection.commit();
                throw new EntitySaveException(e.getMessage());
            } finally {
                connection.close();
            }
        }
        return goldTransaction;
    }
}
