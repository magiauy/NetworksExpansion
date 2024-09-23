package com.ytdd9527.networksexpansion.api.enums;

import lombok.Getter;

@Getter
public enum MCVersion {
    MC1_20(20, 0),
    MC1_20_1(20, 1),
    MC1_20_2(20, 2),
    MC1_20_3(20, 3),
    MC1_20_4(20, 4),
    MC1_20_5(20, 5),
    MC1_20_6(20, 6),
    MC1_21(21, 0),
    MC1_21_1(21, 1),
    UNKNOWN(-1, -1);

    private final int major;
    private final int minor;

    MCVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public static MCVersion of(int major, int minor) {
        for (MCVersion version : values()) {
            if (version.major == major && version.minor == minor) {
                return version;
            }
        }
        return UNKNOWN;
    }

    public boolean isAtLeast(MCVersion version) {
        return this.major > version.major || (this.major == version.major && this.minor >= version.minor);
    }
}
