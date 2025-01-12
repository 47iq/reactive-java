package org.iq47.reactivejava.stream;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BackpressureSubscriber implements FlowableSubscriber<Object> {
    private final long BATCH_SIZE = 10;
    private final DealRepository dealRepository;
    @Setter
    @Getter
    private int delay;
    @Getter
    Map<String, Double> result  = new ConcurrentHashMap<>();
    private Subscription subscription;

    public BackpressureSubscriber(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }


    @Override
    public void onSubscribe(@NonNull Subscription s) {
        subscription = s;
        subscription.request(BATCH_SIZE);
    }

    @SneakyThrows
    @Override
    public void onNext(@NonNull Object obj) {
        Deal deal = (Deal) obj;
        dealRepository.loadDataFromDb(delay);
        String ticker = deal.getInstrument().getTicker();
        Double price = deal.getPrice();
        if (deal.getTradeDateTime().toLocalDate().equals(LocalDate.now())) {
            result.merge(ticker, price, Double::sum);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(Arrays.toString(throwable.getStackTrace()));
    }

    @Override
    public void onComplete() {

    }
}
