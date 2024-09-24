package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Instrument {
    private Long id;
    private String ticker;
    private Market market;
}
