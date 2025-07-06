package andrehsvictor.mooral.common.email.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionEmailArgs {
    private String to;
    private String url;
    private String lifetime;
}
