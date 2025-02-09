package com.system.lms.fo.auth.sns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnsType {

    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver"),
    ;

    private final String value;
}
