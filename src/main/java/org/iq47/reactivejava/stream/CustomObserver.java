package org.iq47.reactivejava.stream;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Getter;
import lombok.SneakyThrows;
import org.iq47.reactivejava.dto.Deal;
import org.iq47.reactivejava.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomObserver implements Observer<Deal> {
    private final DealRepository dealRepository;
    @Getter
    Map<String, Double> result;

    public CustomObserver(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        result = new ConcurrentHashMap<>();
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
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
    }
}
