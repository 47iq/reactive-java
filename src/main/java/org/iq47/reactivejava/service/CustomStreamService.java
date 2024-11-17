package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.CustomCollector;
import org.iq47.reactivejava.stream.CustomSpliterator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.StreamSupport;


@AllArgsConstructor
@Service
public class CustomStreamService implements MetricService {

    private final CustomCollector customCollector;

    @Override
    @Timed(service = "Параллельный stream с кастомным коллектором")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        return StreamSupport
                .stream(new CustomSpliterator<>(dealRepository.getDeals()), true)
                .peek(deal -> {
                    try {
                        dealRepository.loadDataFromDb();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(customCollector);
    }
}
