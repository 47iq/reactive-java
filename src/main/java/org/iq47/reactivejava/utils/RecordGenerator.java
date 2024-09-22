package org.iq47.reactivejava.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.dto.Account;
import org.iq47.reactivejava.dto.Currency;
import org.iq47.reactivejava.dto.Deal;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class RecordGenerator {
    private final DataProperties dataProperties;
    public Map<Long, Deal> generateDeals(List<Long> accountIds) {
        return IntStream
                .rangeClosed(0, dataProperties.getDealQty())
                .mapToObj(x -> Deal.builder()
                        .id((long) x)
                        .buyerFee(Deal.Fee
                                .builder()
                                .currency(Currency.RUB)
                                .price(Math.random() * 10)
                                .build()
                        )
                        .sellerFee(Deal.Fee
                                .builder()
                                .currency(Currency.RUB)
                                .price(Math.random() * 10)
                                .build()
                        )
                        .buyerId(accountIds.get((int) (Math.random() * accountIds.size())))
                        .sellerId(accountIds.get((int) (Math.random() * accountIds.size())))
                        .price(Math.random() * 10000)
                        .tradeDateTime(Math.random() > 0.5
                                ? OffsetDateTime.of(2024, 9, 22, 0, 0, 0, 0, ZoneOffset.UTC)
                                : OffsetDateTime.now()
                        )
                        .currency(Currency.RUB)
                        .ticker(dataProperties.getTickers().get((int) (Math.random() * dataProperties.getTickers().size())))
                        .build())
                        .collect(Collectors.toMap(
                        Deal::getId,
                        Function.identity()
                ));
    }

    public Map<Long, Account> generateAccounts() {
        return IntStream
                .rangeClosed(0, dataProperties.getAccountQty())
                .mapToObj(x -> Account.builder()
                        .description(RandomStringUtils.randomAlphabetic(10))
                        .id((long) x)
                        .login(RandomStringUtils.randomAlphabetic(10))
                        .password(RandomStringUtils.randomAlphabetic(30))
                        .build())
                    .collect(Collectors.toMap(
                            Account::getId,
                            Function.identity()
                    ));
    }
}
