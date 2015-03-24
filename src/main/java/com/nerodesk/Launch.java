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

import com.jcabi.manifests.Manifests;
import com.jcabi.s3.Region;
import com.jcabi.s3.retry.ReBucket;
import com.nerodesk.om.Base;
import com.nerodesk.om.aws.AwsBase;
import com.nerodesk.om.mock.MkBase;
import java.io.IOException;
import java.util.Arrays;
import org.takes.http.Exit;
import org.takes.http.FtCLI;

/**
 * Launch (used only for heroku).
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @todo #14:30min Add exception handling to return 404
 *  when resource not available. Now we show not nice stacktrace.
 *  Don't forget about unit tests.
 */
public final class Launch {

    /**
     * Arguments.
     */
    private final transient Iterable<String> arguments;

    /**
     * Ctor.
     * @param args Command line args
     */
    public Launch(final String[] args) {
        this.arguments = Arrays.asList(args);
    }

    /**
     * Main entry point.
     * @param args Arguments
     * @throws IOException If fails
     */
    public static void main(final String... args) throws IOException {
        new Launch(args).exec();
    }

    /**
     * Run it all.
     * @throws IOException If fails
     */
    public void exec() throws IOException {
        new FtCLI(
            new TsApp(Launch.base()),
            this.arguments
        ).start(Exit.NEVER);
    }

    /**
     * Base.
     * @return Base
     */
    private static Base base() {
        final String key = Manifests.read("Nerodesk-AwsKey");
        final Base base;
        if (key.startsWith("AAAA") || key.startsWith("${")) {
            base = new MkBase();
        } else {
            base = new AwsBase(
                new ReBucket(
                    new Region.Simple(
                        key, Manifests.read("Nerodesk-AwsSecret")
                    ).bucket(Manifests.read("Nerodesk-Bucket"))
                )
            );
        }
        return base;
    }

}
