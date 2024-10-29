package com.example.happenhere;

import com.example.happenhere.init.DbInit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class HappenhereApplicationTests {

	@MockBean
	DbInit dbInit;

	@Test
	void contextLoads() throws Exception {
    }
}