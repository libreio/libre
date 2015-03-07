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

import com.jcabi.aspects.Tv;
import com.jcabi.log.VerboseRunnable;
import com.nerodesk.mock.MkStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@code Launch}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @todo #14:15min Application should be able to get binary file properly.
 *  Add a test to check it works and fix if doesn't.
 */
public final class LaunchTest {

    /**
     * Temp directory.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * Launches web server on random port.
     * @throws Exception If fails
     */
    @Test
    @SuppressWarnings("PMD.DoNotUseThreads")
    public void launchesOnRandomPort() throws Exception {
        final int port = LaunchTest.port();
        final Thread thread = new Thread(
            new VerboseRunnable(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Launch.main(Integer.toString(port));
                        return null;
                    }
                },
                false, true
            )
        );
        thread.start();
        TimeUnit.SECONDS.sleep(1L);
        final URL url = new URL(String.format("http://localhost:%d", port));
        final BufferedReader input = new BufferedReader(
            new InputStreamReader(url.openConnection().getInputStream())
        );
        MatcherAssert.assertThat(
            input.readLine(), Matchers.containsString("version ")
        );
        thread.interrupt();
        thread.join((long) Tv.THOUSAND, 0);
    }

    /**
     * Application can return file content.
     * @throws Exception If fails
     */
    @Test
    @SuppressWarnings("PMD.DoNotUseThreads")
    public void getFile() throws Exception {
        final int port = LaunchTest.port();
        final Storage storage = new MkStorage(
            this.temp.getRoot().getAbsolutePath()
        );
        final Thread thread = new Thread(
            new VerboseRunnable(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        new Launch(port, storage);
                        return null;
                    }
                },
                false, true
            )
        );
        thread.start();
        TimeUnit.SECONDS.sleep(1L);
        final String path = "some_file.txt";
        final String content = "some text content";
        storage.put(path, IOUtils.toInputStream(content));
        final URL url = new URL(
            String.format("http://localhost:%d/api/file/%s", port, path)
        );
        try (final BufferedReader input = new BufferedReader(
            new InputStreamReader(url.openConnection().getInputStream())
        )
        ) {
            MatcherAssert.assertThat(
                input.readLine(), Matchers.containsString(content)
            );
            thread.interrupt();
            thread.join((long) Tv.THOUSAND);
        }
    }

    /**
     * Launcher can return Response for /.
     */
    @Test
    public void returnsRoot() {
        MatcherAssert.assertThat(
            new TkIndex().act(),
            Matchers.notNullValue()
        );
    }

    /**
     * Reserve new port for each call.
     * @return Reserved port.
     */
    private static int port() {
        try (ServerSocket socket = new ServerSocket()) {
            socket.setReuseAddress(true);
            socket.bind(
                new InetSocketAddress(InetAddress.getLoopbackAddress(), 0)
            );
            return socket.getLocalPort();
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
