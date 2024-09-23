package org.iq47.reactivejava.service;

import java.time.LocalDate;
import java.util.Map;

public interface MetricService {
    Map<String, Double> getTodayInstrumentTotalTradeVolume(LocalDate today);
}
