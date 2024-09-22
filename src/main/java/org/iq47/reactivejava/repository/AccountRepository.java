package org.iq47.reactivejava.repository;

import lombok.Data;
import org.iq47.reactivejava.dto.Account;

import java.util.Map;
import java.util.Set;

@Data
public class AccountRepository {
    private Map<Long, Account> accounts;
}
