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

import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
     * A Bucket mock.
     */
    @Mock(answer = Answers.RETURNS_MOCKS)
    private transient Bucket bucket;

    /**
     * Setup mocks.
     */
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * AwsDoc can verify if it exists.
     * @throws Exception in case of error.
     */
    @Test
    public void exists() throws Exception {
        final Ocket ocket = Mockito.mock(Ocket.class);
        final String label = "document-label";
        Mockito.when(this.bucket.ocket(label)).thenReturn(ocket);
        Mockito.when(ocket.exists()).thenReturn(true);
        MatcherAssert.assertThat(
            new AwsDoc(this.bucket, "non-existent-document").exists(),
            Matchers.is(false)
        );
        MatcherAssert.assertThat(
            new AwsDoc(this.bucket, label).exists(),
            Matchers.is(true)
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
     * Creates an AwsDoc with given contents inside of a mock bucket .
     * @param name Doc name.
     * @param contents Doc contents.
     * @return The new AwsDoc.
     * @throws IOException In case of failure.
     */
    private AwsDoc createDoc(final String name, final String contents)
        throws IOException {
        final AwsDoc doc = new AwsDoc(bucket, name);
        doc.write(new ByteArrayInputStream(contents.getBytes()));
        return doc;
    }
}
