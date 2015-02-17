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
import com.jcabi.manifests.Manifests;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import javax.validation.constraints.NotNull;

/**
 * Launch (used only for heroku).
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
public final class Launch {

    /**
     * Utility class.
     */
    private Launch() {
        // intentionally empty
    }

    /**
     * Entry point.
     * @param args Command line args
     * @throws Exception If fails
     */
    public static void main(final String... args) throws Exception {
        final int port = Integer.parseInt(args[0]);
        final HttpServer server = HttpServer.create(
            new InetSocketAddress(port), 0
        );
        server.createContext("/", new Launch.Handler());
        server.setExecutor(null);
        server.start();
        Logger.info(Launch.class, "HTTP server started on port %d", port);
    }

    /**
     * Handler.
     */
    static final class Handler implements HttpHandler {
        @Override
        public void handle(@NotNull final HttpExchange http)
            throws IOException {
            http.getResponseHeaders().set("Content-Type", "text/plain");
            final String body = String.format(
                "version %s is alive",
                Manifests.read("Nerodesk-Version")
            );
            http.sendResponseHeaders(
                HttpURLConnection.HTTP_OK, (long) body.length()
            );
            try (final OutputStream output = http.getResponseBody()) {
                output.write(body.getBytes("UTF-8"));
            }
        }
    }

}
