package com.system.lms.fo.auth;

import com.system.lms.fo.client.EmailSender;
import com.system.lms.fo.client.EmailValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final EmailSender emailSender;


}
