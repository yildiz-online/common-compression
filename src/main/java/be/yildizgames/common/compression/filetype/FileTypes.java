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

import java.util.List;

/**
 * @author Grégory Van den Borre
 */
public enum FileTypes {

    PNG(new FileType("png", List.of("png"), new FileType.FileTypeBytes(List.of(new int[]{0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A})))),
    JPG(new FileType("jpg", List.of("jpg", "jpeg"), new FileType.FileTypeBytes(List.of()))),
    BMP(null),
    GIF(null),
    ZIP(new FileType("zip", List.of("zip"), new FileType.FileTypeBytes(List.of(new int[]{0x50, 0x4b, 0x03, 0x04})))),
    SEVEN_ZIP(new FileType("7zip", List.of("7z"), new FileType.FileTypeBytes(List.of(new int[]{0x37, 0x7A, 0xBC, 0xAF, 0x27, 0x1C})))),
    RAR(null),
    UNKNOWN(null);

    private final FileType type;

    FileTypes(FileType type) {
        this.type = type;
    }

    public final FileType getType() {
        return this.type;
    }
}
