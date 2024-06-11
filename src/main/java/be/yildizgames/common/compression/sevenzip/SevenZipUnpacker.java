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

import be.yildizgames.common.compression.Unpacker;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class is able to unpack files from the 7zip archive format.
 *
 * @author Grégory Van den Borre
 */
public class SevenZipUnpacker implements Unpacker {

    @Override
    public final void unpack(Path archive, Path destination, boolean keepRootDir) {

        try (var sevenZFile = SevenZFile.builder().setPath(archive).setTryToRecoverBrokenArchives(true).get()) {
            if (Files.notExists(destination)) {
                Files.createDirectories(destination);
            }
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null) {
                if (entry.isDirectory()) {
                    if (Files.notExists(destination.resolve(entry.getName()))) {
                        Files.createDirectories(destination.resolve(entry.getName()));
                    }
                } else {
                    unpackEntry(destination, sevenZFile, entry);
                }
                entry = sevenZFile.getNextEntry();
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override
    public void unpack(Path archive, String fileToExtract, Path destination) {
        try (var sevenZFile = SevenZFile.builder().setPath(archive).setTryToRecoverBrokenArchives(true).get()) {
            if (Files.notExists(destination)) {
                Files.createDirectories(destination);
            }
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null) {
                if (entry.getName().equals(fileToExtract)) {
                    unpackEntry(destination, sevenZFile, entry);
                }
                entry = sevenZFile.getNextEntry();
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    private void unpackEntry(Path destination, SevenZFile sevenZFile, SevenZArchiveEntry entry) {
        try (var out = Files.newOutputStream(destination.resolve(entry.getName()))) {
            byte[] content = new byte[(int) entry.getSize()];
            sevenZFile.read(content, 0, content.length);
            out.write(content);
        } catch (final IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    @Override
    public final void unpackDirectoryToDirectory(Path archive, String directoryToExtract, Path destination) {
        throw new UnsupportedOperationException();
    }
}
