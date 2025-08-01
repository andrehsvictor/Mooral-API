package io.github.andrehsvictor.mooral_api.shared.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {
    SESSION_ID("sid"),
    ACTION("action"),
    TYPE("typ");

    private final String value;

}
