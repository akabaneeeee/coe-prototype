package org.aclogistics.coe.infrastructure.jpa.details.api;

import static org.aclogistics.coe.infrastructure.jpa.details.specification.CertificateApplicationSpecification.LATEST_TRANSITIONED_DT_KEY;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.application.filtered.CertificateApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.GetFilteredApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.Metadata;
import org.aclogistics.coe.domain.dto.application.filtered.PaginatedApplicationDto;
import org.aclogistics.coe.domain.model.enumeration.PageModule;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.aclogistics.coe.domain.port.ILatestApplicationDetailsRepository;
import org.aclogistics.coe.domain.utility.DateTimeHelper;
import org.aclogistics.coe.infrastructure.jpa.details.entity.LatestApplicationDetailsEntity;
import org.aclogistics.coe.infrastructure.jpa.details.repository.LatestApplicationDetailsJpaRepository;
import org.aclogistics.coe.infrastructure.jpa.details.specification.CertificateApplicationSpecification;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LatestApplicationDetailsRepository implements ILatestApplicationDetailsRepository {

    private final LatestApplicationDetailsJpaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedApplicationDto findAll(GetFilteredApplicationDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("The provided dto is null");
        }

        int page = dto.buildPage();
        int limit = dto.buildLimit();
        Pageable pageable = PageRequest.of(
            page, limit, Sort.by(Order.desc(LATEST_TRANSITIONED_DT_KEY))
        );

        Specification<LatestApplicationDetailsEntity> specification = buildSpecification(dto);

        long forApprovalCount = 0L;
        if (dto.isLineManager()) {
            forApprovalCount = repository.count(
                Specification.where(CertificateApplicationSpecification.hasLineManagerEmail(dto.getRequesterEmail()))
            );
        }

        return this.buildResponse(repository.findAll(specification, pageable), page, limit, forApprovalCount);
    }

    private Specification<LatestApplicationDetailsEntity> buildSpecification(GetFilteredApplicationDto dto) {
        Specification<LatestApplicationDetailsEntity> specification = Specification.where(null);

        if (PageModule.MY_APPLICATIONS.equals(dto.getModule())) {
            specification = specification.and(CertificateApplicationSpecification.hasRequesterEmail(dto.getRequesterEmail()));
        }

        if (PageModule.MY_APPROVALS.equals(dto.getModule())) {
            specification = specification.and(CertificateApplicationSpecification.hasLineManagerEmail(dto.getRequesterEmail()))
                .and(CertificateApplicationSpecification.hasStatusEquals(Status.PENDING_LINE_MANAGER_APPROVAL));
        }

        if (PageModule.APPLICATIONS.equals(dto.getModule())) {
            specification = specification.and(CertificateApplicationSpecification.hasStatusIn(
                List.of(
                    Status.REQUESTED, Status.LINE_MANAGER_APPROVED, Status.HR_PROCESSING,
                    Status.HR_APPROVED, Status.CERTIFICATE_READY
                )
            ));
        }

        if (StringUtils.isNotBlank(dto.getReferenceNumber())) {
            specification = specification.and(CertificateApplicationSpecification.hasReferenceNumberEquals(dto.getReferenceNumber()));
        }

        if (CollectionUtils.isNotEmpty(dto.getStatuses())) {
            specification = specification.and(CertificateApplicationSpecification.hasStatusIn(dto.getStatuses()));
        }

        if (CollectionUtils.isNotEmpty(dto.getBusinessUnits())) {
            specification = specification.and(CertificateApplicationSpecification.hasBusinessUnitIn(dto.getBusinessUnits()));
        }

        if (CollectionUtils.isNotEmpty(dto.getDepartments())) {
            specification = specification.and(CertificateApplicationSpecification.hasDepartmentIn(dto.getDepartments()));
        }

        if  (CollectionUtils.isNotEmpty(dto.getPurposes())) {
            specification = specification.and(CertificateApplicationSpecification.hasPurposeIn(dto.getPurposes()));
        }

        return specification;
    }

    private PaginatedApplicationDto buildResponse(
        Page<LatestApplicationDetailsEntity> entities, int page, int limit, long forApprovalCount
    ) {
        if (Objects.isNull(entities) || CollectionUtils.isEmpty(entities.getContent())) {
            var metadata = Metadata.builder()
                .limit(limit)
                .currentPage(page + 1)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .total(0)
                .currentCount(0)
                .forApprovalCount(forApprovalCount)
                .build();

            return new PaginatedApplicationDto(metadata);
        }

        var data = entities.getContent()
            .stream()
            .map(e -> CertificateApplicationDto.builder()
                .referenceNumber(e.getReferenceNumber())
                .fullName(e.constructFullName())
                .businessUnit(e.getBusinessUnit().getActualName())
                .department(e.getDepartment().getActualName())
                .purpose(e.getPurpose().getFriendlyName())
                .lineManagerFullName(e.constructLineManagerFullName())
                .lineManagerEmail(StringUtils.defaultIfBlank(e.getLineManagerEmail(), "N/A"))
                .status(e.getLatestStatus().getFriendlyName())
                .transitionedBy(e.getLatestTransitionedBy())
                .transitionedDt(DateTimeHelper.convertToFullDateTimeFormat(e.getLatestTransitionedDt()))
                .build()
            ).toList();

        var pageableDetails = entities.getPageable();
        var metadata = Metadata.builder()
            .limit(limit)
            .currentPage(page + 1)
            .totalPages(entities.getTotalPages())
            .hasNext(entities.hasNext())
            .hasPrevious(entities.hasPrevious())
            .total(entities.getTotalElements())
            .currentCount(
                (pageableDetails.getPageNumber() * pageableDetails.getPageSize())
                    + entities.getNumberOfElements()
            ).forApprovalCount(forApprovalCount)
            .build();

        return new PaginatedApplicationDto(metadata, data);
    }
}
