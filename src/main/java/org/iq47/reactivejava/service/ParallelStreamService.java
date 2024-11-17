package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.CustomSpliterator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class ParallelStreamService implements MetricService {
    @Override
    @Timed(service = "Параллельный stream с дефолтным коллектором")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        return StreamSupport
                .stream(new CustomSpliterator<>(dealRepository.getDeals()), true)
                .parallel()
                .peek(deal -> {
                    try {
                        dealRepository.loadDataFromDb();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(x -> x.getTradeDateTime().toLocalDate().equals(LocalDate.now()))
                .collect(Collectors.toConcurrentMap(
                                x -> x.getInstrument().getTicker(),
                                Deal::getPrice,
                                Double::sum
                        )
                );
    }
}
