package org.iq47.reactivejava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

//    @PostConstruct
//    public void calculateExecutionTime() {
//        LocalDate today = LocalDate.now();
//        for (int dealCnt : dataProperties.getDealQty()) {
//            System.out.println("\nКоличество объектов: " + dealCnt);
//            dealRepository.setDealsMap(recordGenerator.generateDeals(dealCnt));
//            customStreamService.getTodayInstrumentTotalTradeVolume(dealRepository);
//            parallelStreamService.getTodayInstrumentTotalTradeVolume(dealRepository);
//            loopService.getTodayInstrumentTotalTradeVolume(dealRepository);
//            defaultStreamService.getTodayInstrumentTotalTradeVolume(dealRepository);
//        }
//    }

    @PostConstruct
    public void init() throws IOException {
        for (int dealCnt : dataProperties.getDealQty()) {
            String s = objectMapper.writeValueAsString(recordGenerator.generateDeals(dealCnt));
            File file = new File(String.format("deals_%s.json", dealCnt));
            file.createNewFile();
            PrintWriter out = new PrintWriter(file);
            out.println(s);
        }
    }

}
