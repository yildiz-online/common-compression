/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2021-2024 Grégory Van den Borre
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

import be.yildizgames.common.compression.CompressionFactory;
import be.yildizgames.common.compression.Helper;
import be.yildizgames.common.compression.Unpacker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
class SevenZipUnpackerTest {

    @Nested
    class ExtractFiles {

        @Test
        void happyFlow() throws Exception {
            Unpacker unpacker = CompressionFactory.sevenZipUnpacker(false);
            Path zip = getDestinationPath();
            Assertions.assertFalse(Files.exists(zip.resolve("7zip-folder-1").resolve("7zip-file-1.txt")));
            unpacker.unpack(Helper.get7zMultipleFiles(), zip, true);
            Assertions.assertTrue(Files.exists(zip.resolve("7zip-folder-1").resolve("7zip-file-1.txt")));
            Assertions.assertTrue(Files.exists(zip.resolve("7zip-folder-1").resolve("7zip-folder-2").resolve("7zip-file-2.txt")));
        }

        @Test
        void discardSubDirectories() throws Exception {
            Unpacker unpacker = CompressionFactory.sevenZipUnpacker(false);
            Path zip = getDestinationPath();
            unpacker.unpack(Helper.get7zMultipleFiles(), zip, false, true);
            Assertions.assertTrue(Files.exists(zip.resolve("7zip-file-1.txt")));
            Assertions.assertTrue(Files.exists(zip.resolve("7zip-file-2.txt")));
        }
    }

    private static Path getDestinationPath() throws Exception {
        return Files.createTempDirectory("sevenzip");
    }
}
