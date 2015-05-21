/**
 * Copyright (c) 2015, nerodesk.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the nerodesk.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.nerodesk.om.mock;

import com.nerodesk.om.Attributes;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Date;
import org.apache.commons.io.FileUtils;

/**
 * Mock of document attributes.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class MkAttributes implements Attributes {
    /**
     * The "visibility" attribute.
     */
    private static final String VISIBILITY = "nerodesk.mkattributes.visibility";

    /**
     * File of document.
     */
    private final transient File file;

    /**
     * Constructor.
     * @param fle File to use
     */
    public MkAttributes(final File fle) {
        this.file = fle;
    }

    @Override
    public long size() throws IOException {
        return FileUtils.sizeOf(this.file);
    }

    @Override
    public String type() throws IOException {
        String type = Files.probeContentType(this.file.toPath());
        if (type == null) {
            type = "unknown";
        }
        return type;
    }

    @Override
    public Date created() throws IOException {
        return new Date(
            Files.readAttributes(
                this.file.toPath(), BasicFileAttributes.class
            ).creationTime().toMillis()
        );
    }

    @Override
    public boolean visible() throws IOException {
        final boolean shown;
        final UserDefinedFileAttributeView view =
            Files.getFileAttributeView(
                this.file.toPath(), UserDefinedFileAttributeView.class
            );
        if (view.list().contains(MkAttributes.VISIBILITY)) {
            final ByteBuffer buf = ByteBuffer.allocate(
                view.size(MkAttributes.VISIBILITY)
            );
            view.read(MkAttributes.VISIBILITY, buf);
            buf.flip();
            shown = Boolean.valueOf(
                StandardCharsets.UTF_8.decode(buf).toString()
            );
        } else {
            shown = false;
        }
        return shown;
    }

    @Override
    public void show(final boolean shwn) throws IOException {
        Files.getFileAttributeView(
            this.file.toPath(), UserDefinedFileAttributeView.class
        ).write(
                MkAttributes.VISIBILITY,
                StandardCharsets.UTF_8.encode(String.valueOf(shwn))
            );
    }
}
