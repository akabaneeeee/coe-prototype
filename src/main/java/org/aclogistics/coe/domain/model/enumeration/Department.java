package org.aclogistics.coe.domain.model.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum Department {
    HR("Human Resource"),
    IT("Information Technology");

    private final String actualName;

    Department(String actualName) {
        this.actualName = actualName;
    }
}
