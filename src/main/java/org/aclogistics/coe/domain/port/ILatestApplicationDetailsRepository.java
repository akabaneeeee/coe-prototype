package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.dto.application.filtered.GetFilteredApplicationDto;
import org.aclogistics.coe.domain.dto.application.filtered.PaginatedApplicationDto;

/**
 * @author Rosendo Coquilla
 */
public interface ILatestApplicationDetailsRepository {

    PaginatedApplicationDto findAll(GetFilteredApplicationDto dto);
}
