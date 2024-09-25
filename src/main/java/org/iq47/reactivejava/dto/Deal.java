package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Deal {
    private Long id;
    private Instrument instrument;
    private Account seller;
    private Account buyer;
    private Account referrer;
    private Double price;
    private Currency currency;
    private List<Fee> fees;
    private OffsetDateTime tradeDateTime;

    public enum FeeType {
        BROKER,
        MARKET
    }

    public class Fee {
        Double feePrice;
        Currency feeCurrency;
        FeeType feeType;

        public Fee(FeeType feeType) {
            this.feeType = feeType;
            this.feePrice = price * currency.getToRub() * 0.02;
            this.feeCurrency = switch (instrument.getMarket()) {
                case HKEX -> Currency.HKD;
                case MOEX -> Currency.RUB;
                case SPBEX -> Currency.USD;
            };
        }
    }

    public void addFee(FeeType feeType) {
        fees.add(new Fee(feeType));
    }

    public void setInstrument(Instrument instrument) {
        Currency currency = switch (instrument.getMarket()) {
            case HKEX -> Currency.HKD;
            case MOEX -> Currency.RUB;
            case SPBEX -> Currency.USD;
        };
        this.instrument = instrument;
        this.currency = currency;
    }
}
