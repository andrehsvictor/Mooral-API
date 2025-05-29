package andrehsvictor.mooral.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
    
    private String field;
    private String message;
    private Object rejectedValue;
    
}
