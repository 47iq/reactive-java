package org.iq47.reactivejava.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "data")
public class DataProperties {
    private List<String> tickers;
    private int accountQty;
    private int dealQty;
}
