package org.iq47.reactivejava.service;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.BackpressureSubscriber;
import org.iq47.reactivejava.stream.CustomObserver;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class FlowableService implements MetricService {
    private final BackpressureSubscriber backpressureSubscriber;

    public FlowableService(BackpressureSubscriber backpressureSubscriber) {
        this.backpressureSubscriber = backpressureSubscriber;
    }

    @Override
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        ExecutorService pool = newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(pool);
        Flowable<Deal> flowable = Observable.fromIterable(dealRepository.getDeals()).toFlowable(BackpressureStrategy.BUFFER);
        flowable
                .subscribeOn(scheduler)
                .subscribe(backpressureSubscriber);
        return backpressureSubscriber.getResult();
    }
}
