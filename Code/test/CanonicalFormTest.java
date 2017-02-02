package test;

import static org.junit.Assert.*;

import org.junit.Test;

import canonicalForm.CanonicalFormTask;

public class CanonicalFormTest {

	@Test
	public void testValidInputFormat() {
		assertFalse(CanonicalFormTask.validInputFormat("x^2 + 3.5xy + y = y^ - xy + y"));
	}
	@Test
	public void testValidInputFormat2() {
		assertTrue(CanonicalFormTask.validInputFormat("x^2 + 3.5xy + y = y^2 - xy + y"));
	}
	@Test
	public void testValidInputFormat3() {
		assertFalse(CanonicalFormTask.validInputFormat("x^2 + 3.5xy y = y^ - xy + y"));
	}

}
