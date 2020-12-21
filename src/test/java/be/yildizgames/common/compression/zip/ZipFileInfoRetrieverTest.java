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

import be.yildizgames.common.hashing.Algorithm;
import be.yildizgames.common.hashing.ComputedHash;
import be.yildizgames.common.hashing.HashValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Grégory Van den Borre
 */
class ZipFileInfoRetrieverTest {

    @Test
    void testHash() {
        var expectedResult = List.of(
                new HashValue("test-hash.txt", List.of(new ComputedHash("ce2fb5a360962b1394c09cbfe998116f", Algorithm.MD5))),
                new HashValue("test-hash2.txt", List.of(new ComputedHash("0b501728d124430a56c4b42c0e6306d9", Algorithm.MD5)))
        );
        var result = new ZipFileInfoRetriever(Path.of("src/test/resources/test-hash.zip")).getFileInfo(Algorithm.MD5);
    }
}
