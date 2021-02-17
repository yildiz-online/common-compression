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

package be.yildizgames.common.compression.sevenzip;

import be.yildizgames.common.compression.FileInfoRetriever;
import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.ComputedHash;
import be.yildizgames.common.hashing.HashValue;
import be.yildizgames.common.hashing.HashingFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipFileInfoRetriever implements FileInfoRetriever {

    private final Path path;

    public SevenZipFileInfoRetriever(Path path) {
        super();
        this.path = path;
    }

    @Override
    public final List<HashValue> getFileInfo(Algorithm... algorithms) {
        if (algorithms == null || algorithms.length == 0) {
            return List.of();
        }
        var result = new ArrayList<HashValue>();
        Map<String, List<ComputedHash>> hashes = new HashMap<>();

        for (var a : algorithms) {
            try (var sevenZFile = new SevenZFile(this.path.toFile())) {
                for (var e : sevenZFile.getEntries()) {
                    if (!e.isDirectory()) {
                        var is = sevenZFile.getInputStream(e);
                        var hash = new ComputedHash(HashingFactory.get(a).compute(is, (int) e.getSize()), a);
                        if(!hashes.containsKey(e.getName())) {
                             hashes.put(e.getName(), new ArrayList<>());
                        }
                        hashes.get(e.getName()).add(hash);
                    }
                }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
        for(var entry : hashes.entrySet()) {
            result.add(new HashValue(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
