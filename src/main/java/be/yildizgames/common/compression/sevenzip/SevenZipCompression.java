/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2022-2023 Grégory Van den Borre
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

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.IOutCreateCallback;
import net.sf.sevenzipjbinding.IOutItem7z;
import net.sf.sevenzipjbinding.ISequentialInStream;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.OutItemFactory;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
import net.sf.sevenzipjbinding.util.ByteArrayStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipCompression {

    private final static System.Logger LOGGER = System.getLogger(SevenZipCompression.class.getName());

    public SevenZipCompression() {
        super();
    }

    public void unpack(Path archive, Path outputDirectory) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(archive.toAbsolutePath().toString(), "r");
        try (IInArchive inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {
            if (Files.notExists(outputDirectory)) {
                Files.createDirectories(outputDirectory);
            }
            inArchive.extract(null, false, new UnPackCallback(inArchive, outputDirectory));
        }
    }

    private record UnPackCallback(IInArchive inArchive, Path outputDirectory) implements IArchiveExtractCallback {

        @Override
        public ISequentialOutStream getStream(int i, ExtractAskMode extractAskMode) throws SevenZipException {
            String path = (String) inArchive.getProperty(i, PropID.PATH);
            var file = new File(outputDirectory.toAbsolutePath().toString(), path);
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
        public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {
            //Does nothing
        }

        @Override
        public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
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
        public void setCompleted(long l) throws SevenZipException {
            //Does nothing
        }
    }

    public void archive(List<Path> files, Path outputDirectory) {
        LOGGER.log(System.Logger.Level.DEBUG, "Archiving {0} files into {1}", files, outputDirectory);
        if(!files.isEmpty()) {
            try (var raf = new RandomAccessFile(outputDirectory.toAbsolutePath().toString(), "rw");
                 var outArchive = SevenZip.openOutArchive7z()) {
                outArchive.setLevel(5);
                outArchive.setSolid(true);
                outArchive.createArchive(new RandomAccessFileOutStream(raf), files.size(), new ArchiveCallback(files));
            } catch (Exception e) {
                LOGGER.log(System.Logger.Level.ERROR, "", e);
            }
        }
    }

    private record ArchiveCallback(List<Path> files) implements IOutCreateCallback<IOutItem7z> {

        public void setOperationResult(boolean operationResultOk)
                    throws SevenZipException {
                // Track each operation result here
            }

            public void setTotal(long total) throws SevenZipException {
                // Track operation progress here
            }

            public void setCompleted(long complete) throws SevenZipException {
                // Track operation progress here
            }

            public IOutItem7z getItemInformation(int index, OutItemFactory<IOutItem7z> outItemFactory) {
                IOutItem7z item = outItemFactory.createOutItem();
                var file = this.files.get(index);
                item.setPropertyPath(file.getFileName().toString());
                item.setPropertyIsDir(false);
                try {
                    item.setDataSize(Files.size(file));
                } catch (IOException e) {
                    LOGGER.log(System.Logger.Level.ERROR, "", e);
                }
                return item;
            }

            public ISequentialInStream getStream(int index) throws SevenZipException {
                try {
                    return new ByteArrayStream(Files.readAllBytes(this.files.get(index)), true);
                } catch (IOException e) {
                    LOGGER.log(System.Logger.Level.ERROR, "", e);
                    throw new SevenZipException(e);
                }
            }
        }
}
