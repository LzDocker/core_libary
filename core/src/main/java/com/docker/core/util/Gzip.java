package com.docker.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhangshaofang on 2017/8/15.
 */

public class Gzip {

    public static byte[] zip(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(baos);
            gzipOutputStream.write(data);
            gzipOutputStream.flush();
            gzipOutputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip ERROR", e);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] unzip(byte[] zipData) {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos = null;
        try {
            gzi = new GZIPInputStream(new ByteArrayInputStream(zipData));
            bos = new ByteArrayOutputStream(zipData.length);
            int count = 0;
            byte[] tmp = new byte[1024];
            while ((count = gzi.read(tmp)) != -1) {
                bos.write(tmp, 0, count);
            }
            bos.flush();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("unGzip ERROR", e);
        } finally {
            if (gzi != null) {
                try {
                    gzi.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
