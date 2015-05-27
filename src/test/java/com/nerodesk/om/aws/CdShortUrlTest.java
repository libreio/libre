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
package com.nerodesk.om.aws;

import com.nerodesk.om.Attributes;
import com.nerodesk.om.Doc;
import com.nerodesk.om.Friends;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link CdShortUrl}.
 *
 * @author Dmitry Zaytsev (dmitry.zaytsev@gmail.com)
 * @version $Id$
 * @since 0.3.30
 */
public final class CdShortUrlTest {
    /**
     * CdShortUrl can shorten document URL.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void shortenUrl() throws IOException {
        final String url = "http://bit.ly/CdShort";
        final Doc doc = new CdShortUrl(
            // @checkstyle AnonInnerLengthCheck (1 lines)
            new Doc() {
                @Override
                public boolean exists() throws IOException {
                    return false;
                }
                @Override
                public void delete() throws IOException {
                    assert 1 != 0;
                }
                @Override
                public Friends friends() throws IOException {
                    return null;
                }
                @Override
                public void read(final OutputStream output) throws IOException {
                    output.close();
                }
                @Override
                public void write(final InputStream input, final long size)
                    throws IOException {
                    input.close();
                }
                @Override
                public String shortUrl() {
                    return url;
                }
                @Override
                public Attributes attributes() throws IOException {
                    return null;
                }
            }
        );
        MatcherAssert.assertThat(
            doc.shortUrl(),
            Matchers.equalTo(url)
        );
        MatcherAssert.assertThat(
            doc.shortUrl(),
            Matchers.equalTo(url)
        );
    }
}
