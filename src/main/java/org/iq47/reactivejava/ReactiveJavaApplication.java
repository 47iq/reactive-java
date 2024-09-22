package org.iq47.reactivejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class ReactiveJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

}
