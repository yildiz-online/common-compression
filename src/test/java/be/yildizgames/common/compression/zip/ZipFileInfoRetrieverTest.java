/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2020-2024 Grégory Van den Borre
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

import be.yildizgames.common.compression.Helper;
import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.HashingFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Grégory Van den Borre
 */
class ZipFileInfoRetrieverTest {

    @Test
    void testHash() {
        var crc1 = HashingFactory.get(Algorithm.CRC32).compute(Helper.getPlainTestHashFile());
        var crc2 = HashingFactory.get(Algorithm.CRC32).compute(Helper.getPlainTestHash2File());
        var md51 = HashingFactory.get(Algorithm.MD5).compute(Helper.getPlainTestHashFile());
        var md52 = HashingFactory.get(Algorithm.MD5).compute(Helper.getPlainTestHash2File());
        var sha11 = HashingFactory.get(Algorithm.SHA1).compute(Helper.getPlainTestHashFile());
        var sha12 = HashingFactory.get(Algorithm.SHA1).compute(Helper.getPlainTestHash2File());

        var fileInfo = new ZipFileInfoRetriever(Helper.getZipTestHashFile()).getFileInfo(Algorithm.CRC32, Algorithm.MD5, Algorithm.SHA1);

        Assertions.assertEquals(crc1, fileInfo.get(0).hashes().get(0));
        Assertions.assertEquals(md51, fileInfo.get(0).hashes().get(1));
        Assertions.assertEquals(sha11, fileInfo.get(0).hashes().get(2));

        Assertions.assertEquals(crc2, fileInfo.get(1).hashes().get(0));
        Assertions.assertEquals(md52, fileInfo.get(1).hashes().get(1));
        Assertions.assertEquals(sha12, fileInfo.get(1).hashes().get(2));
    }

    @Test
    void testHashBigFile() {
        var crc1 = HashingFactory.get(Algorithm.CRC32).compute(Helper.getPlainBigFile());
        var md51 = HashingFactory.get(Algorithm.MD5).compute(Helper.getPlainBigFile());
        var sha11 = HashingFactory.get(Algorithm.SHA1).compute(Helper.getPlainBigFile());

        var fileInfo = new ZipFileInfoRetriever(Helper.getZipBigFile()).getFileInfo(Algorithm.CRC32, Algorithm.MD5, Algorithm.SHA1);

        Assertions.assertEquals(crc1, fileInfo.get(0).hashes().get(0));
        Assertions.assertEquals(md51, fileInfo.get(0).hashes().get(1));
        Assertions.assertEquals(sha11, fileInfo.get(0).hashes().get(2));
    }
}
