package org.iq47.reactivejava.service;

import lombok.RequiredArgsConstructor;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LoopService implements MetricService {
    private final DealRepository dealRepository;
    @Override
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(LocalDate today) {
        Map<Long, Deal> dealMap = dealRepository.getDeals();
        Map<String, Double> result = new HashMap<>();
        for (Deal deal : dealMap.values()) {
            String ticker = deal.getInstrument().getTicker();
            if (deal.getTradeDateTime().toLocalDate().equals(today)) {
                Double price = deal.getPrice();
                if (!result.containsKey(ticker)) {
                    result.put(ticker, price);
                } else {
                    result.put(ticker, (result.get(ticker) + price));
                }
            }
        }
        return result;
    }
}
