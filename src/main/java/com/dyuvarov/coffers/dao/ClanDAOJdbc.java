package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.exception.EntitySaveException;
import com.dyuvarov.coffers.model.Clan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j
@ApplicationScoped
public class ClanDAOJdbc implements ClanDAO {

    @Inject
    JdbcConnectionProvider connectionProvider;

    @Override
    public Optional<Clan> findById(long id) {
        String sql = "SELECT id, name, gold FROM coffers.clan WHERE id=?";

        Clan clan = null;
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            clan = resultSetToClan(rs);
        } catch (SQLException sqlException) {
            log.error("Bad 'findById' for Clan", sqlException);
        }

        return Optional.ofNullable(clan);
    }

    @Override
    public boolean update(Clan clan, Connection dbConnection) {
        String sql = "UPDATE coffers.clan SET name=?, gold=? WHERE id=?";

        int updatedRowsCount = 0;
        try {
            PreparedStatement ps = dbConnection.prepareStatement(sql);
            ps.setString(1, clan.getName());
            ps.setInt(2, clan.getGold());
            ps.setLong(3, clan.getId());
            updatedRowsCount = ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error("Bad clan update", sqlException);
            throw new EntitySaveException("Bad clan update");
        }
        return updatedRowsCount > 0;
    }

    @SneakyThrows
    private Clan resultSetToClan(ResultSet rs) {
        Clan clan = new Clan();
        while (rs.next()) {
            clan.setId(rs.getLong("id"));
            clan.setName(rs.getString("name"));
            clan.setGold(rs.getInt("gold"));
        }

        return clan.getId() == null ? null : clan;
    }
}
