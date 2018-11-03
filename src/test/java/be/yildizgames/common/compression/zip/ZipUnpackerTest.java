/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2018 Grégory Van den Borre
 *
 *  More infos available: https://www.yildiz-games.be
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

import be.yildizgames.common.compression.Unpacker;
import be.yildizgames.common.compression.exception.ArchiveException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Grégory Van den Borre
 */
class ZipUnpackerTest {

    @Nested
    class ExtractFiles {

        @Test
        void happyFlow() {
            Unpacker unpacker = new ZipUnpacker();
            Path zip = Paths.get("zip");
            unpacker.unpack(getFile("zip-files.zip"), zip, true);
            assertTrue(new File("zip/zip-folder-1/zip-file-1.txt").exists());
        }

        @Test
        void ZipFileNotExisting() {
            Unpacker unpacker = new ZipUnpacker();
            Path zip = Paths.get("zip");
            assertThrows(ArchiveException.class, () -> unpacker.unpack(new File("anything").toPath(), zip, true));
        }
    }

    private static Path getFile(String name) {
        return new File(ZipUnpacker.class.getClassLoader().getResource(name).getFile()).getAbsoluteFile().toPath();
    }
}
