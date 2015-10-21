/**
 * Copyright (c) 2016, libre.io
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the libre.io nor
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
package com.libre.takes;

import com.jcabi.matchers.XhtmlMatchers;
import com.libre.om.Base;
import com.libre.om.User;
import com.libre.om.mock.MkBase;
import java.io.ByteArrayInputStream;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.codecs.CcPlain;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rs.RsPrint;

/**
 * Tests for {@code TkAdmin}.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.4
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkAdminTest {

    /**
     * TkAdmin can return a list of docs of single user.
     * @throws Exception If fails.
     */
    @Test
    public void returnsListOfDocsOfSingleUser() throws Exception {
        final Base base = new MkBase();
        final String urn = "urn:test:1";
        final User user = base.user(urn);
        final String content = "hello, world!";
        user.docs().doc("test.txt").write(
            new ByteArrayInputStream(content.getBytes()), content.length()
        );
        final String second = "hello!";
        user.docs().doc("test-2.txt").write(
            new ByteArrayInputStream(second.getBytes()), second.length()
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkAdmin(base).act(
                    new RqWithHeader(
                        new RqFake(),
                        TkAuth.class.getSimpleName(),
                        new String(
                            new CcPlain().encode(new Identity.Simple(urn))
                        )
                    )
                )
            ).printBody(),
            XhtmlMatchers.hasXPaths(
                "/page/docs[count(doc)=2]",
                "/page/docs/doc[path='urn:test:1/test.txt']",
                "/page/docs/doc[path='urn:test:1/test-2.txt']"
            )
        );
    }

    /**
     * TkAdmin can return a list of docs of several users.
     * @throws Exception If fails.
     */
    @Test
    @Ignore
    public void returnsListOfDocsOfSeveralUsers() throws Exception {
        final Base base = new MkBase();
        final String urn = "urn:test:2";
        final User user = base.user(urn);
        final String content = "User1. File1";
        user.docs().doc("test_u1.txt").write(
            new ByteArrayInputStream(content.getBytes()),
            content.length()
        );
        final String second = "User1. File2";
        user.docs().doc("test_u1_2.txt").write(
            new ByteArrayInputStream(second.getBytes()),
            second.length()
        );
        final String third = "User2. File1";
        final User another = base.user("urn:test:3");
        another.docs().doc("test_u2.txt").write(
            new ByteArrayInputStream(third.getBytes()),
            third.length()
        );
        final String fourth = "User2. File2";
        another.docs().doc("test_u2_2.txt").write(
            new ByteArrayInputStream(fourth.getBytes()),
            fourth.length()
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkAdmin(base).act(
                    new RqWithHeader(
                        new RqFake(),
                        TkAuth.class.getSimpleName(),
                        new String(
                            new CcPlain().encode(new Identity.Simple(urn))
                        )
                    )
                )
            ).printBody(),
            XhtmlMatchers.hasXPaths(
                "/page/docs[count(doc)=4]",
                "/page/docs/doc[path='urn:test:1/test_u1.txt']",
                "/page/docs/doc[path='urn:test:1/test_u1_2.txt']",
                "/page/docs/doc[path='urn:test:2/test_u2.txt']",
                "/page/docs/doc[path='urn:test:2/test_u2_2.txt']"
            )
        );
    }
}
