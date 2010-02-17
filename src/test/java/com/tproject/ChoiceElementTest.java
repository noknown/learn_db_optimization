package com.tproject;

import java.io.Serializable;

import junit.framework.TestCase;

public class ChoiceElementTest extends TestCase {
	
	public void testConstractor() {
		ChoiceElement ce = new ChoiceElement("id", "name");
		
		
		assertEquals(ce.getId(), "id");
		assertEquals(ce.getName(), "name");
	}
	
	public void testToString() {
		ChoiceElement ce = new ChoiceElement("id", "name");
		
		assertEquals(ce.toString(), "name");
	}

}
