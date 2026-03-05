package org.aclogistics.coe.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aclogistics.coe.utility.DateTimeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Rosendo Coquilla
 */
@Slf4j
@Setter
@Service
@RequiredArgsConstructor
public class HtmlTemplateRenderer {

    @Value("${app.signature}")
    private String signature;

    private final TemplateEngine templateEngine;

    public String render() {
        Context context = new Context();
        context.setVariable("dateCreated", DateTimeHelper.convertToFullDateFormat(LocalDate.now()));
        context.setVariable("addressee", "Embassy of Japan in the Philippines");
        context.setVariable("placeOfAddressee", null);
        context.setVariable("fullName", "BRUCE WAYNE");
        context.setVariable("businessUnit", "U-Freight Phils., Inc.");
        context.setVariable("hireDate", DateTimeHelper.convertToFullDateFormat(LocalDate.of(2021, 04, 15)));
        context.setVariable("employeeStatus", "REGULAR");
        context.setVariable("position", "Software Development Lead");
        context.setVariable("department", "Information Technology");
        context.setVariable("withCompensation", true);
        context.setVariable("annualAmount", "10,000,000.00");
        context.setVariable("lastName", "WAYNE");
        context.setVariable("purpose", "GREATER GOOD OF THE UNIVERSE");
        context.setVariable("day", "5");
        context.setVariable("month", "Mar");
        context.setVariable("year", "2026");
        context.setVariable("signature", signature);
        context.setVariable("signatory", "JJ Teodoro");
        context.setVariable("signatoryDesignation", "Chief Executive Officer");

        return templateEngine.process("/lgc-coe.html", context);
    }
}
