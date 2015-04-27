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

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import com.jcabi.s3.mock.MkBucket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for {@link AwsDoc}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class AwsDocTest {
    /**
     * AwsDoc can verify if it exists.
     * @throws Exception in case of error.
     */
    @Test
    public void exists() throws Exception {
        final String label = "document-label";
        final Bucket bucket = this.mockBucket(label);
        MatcherAssert.assertThat(
            new AwsDoc(bucket, "user-urn", "non-existent-document").exists(),
            Matchers.is(false)
        );
    }

    /**
     * AwsDoc conforms to equals and hashCode contract.
     */
    @Test
    public void conformsToEqualsHashCodeContract() {
        EqualsVerifier.forClass(AwsDoc.class)
            .suppress(Warning.TRANSIENT_FIELDS)
            .verify();
    }

    /**
     * AwsDoc can report its existence.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void reportsExistence() throws IOException {
        final String label = "exists";
        final AwsDoc doc = this.createDoc(label, label);
        MatcherAssert.assertThat(doc.exists(), Matchers.equalTo(true));
    }

    /**
     * AwsDoc can delete itself.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void deletes() throws IOException {
        final String label = "deletes";
        final AwsDoc doc = this.createDoc(label, label);
        MatcherAssert.assertThat(doc.exists(), Matchers.equalTo(true));
        doc.delete();
        MatcherAssert.assertThat(doc.exists(), Matchers.equalTo(false));
    }

    /**
     * AwsDoc can read into an OutputStream.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void reads() throws IOException {
        final String label = "reads";
        final AwsDoc doc = this.createDoc(label, label);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.read(out);
        MatcherAssert.assertThat(
            new String(out.toByteArray()),
            Matchers.equalTo(label)
        );
    }

    /**
     * AwsDoc can write from an InputStream.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void writes() throws IOException {
        final String label = "writes";
        final AwsDoc doc = this.createDoc(label, label);
        MatcherAssert.assertThat(doc.exists(), Matchers.equalTo(true));
    }

    /**
     * AwsDoc can upload a file with content length header.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void uploadsFileWithContentLengthHeader() throws IOException {
        final String data = "data";
        final Bucket bucket = Mockito.mock(Bucket.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        Mockito.when(bucket.ocket(Mockito.anyString())).thenReturn(ocket);
        final AtomicBoolean found = new AtomicBoolean(false);
        Mockito.doAnswer(
            new Answer<Void>() {
                @Override
                public Void answer(final InvocationOnMock invocation) {
                    final ObjectMetadata meta = invocation.getArgumentAt(
                        1, ObjectMetadata.class
                    );
                    MatcherAssert.assertThat(
                        meta.getContentLength(),
                        Matchers.equalTo((long) data.getBytes().length)
                    );
                    found.set(true);
                    return null;
                }
            }
        ).when(ocket)
            .write(
                Mockito.any(InputStream.class),
                Mockito.any(ObjectMetadata.class)
            );
        new AwsDoc(bucket, "", "name").write(
            new ByteArrayInputStream(data.getBytes()),
            data.getBytes().length
        );
        MatcherAssert.assertThat(found.get(), Matchers.is(true));
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
    private AwsDoc createDoc(final String name, final String contents)
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
