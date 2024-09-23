package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class Deal {
    private Long id;
    private Instrument instrument;
    private Long sellerId;
    private Long buyerId;
    private Double price;
    private Currency currency;
    private List<Fee> fees;
    private OffsetDateTime tradeDateTime;
    public record Fee(Double price, Currency currency, FeeType feeType) { }
    public enum FeeType {
        BROKER,
        MARKET
    }
}
