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
package com.libre.takes.doc;

import com.libre.om.Base;
import com.libre.om.Doc;
import com.libre.om.mock.MkBase;
import com.libre.takes.RqWithTester;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Response;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Tests for {@code TkRead}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
public final class TkReadTest {

    /**
     * TkRead can read file content.
     * @throws Exception If fails.
     */
    @Test
    public void readsFileContent() throws Exception {
        final Base base = new MkBase();
        final Doc doc = base.user("urn:test:1").docs().doc("hey");
        final String input = "hello, world!";
        doc.write(IOUtils.toInputStream(input), input.getBytes().length);
        final Response resp = new TkRead(base).act(
            new RqWithTester(
                new RqFake("GET", "/?file=hey")
            )
        );
        MatcherAssert.assertThat(
            new RsPrint(resp).printBody(),
            Matchers.endsWith("world!")
        );
        MatcherAssert.assertThat(
            new RsPrint(resp).printHead(),
            Matchers.containsString(
                "Content-Disposition: attachment; filename=\"hey\""
            )
        );
    }

}
