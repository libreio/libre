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

import com.google.common.net.MediaType;
import com.jcabi.log.VerboseProcess;
import com.jcabi.manifests.Manifests;
import com.nerodesk.om.Base;
import com.nerodesk.takes.doc.TkDir;
import com.nerodesk.takes.doc.TkDoc;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import org.takes.Take;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.PsFake;
import org.takes.facets.auth.PsLogout;
import org.takes.facets.auth.TkAuth;
import org.takes.facets.auth.TkSecure;
import org.takes.facets.auth.codecs.CcCompact;
import org.takes.facets.auth.codecs.CcHex;
import org.takes.facets.auth.codecs.CcSafe;
import org.takes.facets.auth.codecs.CcSalted;
import org.takes.facets.auth.codecs.CcXOR;
import org.takes.facets.auth.social.PsFacebook;
import org.takes.facets.fallback.FbChain;
import org.takes.facets.fallback.FbStatus;
import org.takes.facets.fallback.TkFallback;
import org.takes.facets.flash.TkFlash;
import org.takes.facets.fork.FkAnonymous;
import org.takes.facets.fork.FkAuthenticated;
import org.takes.facets.fork.FkFixed;
import org.takes.facets.fork.FkHitRefresh;
import org.takes.facets.fork.FkParams;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.tk.TkClasspath;
import org.takes.tk.TkFiles;
import org.takes.tk.TkFixed;
import org.takes.tk.TkGreedy;
import org.takes.tk.TkRedirect;
import org.takes.tk.TkVerbose;
import org.takes.tk.TkWithType;
import org.takes.tk.TkWrap;

/**
 * App.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle ClassFanOutComplexityCheck (500 lines)
 * @checkstyle ExcessiveMethodLength (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 * @todo #68:30min Error page should be an HTML page with stacktrace.
 *  See how it's done in Rultor (com.rultor.web.App.fallback).
 *  This implies adding velocity template and some styles.
 *  More details available in PR #99
 */
@SuppressWarnings({
    "PMD.UseUtilityClass", "PMD.ExcessiveImports",
    "PMD.ExcessiveMethodLength"
})
public final class TkApp extends TkWrap {

    /**
     * Ctor.
     * @param base Base
     * @throws IOException If something goes wrong.
     */
    public TkApp(final Base base) throws IOException {
        super(TkApp.make(base));
    }

    /**
     * Make itself.
     * @param base Base
     * @return Takes
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.DoNotUseThreads")
    public static Take make(final Base base) throws IOException {
        final Take fork = new TkFork(
            new FkParams(
                PsByFlag.class.getSimpleName(),
                Pattern.compile(".+"),
                new TkRedirect()
            ),
            new FkRegex(
                "/xsl/[a-z]+\\.xsl",
                new TkWithType(
                    new TkFork(
                        new FkHitRefresh(
                            new File("./src/main/xsl"),
                            new Runnable() {
                                @Override
                                public void run() {
                                    new VerboseProcess(
                                        new ProcessBuilder(
                                            "mvn",
                                            "htmlcompressor:xml"
                                        )
                                    ).stdout();
                                }
                            },
                            new TkFiles("./target/classes")
                        ),
                        new FkFixed(new TkClasspath())
                    ),
                    "text/xsl"
                )
            ),
            new FkRegex(
                "/css/[a-z]+\\.css",
                new TkWithType(
                    new TkFork(
                        new FkHitRefresh(
                            new File("./src/main/scss"),
                            new Runnable() {
                                @Override
                                public void run() {
                                    new VerboseProcess(
                                        new ProcessBuilder(
                                            "mvn",
                                            "sass:update-stylesheets",
                                            "minify:minify"
                                        )
                                    ).stdout();
                                }
                            },
                            new TkFiles("./target/classes")
                        ),
                        new FkFixed(new TkClasspath())
                    ),
                    "text/css"
                )
            ),
            new FkRegex(
                "/images/[a-z]+\\.png",
                new TkWithType(new TkClasspath(), MediaType.PNG.toString())
            ),
            new FkRegex(
                "/svg/[a-z]+\\.svg",
                new TkWithType(
                    new TkClasspath(),
                    MediaType.SVG_UTF_8.toString()
                )
            ),
            new FkRegex("/robots.txt", ""),
            new FkRegex(
                "/",
                new TkFork(
                    new FkAuthenticated(new TkDocs(base)),
                    new FkAnonymous(new TkIndex(base))
                )
            ),
            new FkRegex(
                "/doc/.*",
                new TkSecure(new TkDoc(base))
            ),
            new FkRegex(
                "/dir/.*",
                new TkSecure(new TkGreedy(new TkDir()))
            )
        );
        return TkApp.fallback(
            new TkVerbose(new TkFlash(TkApp.auth(fork)))
        );
    }

    /**
     * Fallback.
     * @param take Original take
     * @return Take with fallback
     */
    private static Take fallback(final Take take) {
        return new TkFallback(
            take,
            new FbChain(
                // @checkstyle MagicNumberCheck (1 line)
                new FbStatus(404, new TkFixed(new RsNotFound())),
                new FbError()
            )
        );
    }

    /**
     * Authenticated.
     * @param take Take
     * @return Authenticated takes
     */
    private static Take auth(final Take take) {
        final String key = Manifests.read("Nerodesk-FacebookId");
        return new TkAuth(
            take,
            new PsChain(
                new PsFake(key.startsWith("XXXX") || key.startsWith("${")),
                new PsByFlag(
                    new PsByFlag.Pair(
                        PsFacebook.class.getSimpleName(),
                        new PsFacebook(
                            key,
                            Manifests.read("Nerodesk-FacebookSecret")
                        )
                    ),
                    new PsByFlag.Pair(
                        PsLogout.class.getSimpleName(),
                        new PsLogout()
                    )
                ),
                new PsCookie(
                    new CcSafe(
                        new CcHex(
                            new CcXOR(
                                new CcSalted(new CcCompact()),
                                Manifests.read("Nerodesk-SecurityKey")
                            )
                        )
                    )
                )
            )
        );
    }

}
