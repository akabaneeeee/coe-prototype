package org.aclogistics.coe.domain.dto.upload;

import java.io.Serial;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.dto.Dto;

/**
 * @author Rosendo Coquilla
 */
@Data
@NoArgsConstructor
public class UploadFileResultDto implements Dto {

    @Serial
    private static final long serialVersionUID = -7826867994744697524L;

    private String key;
    private String bucket;
}
