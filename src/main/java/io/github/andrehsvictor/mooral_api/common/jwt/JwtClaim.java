package io.github.andrehsvictor.mooral_api.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {
    TYPE("typ"),
    ACTION("action"),
    SCOPE("scope"),
    SESSION_ID("sid");

    private final String value;
}
