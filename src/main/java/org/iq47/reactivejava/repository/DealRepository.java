package org.iq47.reactivejava.repository;

import lombok.Data;
import org.iq47.reactivejava.dto.Deal;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Data
@Service
public class DealRepository {
    private Map<Long, Deal> deals;
}
