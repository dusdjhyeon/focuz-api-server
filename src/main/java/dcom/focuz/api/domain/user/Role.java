package dcom.focuz.api.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "유저"),
    BLOCKED("ROLE_BLOCKED", "금지됨");

    private final String key;
    private final String title;
}
