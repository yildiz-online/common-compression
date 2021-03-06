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

package be.yildizgames.common.compression.sevenzip;

import be.yildizgames.common.compression.Unpacker;
import be.yildizgames.common.compression.exception.ArchiveException;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipUnpacker implements Unpacker {

    /**
     * Size of the buffer to use.
     */
    private static final int BUFFER_SIZE = 1024;


    @Override
    public final void unpack(Path archive, Path destination, boolean keepRootDir) {
        try (var sevenZFile = new SevenZFile(archive.toFile())) {
            if(Files.notExists(destination)) {
                Files.createDirectories(destination);
            }
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null) {
                if(entry.isDirectory()) {
                    if(Files.notExists(destination.resolve(entry.getName()))) {
                        Files.createDirectories(destination.resolve(entry.getName()));
                    }
                } else {
                    try (var out = Files.newOutputStream(destination.resolve(entry.getName()))) {
                        byte[] content = new byte[(int) entry.getSize()];
                        sevenZFile.read(content, 0, content.length);
                        out.write(content);
                    } catch (final IOException ioe) {
                        throw new ArchiveException(ioe);
                    }
                }
                entry = sevenZFile.getNextEntry();
            }
        } catch (IOException ioe) {
            throw new ArchiveException(ioe);
        }
    }

    @Override
    public final void unpackDirectoryToDirectory(Path archive, String directoryToExtract, Path destination) {

    }
}
