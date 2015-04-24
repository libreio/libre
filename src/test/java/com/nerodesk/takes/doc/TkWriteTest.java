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

import com.google.common.base.Joiner;
import com.nerodesk.om.Base;
import com.nerodesk.om.User;
import com.nerodesk.om.mock.MkBase;
import com.nerodesk.takes.RqWithTester;
import java.io.ByteArrayOutputStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rq.RqWithHeader;
import org.takes.rs.RsPrint;

/**
 * Tests for {@code TkWrite}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
public final class TkWriteTest {

    /**
     * TkWrite can write file content.
     * @throws Exception If fails.
     */
    @Test
    public void writesFileContent() throws Exception {
        final Base base = new MkBase();
        final User user = base.user("urn:test:1");
        final String file = "hey.txt";
        final String body = Joiner.on("\r\n").join(
            " --AaB03x",
            "Content-Disposition: form-data; name=\"name\"",
            "",
            file,
            "--AaB03x",
            String.format(
                // @checkstyle LineLength (1 line)
                "Content-Disposition: form-data; name=\"file\"; filename=\"%s\"",
                file
            ),
            "Content-Transfer-Encoding: utf-8",
            "",
            "hello, world!",
            "--AaB03x--"
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkWrite(base).act(
                    new RqWithTester(
                        new RqWithHeader(
                            new RqFake("POST", "/", body),
                            "Content-Type: multipart/form-data; boundary=AaB03x"
                        )
                    )
                )
            ).print(),
            Matchers.startsWith("HTTP/1.1 303 See Other")
        );
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        user.docs().doc(file).read(baos);
        MatcherAssert.assertThat(
            new String(baos.toByteArray()),
            Matchers.endsWith("world!")
        );
    }

}
