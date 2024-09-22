package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StreamService implements MetricService{
    private final DealRepository dealRepository;
    @Override
    public Map<String, Double> getTodayInstrumentMeanDealPrice() {
        return dealRepository
                .getDeals()
                .values()
                .stream()
                .filter(x -> x.getTradeDateTime().toLocalDate().equals(LocalDate.now()))
                .collect(Collectors.toMap(
                                Deal::getTicker,
                                Deal::getPrice,
                                (x, y) -> (x + y) / 2
                        )
                );
    }
}
