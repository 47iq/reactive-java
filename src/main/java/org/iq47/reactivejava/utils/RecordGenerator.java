package org.iq47.reactivejava.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.dto.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
public class RecordGenerator {
    private final DataProperties dataProperties;
    public List<Instrument> generateInstruments() {
        return LongStream
                .rangeClosed(0, dataProperties.getTickers().size() - 1)
                .mapToObj(x -> Instrument.builder()
                        .id(x)
                        .ticker(dataProperties.getTickers().get((int) x))
                        .market(Market.valueOf((int) (Math.random() * 3)))
                        .deals(new ArrayList<>())
                        .build())
                .collect(Collectors.toList()
                );
    }

    public Map<Long, Deal> generateDeals(List<Long> accountIds, int dealQty) {
        LocalDate today = LocalDate.now();
        List<Instrument> instruments = generateInstruments();
        return LongStream
                .rangeClosed(0, dealQty)
                .mapToObj(x -> {
                    double priceRub = Math.random() * 1000;
                    Instrument instrument =  instruments.get((int) (Math.random() * instruments.size()));
                    List<Deal.Fee> fees = new ArrayList<>();
                    Currency currency = switch (instrument.getMarket()) {
                        case HKEX -> Currency.HKD;
                        case MOEX -> Currency.RUB;
                        case SPBEX -> Currency.USD;
                    };
                    fees.add(new Deal.Fee(priceRub * 0.01, Currency.RUB, Deal.FeeType.BROKER));
                    double price;
                    if (!currency.equals(Currency.RUB)) {
                        fees.add(new Deal.Fee(priceRub / currency.getToRub(), currency, Deal.FeeType.MARKET));
                        price = priceRub / currency.getToRub();
                    } else {
                        price = priceRub;
                    }
                    return Deal.builder()
                            .id(x)
                            .fees(fees)
                            .buyerId(accountIds.get((int) (Math.random() * accountIds.size())))
                            .sellerId(accountIds.get((int) (Math.random() * accountIds.size())))
                            .price(price)
                            .tradeDateTime(OffsetDateTime.of(
                                    today.getYear(),
                                    today.getMonth().getValue(),
                                    (int) (1 + Math.random() * 29),
                                    (int) (9 + Math.random() * 9),
                                    (int) (Math.random() * 60),
                                    (int) (Math.random() * 60),
                                    (int) (Math.random() * 60), ZoneOffset.UTC))
                            .currency(currency)
                            .instrument(instrument)
                            .build();
                })
                        .collect(Collectors.toMap(
                                Deal::getId,
                                Function.identity()
                ));
    }

    public Map<Long, Account> generateAccounts() {
        return LongStream
                .rangeClosed(0, dataProperties.getAccountQty())
                .mapToObj(x -> Account.builder()
                        .id(x)
                        .login(RandomStringUtils.randomAlphabetic(10))
                        .password(RandomStringUtils.randomAlphabetic(30))
                        .build())
                    .collect(Collectors.toMap(
                            Account::getId,
                            Function.identity()
                    ));
    }
}
