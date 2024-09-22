package org.iq47.reactivejava.service;

import java.util.Map;

public interface MetricService {
    Map<String, Double> getTodayInstrumentMeanDealPrice();
}
