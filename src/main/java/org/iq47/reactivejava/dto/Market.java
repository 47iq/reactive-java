package org.iq47.reactivejava.dto;

public enum Market {
    MOEX,
    SPBEX,
    HKEX;
    public static Market valueOf(int code) {
        return switch (code) {
            case 0 -> MOEX;
            case 1 -> SPBEX;
            case 2 -> HKEX;
            default -> throw new IllegalArgumentException("Market not supported");
        };
    }
}
