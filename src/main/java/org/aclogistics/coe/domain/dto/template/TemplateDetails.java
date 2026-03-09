package org.aclogistics.coe.domain.dto.template;

import java.util.Map;

/**
 * @author Rosendo Coquilla
 */
public record TemplateDetails(Map<String, Object> data, String templatePath) {

}
