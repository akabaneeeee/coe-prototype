package org.aclogistics.coe.domain.model.enumeration;

import lombok.Getter;

/**
 * @author Rosendo Coquilla
 */
@Getter
public enum Status {
    REQUESTED("Requested"),
    REJECTED("Rejected"),
    PENDING_LINE_MANAGER_APPROVAL("Pending Line Manager Approval"),
    LINE_MANAGER_APPROVED("Line Manager Approved"),
    HR_PROCESSING("HR Processing"),
    HR_APPROVED("HR Approved"),
    CERTIFICATE_READY("Employment Certificate Ready");

    private final String friendlyName;

    Status(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
