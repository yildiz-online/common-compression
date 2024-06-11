/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2022-2024 Grégory Van den Borre
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

import be.yildizgames.common.compression.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;

/**
 * @author Grégory Van den Borre
 */
class SevenZipArchiverTest {

    @Nested
    class Pack {

        @Test
        void singleFile() throws Exception {
            var result = Files.createTempFile("test-SevenZipArchiverTest-singleFile", ".7z");
            var archiver = new SevenZipArchiver();
            archiver.pack(Helper.getPlainTestHashFile(), result);
        }

        @Test
        void multipleFiles() throws Exception {
            var result = Files.createTempFile("test-SevenZipArchiverTest-multipleFiles", ".7z");
            var archiver = new SevenZipArchiver();
            var file1 = Helper.getPlainTestHashFile();
            var file2 = Helper.getPlainBigFile();
            archiver.pack(List.of(file1, file2), result);

            var destination = Files.createTempDirectory("test-SevenZipArchiverTest-multipleFiles-unpack");
            var unpacker =  new SevenZipUnpacker();
            unpacker.unpack(result, destination, false, false);
            Assertions.assertTrue(Files.exists(destination.resolve(Helper.getPlainTestHashFile())));
            Assertions.assertTrue(Files.exists(destination.resolve(Helper.getPlainBigFile())));
        }

        @Test
        void directory() {

        }

        @Test
        void nonExistingFile() {

        }

        @Test
        void nonExistingDestination() {

        }
    }
}
