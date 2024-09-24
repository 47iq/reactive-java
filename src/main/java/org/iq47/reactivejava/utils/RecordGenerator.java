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
                        .build())
                .collect(Collectors.toList()
                );
    }

    public Map<Long, Deal> generateDeals(int dealQty) {
        LocalDate today = LocalDate.now();
        List<Instrument> instruments = generateInstruments();
        List<Account> accounts = generateAccounts();
        return LongStream
                .rangeClosed(0, dealQty)
                .mapToObj(x -> {
                    double priceRub = Math.random() * 1000;
                    Instrument instrument = instruments.get((int) (Math.random() * instruments.size()));
                    List<Deal.Fee> fees = new ArrayList<>();
                    Currency currency = switch (instrument.getMarket()) {
                        case HKEX -> Currency.HKD;
                        case MOEX -> Currency.RUB;
                        case SPBEX -> Currency.USD;
                    };
                    fees.add(new Deal.Fee(priceRub * 0.02, Currency.RUB, Deal.FeeType.BROKER));
                    fees.add(new Deal.Fee(priceRub / currency.getToRub() * 0.01, currency, Deal.FeeType.MARKET));
                    double price = priceRub / currency.getToRub();
                    int minHour = instrument.getMarket().getTimeTable().getStartHourUTC();
                    int maxHour = instrument.getMarket().getTimeTable().getCloseHourUTC();
                    OffsetDateTime dateTime = OffsetDateTime.of(
                            today.getYear(),
                            today.getMonth().getValue(),
                            (int) (1 + Math.random() * 27),
                            (int) (minHour + Math.random() * (maxHour - minHour - 1)),
                            (int) (Math.random() * 60),
                            (int) (Math.random() * 60),
                            (int) (Math.random() * 60), ZoneOffset.UTC);
                    return Deal.builder()
                            .id(x)
                            .fees(fees)
                            .buyer(accounts.get((int) (Math.random() * accounts.size())))
                            .seller(accounts.get((int) (Math.random() * accounts.size())))
                            .price(price)
                            .tradeDateTime(dateTime)
                            .currency(currency)
                            .instrument(instrument)
                            .build();
                })
                .collect(Collectors.toMap(
                        Deal::getId,
                        Function.identity()
                ));
    }

    public List<Account> generateAccounts() {
        return LongStream
                .rangeClosed(0, dataProperties.getAccountQty())
                .mapToObj(x -> Account.builder()
                        .id(x)
                        .login(RandomStringUtils.randomAlphabetic(10))
                        .passwordHash(RandomStringUtils.randomAlphabetic(30))
                        .build())
                .collect(Collectors.toList());
    }
}
