package com.dreamteam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommonUtils {

	public static byte[] getBytes(Object obj) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectOutputStream os;

		try {
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toByteArray();
	}

	public static String generateChannelName(String nodeTo, String nodeFrom) {
		if (nodeFrom.compareTo(nodeTo) < 0)
			return nodeFrom + "_" + nodeTo;
		else
			return nodeTo + "_" + nodeFrom;

	}

}
