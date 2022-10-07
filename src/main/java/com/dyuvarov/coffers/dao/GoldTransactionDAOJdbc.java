package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.type.GoldAction;
import com.dyuvarov.coffers.type.TransactionStatus;
import com.dyuvarov.coffers.dto.TaskDetailedGoldTransaction;
import com.dyuvarov.coffers.dto.UserDetailedGoldTransaction;
import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.GoldTransaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/** JDBC implementation of GoldTransactionDAO */
@ApplicationScoped
@Log4j
public class GoldTransactionDAOJdbc implements GoldTransactionDAO{
    @Inject
    JdbcConnectionProvider connectionProvider;

    @Override
    public Optional<GoldTransaction> findById(long id, boolean detailed) {
        String sql = detailed ? detailedSingleQuery() : notDetailedSingleQuery();

        GoldTransaction transaction = null;
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                transaction = singleRowToGoldTransaction(rs, detailed);
            }
        } catch (SQLException sqlException) {
            log.error("Bad 'findById' for transaction", sqlException);
        }

        return Optional.ofNullable(transaction);
    }

    private String notDetailedSingleQuery() {
        return "SELECT id, date, clan_id, action, gold_before, gold_change, status, error_description" +
                " FROM coffers.transaction WHERE id=?";
    }

    private String detailedSingleQuery() {
        return "SELECT t.id, t.date, t.clan_id, t.action, t.gold_before, t.gold_change, t.status, t.error_description, ut.user_id, tt.task_id " +
                "FROM coffers.transaction t " +
                "LEFT JOIN coffers.user_coffer_gold_transaction ut ON t.action='USER_ADD' AND ut.transaction_id=t.id " +
                "LEFT JOIN coffers.task_coffer_gold_transaction tt ON t.action='TASK' AND tt.transaction_id=t.id " +
                "WHERE t.id=?";
    }

    @Override
    public List<? extends GoldTransaction> findAllPageable(int pageNumber, int pageSize, boolean detailed) {
        String sql = detailed ? detailedListQuery() : notDetailedListQuery();
        List<? extends GoldTransaction> transactions = Collections.emptyList();
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, pageSize*pageNumber);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();
            transactions = resultSetToList(rs, detailed);
        } catch (SQLException sqlException) {
            log.error("Bad search for transaction", sqlException);
        }

        return transactions;
    }

    private String notDetailedListQuery() {
        return "SELECT id, date, clan_id, action, gold_before, gold_change, status, error_description " +
                "FROM coffers.transaction " +
                "ORDER BY date DESC " +
                "OFFSET ? LIMIT ?";
    }

    private String detailedListQuery() {
        return "SELECT t.id, t.date, t.clan_id, t.action, t.gold_before, t.gold_change, t.status, t.error_description, ut.user_id,  tt.task_id " +
                "FROM coffers.transaction t " +
                "LEFT JOIN coffers.user_coffer_gold_transaction ut ON t.action='USER_ADD' AND ut.transaction_id=t.id " +
                "LEFT JOIN coffers.task_coffer_gold_transaction tt ON t.action='TASK' AND tt.transaction_id=t.id " +
                "ORDER BY date DESC " +
                "OFFSET ? LIMIT ?";
    }

    @Override
    public boolean create(GoldTransaction goldTransaction, Connection dbConnection) {
        String sql = "INSERT INTO coffers.transaction (date, clan_id, action, gold_before, gold_change, status, error_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int insertedRowsCount = 0;
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(goldTransaction.getDate()));
            ps.setLong(2, goldTransaction.getClanId());
            ps.setString(3, goldTransaction.getAction().name());
            ps.setInt(4, goldTransaction.getGoldBefore());
            ps.setInt(5, goldTransaction.getGoldChange());
            ps.setString(6, goldTransaction.getStatus().name());
            if(goldTransaction.getErrorDescription() == null) {
                ps.setNull(7, JDBCType.VARCHAR.getVendorTypeNumber());
            } else {
                ps.setString(7, goldTransaction.getErrorDescription());
            }
            insertedRowsCount = ps.executeUpdate();
            if (insertedRowsCount > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        goldTransaction.setId(generatedKeys.getLong(1));
                    }
                    else {
                        throw new EntitySaveException("Bad gold transaction creation, no ID obtained.");
                    }
                }
            }
        } catch (SQLException sqlException) {
            log.error("Bad gold transaction save", sqlException);
            throw new EntitySaveException("Bad gold transaction creation");
        }
        return insertedRowsCount > 0;
    }

    @SneakyThrows
    private List<? extends GoldTransaction> resultSetToList(ResultSet rs, boolean detailed) {
        List<GoldTransaction> transactions = new LinkedList<>();
        while (rs.next()) {
            transactions.add(singleRowToGoldTransaction(rs, detailed));;
        }
        return transactions;
    }

    @SneakyThrows
    private GoldTransaction singleRowToGoldTransaction(ResultSet rs, boolean detailed) {
        GoldTransaction goldTransaction;
        GoldAction action = GoldAction.valueOf(rs.getString("action"));

        if (detailed && GoldAction.USER_ADD == action) {
            goldTransaction = new UserDetailedGoldTransaction(rs.getLong("user_id"));
        } else if (detailed && GoldAction.TASK == action) {
            goldTransaction = new TaskDetailedGoldTransaction(rs.getLong("task_id"));
        } else {
            goldTransaction = new GoldTransaction();
        }
        goldTransaction.setId(rs.getLong("id"));
        goldTransaction.setDate(rs.getTimestamp("date").toLocalDateTime());
        goldTransaction.setClanId(rs.getLong("clan_id"));
        goldTransaction.setAction(GoldAction.valueOf(rs.getString("action")));
        goldTransaction.setGoldBefore(rs.getInt("gold_before"));
        goldTransaction.setGoldChange(rs.getInt("gold_change"));
        goldTransaction.setStatus(TransactionStatus.valueOf(rs.getString("status")));
        goldTransaction.setErrorDescription(rs.getString("error_description"));

        return goldTransaction;
    }
}
