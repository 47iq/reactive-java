package org.iq47.reactivejava;

import jakarta.annotation.PostConstruct;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.*;
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

import static org.iq47.reactivejava.ReactiveJavaApplicationTests.Source.*;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CalculationRunnerTest extends AbstractBenchmark {
    private static Map<ReactiveJavaApplicationTests.Source, MetricService> metricServiceMap;
    private static DealRepository dealRepository;
    private static DataProperties dataProperties;

    @Autowired
    void init(CustomStreamService customStreamService,
              DefaultStreamService defaultStreamService,
              ParallelStreamService parallelStreamService,
              ObservableService observableService,
              FlowableService flowableService,
              LoopService loopService,
              DataProperties dataProperties,
              RecordGenerator recordGenerator) {
        metricServiceMap = Map.of(
                CUSTOM, customStreamService,
                PARALLEL, parallelStreamService,
                LOOP, loopService,
                DEFAULT, defaultStreamService,
                OBSERVABLE, observableService,
                FLOWABLE, flowableService);
        dealRepository = new DealRepository();
        dealRepository.setDealsMap(recordGenerator.generateDeals(dataProperties.getDealQty().get(0)));
        dealRepository.setDelayEnabled(dataProperties.isDelayEnabled());
        CalculationRunnerTest.dataProperties = dataProperties;
    }

    @Benchmark
    public void calculationRunner() {
        MetricService metricService = metricServiceMap.get(ReactiveJavaApplicationTests.Source.valueOf(dataProperties.getServiceType()));
        execute(metricService, dealRepository);
    }

    public void execute(MetricService metricService, DealRepository dealRepository) {
        metricService.getTodayInstrumentTotalTradeVolume(dealRepository);
    }
}
