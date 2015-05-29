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

import com.nerodesk.om.Doc;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

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
        final String first = "http://bit.ly/1";
        final String second = "http://bit.ly/2";
        final Doc fdoc = new CdShortUrl(this.doc(first));
        final Doc sdoc = new CdShortUrl(this.doc(second));
        MatcherAssert.assertThat(
            fdoc.shortUrl(),
            Matchers.equalTo(first)
        );
        MatcherAssert.assertThat(
            fdoc.shortUrl(),
            Matchers.equalTo(first)
        );
        MatcherAssert.assertThat(
            sdoc.shortUrl(),
            Matchers.equalTo(second)
        );
        MatcherAssert.assertThat(
            sdoc.shortUrl(),
            Matchers.equalTo(second)
        );
    }

    /**
     * Creates mocked Doc.
     * @param name Doc name
     * @return Doc instance
     */
    private Doc doc(final String name) {
        final Doc doc = Mockito.mock(Doc.class);
        Mockito.when(doc.shortUrl()).thenReturn(name)
            .thenReturn(String.format("%s/next", name));
        return doc;
    }
}
