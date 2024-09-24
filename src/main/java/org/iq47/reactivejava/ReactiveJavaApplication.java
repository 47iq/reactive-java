package org.iq47.reactivejava;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.CustomStreamService;
import org.iq47.reactivejava.service.DefaultStreamService;
import org.iq47.reactivejava.service.LoopService;
import org.iq47.reactivejava.service.ParallelStreamService;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;

@SpringBootApplication
@EnableAspectJAutoProxy
@AllArgsConstructor
public class ReactiveJavaApplication {

    CustomStreamService customStreamService;
    ParallelStreamService parallelStreamService;
    LoopService loopService;
    DefaultStreamService defaultStreamService;
    DataProperties dataProperties;
    DealRepository dealRepository;
    RecordGenerator recordGenerator;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

    @PostConstruct
    public void calculateExecutionTime() {
        LocalDate today = LocalDate.now();
        for (int dealCnt : dataProperties.getDealQty()) {
            System.out.println("\nКоличество объектов: " + dealCnt);
            dealRepository.setDeals(recordGenerator.generateDeals(dealCnt));
            customStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            parallelStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            loopService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            defaultStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
        }
    }

}
