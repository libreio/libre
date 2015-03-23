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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link AwsDoc}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class AwsDocTest {

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
        final Bucket bucket = Mockito.mock(Bucket.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        Mockito.doReturn(true).when(ocket).exists();
        final String label = "exists";
        Mockito.doReturn(ocket).when(bucket).ocket(label);
        MatcherAssert.assertThat(
            new AwsDoc(bucket, label).exists(),
            Matchers.equalTo(ocket.exists())
        );
    }

    /**
     * AwsDoc can delete itself.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void deletes() throws IOException {
        final Bucket bucket = Mockito.mock(Bucket.class);
        final String label = "deletes";
        new AwsDoc(bucket, label).delete();
        Mockito.verify(bucket).remove(label);
    }

    /**
     * AwsDoc can read into an OutputStream.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void reads() throws IOException {
        final Bucket bucket = Mockito.mock(Bucket.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        final String label = "reads";
        Mockito.doReturn(ocket).when(bucket).ocket(label);
        final OutputStream out = new ByteArrayOutputStream();
        new AwsDoc(bucket, label).read(out);
        Mockito.verify(ocket).read(out);
    }

    /**
     * AwsDoc can write from an InputStream.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void writes() throws IOException {
        final Bucket bucket = Mockito.mock(Bucket.class);
        final Ocket ocket = Mockito.mock(Ocket.class);
        final String label = "writes";
        Mockito.doReturn(ocket).when(bucket).ocket(label);
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        new AwsDoc(bucket, label).write(input);
        Mockito.verify(ocket).write(
            org.mockito.Matchers.eq(input),
            org.mockito.Matchers.any(ObjectMetadata.class)
        );
    }

}
