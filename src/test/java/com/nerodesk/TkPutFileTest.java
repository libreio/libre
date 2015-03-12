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

import com.nerodesk.mock.MkStorage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.takes.ts.fork.RqRegex;

/**
 * Tests for {@code TkPutFile}.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.2
 */
public final class TkPutFileTest {
    /**
     * Temp directory.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * Template for API call.
     */
    private static final transient String TEMPLATE =
        "http://localhost:8080/api/file/%s";

    /**
     * TkPutFile can save content to file storage.
     * @throws Exception If something went wrong
     */
    @Test
    public void savesContent() throws Exception {
        final String dest = this.temp.getRoot().getAbsolutePath();
        final Storage storage = new MkStorage(dest);
        final String path = "saves.txt";
        final String content = "saved!";
        final Matcher matcher = Pattern.compile(TkPutFile.PATH)
            .matcher(String.format(TkPutFileTest.TEMPLATE, path));
        matcher.find();
        final RqRegex regex = Mockito.mock(RqRegex.class);
        Mockito.when(regex.matcher()).thenReturn(matcher);
        Mockito.when(regex.body()).thenReturn(IOUtils.toInputStream(content));
        final List<String> head = new TkPutFile(storage).route(regex).act()
            .head();
        MatcherAssert.assertThat(head.size(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(
            head.get(0),
            Matchers.equalTo("HTTP/1.1 200 OK")
        );
        final File file = Paths.get(dest, path).toFile();
        MatcherAssert.assertThat(file.exists(), Matchers.equalTo(true));
        MatcherAssert.assertThat(
            FileUtils.readFileToString(file, Charset.forName("UTF-8")),
            Matchers.equalTo(content)
        );
    }

    /**
     * TkPutFile can handle a storage error.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void handlesStorageError() throws IOException {
        final Storage storage = Mockito.mock(Storage.class);
        Mockito.doThrow(new IOException("IO error")).when(storage).put(
            org.mockito.Matchers.anyString(),
            org.mockito.Matchers.any(InputStream.class)
        );
        final String path = "ioerror.txt";
        final Matcher matcher = Pattern.compile(TkPutFile.PATH)
            .matcher(String.format(TkPutFileTest.TEMPLATE, path));
        matcher.find();
        final RqRegex regex = Mockito.mock(RqRegex.class);
        Mockito.when(regex.matcher()).thenReturn(matcher);
        final List<String> head = new TkPutFile(storage).route(regex).act()
            .head();
        MatcherAssert.assertThat(head.size(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(
            head.get(0),
            Matchers.equalTo("HTTP/1.1 500 Internal Error")
        );
    }

}
