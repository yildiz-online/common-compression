/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2022 Grégory Van den Borre
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

import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
public class Helper {

    public static Path get7zMultipleFiles() {
        return getFile("7zip-files.7z");
    }

    public static Path getZipMultipleFiles() {
        return getFile("zip-files.zip");
    }

    public static Path get7zBigFile() {
        return getFile("amaryllis-4701720_1920.7z");
    }

    public static Path getZipBigFile() {
        return getFile("amaryllis-4701720_1920.zip");
    }

    public static Path getPlainBigFile() {
        return getFile("amaryllis-4701720_1920.jpg");
    }

    public static Path get7zTestHashFile() {
        return getFile("test-hash.7z");
    }

    public static Path getZipTestHashFile() {
        return getFile("test-hash.zip");
    }

    public static Path getPlainTestHashFile() {
        return getFile("test-hash.txt");
    }

    public static Path getPlainTestHash2File() {
        return getFile("test-hash2.txt");
    }

    private static Path getFile(String fileName) {
        var f = Helper.class.getClassLoader().getResource(fileName);
        if (f == null) {
            throw new IllegalStateException("File " + fileName + " does not exists");
        }
        try {
            return Path.of(f.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }


}
