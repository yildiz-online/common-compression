/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2022-2024 Grégory Van den Borre
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

import be.yildizgames.common.compression.Archiver;
import net.sf.sevenzipjbinding.IOutCreateCallback;
import net.sf.sevenzipjbinding.IOutItem7z;
import net.sf.sevenzipjbinding.ISequentialInStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.OutItemFactory;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
import net.sf.sevenzipjbinding.util.ByteArrayStream;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Grégory Van den Borre
 */
public class SevenZipNativeArchiver extends SevenZipNative implements Archiver {

    private final static System.Logger LOGGER = System.getLogger(SevenZipNativeArchiver.class.getName());

    public SevenZipNativeArchiver() {
        super();
    }


    @Override
    public void pack(Path file, Path destination) {
        LOGGER.log(System.Logger.Level.DEBUG, "Archiving {0} files into {1}", file, destination);
        if (Files.isDirectory(file)) {
            var root = file.getParent().toAbsolutePath().toString();
            try (var paths = Files.walk(file)) {
                doPack(paths.map(p -> fromFile(p, root)).toList(), destination);
            } catch (Exception e) {
                LOGGER.log(System.Logger.Level.ERROR, "", e);
            }
        } else {
            doPack(List.of(fromFileName(file)), destination);
        }
    }

    @Override
    public void pack(List<Path> files, Path destination) {
        throw new UnsupportedOperationException();
    }

    private static Entry fromFile(Path path, String root) {
        var dir = Files.isDirectory(path);
        long size = 0;
        try {
            size = dir ? 0 : Files.size(path);
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "", e);
        }
        return new Entry(path.toString().replace(root, ""), dir, size, path);
    }

    private static Entry fromFileName(Path path) {
        long size = 0;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "", e);
        }
        return new Entry(path.getFileName().toString(), false, size, path);
    }

    private void doPack(List<Entry> files, Path destination) {
        init();
        if (!files.isEmpty()) {
            try (var raf = new RandomAccessFile(destination.toAbsolutePath().toString(), "rw");
                 var outArchive = SevenZip.openOutArchive7z()) {
                outArchive.setLevel(5);
                outArchive.setSolid(true);
                outArchive.createArchive(new RandomAccessFileOutStream(raf), files.size(), new ArchiveCallback(files));
            } catch (Exception e) {
                LOGGER.log(System.Logger.Level.ERROR, "", e);
            }
        }
    }

    private record ArchiveCallback(List<Entry> files) implements IOutCreateCallback<IOutItem7z> {

        public void setOperationResult(boolean operationResultOk) {
            // Track each operation result here
        }

        public void setTotal(long total) {
            // Track operation progress here
        }

        public void setCompleted(long complete) {
            // Track operation progress here
        }

        public IOutItem7z getItemInformation(int index, OutItemFactory<IOutItem7z> outItemFactory) {
            var item = outItemFactory.createOutItem();
            var file = this.files.get(index);
            item.setPropertyPath(file.name);
            item.setPropertyIsDir(file.directory);
            item.setDataSize(file.size);
            return item;
        }

        public ISequentialInStream getStream(int index) throws SevenZipException {
            try {
                return new ByteArrayStream(Files.readAllBytes(this.files.get(index).path), true);
            } catch (IOException e) {
                LOGGER.log(System.Logger.Level.ERROR, "", e);
                throw new SevenZipException(e);
            }
        }
    }

    private record Entry(String name, boolean directory, long size, Path path) {}
}
