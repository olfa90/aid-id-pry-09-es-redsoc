package com.andago.restlayer.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

public class HttpUtils {

		private static String FILENAME_PARAM = "filename";

		public static String getFilename(String content_disposition) {
			return splitContentDisposition(content_disposition).get(FILENAME_PARAM);
		}

		public static Map<String, String> splitContentDisposition(String content_disposition) {
	    	System.out.println("Content-disposition: " + content_disposition);
			String[] comps = content_disposition.split("[;]");
			String[] sub_comps;
			Map<String, String> header_map = new HashMap<String, String>();
			for(String comp : comps) {
				if(comp.contains("=")) {
					sub_comps = comp.split("[=]");
					if(sub_comps.length == 2) {
						header_map.put(sub_comps[0].trim(),
								sub_comps[1].trim().replaceAll("[\"]",""));
					}
				}
			}
			return header_map;
		}

		public static void showHeaders(HttpHeaders headers) {
			System.out.println("---------HTTP HEADERS-------------");
			System.out.println("Media type: " + headers.getMediaType());
	    	for(String header : headers.getRequestHeaders().keySet()) {
	    		System.out.println("Name: " + header+ " Value: " +
	    				headers.getRequestHeader(header));
	    	}
	    	System.out.println("----------------------------------------------");
		}

}