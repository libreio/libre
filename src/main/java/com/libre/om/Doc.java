/**
 * Copyright (c) 2016, libre.io
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the libre.io nor
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
package com.libre.om;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Document.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
@Immutable
public interface Doc {

    /**
     * Does it exist?
     * @return TRUE if exists
     * @throws IOException If fails
     */
    boolean exists() throws IOException;

    /**
     * Delete it (fails if the document is not mine).
     * @throws IOException If fails
     */
    void delete() throws IOException;

    /**
     * Everybody who has access to this document.
     * @return Friends
     * @throws IOException If fails
     */
    Friends friends() throws IOException;

    /**
     * Read its entire content into this output stream.
     * @param output Output stream
     * @throws IOException If fails
     */
    void read(OutputStream output) throws IOException;

    /**
     * Write its entire content from this input stream.
     * @param input Input stream
     * @param size Size of the stream in bytes
     * @throws IOException If fails
     */
    void write(InputStream input, long size) throws IOException;

    /**
     * Shorten the URL to the document.
     * @return Shortened URL as a string.
     */
    String shortUrl();

    /**
     * Document attributes.
     * @return Attributes
     * @throws IOException If fails
     */
    Attributes attributes() throws IOException;
}
