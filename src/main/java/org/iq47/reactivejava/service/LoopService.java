package org.iq47.reactivejava.service;

import lombok.RequiredArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoopService implements MetricService {
    @Override
    @Timed(service = "Преобразование через цикл")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {

        Map<String, Double> result = new HashMap<>();
        for (Deal deal : dealRepository.getDeals()) {
            String ticker = deal.getInstrument().getTicker();
            if (deal.getTradeDateTime().toLocalDate().equals(LocalDate.now())) {
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
