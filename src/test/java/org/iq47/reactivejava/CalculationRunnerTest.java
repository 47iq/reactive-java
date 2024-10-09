package org.iq47.reactivejava;

import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.CUSTOM;
import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.DEFAULT;
import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.LOOP;
import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.PARALLEL;

import jakarta.annotation.PostConstruct;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.CustomStreamService;
import org.iq47.reactivejava.service.DefaultStreamService;
import org.iq47.reactivejava.service.LoopService;
import org.iq47.reactivejava.service.MetricService;
import org.iq47.reactivejava.service.ParallelStreamService;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CalculationRunnerTest {

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

    private Map<ReactiveJavaApplicationTests.Source, MetricService> metricServiceMap;

    private final Map<Integer, DealRepository> repositoryMap = new HashMap<>();

    @PostConstruct
    void init() {
        metricServiceMap = Map.of(
                CUSTOM, customStreamService,
                PARALLEL, parallelStreamService,
                LOOP, loopService,
                DEFAULT, defaultStreamService);

        for (int cnt: dataProperties.getDealQty()) {
            DealRepository repo = new DealRepository();
            repo.setDealsMap(recordGenerator.generateDeals(cnt));
            repositoryMap.put(cnt, repo);
        }
    }

    @ParameterizedTest
    @MethodSource("calcProps_source")
    void calculationRunner(ReactiveJavaApplicationTests.Source source, int cnt) {
        System.out.println("Deals: " + cnt);
        System.out.println("Source: " + source.name());
        MetricService metricService = metricServiceMap.get(source);
        metricService.getTodayInstrumentTotalTradeVolume(repositoryMap.get(cnt));
    }

    public static List<Arguments> calcProps_source() {
        return List.of(
                Arguments.of(DEFAULT, 50000),
                Arguments.of(DEFAULT, 500000),
                Arguments.of(DEFAULT, 2500000),
                Arguments.of(PARALLEL, 50000),
                Arguments.of(PARALLEL, 500000),
                Arguments.of(PARALLEL, 2500000),
                Arguments.of(LOOP, 50000),
                Arguments.of(LOOP, 500000),
                Arguments.of(LOOP, 2500000),
                Arguments.of(CUSTOM, 50000),
                Arguments.of(CUSTOM, 500000),
                Arguments.of(CUSTOM, 2500000)
        );
    }
}
