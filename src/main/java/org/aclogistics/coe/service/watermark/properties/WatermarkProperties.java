package org.aclogistics.coe.service.watermark.properties;

import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.aclogistics.coe.enumeration.BusinessUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Rosendo Coquilla
 */
@Getter
@Setter
@Component
@ConfigurationProperties("watermark")
public class WatermarkProperties {

    private Map<BusinessUnit, UnitConfig> units = new EnumMap<>(BusinessUnit.class);

    @Getter
    @Setter
    public static class UnitConfig {
        private String logoPath;
        private float imageWidth;
        private float imageHeight;
        private float x;
        private float y;
    }
}
