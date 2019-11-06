package com.tax.verify;

import com.tax.verify.jpa.ValidationQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerifyApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void validationQuery(){
		System.out.println(new ValidationQuery().getValidationQuery("org.postgresql.Driver"));
	}

}
