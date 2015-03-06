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
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.ts.TsRegex;

/**
 * Launch (used only for heroku).
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @checkstyle HideUtilityClassConstructorCheck (500 lines)
 * @todo #48:30min This is a utility class and should only launch the app. The
 *  web server code should be moved to its own class. Remove the checkstyle
 *  suppression above and also the PMD suppression below. This is continuation
 *  of task #48. I moved `TkIndex` to separate class but it's not enough.
 */
@SuppressWarnings("PMD.UseUtilityClass")
public final class Launch {

    /**
     * Default port.
     */
    private static final int DEFAULT_PORT = 8080;

    /**
     * Ctor.
     * @param port Port.
     * @throws IOException If something goes wrong.
     */
    public Launch(final int port) throws IOException {
        Logger.info(Launch.class, "HTTP server starting on port %d", port);
        new FtBasic(
            new TsRegex().with("/", new TkIndex()),
            port
        ).start(Exit.NEVER);
    }

    /**
     * Entry point.
     * @param args Command line args
     * @throws IOException If fails
     */
    public static void main(final String... args) throws IOException {
        // @checkstyle MagicNumberCheck (1 line)
        int port = Launch.DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new Launch(port);
    }

}
