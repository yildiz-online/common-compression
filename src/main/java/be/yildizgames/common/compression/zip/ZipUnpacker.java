/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */

package be.yildizgames.common.compression.zip;

import be.yildizgames.common.compression.Unpacker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Grégory Van den Borre
 */
public class ZipUnpacker implements Unpacker {

    /**
     * Size of the buffer to use.
     */
    private static final int BUFFER_SIZE = 1024;


    @Override
    public final void unpack(Path archive, Path destination, boolean keepRootDir) {
        try (ZipFile file = new ZipFile(URLDecoder.decode(archive.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {
            String rootDir = "";
            Files.createDirectories(destination);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipentry = entries.nextElement();
                if (zipentry.isDirectory()) {
                    if (!keepRootDir && rootDir.isEmpty()) {
                        rootDir = zipentry.getName();
                    } else {
                        Files.createDirectories(destination.toAbsolutePath().resolve(zipentry.getName().replace(rootDir, "")));
                    }
                } else {
                    Path current;
                    if (keepRootDir) {
                        current = destination.toAbsolutePath().resolve(zipentry.getName());
                    } else {
                        current = destination.toAbsolutePath().resolve(zipentry.getName().replace(rootDir, ""));
                    }
                    if(current.getParent() != null) {
                        Files.createDirectories(current.getParent());
                    }
                    try(InputStream in = file.getInputStream(zipentry); OutputStream out = Files.newOutputStream(current)) {
                        extractFile(in, out);
                    }
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override
    public final void unpackDirectoryToDirectory(Path archive, String directoryToExtract, Path destination) {
        try (ZipFile file = new ZipFile(URLDecoder.decode(archive.toAbsolutePath().toString(), StandardCharsets.UTF_8))) {
            Files.createDirectories(destination);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipentry = entries.nextElement();
                String name = zipentry.getName().replace("/", archive.getFileSystem().getSeparator());
                if (name.startsWith(directoryToExtract + archive.getFileSystem().getSeparator())) {
                    if (zipentry.isDirectory() && Files.notExists(destination.resolve(zipentry.getName()))) {
                        Files.createDirectory(destination.resolve(zipentry.getName()));
                    } else if(!zipentry.isDirectory()) {
                        Path current = destination.resolve(zipentry.getName());
                        try(InputStream in = file.getInputStream(zipentry); OutputStream out = Files.newOutputStream(current)) {
                            extractFile(in, out);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Error unpacking" + archive.toString() + ":" + destination.toString(), ioe);
        }
    }

    /**
     * Logic to extract the file.
     *
     * @param in  Zip input stream.
     * @param out File output stream.
     * @throws IOException If Exception occurs during the copy.
     */
    private static void extractFile(final InputStream in, final OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int l;
        while ((l = in.read(buf)) >= 0) {
            out.write(buf, 0, l);
        }
    }
}
