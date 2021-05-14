package com.yapp18.retrospect.auth.helper;

public enum SocialPlatform {
    GOOGLE("google"),
    KAKAO("kakao");

    private final String ROLE_PREFIX = "ROLE_";
    private String platform;

    SocialPlatform(String name) {
        this.platform = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + platform.toUpperCase();
    }

    public String getValue() {
        return platform;
    }

    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);
    }
}
