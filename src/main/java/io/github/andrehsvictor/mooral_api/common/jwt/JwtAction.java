package io.github.andrehsvictor.mooral_api.common.jwt;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtAction {
    VERIFY_EMAIL("verify_email"),
    RESET_PASSWORD("reset_password"),
    UPDATE_PROFILE("change_email");

    final String value;
}
