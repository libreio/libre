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

import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.jcabi.immutable.ArrayMap;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import com.nerodesk.om.Friends;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.IOUtils;

/**
 * AWS-based version of Doc.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.3
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
@ToString
@EqualsAndHashCode(of = { "bucket", "user", "label" })
final class AwsFriends implements Friends {

    /**
     * Header with a list of friends.
     */
    public static final String HEADER = "x-ndk-friends";

    /**
     * Bucket.
     */
    private final transient Bucket bucket;

    /**
     * URN of the user.
     */
    private final transient String user;

    /**
     * Doc name.
     */
    private final transient String label;

    /**
     * Ctor.
     * @param bkt Bucket
     * @param urn URN of the user
     * @param doc Name of document
     */
    AwsFriends(final Bucket bkt, final String urn, final String doc) {
        this.bucket = bkt;
        this.user = urn;
        this.label = doc;
    }

    @Override
    public boolean leader() throws IOException {
        final Ocket ocket = this.bucket.ocket(this.key());
        return ocket.meta().getUserMetaDataOf(AwsDoc.HEADER) == null;
    }

    @Override
    public Iterable<String> names() throws IOException {
        return this.list();
    }

    @Override
    public void add(final String name) throws IOException {
        final Collection<String> friends = this.list();
        friends.add(name);
        final Ocket ocket = this.bucket.ocket(this.key());
        final ObjectMetadata meta = ocket.meta();
        meta.setUserMetadata(
            new ArrayMap<>(meta.getUserMetadata()).with(
                AwsFriends.HEADER,
                Joiner.on(';').join(friends)
            )
        );
        ocket.bucket().region().aws().copyObject(
            new CopyObjectRequest(
                ocket.bucket().name(),
                ocket.key(),
                ocket.bucket().name(),
                ocket.key()
            ).withNewObjectMetadata(meta)
        );
        final ObjectMetadata fmeta = new ObjectMetadata();
        fmeta.addUserMetadata(AwsDoc.HEADER, "true");
        this.bucket.ocket(String.format("%s/%s", name, this.label)).write(
            IOUtils.toInputStream(""), fmeta
        );
    }

    @Override
    public void eject(final String name) throws IOException {
        final Collection<String> friends = this.list();
        friends.add(name);
        final Ocket ocket = this.bucket.ocket(this.key());
        final ObjectMetadata meta = ocket.meta();
        meta.setUserMetadata(
            new ArrayMap<>(meta.getUserMetadata()).with(
                AwsFriends.HEADER,
                Joiner.on(';').join(friends)
            )
        );
        ocket.bucket().region().aws().copyObject(
            new CopyObjectRequest(
                ocket.bucket().name(),
                ocket.key(),
                ocket.bucket().name(),
                ocket.key()
            ).withNewObjectMetadata(meta)
        );
        this.bucket.remove(String.format("%s/%s", name, this.label));
    }

    /**
     * Ocket key.
     * @return Key
     */
    private String key() {
        return String.format("%s/%s", this.user, this.label);
    }

    /**
     * Get a collection of friends.
     * @return Names of them
     * @throws IOException If fails
     */
    private Collection<String> list() throws IOException {
        final Ocket ocket = this.bucket.ocket(this.key());
        final ObjectMetadata meta = ocket.meta();
        final String header = meta.getUserMetaDataOf(AwsFriends.HEADER);
        final Collection<String> friends;
        if (header == null) {
            friends = new ArrayList<>(1);
        } else {
            friends = Sets.newHashSet(Arrays.asList(header.split(";")));
        }
        return friends;
    }
}
