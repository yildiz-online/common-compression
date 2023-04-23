/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2018-2023 Grégory Van den Borre
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

package be.yildizgames.common.compression;

import be.yildizgames.common.compression.sevenzip.SevenZipArchiver;
import be.yildizgames.common.compression.sevenzip.SevenZipNativeArchiver;
import be.yildizgames.common.compression.sevenzip.SevenZipFileInfoRetriever;
import be.yildizgames.common.compression.sevenzip.SevenZipNativeUnpacker;
import be.yildizgames.common.compression.sevenzip.SevenZipUnpacker;
import be.yildizgames.common.compression.zip.ZipArchiver;
import be.yildizgames.common.compression.zip.ZipFileInfoRetriever;
import be.yildizgames.common.compression.zip.ZipUnpacker;

import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
public class CompressionFactory {

    private static final Unpacker ZIP_UNPACKER = new ZipUnpacker();

    private static final Archiver ZIP_ARCHIVER = new ZipArchiver();

    private static final Unpacker SEVENZIP_UNPACKER = new SevenZipUnpacker();

    private static final Archiver SEVENZIP_ARCHIVER = new SevenZipArchiver();
    private static final Archiver SEVENZIP_NATIVE_ARCHIVER = new SevenZipNativeArchiver();
    private static final Unpacker SEVENZIP_NATIVE_UNPACKER = new SevenZipNativeUnpacker();

    private CompressionFactory() {
        super();
    }

    public static Unpacker zipUnpacker() {
        return ZIP_UNPACKER;
    }

    public static Archiver zipArchiver() {
        return ZIP_ARCHIVER;
    }

    public static FileInfoRetriever zipFileInfo(Path path) {
        return new ZipFileInfoRetriever(path);
    }

    public static Unpacker sevenZipUnpacker(boolean nativeImplementation) {
        if(nativeImplementation) {
            return SEVENZIP_NATIVE_UNPACKER;
        }
        return SEVENZIP_UNPACKER;
    }

    public static Archiver sevenZipArchiver(boolean nativeImplementation) {
        if(nativeImplementation) {
            return SEVENZIP_NATIVE_ARCHIVER;
        }
        return SEVENZIP_ARCHIVER;
    }

    public static Archiver archiver(Path path) {
        if(path.toString().endsWith(".7z")) {
            return sevenZipArchiver(false);
        } else if (path.toString().endsWith(".zip")) {
            return zipArchiver();
        } else {
            throw new IllegalArgumentException("Unknown extension for file: " + path.getFileName().toString());
        }
    }

    public static Unpacker unpacker(Path path) {
        if(path.toString().endsWith(".7z")) {
            return sevenZipUnpacker(false);
        } else if (path.toString().endsWith(".zip")) {
            return zipUnpacker();
        } else {
            throw new IllegalArgumentException("Unknown extension for file: " + path.getFileName().toString());
        }
    }

    public static Unpacker nativeUnpacker(Path path) {
        if(path.toString().endsWith(".7z")) {
            return sevenZipUnpacker(true);
        } else if (path.toString().endsWith(".zip")) {
            return zipUnpacker();
        } else {
            throw new IllegalArgumentException("Unknown extension for file: " + path.getFileName().toString());
        }
    }

    public static Archiver nativeArchiver(Path path) {
        if(path.toString().endsWith(".7z")) {
            return sevenZipArchiver(true);
        } else if (path.toString().endsWith(".zip")) {
            return zipArchiver();
        } else {
            throw new IllegalArgumentException("Unknown extension for file: " + path.getFileName().toString());
        }
    }

    public static FileInfoRetriever sevenZipFileInfo(Path path) {
        return new SevenZipFileInfoRetriever(path);
    }

    public static FileInfoRetriever fileInfo(Path path) {
        if(path.toString().endsWith(".7z")) {
            return new SevenZipFileInfoRetriever(path);
        } else if (path.toString().endsWith(".zip")) {
            return new ZipFileInfoRetriever(path);
        } else {
            throw new IllegalArgumentException("Unknown extension for file: " + path.getFileName().toString());
        }
    }

}
