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

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Response;
import org.takes.facets.fallback.RqFallback;
import org.takes.misc.Opt;

/**
 * Tests for {@link FbError}.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class FbErrorTest {

    /**
     * FbError can display error message.
     * @throws IOException If unsuccessful.
     */
    @Test
    public void displaysErrorMsg() throws IOException {
        final String msg = "errormsg";
        final Opt<Response> opt = new FbError().route(
            new RqFallback.Fake(500, new IOException(msg))
        );
        MatcherAssert.assertThat(opt.has(), Matchers.equalTo(true));
        MatcherAssert.assertThat(
            IOUtils.toString(opt.get().body()),
            Matchers.allOf(
                Matchers.containsString("oops, something "),
                Matchers.containsString("IOException"),
                Matchers.containsString(msg)
            )
        );
    }

}
