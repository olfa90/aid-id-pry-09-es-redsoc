package com.andago.util;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

public class HashGeneratorTest {
	
	private static Logger log = Logger.getLogger(HashGeneratorTest.class);
	
	@Test
	public void testGetEncoded() throws NoSuchAlgorithmException {
		String strBrut = "alonso@hotmail.com";
		String encodedStr = HashGenerator.getSuminHex(strBrut, "SHA");
		log.debug("EncodedStr: " + encodedStr);
	}
	
	
}