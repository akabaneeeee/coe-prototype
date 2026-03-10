package org.aclogistics.coe.domain.model.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum Beneficiary {
    EMPLOYEE("Employee"),
    CHILDREN("Children");

    private final String value;

    Beneficiary(String value) {
        this.value = value;
    }
}
