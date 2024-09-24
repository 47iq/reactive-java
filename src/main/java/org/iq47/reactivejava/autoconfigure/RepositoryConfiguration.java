package org.iq47.reactivejava.autoconfigure;

import org.iq47.reactivejava.repository.AccountRepository;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public AccountRepository accountRepository(RecordGenerator recordGenerator) {
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.setAccounts(recordGenerator.generateAccounts());
        return accountRepository;
    }
}
