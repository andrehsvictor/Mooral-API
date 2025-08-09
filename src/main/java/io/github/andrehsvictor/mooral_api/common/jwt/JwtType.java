package io.github.andrehsvictor.mooral_api.common.jwt;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtType {
    BEARER("Bearer"),
    REFRESH("Refresh"),
    ACTION("Action");

    final String value;

}
