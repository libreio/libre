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
package com.nerodesk.takes.doc;

import com.nerodesk.om.mock.MkBase;
import java.io.IOException;
import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Request;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.codecs.CcPlain;
import org.takes.rq.RqFake;

/**
 * Tests for {@link TsDoc}.
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 */
public final class TsDocTest {
    /**
     * TsDoc can route to file reading.
     * @throws Exception If something goes wrong
     */
    @Test
    public void routesToFileReading() throws Exception {
        MatcherAssert.assertThat(
            new TsDoc(new MkBase()).route(this.request("/doc/read?file=name")),
            Matchers.instanceOf(TkRead.class)
        );
    }

    /**
     * TsDoc can route to file removal.
     * @throws Exception If something goes wrong
     */
    @Test
    public void routesToFileRemoval() throws Exception {
        MatcherAssert.assertThat(
            new TsDoc(new MkBase())
                .route(this.request("/doc/delete?file=name")),
            Matchers.instanceOf(TkDelete.class)
        );
    }

    /**
     * TsDoc can route to file writing.
     * @throws Exception If something goes wrong
     */
    @Test
    public void routesToFileWriting() throws Exception {
        MatcherAssert.assertThat(
            new TsDoc(new MkBase()).route(this.request("/doc/write?file=name")),
            Matchers.instanceOf(TkWrite.class)
        );
    }

    /**
     * TsDoc can route to friend addition.
     * @throws Exception If something goes wrong
     */
    @Test
    public void routesToFriendAddition() throws Exception {
        MatcherAssert.assertThat(
            new TsDoc(new MkBase())
                .route(this.request("/doc/add-friend?file=name")),
            Matchers.instanceOf(TkAddFriend.class)
        );
    }

    /**
     * TsDoc can route to friend ejection.
     * @throws Exception If something goes wrong
     */
    @Test
    public void routesToFriendEjection() throws Exception {
        MatcherAssert.assertThat(
            new TsDoc(new MkBase())
                .route(this.request("/doc/eject-friend?file=name")),
            Matchers.instanceOf(TkEjectFriend.class)
        );
    }
    /**
     * Create a fake request with given path.
     * @param path Path in the request
     * @return Request created
     * @throws IOException In case of error
     */
    private Request request(final String path) throws IOException {
        return new RqFake(
            Arrays.asList(
                String.format("GET %s", path),
                "Host: www.example.com",
                "Content-Type: multipart/form-data",
                String.format(
                    "TsAuth: %s",
                    new String(
                        new CcPlain()
                            .encode(new Identity.Simple("urn:test:1"))
                    )
                )
            ), ""
        );
    }
}
