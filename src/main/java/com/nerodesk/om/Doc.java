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

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

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
     * Size of the document in bytes.
     * @return Number of bytes
     * @throws IOException If fails
     */
    long size() throws IOException;

    /**
     * Mime type of the document.
     * @return Mime type.
     * @throws IOException If fails
     */
    String type() throws IOException;

    /**
     * Is directory (folder).
     * @return Returns true if document is directory.
     * @throws IOException On I/O error.
     */
    boolean isDir() throws IOException;

    /**
     * Creates directory.
     * @throws IOException On I/O error.
     */
    void mkDir() throws IOException;

    /**
     * Remove directory.
     * @param force If true - drop even non-empty.
     * @throws IOException On I/O error.
     */
    void rmDir(final boolean force) throws IOException;

    /**
     * Timestamp when the document was created.
     * @return Date in UTC.
     * @throws IOException If fails
     */
    Date created() throws IOException;

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
     * @throws IOException If fails
     */
    void write(InputStream input) throws IOException;

}
