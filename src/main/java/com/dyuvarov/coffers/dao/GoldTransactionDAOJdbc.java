package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.GoldAction;
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

@ApplicationScoped
@Log4j
public class GoldTransactionDAOJdbc implements GoldTransactionDAO{
    @Inject
    JdbcConnectionProvider connectionProvider;

    @Override
    public Optional<GoldTransaction> findById(long id) {
        String sql = "SELECT id, date, clan_id, action, gold_before, gold_after, gold_change FROM coffers.transaction WHERE id=?";

        GoldTransaction transaction = null;
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                transaction = singleRowToGoldTransaction(rs);
            }
        } catch (SQLException sqlException) {
            log.error("Bad 'findById' for transaction", sqlException);
        }

        return Optional.ofNullable(transaction);
    }

    @Override
    public List<GoldTransaction> findByClanPageable(long clanId, int pageNumber, int pageSize) {
        String sql = "SELECT id, date, clan_id, action, gold_before, gold_after, gold_change " +
                "FROM coffers.transaction " +
                "WHERE id=?" +
                "OFFSET ? LIMIT ?";

        List<GoldTransaction> transactions = Collections.emptyList();
        try (Connection connection = connectionProvider.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, clanId);
            ps.setInt(2, pageSize*pageNumber);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();
            transactions = resultSetToList(rs);
        } catch (SQLException sqlException) {
            log.error("Bad 'findByClan' for transaction", sqlException);
        }

        return transactions;
    }

    @Override
    public boolean create(GoldTransaction goldTransaction, Connection dbConnection) {
        String sql = "INSERT INTO coffers.transaction (date, clan_id, action, gold_before, gold_after, gold_change, status, error_description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int insertedRowsCount = 0;
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(goldTransaction.getDate()));
            ps.setLong(2, goldTransaction.getClanId());
            ps.setString(3, goldTransaction.getAction().name());
            ps.setInt(4, goldTransaction.getGoldBefore());
            ps.setInt(5, goldTransaction.getGoldAfter());
            ps.setInt(6, goldTransaction.getGoldChange());
            ps.setString(7, goldTransaction.getStatus().name());
            if(goldTransaction.getErrorDescription() == null) {
                ps.setNull(8, JDBCType.VARCHAR.getVendorTypeNumber());
            } else {
                ps.setString(8, goldTransaction.getErrorDescription());
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
    private List<GoldTransaction> resultSetToList(ResultSet rs) {
        List<GoldTransaction> transactions = new LinkedList<>();
        while (rs.next()) {
            transactions.add(singleRowToGoldTransaction(rs));
        }
        return transactions;
    }

    @SneakyThrows
    private GoldTransaction singleRowToGoldTransaction(ResultSet rs) {
        GoldTransaction goldTransaction = new GoldTransaction();

        goldTransaction.setId(rs.getLong("id"));
        goldTransaction.setDate(rs.getTimestamp("date").toLocalDateTime());
        goldTransaction.setClanId(rs.getLong("clan_id"));
        goldTransaction.setAction(GoldAction.valueOf(rs.getString("action")));
        goldTransaction.setGoldBefore(rs.getInt("gold_before"));
        goldTransaction.setGoldBefore(rs.getInt("gold_after"));
        goldTransaction.setGoldBefore(rs.getInt("gold_change"));

        return goldTransaction;
    }
}
