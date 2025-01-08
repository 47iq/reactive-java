package org.iq47.reactivejava.service;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.BackpressureSubscriber;
import org.iq47.reactivejava.stream.CustomObserver;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
@Getter
public class FlowableService implements MetricService {
    private final BackpressureSubscriber backpressureSubscriber;
    private final RecordGenerator recordGenerator;
    private Scheduler scheduler;
    private Flowable<Deal> flowable;
    private int inputSpeed = 10000;

    @PostConstruct
    public void init() {
        ExecutorService pool = newFixedThreadPool(1);
        scheduler = Schedulers.from(pool);
        flowable = Observable.fromStream(Stream.generate(recordGenerator::generateDeal)).toFlowable(BackpressureStrategy.BUFFER);
        backpressureSubscriber.setDelay(inputSpeed);
        flowable.subscribeOn(scheduler)
                .subscribe(backpressureSubscriber);
    }

    public FlowableService(BackpressureSubscriber backpressureSubscriber, RecordGenerator recordGenerator) {
        this.backpressureSubscriber = backpressureSubscriber;
        this.recordGenerator = recordGenerator;
    }

    @Override
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
//        ExecutorService pool = newFixedThreadPool(1);
//        Scheduler scheduler = Schedulers.from(pool);
//        Flowable<Deal> flowable = Observable.fromIterable(dealRepository.getDeals()).toFlowable(BackpressureStrategy.BUFFER);
//        flowable
//                .subscribeOn(scheduler)
//                .subscribe(backpressureSubscriber);
//        return backpressureSubscriber.getResult();
        return null;
    }

    public void updateConfig(int inputSpeed) {
        this.inputSpeed = inputSpeed;
        backpressureSubscriber.setDelay(inputSpeed);
    }

    public Map<String, Double> getCurrentMetrics() {
        return backpressureSubscriber.getResult();
    }

    public int getQueueLength() {
        return 1; //todo
    }
}
