package org.iq47.reactivejava.service;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.stream.BackpressureSubscriber;
import org.iq47.reactivejava.stream.CustomObserver;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.reactivestreams.Publisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
@Slf4j
@Getter
public class FlowableService implements MetricService {
    private final BackpressureSubscriber backpressureSubscriber;
    private final RecordGenerator recordGenerator;
    private Scheduler scheduler;
    private ThreadPoolExecutor pool;
    private @NonNull Flowable<@NonNull Object> flowable;
    private int threadsCount = 1;
    private int inputSpeed;
    final BlockingQueue<Deal> queue = new LinkedBlockingQueue<>();
    ScheduledExecutorService inputScheduler = new ScheduledThreadPoolExecutor(1);
    private LocalDateTime lastScaleStartTime = LocalDateTime.now();
    private int lastRecordedQueueSize = 0;


    @PostConstruct
    public void init() {
        backpressureSubscriber.setDelay(100);
        inputSpeed = 10000;
        inputScheduler.scheduleWithFixedDelay(() -> {
            queue.offer(recordGenerator.generateDeal());
        }, inputSpeed, inputSpeed, TimeUnit.NANOSECONDS);

        pool = (ThreadPoolExecutor) newFixedThreadPool(1);
        scheduler = Schedulers.from(pool);
        flowable = Observable.fromStream(Stream.generate(() -> {
            try {
                return queue.take();
            } catch (InterruptedException e) {
            }
            return -1;
        })).toFlowable(BackpressureStrategy.ERROR);
    }

    @EventListener(classes = {ContextRefreshedEvent.class})
    public void handleMultipleEvents() {
        new Thread(() -> {
            flowable.forEach(
                    it -> Flowable
                            .just(it)
                            .map(x -> {
//                                System.out.println(Integer.MAX_VALUE - pool.getQueue().remainingCapacity());
                                if (Integer.MAX_VALUE - pool.getQueue().remainingCapacity() > 10000 &&
                                        Integer.MAX_VALUE - pool.getQueue().remainingCapacity() > lastRecordedQueueSize &&
                                        ChronoUnit.MILLIS.between(lastScaleStartTime, LocalDateTime.now()) > 100) {
                                    lastScaleStartTime = LocalDateTime.now();
                                    int currentThreadsCount = threadsCount;
                                    int currentDelay = backpressureSubscriber.getDelay();
                                    if (currentThreadsCount <= 21) {
                                        setThreadsCount(currentThreadsCount + 1);
                                        if (currentDelay > 1) {
                                            backpressureSubscriber.setDelay(currentDelay - 1);
                                        }
                                        lastRecordedQueueSize = Integer.MAX_VALUE - pool.getQueue().remainingCapacity();
                                    } else {
                                        if (currentDelay > 1) {
                                            backpressureSubscriber.setDelay(currentDelay - 1);
                                            lastRecordedQueueSize = Integer.MAX_VALUE - pool.getQueue().remainingCapacity();
                                        }
                                    }
                                }
                                return x;
                            })
                            .subscribeOn(scheduler)
                            .subscribe(backpressureSubscriber)
            );
        }).start();
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
        this.inputScheduler.shutdown();
        ScheduledExecutorService inputScheduler = new ScheduledThreadPoolExecutor(1);
        inputScheduler.scheduleWithFixedDelay(() -> {
            queue.offer(recordGenerator.generateDeal());
        }, inputSpeed, inputSpeed, TimeUnit.NANOSECONDS);
        this.inputScheduler = inputScheduler;
    }

    public Map<String, Double> getCurrentMetrics() {
        return backpressureSubscriber.getResult();
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
        if (threadsCount > pool.getMaximumPoolSize()) {
            pool.setMaximumPoolSize(threadsCount);
            pool.setCorePoolSize(threadsCount);
        } else {
            pool.setCorePoolSize(threadsCount);
            pool.setMaximumPoolSize(threadsCount);
        }
    }

    public int getQueueLength() {
        return 1; //todo
    }
}
