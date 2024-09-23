package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Instrument {
    private Long id;
    private List<Deal> deals;
    private String ticker;
    private Market market;
}
