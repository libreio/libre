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

import com.google.common.collect.Lists;
import com.libre.om.Base;
import com.libre.om.Doc;
import com.libre.om.Docs;
import com.libre.om.mock.MkBase;
import com.libre.takes.RqWithTester;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.misc.Href;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkAddFriend}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class TkAddFriendTest {

    /**
     * TkAddFriend can add a friend.
     * @throws Exception If fails.
     */
    @Test
    public void addsFriend() throws Exception {
        final Base base = new MkBase();
        final Docs docs = base.user("urn:test:1").docs();
        final String name = "hey.txt";
        final Doc doc = docs.doc(name);
        final String friend = "The Dude";
        MatcherAssert.assertThat(
            Lists.newArrayList(doc.friends().names()),
            Matchers.not(Matchers.hasItem(friend))
        );
        MatcherAssert.assertThat(
            new RsPrint(
                new TkAddFriend(base).act(
                    new RqWithTester(
                        new RqFake(
                            "POST",
                            new Href()
                                .with("file", name)
                                .with("friend", friend)
                        )
                    )
                )
            ).print(),
            Matchers.containsString("document+shared")
        );
        MatcherAssert.assertThat(
            Lists.newArrayList(doc.friends().names()),
            Matchers.hasItem(friend)
        );
    }

}
