package com.preet.currencyconverter.Repo;

import com.preet.currencyconverter.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionHistoryRepo extends JpaRepository<ConversionHistory, Long> {
}
