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
import org.apache.commons.lang3.StringUtils;
import org.takes.Request;
import org.takes.Take;
import org.takes.Takes;
import org.takes.facets.auth.RqAuth;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TsFork;
import org.takes.misc.Href;
import org.takes.rq.RqHeaders;
import org.takes.rq.RqHref;
import org.takes.rq.RqMultipart;

/**
 * Takes for a specific document.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.3
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
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

    // @todo #94:30min I tried to get filename from file meta data and it
    //  doesn't look clean but works for now. We have to either wait for impl.
    //  from `Takes` framework or implement in this project class that is going
    //  to do this in clean way.
    @Override
    public Take route(final Request req) throws IOException {
        final String file;
        final Href href = new RqHref(req).href();
        final String key = "file";
        final Iterator<String> param = href.param(key).iterator();
        if (param.hasNext()) {
            file = param.next();
        } else {
            final String disposition = new RqHeaders(
                new RqMultipart(req).part(key).iterator().next()
            ).header("Content-Disposition").iterator().next();
            file = StringUtils.substringBefore(
                StringUtils.substringAfter(disposition, "filename=\""),
                "\""
            );
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
                    public Take route(final Request request) {
                        return new TkAddFriend(
                            doc, href.param("friend").iterator().next()
                        );
                    }
                }
            )
        ).route(req);
    }
}
