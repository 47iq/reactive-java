package org.iq47.reactivejava.autoconfigure;

import org.iq47.reactivejava.dto.Account;
import org.iq47.reactivejava.repository.AccountRepository;
import org.iq47.reactivejava.repository.DealRepository;
import org.iq47.reactivejava.utils.RecordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class RepositoryConfiguration {
    @Bean
    public DealRepository dealRepository1(AccountRepository accountRepository, RecordGenerator recordGenerator, DataProperties dataProperties) {
        DealRepository dealRepository = new DealRepository();
        dealRepository.setDeals(recordGenerator.generateDeals(
                accountRepository.getAccounts().values().stream().map(Account::getId).collect(Collectors.toList()),
                dataProperties.getDealQty().get(0)
        ));
        return dealRepository;
    }

    @Bean
    public DealRepository dealRepository2(AccountRepository accountRepository, RecordGenerator recordGenerator, DataProperties dataProperties) {
        DealRepository dealRepository = new DealRepository();
        dealRepository.setDeals(recordGenerator.generateDeals(
                accountRepository.getAccounts().values().stream().map(Account::getId).collect(Collectors.toList()),
                dataProperties.getDealQty().get(1)
        ));
        return dealRepository;
    }

    @Bean
    public DealRepository dealRepository3(AccountRepository accountRepository, RecordGenerator recordGenerator, DataProperties dataProperties) {
        DealRepository dealRepository = new DealRepository();
        dealRepository.setDeals(recordGenerator.generateDeals(
                accountRepository.getAccounts().values().stream().map(Account::getId).collect(Collectors.toList()),
                dataProperties.getDealQty().get(2)
        ));
        return dealRepository;
    }

    @Bean
    public AccountRepository accountRepository(RecordGenerator recordGenerator) {
        AccountRepository accountRepository = new AccountRepository();
        accountRepository.setAccounts(recordGenerator.generateAccounts());
        return accountRepository;
    }
}
