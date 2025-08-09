package io.github.andrehsvictor.mooral_api.common.jwt;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtClaim {
    TYPE("typ"),
    ACTION("action"),
    SCOPE("scope"),
    SESSION_ID("sid");

    final String value;

}
