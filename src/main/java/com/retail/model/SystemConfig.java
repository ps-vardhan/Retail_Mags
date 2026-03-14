package com.retail.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class SystemConfig {
    @Id
    private String configKey;
    private String configValue;
    private String encryptedValue;
}
