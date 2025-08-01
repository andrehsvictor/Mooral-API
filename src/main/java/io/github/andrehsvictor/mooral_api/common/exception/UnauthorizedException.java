package io.github.andrehsvictor.mooral_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -6052480707108787323L;

    public UnauthorizedException() {
        super("");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}
