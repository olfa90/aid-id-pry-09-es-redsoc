package com.andago.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class MD5Encoder {
	
	private static Logger log = Logger.getLogger(MD5Encoder.class);
	
	/** * Clase con métodos estáticos de cifrado * */
    public String getEncoded(String texto, String algoritmo) {
         String output="";
        try {
           
            byte[] textBytes = texto.getBytes();
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(textBytes);
            byte[] codigo = md.digest();            
            output = new String(codigo);
            
        } catch (NoSuchAlgorithmException ex) {
            log.error("Error encoding: " + ex.getMessage());
        }
        return output;

    }
}
