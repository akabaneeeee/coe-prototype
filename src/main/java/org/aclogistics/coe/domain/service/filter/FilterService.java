package org.aclogistics.coe.domain.service.filter;

import java.util.Arrays;
import java.util.List;
import org.aclogistics.coe.domain.dto.filter.BeneficiaryFilter;
import org.aclogistics.coe.domain.dto.filter.BusinessUnitFilter;
import org.aclogistics.coe.domain.dto.filter.DepartmentFilter;
import org.aclogistics.coe.domain.dto.filter.FilterDto;
import org.aclogistics.coe.domain.dto.filter.PurposeFilter;
import org.aclogistics.coe.domain.dto.filter.StatusFilter;
import org.aclogistics.coe.domain.model.enumeration.Beneficiary;
import org.aclogistics.coe.domain.model.enumeration.BusinessUnit;
import org.aclogistics.coe.domain.model.enumeration.Department;
import org.aclogistics.coe.domain.model.enumeration.Purpose;
import org.aclogistics.coe.domain.model.enumeration.Status;
import org.springframework.stereotype.Service;

/**
 * @author Rosendo Coquilla
 */
@Service
public class FilterService implements IFilterService {

    @Override
    public FilterDto getAllFilters() {
        List<BeneficiaryFilter> beneficiaries = Arrays.stream(Beneficiary.values())
            .map(beneficiary -> new BeneficiaryFilter(beneficiary, beneficiary.getValue()))
            .sorted()
            .toList();

        List<BusinessUnitFilter> businessUnits = Arrays.stream(BusinessUnit.values())
            .map(bu -> new BusinessUnitFilter(bu, bu.getActualName()))
            .sorted()
            .toList();

        List<DepartmentFilter> departments = Arrays.stream(Department.values())
            .map(dept -> new DepartmentFilter(dept, dept.getActualName()))
            .sorted()
            .toList();

        List<PurposeFilter> purposes = Arrays.stream(Purpose.values())
            .map(purpose -> new PurposeFilter(purpose, purpose.getFriendlyName()))
            .sorted()
            .toList();

        List<StatusFilter> statuses = Arrays.stream(Status.values())
            .map(status -> new StatusFilter(status, status.getFriendlyName()))
            .sorted()
            .toList();

        return FilterDto.builder()
            .beneficiaries(beneficiaries)
            .businessUnits(businessUnits)
            .departments(departments)
            .purposes(purposes)
            .statues(statuses)
            .build();
    }
}
