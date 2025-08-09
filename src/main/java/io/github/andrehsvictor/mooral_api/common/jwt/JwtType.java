package io.github.andrehsvictor.mooral_api.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtType {
    BEARER("Bearer"),
    REFRESH("Refresh"),
    ACTION("Action");

    private final String value;
}
