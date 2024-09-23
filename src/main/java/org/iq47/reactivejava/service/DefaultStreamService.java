package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DefaultStreamService implements MetricService{
    private final DealRepository dealRepository;
    @Override
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(LocalDate today) {
        return dealRepository
                .getDeals()
                .values()
                .stream()
                .filter(x -> x.getTradeDateTime().toLocalDate().equals(today))
                .collect(Collectors.toMap(
                                x -> x.getInstrument().getTicker(),
                                Deal::getPrice,
                                Double::sum
                        )
                );
    }
}
