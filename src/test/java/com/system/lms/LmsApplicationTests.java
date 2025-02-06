package com.system.lms;

import com.system.lms.fo.auth.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LmsApplicationTests {

	@Autowired
	public JwtHelper jwtHelper;

	@Test
	void contextLoads() {
		Map request = Map.of("email", "mock123@test.com");
		String jwtToken = jwtHelper.createJwt(request);
		boolean isValid = jwtHelper.checkJwt(jwtToken);

		assertTrue(isValid);
	}

}
