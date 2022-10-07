package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.Clan;

import java.sql.Connection;
import java.util.Optional;

/** DAO for "Clan" entity */
public interface ClanDAO {
    /** Find single clan by id */
    Optional<Clan> findById(long id);

    /**
     * Update single clan
     * @param clan clan entity to save
     * @param dbConnection connection to database
     * @return true on save success, else false
     */
    boolean update(Clan clan, Connection dbConnection);
}
