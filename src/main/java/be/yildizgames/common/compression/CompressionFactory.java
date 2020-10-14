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

package be.yildizgames.common.compression;

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
}
