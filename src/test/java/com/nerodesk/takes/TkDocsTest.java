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
package com.nerodesk.takes;

import com.jcabi.matchers.XhtmlMatchers;
import com.nerodesk.om.User;
import com.nerodesk.om.mock.MkBase;
import java.io.ByteArrayInputStream;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Tests for {@code TkDocs}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
public final class TkDocsTest {

    /**
     * TkDocs can return a list of docs.
     * @throws Exception If fails.
     */
    @Test
    public void returnsListOfDocs() throws Exception {
        final User user = new MkBase().user("urn:test:1");
        user.docs().doc("test.txt").write(
            new ByteArrayInputStream("hello, world!".getBytes())
        );
        user.docs().doc("test-2.txt").write(
            new ByteArrayInputStream("hello!".getBytes())
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkDocs(user, new RqFake()).act()
            ).printBody(),
            XhtmlMatchers.hasXPaths(
                "/page/user[balance=0]",
                "/page/docs[count(doc)=2]",
                "/page/docs/doc[name='test.txt']",
                "/page/docs/doc/read"
            )
        );
    }
}
