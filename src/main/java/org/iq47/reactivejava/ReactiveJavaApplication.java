package org.iq47.reactivejava;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.dto.Account;
import org.iq47.reactivejava.repository.AccountRepository;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.service.CustomStreamService;
import org.iq47.reactivejava.service.DefaultStreamService;
import org.iq47.reactivejava.service.LoopService;
import org.iq47.reactivejava.service.ParallelStreamService;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;
import java.util.stream.Collectors;

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
    AccountRepository accountRepository;
    RecordGenerator recordGenerator;

    @PostConstruct
    public void calculateExecutionTime() {
        LocalDate today = LocalDate.now();
        for (int dealCnt : dataProperties.getDealQty()) {
            System.out.println("\nКоличество объектов: " + dealCnt);
            dealRepository.setDeals(recordGenerator.generateDeals(
                    accountRepository.getAccounts().values().stream().map(Account::getId).collect(Collectors.toList()),
                    dealCnt
            ));
            customStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            parallelStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            loopService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
            defaultStreamService.getTodayInstrumentTotalTradeVolume(today, dealRepository);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

}
