package edu.tsinghua.hotmobi.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by mariotaku on 16/1/29.
 */
public class IOUtils {
    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            // Ignore
        }
    }
}
