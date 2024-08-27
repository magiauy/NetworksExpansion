package com.ytdd9527.networksexpansion.api.enums;

import com.ytdd9527.networksexpansion.utils.TextUtil;

public enum TransportMode {
    NONE,
    NULL_ONLY,
    NONNULL_ONLY,
    FIRST_ONLY,
    LAST_ONLY,
    FIRST_STOP,
    LAZY;

    public String getName() {
        return TextUtil.colorRandomString(getRawName());
    }

    public String getRawName() {
        return switch (this) {
            case NONE -> "Unlimited";
            case NULL_ONLY -> "Null Only";
            case NONNULL_ONLY -> "Non-Null Only";
            case FIRST_ONLY -> "Frist Only";
            case LAST_ONLY -> "Last Only";
            case FIRST_STOP -> "First Stop";
            case LAZY -> "Lazy";
        };
    }

    public TransportMode next() {
        int index = this.ordinal() + 1;
        if (index >= TransportMode.values().length) {
            index = 0;
        }
        return TransportMode.values()[index];
    }

    public TransportMode previous() {
        int index = this.ordinal() - 1;
        if (index < 0) {
            index = TransportMode.values().length - 1;
        }
        return TransportMode.values()[index];
    }
}
