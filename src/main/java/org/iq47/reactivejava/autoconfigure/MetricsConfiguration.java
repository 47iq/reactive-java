package org.iq47.reactivejava.autoconfigure;

import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.CustomStreamService;
import org.iq47.reactivejava.service.DefaultStreamService;
import org.iq47.reactivejava.service.LoopService;
import org.iq47.reactivejava.service.ParallelStreamService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class MetricsConfiguration {
    @Bean
    public ParallelStreamService streamService(
            @Qualifier("dealRepository1") DealRepository dealRepository1,
            @Qualifier("dealRepository2") DealRepository dealRepository2,
            @Qualifier("dealRepository3") DealRepository dealRepository3
    ) {
        System.out.println("\nParallel stream with a default collector");
        ParallelStreamService streamService = new ParallelStreamService(dealRepository1);
        LocalDate today = LocalDate.now();
        long start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository1.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        streamService = new ParallelStreamService(dealRepository2);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository2.getDeals().size() + " Calculated prices in " + executionTime + "ms");


        streamService = new ParallelStreamService(dealRepository3);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository3.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        return streamService;
    }

    @Bean
    public CustomStreamService customStreamService(
            @Qualifier("dealRepository1") DealRepository dealRepository1,
            @Qualifier("dealRepository2") DealRepository dealRepository2,
            @Qualifier("dealRepository3") DealRepository dealRepository3
            ) {
        System.out.println("\nParallel stream with a custom collector");
        CustomStreamService streamService = new CustomStreamService(dealRepository1);
        LocalDate today = LocalDate.now();
        long start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository1.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        streamService = new CustomStreamService(dealRepository2);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository2.getDeals().size() + " Calculated prices in " + executionTime + "ms");


        streamService = new CustomStreamService(dealRepository3);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository3.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        return streamService;
    }

    @Bean
    public LoopService loopService(
            @Qualifier("dealRepository1") DealRepository dealRepository1,
            @Qualifier("dealRepository2") DealRepository dealRepository2,
            @Qualifier("dealRepository3") DealRepository dealRepository3
    ) {
        System.out.println("\nLoop execution");
        LoopService streamService = new LoopService(dealRepository1);
        LocalDate today = LocalDate.now();
        long start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository1.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        streamService = new LoopService(dealRepository2);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository2.getDeals().size() + " Calculated prices in " + executionTime + "ms");


        streamService = new LoopService(dealRepository3);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository3.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        return streamService;
    }

    @Bean
    public DefaultStreamService defaultStreamService(
            @Qualifier("dealRepository1") DealRepository dealRepository1,
            @Qualifier("dealRepository2") DealRepository dealRepository2,
            @Qualifier("dealRepository3") DealRepository dealRepository3
    ) {
        System.out.println("\nNon-parallel stream");
        DefaultStreamService streamService = new DefaultStreamService(dealRepository1);
        LocalDate today = LocalDate.now();
        long start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository1.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        streamService = new DefaultStreamService(dealRepository2);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository2.getDeals().size() + " Calculated prices in " + executionTime + "ms");


        streamService = new DefaultStreamService(dealRepository3);
        today = LocalDate.now();
        start = System.currentTimeMillis();
        streamService.getTodayInstrumentTotalTradeVolume(today);
        executionTime = System.currentTimeMillis() - start;
        System.out.println("N=" + dealRepository3.getDeals().size() + " Calculated prices in " + executionTime + "ms");

        return streamService;
    }
}
