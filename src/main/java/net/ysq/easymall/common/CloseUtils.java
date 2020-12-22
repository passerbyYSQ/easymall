package net.ysq.easymall.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author	passerbyYSQ
 * @date	2020-9-29 14:13:03
 */
public class CloseUtils {
	
	public static void close(Closeable... closeables) {
		if (closeables == null) {
			return;
		}
		
		for (Closeable closeable : closeables) {
			try {
				if (closeable != null) {
					closeable.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
