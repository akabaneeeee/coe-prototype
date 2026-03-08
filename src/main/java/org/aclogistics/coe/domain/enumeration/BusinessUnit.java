package org.aclogistics.coe.domain.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum BusinessUnit {
    ACL("AC Logistics Holdings Corporation"),
    AIR21("Air21 Holdings Inc."),
    CHI("Cargohaus Inc."),
    UF("U-Freight Phils., Inc."),
    LGC("LGC Logistics, Inc."),
    AMC("A-Movement Corporation"),
    AF2100("Airfreight 2100, Incorporated"),
    UO("U-Ocean Inc.");

    private final String actualName;

    BusinessUnit(String actualName) {
        this.actualName = actualName;
    }
}
