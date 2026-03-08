package org.aclogistics.coe.domain.utility;

import java.text.DecimalFormat;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

/**
 * @author Rosendo Coquilla
 */
@UtilityClass
public class NumberUtility {

    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00");

    public String format(Number value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        return DECIMAL_FORMATTER.format(value);
    }
}
