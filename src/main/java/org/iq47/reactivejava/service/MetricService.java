package org.iq47.reactivejava.service;

import org.iq47.reactivejava.repository.DealRepository;

import java.time.LocalDate;
import java.util.Map;

public interface MetricService {
    Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository);
}
