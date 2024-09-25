package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ParallelStreamService implements MetricService {
    @Override
    @Timed(service = "Параллельный stream с дефолтным коллектором")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        return dealRepository.getDeals()
                .stream()
                .filter(x -> x.getTradeDateTime().toLocalDate().equals(LocalDate.now()))
                .parallel()
                .collect(Collectors.toConcurrentMap(
                                x -> x.getInstrument().getTicker(),
                                Deal::getPrice,
                                Double::sum
                        )
                );
    }
}
