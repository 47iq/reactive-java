package org.iq47.reactivejava.repository;

import lombok.Data;
import org.iq47.reactivejava.dto.Deal;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Service
public class DealRepository {
    private Map<Long, Deal> dealsMap;
    private boolean delayEnabled;

    public List<Deal> getDeals(){
        return dealsMap.values().stream().toList();
    }

    public void loadDataFromDb() throws InterruptedException {
        if (delayEnabled) {
            Thread.sleep(0, 1);
        }
    }
}
