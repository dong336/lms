package com.system.lms.fo.common;

import jakarta.servlet.http.Cookie;

public class CookieBuilder {
    private String name;
    private String value;
    private int maxAge = -1; // 기본값: 세션 쿠키
    private String path = "/";
    private boolean httpOnly = false;
    private boolean secure = false;

    public static CookieBuilder builder() {
        return new CookieBuilder();
    }

    public CookieBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CookieBuilder value(String value) {
        this.value = value;
        return this;
    }

    public CookieBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieBuilder secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public Cookie build() {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        return cookie;
    }
}
