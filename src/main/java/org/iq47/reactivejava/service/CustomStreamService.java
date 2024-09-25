package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;


@AllArgsConstructor
@Service
public class CustomStreamService implements MetricService {

    private final CustomCollector customCollector;

    @Override
    @Timed(service = "Параллельный stream с кастомным коллектором")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        return dealRepository.getDeals()
                .stream()
                .parallel()
                .collect(customCollector);
    }
}
