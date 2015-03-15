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
package com.nerodesk;

import com.jcabi.log.Logger;
import java.io.IOException;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsWithBody;
import org.takes.rs.RsWithStatus;
import org.takes.rs.RsWithType;
import org.takes.tk.TkFixed;
import org.takes.ts.fork.RqRegex;
import org.takes.ts.fork.Target;

/**
 * Part of REST API to put a file into the storage.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.2
 */
public final class TkPutFile implements Target<RqRegex> {

    /**
     * File path.
     */
    public static final String PATH = "/api/file/(?<path>[^/]+)";

    /**
     * File storage.
     */
    private final transient Storage storage;

    /**
     * Ctor.
     * @param store Storage.
     */
    public TkPutFile(final Storage store) {
        this.storage = store;
    }

    @Override
    public Take route(final RqRegex req) throws IOException {
        final String path = req.matcher().group("path");
        Response response = new RsWithType(
            new RsWithBody("Success!"), "text/html"
        );
        try {
            this.storage.put(
                path,
                req.body()
            );
        } catch (final IOException ex) {
            Logger.error(
                this,
                String.format(
                    "Internal error saving to storage at %s: %s",
                    path,
                    ex.getMessage()
                )
            );
            response = new RsWithStatus(
                new RsWithBody(
                    String.format(
                        "Internal error saving to storage at %s",
                        path
                    )
                ),
                // @checkstyle MagicNumberCheck (1 lines)
                500
            );
        }
        return new TkFixed(response);
    }

}
