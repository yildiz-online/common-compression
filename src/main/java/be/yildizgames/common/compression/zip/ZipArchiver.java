/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2018-2024 Grégory Van den Borre
 More infos available: https://engine.yildiz-games.be
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.compression.zip;

import be.yildizgames.common.compression.Archiver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Grégory Van den Borre
 */
public class ZipArchiver implements Archiver {

    @Override
    public final void pack(Path file, Path destination) {
        try(var zos = new ZipOutputStream(Files.newOutputStream(destination))) {
            if(Files.isDirectory(file)) {
                zipDirectory(file.toFile(), file.toFile().getName(), zos);
            } else {
                doZip(file.getFileName().toString(), file.toFile(), zos);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public final void pack(List<Path> file, Path destination) {
        throw new UnsupportedOperationException();
    }

    private void zipDirectory(File folder, String parentFolder,
                              ZipOutputStream zos) throws IOException {
        var files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                    continue;
                }
                doZip(parentFolder + "/" + file.getName(), file, zos);
            }
        }

    }

    private void doZip(String name, File file, ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(name));
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
            }
        }
        zos.closeEntry();
    }
}
