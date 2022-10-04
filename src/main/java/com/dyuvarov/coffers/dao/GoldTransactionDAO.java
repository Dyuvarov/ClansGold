package com.dyuvarov.coffers.dao;

import com.dyuvarov.coffers.model.GoldTransaction;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface GoldTransactionDAO {
    Optional<GoldTransaction> findById(long id);

    List<GoldTransaction> findByClanPageable(long clanId, int pageNumber, int pageSize);

    boolean save(GoldTransaction goldTransaction);
}
