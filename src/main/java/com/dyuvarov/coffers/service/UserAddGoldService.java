package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.model.Clan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserAddGoldService {

    @Inject
    private ClanService clans; //todo final

    @Transactional
    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = clans.get(clanId).orElseThrow(EntityNotFoundException::new);
        int oldGold = clan.getGold();
        int newGold = oldGold + gold;

        clans.update(clan);
    }
}
