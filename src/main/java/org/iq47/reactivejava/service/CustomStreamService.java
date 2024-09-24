package org.iq47.reactivejava.service;

import lombok.AllArgsConstructor;
import org.iq47.reactivejava.aop.Timed;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@AllArgsConstructor
@Service
public class CustomStreamService implements MetricService {
    @Override
    @Timed(service = "Параллельный stream с кастомным коллектором")
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(LocalDate today, DealRepository dealRepository) {
        return dealRepository
                .getDeals()
                .values()
                .stream()
                .parallel()
                .filter(x -> x.getTradeDateTime().toLocalDate().equals(today))
                .collect(new Collector<Deal, Map<String, Double>, Map<String, Double>>() {
                             @Override
                             public Supplier<Map<String, Double>> supplier() {
                                 return ConcurrentHashMap::new;
                             }

                             @Override
                             public BiConsumer<Map<String, Double>, Deal> accumulator() {
                                 return (res, deal) -> {
                                     String ticker = deal.getInstrument().getTicker();
                                     Double price = deal.getPrice();
                                     res.put(ticker, (res.getOrDefault(ticker, 0.0) + price));
                                 };
                             }

                             @Override
                             public BinaryOperator<Map<String, Double>> combiner() {
                                 return (map1, map2) -> {
                                     map2.forEach((k, v) -> map1.merge(k, v, Double::sum));
                                     return map1;
                                 };
                             }

                             @Override
                             public Function<Map<String, Double>, Map<String, Double>> finisher() {
                                 return Function.identity();
                             }

                             @Override
                             public Set<Characteristics> characteristics() {
                                 return EnumSet.of(Characteristics.UNORDERED);
                             }
                         }
                );
    }
}
