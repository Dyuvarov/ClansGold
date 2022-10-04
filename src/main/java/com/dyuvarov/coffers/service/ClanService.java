package com.dyuvarov.coffers.service;

import com.dyuvarov.coffers.model.Clan;

import java.util.Optional;

public interface ClanService {
    Optional<Clan> get(long clanId);

    boolean update(Clan clan);
}
