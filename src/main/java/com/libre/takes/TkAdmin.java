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

import com.libre.om.Base;
import com.libre.om.User;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.auth.RqAuth;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeSource;
import org.takes.rs.xe.XeTransform;
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
            this.base,
            new XeAppend(
                "docs",
                new XeTransform<>(
                    user.docs().names(),
                    new XeTransform.Func<String>() {
                        @Override
                        public XeSource transform(final String name) {
                            return new XeAppend(
                                "doc",
                                new XeDirectives(
                                    new Directives()
                                        .add("path").set(
                                        String.format(
                                            "%s/%s", user.urn(), name
                                        )
                                    )
                                )
                            );
                        }
                    }
                )
            )
        );
    }
}
