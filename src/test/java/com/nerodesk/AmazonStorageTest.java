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
package com.nerodesk;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import com.jcabi.s3.Region;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link AmazonStorage}.
 *
 * @author Alexey Saenko (alexey.saenko@gmail.com)
 * @version $Id$
 * @since 0.2
 */
public final class AmazonStorageTest {

    /**
     * Region.
     */
    private transient Region region;

    /**
     * Bucket.
     */
    private transient Bucket bucket;

    /**
     * Ocket.
     */
    private transient Ocket ocket;

    /**
     * Storage to test.
     */
    private transient AmazonStorage storage;

    /**
     * Sets all mocks up.
     */
    @Before
    public void setUp() {
        this.region = Mockito.mock(Region.class);
        this.bucket = Mockito.mock(Bucket.class);
        this.ocket = Mockito.mock(Ocket.class);
        Mockito.when(
            this.region.bucket(Mockito.anyString())
        ).thenReturn(this.bucket);
        Mockito.when(
            this.bucket.ocket(Mockito.anyString())
        ).thenReturn(this.ocket);
        // @checkstyle MultipleStringLiteralsCheck (1 line)
        this.storage = new AmazonStorage(this.region, "bucket");
    }

    /**
     * Storage can get something successfully.
     * @throws IOException If fails.
     */
    @Test
    public void getSuccessfully() throws IOException {
        this.storage.get("get.txt");
        Mockito.verify(this.ocket).read(Mockito.any(OutputStream.class));
    }

    /**
     * Storage can throw an exception getting something.
     * @throws IOException If succeeds.
     */
    @Test(expected = IOException.class)
    public void getThrowsException() throws IOException {
        Mockito
            .doThrow(IOException.class)
            .when(this.ocket)
            .read(Mockito.any(OutputStream.class));
        this.storage.get("get_throws.txt");
    }

    /**
     * Storage can put something successfully.
     * @throws IOException If fails.
     */
    @Test
    public void putSuccessfully() throws IOException {
        final String content = "test text\n\ttest2";
        final InputStream input = IOUtils.toInputStream(content);
        this.storage.put("put.txt", input);
        Mockito.verify(this.ocket).write(
            Mockito.eq(input),
            Mockito.any(ObjectMetadata.class)
        );
        Mockito.verify(this.ocket).meta();
    }

    /**
     * Storage can throw an exception putting something.
     * @throws IOException If succeeds.
     */
    @Test(expected = IOException.class)
    public void putThrowsException() throws IOException {
        final String content = "test text\n\ttest1";
        final InputStream input = IOUtils.toInputStream(content);
        Mockito.doThrow(IOException.class).when(this.ocket).meta();
        this.storage.put("put_throws.txt", input);
    }

    /**
     * Storage can delete something successfully.
     * @throws IOException If fails.
     */
    @Test
    public void deleteSuccessfully() throws IOException {
        final String path = "delete.txt";
        this.storage.delete(path);
        Mockito.verify(this.bucket).remove(Mockito.eq(path));
    }

    /**
     * Storage can throw an exception deleting something.
     * @throws IOException If succeeds.
     */
    @Test(expected = IOException.class)
    public void deleteThrowsException() throws IOException {
        final String path = "delete_throws.txt";
        Mockito.doThrow(IOException.class).when(this.bucket).remove(path);
        this.storage.delete(path);
    }

    /**
     * Storages can be compared over equals / hashcode.
     */
    @Test
    public void equalsAndHashcodeWorks() {
        // @checkstyle MultipleStringLiteralsCheck (1 line)
        final AmazonStorage strg = new AmazonStorage(this.region, "bucket");
        MatcherAssert.assertThat(
            "Storages are equal", this.storage.equals(strg)
        );
        MatcherAssert.assertThat(
            "Storages has the same hashcode",
            this.storage.hashCode() == strg.hashCode()
        );
    }
}
