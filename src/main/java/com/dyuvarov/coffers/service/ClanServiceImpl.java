package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.dao.ClanDAO;
import com.dyuvarov.coffers.model.Clan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class ClanServiceImpl implements ClanService {

    @Inject
    ClanDAO clanDAO;
    @Override
    @Transactional
    public Optional<Clan> get(long clanId) {
        return clanDAO.findById(clanId);
    }

    @Override
    @Transactional
    public boolean update(Clan clan) {
        return clanDAO.update(clan);
    }
}
