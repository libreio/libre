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

import com.jcabi.s3.Bucket;
import com.jcabi.s3.mock.MkBucket;
import com.nerodesk.om.aws.AwsDocs;
import java.util.Arrays;
import java.util.List;
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
 * Tests for AwsDocs
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 * @since 0.3
 */
public final class AwsDocsTest {
    /**
     * A Bucket mock.
     */
    @Mock(answer=Answers.RETURNS_MOCKS)
    private transient Bucket bucket;
    
    /**
     * Setup mocks.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * AwsDocs can list file names.
     */
    @Test
    public void listsFilenames() throws Exception {
        final List<String> names = Arrays.asList("a", "b", "c");
        Mockito.when(this.bucket.list(""))
            .thenReturn(names);
        MatcherAssert.assertThat(
            new AwsDocs(this.bucket).names(),
            Matchers.equalTo(names)
        );
    }
    
    /**
     * AwsDocs can obtain an AwsDoc.
     */
    @Test
    public void obtainsDoc() throws Exception {
        MatcherAssert.assertThat(
            new AwsDocs(this.bucket).doc("doc1"),
            Matchers.notNullValue()
        );
    }

    /**
     * AwsDocs can conform to the equals and hashCode contract.
     */
    @Test
    public void verifyEquality() {
        EqualsVerifier.forClass(AwsDocs.class)
            .suppress(Warning.TRANSIENT_FIELDS)
            .withPrefabValues(
                Bucket.class,
                new MkBucket("bucket1"),
                new MkBucket("bucket2")
            ).verify();
    }
}