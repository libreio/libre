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
import com.nerodesk.om.Docs;
import com.nerodesk.takes.RqUser;
import java.io.IOException;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqForm;

/**
 * Remove directory (tree).
 *
 * @author Dmitry Koudryavtsev (juliasoft@mail.ru)
 * @version $Id$
 * @since 0.2
 */
public final class TkRmDir implements Take {
    /**
     * Base.
     */
    private final transient Base base;

    /**
     * Force rmDir.
     */
    private final transient boolean force;

    /**
     * Ctor.
     * @param bse Base
     * @param frc If true - force remove dirs.
     */
    TkRmDir(final Base bse, final boolean frc) {
        this.base = bse;
        this.force = frc;
    }

    /**
     * Ctor.
     * @param bse Base
     */
    TkRmDir(final Base bse) {
        this.base = bse;
        this.force = false;
    }

    /**
     * Process remove.
     * @param req Request.
     * @return Response.
     * @throws IOException On error.
     */
    @Override
    public Response act(final Request req) throws IOException {
        final RqForm frm = new RqForm.Base(req);
        final Iterable<String> dirNames = frm.param("name");
        final Docs docs = new RqUser(req, this.base).user().docs();
        for (final String dirName : dirNames) {
            docs.doc(dirName).rmDir(this.force);
        }
        return new RsForward(new RsFlash("direciory removed"));
    }
}
