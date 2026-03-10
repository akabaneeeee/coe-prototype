package org.aclogistics.coe.application.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.filter.FilterDto;
import org.aclogistics.coe.domain.service.filter.IFilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ecgen/filters")
public class FilterController {

    private final IFilterService filterService;

    @GetMapping
    public ResponseEntity<FilterDto> getAllFilters() {
        return ResponseEntity.ok(filterService.getAllFilters());
    }
}
