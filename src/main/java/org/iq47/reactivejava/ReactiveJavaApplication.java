package org.iq47.reactivejava;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.iq47.reactivejava.autoconfigure.DataProperties;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebMvc
@AllArgsConstructor
public class ReactiveJavaApplication {

    DataProperties dataProperties;
    DealRepository dealRepository;
    RecordGenerator recordGenerator;
    ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }
}
