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

import com.jcabi.s3.Ocket;
import com.jcabi.s3.Region;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Amazon S3 storage.
 *
 * @author Alexey Saenko (alexey.saenko@gmail.com)
 * @version $Id$
 * @since 0.2
 */
@ToString
@EqualsAndHashCode
public final class AmazonStorage implements Storage {

    /**
     * Region.
     */
    private final transient Region region;

    /**
     * Bucket name.
     */
    private final transient String bucket;

    /**
     * Ctor.
     * @param key Amazon key
     * @param secret Amazon secret
     * @param bckt The name of the bucket to get
     */
    public AmazonStorage(@NotNull final String key,
        @NotNull final String secret, @NotNull final String bckt) {
        this(new Region.Simple(key, secret), bckt);
    }

    /**
     * Ctor.
     * @param rgn Amazon region abstraction
     * @param bckt The name of the bucket to get
     */
    public AmazonStorage(
        @NotNull final Region rgn,
        @NotNull final String bckt) {
        super();
        this.region = rgn;
        this.bucket = bckt;
    }

    @Override
    public InputStream get(@NotNull final String path) throws IOException {
        final PipedOutputStream pos = new PipedOutputStream();
        this.region.bucket(this.bucket).ocket(path).read(pos);
        return new PipedInputStream(pos);
    }

    @Override
    public void put(
        @NotNull final String path,
        @NotNull final InputStream input) throws IOException {
        final Ocket ocket = this.region.bucket(this.bucket).ocket(path);
        ocket.write(input, ocket.meta());
    }

    @Override
    public void delete(@NotNull final String path) throws IOException {
        this.region.bucket(this.bucket).remove(path);
    }

}
