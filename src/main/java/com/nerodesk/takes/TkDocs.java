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

import com.nerodesk.om.Attributes;
import com.nerodesk.om.Base;
import com.nerodesk.om.Doc;
import com.nerodesk.om.Docs;
import com.nerodesk.om.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.RqAuth;
import org.takes.misc.Href;
import org.takes.rq.RqHref;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeLink;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeTransform;
import org.xembly.Directives;

/**
 * List of docs.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkDocs implements Take {

    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Ctor.
     * @param bse Base
     */
    public TkDocs(final Base bse) {
        this.base = bse;
    }

    // @todo #118:30min User balance shouldn't be inside docs page but in some
    //  kind of decorator of all the pages that are show to logged in users.
    //  Create such place and move user directive there.
    @Override
    public Response act(final Request req) throws IOException {
        final User user = this.base.user(new RqAuth(req).identity().urn());
        final Docs docs = user.docs();
        return new RsPage(
            "/xsl/docs.xsl",
            req,
            this.base,
            new XeLink("upload", "/doc/write"),
            new XeAppend(
                "user",
                new XeAppend(
                    "balance",
                    new XeDirectives(
                        new Directives().set(
                            Integer.toString(user.account().balance())
                        )
                    )
                )
            ),
            new XeAppend(
                "docs",
                new XeTransform<>(
                    docs.names(),
                    new XeTransform.Func<String>() {
                        @Override
                        public XeSource transform(final String doc)
                            throws IOException {
                            return TkDocs.source(docs.doc(doc), doc, req);
                        }
                    }
                )
            ),
            new XeLink("mkdir", "/dir/create")
        );
    }

    /**
     * Convert doc into XE source.
     * @param doc Doc
     * @param name Document name
     * @param req Request
     * @return Source
     * @throws IOException If fails
     */
    private static XeSource source(final Doc doc, final String name,
        final Request req) throws IOException {
        final Href home = new RqHref.Base(req).href()
            .path("doc").with("file", name);
        final Attributes attrs = doc.attributes();
        final String created;
        if (attrs.created() == null) {
            created = "";
        } else {
            created = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.US
            ).format(attrs.created().getTime());
        }
        return new XeAppend(
            "doc",
            new XeChain(
                new XeDirectives(
                    new Directives()
                        .add("name").set(name).up()
                        .add("size").set(Long.toString(attrs.size())).up()
                        .add("created")
                        .set(created).up()
                        .add("type").set(attrs.type()).up()
                        .add("name").set(name)
                ),
                new XeLink("read", home.path("read")),
                new XeLink("short", new Href(doc.shortUrl())),
                new XeLink("delete", home.path("delete")),
                new XeLink("add-friend", home.path("add-friend")),
                new XeLink("set-visibility", home.path("set-visibility")),
                new XeAppend(
                    "friends",
                    new XeTransform<>(
                        doc.friends().names(),
                        new XeTransform.Func<String>() {
                            @Override
                            public XeSource transform(final String friend) {
                                return TkDocs.source(friend, home);
                            }
                        }
                    )
                )
            )
        );
    }

    /**
     * Convert friend into XE source.
     * @param friend Name of the friend
     * @param home Home of the doc
     * @return Source
     */
    private static XeSource source(final String friend,
        final Href home) {
        return new XeAppend(
            "friend",
            new XeChain(
                new XeDirectives(
                    new Directives().add("name").set(friend)
                ),
                new XeLink(
                    "eject",
                    home.path("eject-friend").with("friend", friend)
                )
            )
        );
    }

}
