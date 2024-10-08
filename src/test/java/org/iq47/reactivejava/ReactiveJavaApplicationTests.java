package org.iq47.reactivejava;

import jakarta.annotation.PostConstruct;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.*;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReactiveJavaApplicationTests {
    private final int DEALS_CNT = 1000000;
    enum Source {
        CUSTOM,
        PARALLEL,
        LOOP,
        DEFAULT
    }
    private Map<Source, MetricService> metricServiceMap;
    private Map<String, Double> defaultResult;
    @Autowired
    CustomStreamService customStreamService;
    @Autowired
    DefaultStreamService defaultStreamService;
    @Autowired
    ParallelStreamService parallelStreamService;
    @Autowired
    LoopService loopService;
    @Autowired
    DealRepository dealRepository;
    @Autowired
    RecordGenerator recordGenerator;

    @BeforeEach
    void init() {
        metricServiceMap = Map.of(
                CUSTOM, customStreamService,
                PARALLEL, parallelStreamService,
                LOOP, loopService,
                DEFAULT, defaultStreamService
        );
        dealRepository.setDealsMap(recordGenerator.generateDeals(DEALS_CNT));
        defaultResult = defaultStreamService.getTodayInstrumentTotalTradeVolume(dealRepository);
    }

    @ParameterizedTest
    @EnumSource(Source.class)
    void testEqualResults(Source source) {
        MetricService metricService = metricServiceMap.get(source);
        Map<String, Double> result = metricService.getTodayInstrumentTotalTradeVolume(dealRepository);
        for (var entry: result.entrySet()) {
            assertEquals(entry.getValue(), defaultResult.get(entry.getKey()), 0.0000001d);
        }
    }

}
