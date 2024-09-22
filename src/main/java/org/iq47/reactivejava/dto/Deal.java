package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Deal {
    private Long id;
    private String ticker;
    private Long sellerId;
    private Long buyerId;
    private Double price;
    private Currency currency;
    private Fee buyerFee;
    private Fee sellerFee;
    private OffsetDateTime tradeDateTime;

    @Data
    @Builder
    public static class Fee {
        private Double price;
        private Currency currency;
    }
}
