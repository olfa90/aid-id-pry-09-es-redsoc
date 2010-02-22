package com.andago.util;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.andago.semanthic.annotation.person.impl.FOAFAnnotator;

public class MD5EncoderTest {
	
	private MD5Encoder encoder;
	private static Logger log = Logger.getLogger(MD5EncoderTest.class);
	
	@Before
	public void setUp() {
		this.encoder = new MD5Encoder();
	}
	
	@Test
	public void testGetEncoded() {
		String strBrut = "alonso@hotmail.com";
		String encodedStr = this.encoder.getEncoded(strBrut, "SHA1");
		log.debug("EncodedStr: " + encodedStr);
	}
	
	
}