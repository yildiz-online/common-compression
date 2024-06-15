/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2023-2024 Grégory Van den Borre
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
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipNativeUnpacker extends SevenZipNative implements Unpacker {

    private final static System.Logger LOGGER = System.getLogger(SevenZipNativeUnpacker.class.getName());

    public SevenZipNativeUnpacker() {
        super();
    }

    @Override
    public final void unpack(Path archive, Path destination, boolean keepRootDir) {
        unpack(archive, destination, keepRootDir, false);
    }

    @Override
    public final void unpack(Path archive, Path destination, boolean keepRootDir, boolean discardSubDirectories) {
        init();
        try (
                var randomAccessFile = new RandomAccessFile(archive.toAbsolutePath().toString(), "r");
                var inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {
            if (Files.notExists(destination)) {
                Files.createDirectories(destination);
            }
            inArchive.extract(null, false, new UnPackCallback(inArchive, destination, discardSubDirectories));
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "", e);
        }
    }

    @Override
    public final void unpack(Path archive, String fileToExtract, Path destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void unpackDirectoryToDirectory(Path archive, String directoryToExtract, Path destination) {
        throw new UnsupportedOperationException();
    }

    private record UnPackCallback(IInArchive inArchive, Path outputDirectory, boolean discardSubDirectories) implements IArchiveExtractCallback {

        @Override
        public ISequentialOutStream getStream(int i, ExtractAskMode extractAskMode) throws SevenZipException {
            var path = (String) this.inArchive.getProperty(i, PropID.PATH);
            if(this.discardSubDirectories) {
                var sub = path.split("/");
                path = sub.length > 1 ? sub[sub.length - 1] : sub[0];
            }
            var file = new File(this.outputDirectory.toAbsolutePath().toString(), path);
            return data -> {
                try (var outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    outputStream.write(data);
                } catch (IOException e) {
                    LOGGER.log(System.Logger.Level.ERROR, "", e);
                }
                return data.length;
            };
        }

        @Override
        public void prepareOperation(ExtractAskMode extractAskMode) {
            //Does nothing
        }

        @Override
        public void setOperationResult(ExtractOperationResult extractOperationResult) {
            //Does nothing
        }

        @Override
        public void setTotal(long total) throws SevenZipException {
            var space = new File(System.getProperty("java.io.tmpdir")).getUsableSpace();
            if (space < total) {
                throw new SevenZipException("Insufficient available space to extract: " + space + ", required space: " + total);
            }
        }

        @Override
        public void setCompleted(long l) {
            //Does nothing
        }
    }
}
