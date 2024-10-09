package org.iq47.reactivejava;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.*;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    DataProperties dataProperties;
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
                DEFAULT, defaultStreamService);
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
