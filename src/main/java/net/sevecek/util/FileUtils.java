package net.sevecek.util;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class FileUtils {

    public byte[] readAllBytes(URL url) throws IOException {
        URLConnection pictureLoader = url.openConnection();
        long size = pictureLoader.getContentLengthLong();
        InputStream inputStream = pictureLoader.getInputStream();
        try {
            return readAllBytes(inputStream, size);
        } finally {
            inputStream.close();
        }
    }

    public byte[] readAllBytes(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            return readAllBytes(inputStream, file.length());
        } finally {
            inputStream.close();
        }
    }

    public byte[] readAllBytes(InputStream inputStream) throws IOException {
        return readAllBytes(inputStream, -1L);
    }

    public byte[] readAllBytes(InputStream inputStream, long size) throws IOException {
        if (size > (long) Integer.MAX_VALUE) {
            throw new ApplicationInternalException("Content size {0} is too large. Maximum is {0}", size, Integer.MAX_VALUE);
        }
        ByteArrayOutputStream buffer;
        if (size <= -1L) {
            buffer = new ByteArrayOutputStream();
        } else {
            buffer = new ByteArrayOutputStream((int) size);
        }
        copy(inputStream, buffer);
        return buffer.toByteArray();
    }

    public byte[] readAllBytes(Path path) throws IOException {
        InputStream inputStream = Files.newInputStream(path);
        try {
            return readAllBytes(inputStream, Files.size(path));
        } finally {
            inputStream.close();
        }
    }

    public void copy(InputStream inputStream, OutputStream outputStream) throws IOException, CancellationException {
        byte[] buffer = new byte[4096];
        while (true) {
            if (Thread.currentThread().isInterrupted()) throw new CancellationException();
            int count = inputStream.read(buffer);
            if (count == -1) break;
            outputStream.write(buffer, 0, count);
        }
    }

}
