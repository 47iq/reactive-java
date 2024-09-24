package org.iq47.reactivejava.dto;

import lombok.Getter;

public enum Market {
    MOEX(new TimeTable(7, 18)),
    SPBEX(new TimeTable(7, 18)),
    HKEX(new TimeTable(9, 24));

    @Getter
    final TimeTable timeTable;

    Market(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public static Market valueOf(int code) {
        return switch (code) {
            case 0 -> MOEX;
            case 1 -> SPBEX;
            case 2 -> HKEX;
            default -> throw new IllegalArgumentException("Market not supported");
        };
    }

    public record TimeTable(int startHourUTC, int closeHourUTC) {
        public int getStartHourUTC() {
            return startHourUTC;
        }

        public int getCloseHourUTC() {
            return closeHourUTC;
        }
    }
}
