package com.apos.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Vector;

import org.junit.jupiter.api.Test;

public class TestDomHierarchieParse {

	@Test
	void testInit() {
		
		String xml = "<TAG>"
				+ "<HP/>"
				+ "<HP/>"
				+ "</TAG>";
		
		assertDoesNotThrow(()->{
			 new DomHierarchieParse(xml);
		});
		
		DomHierarchieParse parser = new DomHierarchieParse(xml);
		assertEquals("TAG", parser.getType());

		Vector<DomHierarchieParse> tags = parser.getHierarchie("HP", true);
		assertTrue(tags!=null);
		assertTrue(tags.size()==2);
		
		DomHierarchieParse tag = tags.get(0);
		assertEquals("HP", tag.getType());
		

		
	}
}
