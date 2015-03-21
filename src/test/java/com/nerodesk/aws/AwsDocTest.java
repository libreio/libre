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
package com.nerodesk.aws;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import com.jcabi.s3.mock.MkBucket;
import com.nerodesk.om.aws.AwsDoc;
import java.io.InputStream;
import java.io.OutputStream;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Tests for AwsDoc.
 *
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
    public void setup() {
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
            new AwsDoc(this.bucket, "non-existant-document").exists(),
            Matchers.is(false)
        );
        MatcherAssert.assertThat(
            new AwsDoc(this.bucket, label).exists(),
            Matchers.is(true)
        );
    }

    /**
     * AwsDoc can delete itself.
     * @throws Exception In case of error.
     */
    @Test
    public void deletes() throws Exception {
        final String label = "document-to-delete";
        new AwsDoc(this.bucket, label).delete();
        Mockito.verify(this.bucket).remove(label);
    }

    /**
     * AwsDoc can read itself to a OutputStream.
     * @throws Exception In case of error.
     */
    @Test
    public void reads() throws Exception {
        final String label = "document-to-read";
        final OutputStream output = Mockito.mock(OutputStream.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        Mockito.when(this.bucket.ocket(label)).thenReturn(ocket);
        new AwsDoc(this.bucket, label).read(output);
        Mockito.verify(ocket).read(output);
    }

    /**
     * AwsDoc can write itself to a InputStream.
     * @throws Exception In case of error.
     */
    @Test
    public void writes() throws Exception {
        final String label = "document-to-write";
        final InputStream input = Mockito.mock(InputStream.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        Mockito.when(this.bucket.ocket(label)).thenReturn(ocket);
        new AwsDoc(this.bucket, label).write(input);
        Mockito.verify(ocket).write(
            Mockito.eq(input),
            Mockito.any(ObjectMetadata.class)
        );
    }

    /**
     * AwsDoc can conform to the equals and hashCode contract.
     */
    @Test
    public void verifyEquality() {
        EqualsVerifier.forClass(AwsDoc.class)
            .suppress(Warning.TRANSIENT_FIELDS)
            .withPrefabValues(
                Bucket.class,
                new MkBucket("bucket1"),
                new MkBucket("bucket2")
            ).verify();
    }
}
