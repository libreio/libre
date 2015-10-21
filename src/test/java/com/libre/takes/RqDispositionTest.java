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
package com.nerodesk.takes;

import java.io.IOException;
import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Request;
import org.takes.rq.RqFake;

/**
 * Tests for {@link RqDisposition}.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 * @since 0.3.2
 */
public final class RqDispositionTest {
    /**
     * {@link RqDisposition} can get the filename from
     * Content-Disposition.
     * @throws Exception In case of error.
     */
    @Test
    public void getsFilenameFromHeaders() throws Exception {
        final String filename = "file.txt";
        final RqDisposition disposition = new RqDisposition(
            this.fakeRequestWithHeader(
                String.format(
                    "Content-Disposition: attachment; filename=\"%s\"",
                    filename
                )
            )
        );
        MatcherAssert.assertThat(
            disposition.filename(),
            Matchers.equalTo(filename)
        );
    }

    /**
     * {@link RqDisposition} can get the filename from
     * Content-Disposition, even if the filename is empty.
     * @throws Exception In case of error.
     */
    @Test
    public void returnsEmptyWhenFilenameIsEmpty() throws Exception {
        final RqDisposition disposition = new RqDisposition(
            this.fakeRequestWithHeader(
                "Content-Disposition: attachment; filename=\"\""
            )
        );
        MatcherAssert.assertThat(
            disposition.filename(),
            Matchers.equalTo("")
        );
    }

    /**
     * {@link RqDisposition} can throw an error indicating that the
     * filename was not given.
     * @throws Exception In case of error.
     */
    @Test(expected = IOException.class)
    public void throwsErrorWhenNoFilenameGiven() throws Exception {
        final RqDisposition disposition = new RqDisposition(
            this.fakeRequestWithHeader("Content-Disposition: attachment;")
        );
        disposition.filename();
    }

    /**
     * Creates a Fake Request with the given header.
     * @param header A request header.
     * @return A fake request.
     */
    private Request fakeRequestWithHeader(final String header) {
        return new RqFake(Arrays.asList("GET /", header), "");
    }
}
