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

import java.io.IOException;
import java.io.InputStream;

/**
 * Decorator for the {@code InputStream} which throws IllegalStateException
 * if more than {@code max} bytes read from underlying stream.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.4
 * @todo #180:30min Increase test coverage for the ThresholdInputStream class.
 *  Please remove this class form cobertura excludes if entire class is covered
 *  properly.
 */
final class ThresholdInputStream extends InputStream {

    /**
     * The wrapped input stream.
     */
    private final transient InputStream origin;

    /**
     * The max length to provide.
     */
    private final transient long max;

    /**
     * Number of bytes already returned.
     */
    private transient long pos;

    /**
     * Marked position.
     */
    private transient long marker = -1L;

    /**
     * Sync lock.
     */
    private final transient Object lock = new Object();

    /**
     * Creates a new {@code ThresholdInputStream} that wraps
     * given input stream and limits it to a certain size.
     *
     * @param inp The wrapped input stream
     * @param size The maximum number of bytes to return.
     *  Should be greater or equal to zero
     */
    ThresholdInputStream(final InputStream inp, final long size) {
        super();
        if (size < 0L) {
            throw new IllegalStateException(
                "Maximal size must be grater or equal to zero"
            );
        }
        this.max = size;
        this.origin = inp;
    }

    /**
     * Invokes the delegate's {@code read()} method if
     * the current position is less than the limit.
     * @return The byte read or -1 if the end of stream has been reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        if (this.pos >= this.max) {
            throw new ThresholdInputStream.LimitReachedException();
        }
        ++this.pos;
        return this.origin.read();
    }

    /**
     * Invokes the delegate's {@code read(byte[])} method.
     * @param bytes The buffer to read the bytes into
     * @return The number of bytes read or -1 if the end of stream or
     *  the limit has been reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(final byte[] bytes) throws IOException {
        return this.read(bytes, 0, bytes.length);
    }

    /**
     * Invokes the delegate's {@code read(byte[], int, int)} method.
     * @param bytes The buffer to read the bytes into
     * @param off The start offset
     * @param len The number of bytes to read
     * @return The number of bytes read or -1 if the end of stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(final byte[] bytes, final int off, final int len)
        throws IOException {
        if (this.pos >= this.max) {
            throw new ThresholdInputStream.LimitReachedException();
        }
        final long maxread;
        if (this.max >= 0L) {
            maxread = Math.min((long) len, this.max - this.pos);
        } else {
            maxread = (long) len;
        }
        final int rbytes;
        rbytes = this.origin.read(bytes, off, (int) maxread);
        if (rbytes != -1) {
            this.pos += (long) rbytes;
        }
        return rbytes;
    }

    /**
     * Invokes the delegate's {@code skip(long)} method.
     * @param count The number of bytes to skip
     * @return The actual number of bytes skipped
     * @throws IOException if an I/O error occurs
     */
    @Override
    public long skip(final long count) throws IOException {
        final long skipped = this.origin.skip(
            Math.min(count, this.max - this.pos)
        );
        this.pos += skipped;
        return skipped;
    }

    @Override
    public int available() throws IOException {
        final int aval;
        if (this.pos >= this.max) {
            aval = 0;
        } else {
            aval = this.origin.available();
        }
        return aval;
    }
    @Override
    public String toString() {
        return this.origin.toString();
    }
    @Override
    public void close() throws IOException {
        this.origin.close();
    }
    @Override
    public void reset() throws IOException {
        synchronized (this.lock) {
            this.origin.reset();
            this.pos = this.marker;
        }
    }
    @Override
    public void mark(final int rlimit) {
        synchronized (this.lock) {
            this.origin.mark(rlimit);
            this.marker = this.pos;
        }
    }
    @Override
    public boolean markSupported() {
        return this.origin.markSupported();
    }

    /**
     * Exceptional case when limit was reached during reading from the stream.
     */
    public static class LimitReachedException extends RuntimeException {
        /**
         * Serial version UID.
         */
        private static final long serialVersionUID = 1402905235180338966L;
    }
}
