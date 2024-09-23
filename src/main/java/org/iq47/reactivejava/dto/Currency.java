package org.iq47.reactivejava.dto;

import lombok.Getter;

public enum Currency {
    RUB(1),
    USD(90),
    EUR(100),
    HKD(13);
    @Getter
    private final double toRub;
    Currency(double toRub) {
        this.toRub = toRub;
    }
}
