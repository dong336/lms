package com.system.lms;

import com.system.lms.fo.auth.jwt.JwtCustomClaims;
import com.system.lms.fo.auth.jwt.JwtHelper;
import com.system.lms.fo.service.FaqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LmsApplicationTests {

	@Autowired
	public JwtHelper jwtHelper;

	@Autowired
	public FaqService faqService;

	@Test
	void contextLoads() {
		JwtCustomClaims customClaims = new JwtCustomClaims("", "email", "mock123@test.com");
		String jwtToken = jwtHelper.createJwt(customClaims);
		boolean isValid = jwtHelper.checkJwt(jwtToken);

		assertTrue(isValid);
	}

	@Test
	void serviceTest() {
	}

}
