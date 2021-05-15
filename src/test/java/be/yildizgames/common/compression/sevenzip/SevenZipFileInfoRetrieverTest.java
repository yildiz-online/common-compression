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

import be.yildizgames.common.compression.zip.ZipFileInfoRetriever;
import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.HashingFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
class SevenZipFileInfoRetrieverTest {

    @Test
    void testHash() {
        var crc1 = HashingFactory.get(Algorithm.CRC32).compute(Path.of("src/test/resources/test-hash.txt"));
        var crc2 = HashingFactory.get(Algorithm.CRC32).compute(Path.of("src/test/resources/test-hash2.txt"));
        var md51 = HashingFactory.get(Algorithm.MD5).compute(Path.of("src/test/resources/test-hash.txt"));
        var md52 = HashingFactory.get(Algorithm.MD5).compute(Path.of("src/test/resources/test-hash2.txt"));
        var sha11 = HashingFactory.get(Algorithm.SHA1).compute(Path.of("src/test/resources/test-hash.txt"));
        var sha12 = HashingFactory.get(Algorithm.SHA1).compute(Path.of("src/test/resources/test-hash2.txt"));

        var fileInfo = new SevenZipFileInfoRetriever(Path.of("src/test/resources/test-hash.7z")).getFileInfo(Algorithm.CRC32, Algorithm.MD5, Algorithm.SHA1);

        boolean found1 = false;
        boolean found2 = false;

        for(var fi : fileInfo) {
            if(fi.getName().equals("test-hash.txt")) {
                Assertions.assertEquals(crc1, fi.getHashes().get(0));
                Assertions.assertEquals(md51, fi.getHashes().get(1));
                Assertions.assertEquals(sha11, fi.getHashes().get(2));
                found1 = true;
            } else if (fi.getName().equals("test-hash2.txt")) {
                Assertions.assertEquals(crc2, fi.getHashes().get(0));
                Assertions.assertEquals(md52, fi.getHashes().get(1));
                Assertions.assertEquals(sha12, fi.getHashes().get(2));
                found2 = true;
            }
        }
        Assertions.assertTrue(found1);
        Assertions.assertTrue(found2);
    }

    @Test
    void testHashBigFile() {
        var crc1 = HashingFactory.get(Algorithm.CRC32).compute(Path.of("src/test/resources/amaryllis-4701720_1920.jpg"));
        var md51 = HashingFactory.get(Algorithm.MD5).compute(Path.of("src/test/resources/amaryllis-4701720_1920.jpg"));
        var sha11 = HashingFactory.get(Algorithm.SHA1).compute(Path.of("src/test/resources/amaryllis-4701720_1920.jpg"));

        var fileInfo = new SevenZipFileInfoRetriever(Path.of("src/test/resources/amaryllis-4701720_1920.7z")).getFileInfo(Algorithm.CRC32, Algorithm.MD5, Algorithm.SHA1);

        Assertions.assertEquals(crc1, fileInfo.get(0).getHashes().get(0));
        Assertions.assertEquals(md51, fileInfo.get(0).getHashes().get(1));
        Assertions.assertEquals(sha11, fileInfo.get(0).getHashes().get(2));
    }
}
