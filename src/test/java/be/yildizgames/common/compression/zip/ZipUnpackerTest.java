/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
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

package be.yildizgames.common.compression.zip;

import be.yildizgames.common.compression.CompressionFactory;
import be.yildizgames.common.compression.Unpacker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Grégory Van den Borre
 */
class ZipUnpackerTest {

    @Nested
    class ExtractFiles {

        @Test
        void happyFlow() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            Assertions.assertFalse(Files.exists(zip.resolve("zip-folder-1").resolve("zip-file-1.txt")));
            unpacker.unpack(getFile("zip-files.zip"), zip, true);
            Assertions.assertTrue(Files.exists(zip.resolve("zip-folder-1").resolve("zip-file-1.txt")));
        }

        @Test
        void ZipFileNotExisting() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            Assertions.assertThrows(IllegalStateException.class, () -> unpacker.unpack(Paths.get("anything"), zip, true));
        }

        @Test
        void directoryAlreadyExisting() {

        }

        @Test
        void fileAlreadyExisting() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            unpacker.unpack(getFile("zip-files.zip"), zip, true);
            unpacker.unpack(getFile("zip-files.zip"), zip, true);
            Assertions.assertTrue(Files.exists(zip.resolve("zip-folder-1").resolve("zip-file-1.txt")));
        }
    }

    @Nested
    class UnpackDirectoryToDirectory {

        @Test
        void happyFlow() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            unpacker.unpackDirectoryToDirectory(getFile("zip-files.zip"), "zip-folder-1", zip);
            Assertions. assertTrue(Files.exists(zip.resolve("zip-folder-1").resolve("zip-file-1.txt")));
        }

        @Test
        void directoryAlreadyExist() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            unpacker.unpackDirectoryToDirectory(getFile("zip-files.zip"), "zip-folder-1", zip);
            unpacker.unpackDirectoryToDirectory(getFile("zip-files.zip"), "zip-folder-1", zip);
        }

        @Test
        void directoryNotExistInArchive() throws IOException {
            Unpacker unpacker = CompressionFactory.zipUnpacker();
            Path zip = getDestinationPath();
            unpacker.unpackDirectoryToDirectory(getFile("zip-files.zip"), "zip-folder-17", zip);
            //check zip dir is empty
        }
    }

    private static Path getFile(String name) {
        return new File(ZipUnpacker.class.getClassLoader().getResource(name).getFile()).getAbsoluteFile().toPath();
    }

    private static Path getDestinationPath() throws IOException {
        return Files.createTempDirectory("zip");
    }
}
