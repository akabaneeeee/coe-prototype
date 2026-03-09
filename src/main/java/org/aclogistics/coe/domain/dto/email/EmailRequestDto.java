package org.aclogistics.coe.domain.dto.email;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aclogistics.coe.domain.dto.Dto;

/**
 * @author Rosendo Coquilla
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto implements Dto {

    @Serial
    private static final long serialVersionUID = -633237011854731769L;

    private String source;

    @Default
    private Set<String> to = new HashSet<>();

    @Default
    private Set<String> cc = new HashSet<>();

    @Default
    private Set<String> bcc = new HashSet<>();

    private String htmlContent;
    private String subject;
}
