package org.aclogistics.coe.domain.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum Status {
    REQUESTED("Requested"),
    REJECTED("Rejected"),
    LINE_MANAGER_FOR_APPROVAL("Line Manager for Approval"),
    LINE_MANAGER_APPROVED("Line Manager Approved"),
    HR_PROCESSING("HR Processing"),
    HR_APPROVED("HR Approved"),
    CERTIFICATE_READY("Certificate Ready");

    private final String friendlyName;

    Status(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
