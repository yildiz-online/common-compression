/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2018-2022 Grégory Van den Borre
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

package be.yildizgames.common.compression;

import be.yildizgames.common.compression.sevenzip.SevenZipArchiver;
import be.yildizgames.common.compression.sevenzip.SevenZipFileInfoRetriever;
import be.yildizgames.common.compression.sevenzip.SevenZipUnpacker;
import be.yildizgames.common.compression.zip.ZipArchiver;
import be.yildizgames.common.compression.zip.ZipFileInfoRetriever;
import be.yildizgames.common.compression.zip.ZipUnpacker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Grégory Van den Borre
 */
class CompressionFactoryTest {

    @Nested
    class ZipUnpackerGet {

        @Test
        void correctClass() {
            assertTrue(CompressionFactory.zipUnpacker() instanceof ZipUnpacker);
        }

        @Test
        void sameInstance() {
            assertSame(CompressionFactory.zipUnpacker(), CompressionFactory.zipUnpacker());
        }

    }

    @Nested
    class SevenZipUnpackerGet {

        @Test
        void correctClass() {
            assertTrue(CompressionFactory.sevenZipUnpacker(false) instanceof SevenZipUnpacker);
        }

        @Test
        void sameInstance() {
            assertSame(CompressionFactory.sevenZipUnpacker(false), CompressionFactory.sevenZipUnpacker(false));
        }

    }

    @Nested
    class ZipArchiverGet {

        @Test
        void correctClass() {
            assertTrue(CompressionFactory.zipArchiver() instanceof ZipArchiver);
        }

        @Test
        void sameInstance() {
            assertSame(CompressionFactory.zipUnpacker(), CompressionFactory.zipUnpacker());
        }

    }

    @Nested
    class SevenZipArchiverGet {

        @Test
        void correctClass() {
            assertTrue(CompressionFactory.sevenZipArchiver(false) instanceof SevenZipArchiver);
        }

        @Test
        void sameInstance() {
            assertSame(CompressionFactory.sevenZipArchiver(false), CompressionFactory.sevenZipArchiver(false));
        }

    }

    @Nested
    class ZipFileInfoGet {

        @Test
        void correctClass() {
            Assertions.assertTrue(CompressionFactory.zipFileInfo(Helper.getZipTestHashFile()) instanceof ZipFileInfoRetriever);
        }

    }

    @Nested
    class SevenZipFileInfoGet {

        @Test
        void correctClass() {
            Assertions.assertTrue(CompressionFactory.sevenZipFileInfo(Helper.get7zTestHashFile()) instanceof SevenZipFileInfoRetriever);
        }

    }

    @Nested
    class FileInfoGet {

        @Test
        void correctClassZip() {
            Assertions.assertTrue(CompressionFactory.fileInfo(Helper.getZipTestHashFile()) instanceof ZipFileInfoRetriever);
        }

        @Test
        void correctClass7Zip() {
            Assertions.assertTrue(CompressionFactory.fileInfo(Helper.get7zMultipleFiles()) instanceof SevenZipFileInfoRetriever);
        }

        @Test
        void wrongExtension() {
            var file = Helper.getPlainTestHashFile();
            Assertions.assertThrows(IllegalArgumentException.class, () -> CompressionFactory.fileInfo(file));
        }

    }

}
