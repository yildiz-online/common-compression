/*
 * MIT License
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.compression.zip;

import be.yildizgames.common.compression.FileInfoRetriever;
import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.ComputedHash;
import be.yildizgames.common.hashing.HashValue;
import be.yildizgames.common.hashing.HashingFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Grégory Van den Borre
 */
public class ZipFileInfoRetriever implements FileInfoRetriever {

    private final Path path;

    public ZipFileInfoRetriever(Path path) {
        super();
        this.path = path;
    }

    @Override
    public final List<HashValue> getFileInfo(Algorithm... algorithms) {
        if(algorithms == null || algorithms.length == 0) {
            return List.of();
        }
        var result = new ArrayList<HashValue>();
        try (ZipInputStream stream = new ZipInputStream(Files.newInputStream(path))) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                result.add(new HashValue(entry.getName(), Arrays.stream(algorithms).map(
                        a -> new ComputedHash(HashingFactory.get(a).compute(stream), a)
                ).collect(Collectors.toList())));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
