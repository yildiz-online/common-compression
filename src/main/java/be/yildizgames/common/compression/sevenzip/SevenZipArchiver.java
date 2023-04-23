/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2021-2023 Grégory Van den Borre
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

package be.yildizgames.common.compression.sevenzip;

import be.yildizgames.common.compression.Archiver;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class is able to compress into the 7zip archive format.
 *
 * @author Grégory Van den Borre
 */
public class SevenZipArchiver implements Archiver {

    @Override
    public final void pack(Path file, Path destination) {
        try (SevenZOutputFile out = new SevenZOutputFile(destination.toFile())) {
            addToArchiveCompression(out, file, "");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void pack(List<Path> files, Path destination) {
        throw new UnsupportedOperationException();
    }

    private static void addToArchiveCompression(SevenZOutputFile out, Path file, String dir) {
        var n = file.getFileName().toString();
        if(!dir.isEmpty()) {
            n = dir + "/" + n;
        }
        var name = n;
        try {
            if (Files.isRegularFile(file)) {
                var entry = out.createArchiveEntry(file, name);
                out.putArchiveEntry(entry);
                try (var in = Files.newInputStream(file)) {
                    var b = new byte[1024];
                    int count;
                    while ((count = in.read(b)) > 0) {
                        out.write(b, 0, count);
                    }
                    out.closeArchiveEntry();
                }
            } else if (Files.isDirectory(file)) {
                try (var files = Files.list(file)) {
                    files.forEach(f -> addToArchiveCompression(out, f, name));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
