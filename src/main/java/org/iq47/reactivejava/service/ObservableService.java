package org.iq47.reactivejava.service;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.CustomObserver;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class ObservableService implements MetricService {
    private final CustomObserver customObserver;

    public ObservableService(CustomObserver customObserver) {
        this.customObserver = customObserver;
    }

    @Override
    public Map<String, Double> getTodayInstrumentTotalTradeVolume(DealRepository dealRepository) {
        ExecutorService pool = newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(pool);
        Observable<Deal> observable = Observable.fromIterable(dealRepository.getDeals());
        observable
                .subscribeOn(scheduler)
                .subscribe(customObserver);
        return customObserver.getResult();
    }
}
