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

import com.nerodesk.om.Base;
import com.nerodesk.om.Doc;
import com.nerodesk.om.Docs;
import com.nerodesk.om.mock.MkBase;
import com.nerodesk.takes.RqWithTester;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.takes.misc.Href;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkSetVisibility}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class TkSetVisibilityTest {

    /**
     * TkSetVisibility can return a response.
     * @throws Exception If something goes wrong
     */
    @Test
    public void returnsResponse() throws Exception {
        final Base base = new MkBase();
        final String name = "returnsResponse.txt";
        MatcherAssert.assertThat(
            new TkSetVisibility(base).act(
                new RqWithTester(
                    new RqFake(
                        "POST",
                        new Href()
                            .with("file", name)
                            .with("visibility", "Private")
                    )
                )
            ),
            Matchers.notNullValue()
        );
    }

    /**
     * TkSetVisibility can set a file's visibility.
     * @throws Exception If something goes wrong
     * @todo #255:30min Make visibility functionality in MkAttributes be a
     *  function of the underlying file instead of a boolean field. This is
     *  necessary in order for different instances of MkDoc which point to the
     *  same underlying file to see the same visibility status. Once this is
     *  done, un-ignore the test below.
     */
    @Test
    @Ignore
    public void setsVisibility() throws Exception {
        final Base base = new MkBase();
        final String name = "setsVisibility.txt";
        final Docs docs = base.user("urn:test:1").docs();
        final Doc doc = docs.doc(name);
        MatcherAssert.assertThat(
            doc.attributes().visible(),
            Matchers.is(false)
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkSetVisibility(base).act(
                    new RqWithTester(
                        new RqFake(
                            "POST",
                            new Href()
                                .with("file", name)
                                .with("visibility", "Public")
                        )
                    )
                )
            ).print(),
            Matchers.containsString("document+visibility+set+to")
        );
        MatcherAssert.assertThat(
            doc.attributes().visible(),
            Matchers.is(true)
        );
    }

}
