package andrehsvictor.mooral.exception.handler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String correlationId;
    private List<ValidationError> validationErrors;
    
}
