package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.dao.ClanDAO;
import com.dyuvarov.coffers.model.Clan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.util.Optional;

@ApplicationScoped
public class ClanServiceImpl implements ClanService {

    @Inject
    ClanDAO clanDAO;
    @Override
    public Optional<Clan> get(long clanId) {
        return clanDAO.findById(clanId);
    }

    @Override
    public boolean update(Clan clan, Connection dbConnection) {
        return clanDAO.update(clan, dbConnection);
    }
}
