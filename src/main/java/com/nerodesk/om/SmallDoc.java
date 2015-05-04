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
package com.nerodesk.om;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Decorator for {@link Doc} which throws the exception
 * if file to write is too big.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.4
 */
@ToString
@EqualsAndHashCode
public final class SmallDoc implements Doc {
    /**
     * Decorated.
     */
    private final transient Doc decorated;

    /**
     * Max file size.
     */
    private final transient long mlength;
    /**
     * Constructor with default max file size = 10Mb.
     * @param doc Doc to be decorated
     * @checkstyle MagicNumberCheck (3 lines)
     */
    public SmallDoc(final Doc doc) {
        this(doc, 10_000_000L);
    }
    /**
     * Constructor.
     * @param doc Doc to be decorated
     * @param max Max file size
     */
    public SmallDoc(final Doc doc, final long max) {
        this.decorated = doc;
        this.mlength = max;
    }

    @Override
    public boolean exists() throws IOException {
        return this.decorated.exists();
    }

    @Override
    public void delete() throws IOException {
        this.decorated.delete();
    }

    @Override
    public Friends friends() throws IOException {
        return this.decorated.friends();
    }

    @Override
    public void read(final OutputStream output) throws IOException {
        this.decorated.read(output);
    }

    @Override
    public void write(final InputStream input, final long size)
        throws IOException {
        try {
            this.decorated.write(
                new ThresholdInputStream(input, this.mlength), size
            );
        } catch (final ThresholdInputStream.LimitReachedException exc) {
            throw new IllegalArgumentException(
                String.format(
                    "Now you can't upload a file that is larger than %s bytes",
                    this.mlength
                ), exc
            );
        }
    }

    @Override
    public String shortUrl() {
        return this.decorated.shortUrl();
    }

    @Override
    public Attributes attributes() throws IOException {
        return this.decorated.attributes();
    }
}
