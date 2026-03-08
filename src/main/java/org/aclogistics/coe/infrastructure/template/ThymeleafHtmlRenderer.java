package org.aclogistics.coe.infrastructure.template;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.domain.dto.TemplateDetails;
import org.aclogistics.coe.domain.port.IHtmlTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThymeleafHtmlRenderer implements IHtmlTemplateRenderer {

    private final TemplateEngine templateEngine;

    @Override
    public String render(TemplateDetails details) {
        if (Objects.isNull(details)) {
            throw new IllegalArgumentException("The provided template details is null");
        }

        Context context = new Context();
        context.setVariables(details.data());

        return templateEngine.process(details.templatePath(), context);
    }

//    @Override
//    public String render() {
//        Context context = new Context();
//        context.setVariable("dateCreated", DateTimeHelper.convertToFullDateFormat(LocalDate.now()));
//        context.setVariable("addressee", "Embassy of Japan in the Philippines");
//        context.setVariable("placeOfAddressee", null);
//        context.setVariable("fullName", "BRUCE WAYNE");
//        context.setVariable("businessUnit", "U-Freight Phils., Inc.");
//        context.setVariable("hireDate", DateTimeHelper.convertToFullDateFormat(LocalDate.of(2021, 4, 15)));
//        context.setVariable("employeeStatus", "REGULAR");
//        context.setVariable("position", "Software Development Lead");
//        context.setVariable("department", "Information Technology");
//        context.setVariable("withCompensation", true);
//        context.setVariable("annualAmount", "10,000,000.00");
//        context.setVariable("lastName", "WAYNE");
//        context.setVariable("purpose", "GREATER GOOD OF THE UNIVERSE");
//        context.setVariable("day", "5");
//        context.setVariable("month", "Mar");
//        context.setVariable("year", "2026");
//        context.setVariable("signature", signature);
//        context.setVariable("signatory", "JJ Teodoro");
//        context.setVariable("signatoryDesignation", "Chief Executive Officer");
//
//        return templateEngine.process("/coe/amc.html", context);
//    }
}
