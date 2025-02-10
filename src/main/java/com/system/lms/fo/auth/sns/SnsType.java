package com.system.lms.fo.auth.sns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum SnsType {

    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver"),
    ;

    private final String value;
}
