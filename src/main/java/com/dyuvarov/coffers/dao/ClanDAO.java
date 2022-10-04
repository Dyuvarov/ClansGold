package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.Clan;

import java.util.Optional;

public interface ClanDAO {
    Optional<Clan> findById(long id);

    boolean update(Clan clan);
}
