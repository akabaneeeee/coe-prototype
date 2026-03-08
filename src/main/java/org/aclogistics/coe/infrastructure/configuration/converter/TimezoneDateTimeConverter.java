package org.aclogistics.coe.infrastructure.configuration.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import java.util.Objects;
import org.aclogistics.coe.domain.utility.DateTimeHelper;

/**
 * @author Rosendo Coquilla
 */
@Converter(autoApply = true)
public class TimezoneDateTimeConverter implements AttributeConverter<LocalDateTime, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }

        return DateTimeHelper.convertToUTCDefaultFormat(dateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }

        return DateTimeHelper.convertToPHTDefaultFormat(dateTime);
    }
}
