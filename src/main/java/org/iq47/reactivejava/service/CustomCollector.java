package org.iq47.reactivejava.service;

import org.iq47.reactivejava.dto.Deal;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Component
public class CustomCollector implements Collector<Deal, Map<String, Double>, Map<String, Double>> {


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
        return EnumSet.of(
                Characteristics.UNORDERED,
                Characteristics.CONCURRENT,
                Characteristics.IDENTITY_FINISH
        );
    }
}