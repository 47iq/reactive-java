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
            try {
                dealRepository.loadDataFromDb();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (deal.getTradeDateTime().toLocalDate().equals(LocalDate.now())) {
                result.merge(deal.getInstrument().getTicker(), deal.getPrice(), Double::sum);
            }
        }
        return result;
    }
}
