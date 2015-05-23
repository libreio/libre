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

import com.jcabi.manifests.Manifests;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.mock.MkBucket;
import com.nerodesk.om.Doc;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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
        Manifests.DEFAULT.put("Nerodesk-BitlyId", "nerodesk");
        Manifests.DEFAULT.put(
            "Nerodesk-BitlyKey",
            "R_95c4f6c85c67498bba37a73872577410"
        );
        final String label = "shorten";
        final CdShortUrl doc = new CdShortUrl(this.createDoc(label, label));
        MatcherAssert.assertThat(
            doc.shortUrl(),
            Matchers.equalTo("http://bit.ly/1GgY5gX")
        );
    }

    /**
     * Constructs a mock bucket.
     * @param name Bucket name.
     * @throws IOException In case of failure.
     * @return The mock bucket.
     */
    private MkBucket mockBucket(final String name) throws IOException {
        final TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        return new MkBucket(folder.getRoot(), name);
    }

    /**
     * Creates an AwsDoc with given contents inside of a mock bucket .
     * @param name Doc name.
     * @param contents Doc contents.
     * @return The new AwsDoc.
     * @throws IOException In case of failure.
     */
    private Doc createDoc(final String name, final String contents)
        throws IOException {
        final Bucket bucket = this.mockBucket(name);
        final AwsDoc doc = new AwsDoc(bucket, "", name);
        doc.write(
            new ByteArrayInputStream(contents.getBytes()),
            contents.getBytes().length
        );
        return doc;
    }
}
