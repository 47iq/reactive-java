package org.iq47.reactivejava.stream;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import lombok.Getter;
import lombok.SneakyThrows;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BackpressureSubscriber implements FlowableSubscriber<Deal> {
    private final long BATCH_SIZE = 10;
    private final DealRepository dealRepository;
    @Getter
    Map<String, Double> result;
    private Subscription subscription;

    public BackpressureSubscriber(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }


    @Override
    public void onSubscribe(@NonNull Subscription s) {
        subscription = s;
        result = new ConcurrentHashMap<>();
        subscription.request(BATCH_SIZE);
    }

    @SneakyThrows
    @Override
    public void onNext(@NonNull Deal deal) {
        dealRepository.loadDataFromDb();
        String ticker = deal.getInstrument().getTicker();
        Double price = deal.getPrice();
        if (deal.getTradeDateTime().toLocalDate().equals(LocalDate.now())) {
            result.merge(ticker, price, Double::sum);
        }
        subscription.request(BATCH_SIZE);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
