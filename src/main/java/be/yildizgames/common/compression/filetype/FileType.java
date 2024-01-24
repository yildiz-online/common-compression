/*
 This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 Copyright (c) 2023 Grégory Van den Borre
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

package be.yildizgames.common.compression.filetype;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Check the file type based on its extension or using magic numbers.
 * This class is not mutable.
 * This class does not allow null input.
 * This class never returns null values.
 *
 * @author Grégory Van den Borre
 */
public class FileType {

    private final String name;

    private final List<String> extensions;

    private final FileTypeBytes bytes;

    public FileType(String name, List<String> extensions, FileTypeBytes bytes) {
        super();
        this.name = Objects.requireNonNull(name);
        this.extensions = Collections.unmodifiableList(Objects.requireNonNull(extensions));
        this.bytes = Objects.requireNonNull(bytes);
    }

    public final List<String> getExtensions() {
        return this.extensions;
    }

    public final boolean is(InputStream stream) {
        try {
            return is(stream.readAllBytes());
        } catch (IOException e) {
            System.getLogger(FileType.class.getName()).log(System.Logger.Level.ERROR, "", e);
            return false;
        }
    }

    /**
     * Check the type based on extension.
     * @param name File name.
     * @return true if the extension matches the type.
     */
    public final boolean is(String name) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return false;
        }
        var ext = name.substring(lastDot + 1).toLowerCase();
        return this.extensions.contains(ext);
    }

    /**
     * Check the type based on magic number.
     * @param bytes File first 10 bytes.
     * @return true if the bytes matches the type.
     */
    public final boolean is(byte[] bytes) {
        return this.bytes.is(bytes);
    }

    public final String getName() {
        return this.name;
    }

    public static class FileTypeBytes {

        private final List<int[]> bytes;

        public FileTypeBytes(List<int[]> bytes) {
            this.bytes = bytes;
        }

        private boolean is(byte[] b) {
            for(var n : bytes) {
                if(is(n, b)) {
                    return true;
                }
            }
            return false;
        }

        private boolean is(int[] n, byte[] b) {
            for (int i = 0; i < n.length; i++) {
                if (Byte.toUnsignedInt(b[i]) != n[i]) {
                    return false;
                }
            }
            return true;
        }

    }

}
