package org.aclogistics.coe.domain.model.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum EmploymentStatus {
    PERMANENT("Permanent"),
    PROBATIONARY("Probationary");

    private final String value;

    EmploymentStatus(String value) {
        this.value = value;
    }
}
