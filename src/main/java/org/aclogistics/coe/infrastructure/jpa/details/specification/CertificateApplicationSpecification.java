package org.aclogistics.coe.infrastructure.jpa.details.specification;

import java.util.List;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.Purpose;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.aclogistics.coe.infrastructure.jpa.details.entity.LatestApplicationDetailsEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author Rosendo Coquilla
 */
public final class CertificateApplicationSpecification {

    private CertificateApplicationSpecification() {
        // do nothing
    }

    public static final String LATEST_TRANSITIONED_DT_KEY = "latestTransitionedDt";
    private static final String REQUESTER_EMAIL_KEY = "requesterEmail";
    private static final String LINE_MANAGER_EMAIL_KEY = "lineManagerEmail";
    private static final String LATEST_STATUS_KEY = "latestStatus";
    private static final String REFERENCE_NUMBER_KEY = "referenceNumber";
    private static final String BUSINESS_UNIT_KEY = "businessUnit";
    private static final String PURPOSE_KEY = "purpose";
    private static final String DEPARTMENT_KEY = "department";

    public static Specification<LatestApplicationDetailsEntity> hasRequesterEmail(String email) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(REQUESTER_EMAIL_KEY), email);
    }

    public static Specification<LatestApplicationDetailsEntity> hasLineManagerEmail(String email) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(LINE_MANAGER_EMAIL_KEY), email);
    }

    public static Specification<LatestApplicationDetailsEntity> hasStatusEquals(Status status) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(LATEST_STATUS_KEY), status);
    }

    public static Specification<LatestApplicationDetailsEntity> hasStatusIn(List<Status> statuses) {
        return (root, query, criteriaBuilder) ->
            root.get(LATEST_STATUS_KEY).in(statuses);
    }

    public static Specification<LatestApplicationDetailsEntity> hasReferenceNumberEquals(String referenceNumber) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(REFERENCE_NUMBER_KEY), referenceNumber);
    }

    public static Specification<LatestApplicationDetailsEntity> hasBusinessUnitIn(List<BusinessUnit> businessUnits) {
        return (root, query, criteriaBuilder) ->
            root.get(BUSINESS_UNIT_KEY).in(businessUnits);
    }

    public static Specification<LatestApplicationDetailsEntity> hasPurposeIn(List<Purpose> purposes) {
        return (root, query, criteriaBuilder) ->
            root.get(PURPOSE_KEY).in(purposes);
    }

    public static Specification<LatestApplicationDetailsEntity> hasDepartmentIn(List<Department> departments) {
        return (root, query, criteriaBuilder) ->
            root.get(DEPARTMENT_KEY).in(departments);
    }
}
