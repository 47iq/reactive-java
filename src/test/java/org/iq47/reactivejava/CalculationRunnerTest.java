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
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CalculationRunnerTest extends AbstractBenchmark {
    private static Map<ReactiveJavaApplicationTests.Source, MetricService> metricServiceMap;
    private static DealRepository dealRepository;

    @Autowired
    void init(CustomStreamService customStreamService,
              DefaultStreamService defaultStreamService,
              ParallelStreamService parallelStreamService,
              LoopService loopService,
              DataProperties dataProperties,
              RecordGenerator recordGenerator) {
        metricServiceMap = Map.of(
                CUSTOM, customStreamService,
                PARALLEL, parallelStreamService,
                LOOP, loopService,
                DEFAULT, defaultStreamService);
        dealRepository = new DealRepository();
        dealRepository.setDealsMap(recordGenerator.generateDeals(dataProperties.getDealQty().get(0)));
        dealRepository.setDelayEnabled(dataProperties.isDelayEnabled());
    }

    @Benchmark
    @MethodSource("calcProps_source")
    public void calculationRunner() {
        MetricService metricService = metricServiceMap.get(LOOP);
        execute(metricService, dealRepository);
    }

    public void execute(MetricService metricService, DealRepository dealRepository) {
        metricService.getTodayInstrumentTotalTradeVolume(dealRepository);
    }

    public static List<Arguments> calcProps_source() {
        return List.of(
                Arguments.of(DEFAULT, 5000),
                Arguments.of(DEFAULT, 50000),
                Arguments.of(DEFAULT, 250000),
                Arguments.of(PARALLEL, 5000),
                Arguments.of(PARALLEL, 50000),
                Arguments.of(PARALLEL, 250000),
                Arguments.of(LOOP, 5000),
                Arguments.of(LOOP, 50000),
                Arguments.of(LOOP, 250000),
                Arguments.of(CUSTOM, 5000),
                Arguments.of(CUSTOM, 50000),
                Arguments.of(CUSTOM, 250000)
        );
    }
}
