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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grégory Van den Borre
 */
public class FileTypeCategory {

    /**
     * List of types belonging to this category.
     */
    private final List<FileType> types = new ArrayList<>();

    public FileTypeCategory(final String name) {
        super();
    }

    public final Optional<FileType> is(InputStream stream) throws IOException {
        int length = 10;
        try (stream) {
            byte[] buffer = new byte[length];
            stream.read(buffer, 0, length);
            return this.types.stream().filter(t -> t.is(buffer)).findFirst();
        }
    }

    public final FileTypeCategory addType(FileType type) {
       this.types.add(type);
       return this;
    }

    public final FileTypeCategory addType(FileTypes type) {
        return addType(type.getType());
    }
}
