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

import com.nerodesk.om.Base;
import com.nerodesk.om.Docs;
import com.nerodesk.om.User;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.RqAuth;
import org.takes.rs.xe.XeSource;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * App.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.4
 * @todo #213:30min Admin page shows docs of currently logged in user only.
 *  We need to show docs of all users. When done, please unignore
 *  TkAdminTest.returnsListOfDocsOfSeveralUsers() and check if it works.
 * @todo #213:30min Admin page should be accessible for predefined
 *  and hardcoded urns. Now it is accessible for any registered user.
 *  Please, specify several urns and check if logged user is authorized
 *  to see admin page. If not - redirect to root (?)
 */
public final class TkAdmin implements Take {
    /**
     * Base.
     */
    private final transient Base base;
    /**
     * Ctor.
     * @param bse Base
     */
    public TkAdmin(final Base bse) {
        this.base = bse;
    }

    @Override
    public Response act(final Request req) throws IOException {
        final User user = this.base.user(new RqAuth(req).identity().urn());
        return new RsPage(
            "/xsl/admin.xsl",
            req,
            new XeSource() {
                @Override
                public Iterable<Directive> toXembly() throws IOException {
                    return TkAdmin.this.list(user);
                }
            }
        );
    }

    /**
     * Convert docs into directives.
     * @param user User
     * @return Directives
     * @throws IOException If fails
     */
    private Iterable<Directive> list(final User user)
        throws IOException {
        final Directives dirs = new Directives();
        final Docs docs = user.docs();
        dirs.add("docs");
        for (final String name : docs.names()) {
            dirs.add("doc")
                .add("path").set(
                    String.format(
                        "%s/%s", user.urn().replaceAll(":", "/"), name
                    )
                ).up();
            dirs.up();
        }
        return dirs;
    }
}
