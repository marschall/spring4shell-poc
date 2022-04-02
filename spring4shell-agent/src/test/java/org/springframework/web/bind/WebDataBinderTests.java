package org.springframework.web.bind;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebDataBinderTests {
	
	private WebDataBinder binder;

	@BeforeEach
	void setUp() {
		this.binder = new WebDataBinder(null);
	}

	@Test
	void getDisallowedFields() {
		assertArrayEquals(new String[]{"class.*", "Class.*", "*.class.*", "*.Class.*"}, this.binder.getDisallowedFields());
	}

}
