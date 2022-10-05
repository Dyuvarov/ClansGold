package com.dyuvarov.coffers.service;

public interface UserAddGoldService {
    void addGoldToClan(long userId, long clanId, int gold);
}
