package org.iq47.reactivejava.repository;

import lombok.Data;
import org.iq47.reactivejava.dto.Instrument;

import java.util.Map;

@Data
public class InstrumentRepository {
    private Map<Long, Instrument> instruments;
}
