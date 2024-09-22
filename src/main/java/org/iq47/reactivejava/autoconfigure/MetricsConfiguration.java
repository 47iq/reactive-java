package org.iq47.reactivejava.autoconfigure;

import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.LoopService;
import org.iq47.reactivejava.service.StreamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

@Configuration
public class MetricsConfiguration {
    @Bean
    public StreamService streamService(DealRepository dealRepository) {
        StreamService streamService = new StreamService(dealRepository);
        long start = System.currentTimeMillis();
        Map<String, Double> prices = streamService.getTodayInstrumentMeanDealPrice();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("\nCalculated prices by stream: in " + executionTime + "ms");
        for (String keys : prices.keySet())
        {
            System.out.println("\t" + keys + ":"+ prices.get(keys));
        }
        return streamService;
    }

    @Bean
    public LoopService loopService(DealRepository dealRepository) {
        LoopService loopService = new LoopService(dealRepository);
        long start = System.currentTimeMillis();
        Map<String, Double> prices = loopService.getTodayInstrumentMeanDealPrice();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("\nCalculated prices by loop: in " + executionTime + "ms");
        for (String keys : prices.keySet())
        {
            System.out.println("\t" + keys + ":"+ prices.get(keys));
        }
        return loopService;
    }
}
