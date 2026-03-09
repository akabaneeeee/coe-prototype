package org.aclogistics.coe.domain.model.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum Purpose {
    SPONSORSHIP("Sponsorship"),
    VISA_APPLICATION("Visa Application"),
    PAGIBIG_MP_LOAN("PAGIBIG Multi-Purpose Loan"),
    PAGIBIG_HOUSING_LOAN("PAGIBIG Housing Loan"),
    SCHOLARSHIP("Scholarship"),
    NO_LOAN_AND_ACCOUNTABILITIES("No Existing Loan and Accountabilities"),
    TRAVEL_PURPOSES("Travel Purposes"),
    APPROVED_LEAVE_APPLICATION("Approved Leave Application"),
    LOAN_APPLICATION("Loan Application"),
    SCHOOL_REQUIREMENTS("School Requirements");

    private final String friendlyName;

    Purpose(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
