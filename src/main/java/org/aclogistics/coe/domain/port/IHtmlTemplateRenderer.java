package org.aclogistics.coe.domain.port;

import org.aclogistics.coe.domain.dto.TemplateDetails;

/**
 * @author Rosendo Coquilla
 */
public interface IHtmlTemplateRenderer {

    String render(TemplateDetails details);
}
