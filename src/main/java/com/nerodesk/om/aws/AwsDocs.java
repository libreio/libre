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

import com.google.common.collect.Lists;
import com.jcabi.s3.Bucket;
import com.nerodesk.om.Doc;
import com.nerodesk.om.Docs;
import java.io.IOException;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * AWS-based version of Docs.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
@ToString
@EqualsAndHashCode(of = "bucket")
public final class AwsDocs implements Docs {

    /**
     * Bucket.
     */
    private final transient Bucket bucket;

    /**
     * Ctor.
     * @param bkt Bucket
     */
    public AwsDocs(final Bucket bkt) {
        this.bucket = bkt;
    }

    @Override
    public List<String> names() throws IOException {
        return Lists.newArrayList(this.bucket.list(""));
    }

    @Override
    public Doc doc(final String doc) {
        return new AwsDoc(this.bucket, doc);
    }

    @Override
    public long size() throws IOException {
        long total = 0;
        for (final String object : this.bucket.list("")) {
            total += this.bucket.ocket(object).meta().getContentLength();
        }
        return total;
    }
}
