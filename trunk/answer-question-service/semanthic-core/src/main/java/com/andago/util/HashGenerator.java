package com.andago.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class HashGenerator {

	private static Logger log = Logger.getLogger(HashGenerator.class);

	/** * Clase con métodos estáticos de cifrado * */
	public static String getSuminHex(String texto, String algoritmo)
			throws NoSuchAlgorithmException {
		String output = "";
		byte[] textBytes = texto.getBytes();
		MessageDigest md = MessageDigest.getInstance(algoritmo);
		md.update(textBytes);
		byte[] codigo = md.digest();
		for (int i = 0; i < codigo.length; i++) {
			output += Integer.toHexString((codigo[i] >> 4) & 0xf);
			output += Integer.toHexString(codigo[i] & 0xf);
		}

		return output;

	}
}
