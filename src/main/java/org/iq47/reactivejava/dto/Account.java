package org.iq47.reactivejava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    Long id;
    String description;
    String login;
    String password;
}
