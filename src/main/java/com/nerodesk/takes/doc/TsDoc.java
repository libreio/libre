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
import java.io.IOException;
import java.util.Iterator;
import org.takes.Request;
import org.takes.Take;
import org.takes.Takes;
import org.takes.facets.auth.RqAuth;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TsFork;
import org.takes.misc.Href;
import org.takes.rq.RqForm;
import org.takes.rq.RqHref;
import org.takes.rq.RqMultipart;
import org.takes.rq.RqPrint;

/**
 * Takes for a specific document.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.3
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 * @todo #103:30m/DEV Friend ejection is not implemented now. Let's create
 *  new "take" TkEjectFriend and implement ejection there. Also, let's not
 *  forget to create a unit test for it.
 */
public final class TsDoc implements Takes {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    public TsDoc(final Base bse) {
        this.base = bse;
    }

    @Override
    public Take route(final Request req) throws IOException {
        final String file;
        final Href href = new RqHref(req).href();
        final Iterator<String> param = href.param("file").iterator();
        if (param.hasNext()) {
            file = param.next();
        } else {
            file = new RqPrint(
                new RqMultipart(req).part("name").iterator().next()
            ).printBody();
        }
        final Doc doc = this.base.user(
            new RqAuth(req).identity().urn()
        ).docs().doc(file);
        return new TsFork(
            new FkRegex("/doc/read", new TkRead(doc)),
            new FkRegex("/doc/delete", new TkDelete(doc)),
            new FkRegex(
                "/doc/write",
                new Takes() {
                    @Override
                    public Take route(final Request request) {
                        return new TkWrite(doc, req);
                    }
                }
            ),
            new FkRegex(
                "/doc/add-friend",
                new Takes() {
                    @Override
                    public Take route(final Request rst) throws IOException {
                        return new TkAddFriend(
                            doc,
                            new RqForm(rst).param("friend").iterator().next()
                        );
                    }
                }
            ),
            new FkRegex(
                "/doc/eject-friend",
                new Takes() {
                    @Override
                    public Take route(final Request rst) {
                        return new TkEjectFriend(
                            doc, href.param("friend").iterator().next()
                        );
                    }
                }
            )
        ).route(req);
    }
}
