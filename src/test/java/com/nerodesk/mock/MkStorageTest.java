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
package com.nerodesk.mock;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test case for {@code MkStorage}.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @version $Id$
 * @since 0.1
 */
public final class MkStorageTest {

    /**
     * Temp directory.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * Get file from storage.
     * @throws Exception If fails
     */
    @Test
    public void getFile() throws Exception {
        final File root = this.temp.getRoot();
        final String path = "/test.txt";
        final String content = "Test content";
        Files.write(
            FileSystems.getDefault().getPath(root.getAbsolutePath(), path),
            content.getBytes()
        );
        MatcherAssert.assertThat(
            IOUtils.toString(new MkStorage(root.getAbsolutePath()).get(path)),
            Matchers.is(content)
        );
    }

    /**
     * Throws exception if file is not found.
     * @throws Exception If fails
     */
    @Test(expected = NoSuchFileException.class)
    public void getAbsentFile() throws Exception {
        new MkStorage(this.temp.getRoot().getAbsolutePath()).get("absent");
    }

    /**
     * MkStorage can write file to storage.
     * @throws Exception If fails
     */
    @Test
    public void writesFile() throws Exception {
        final String path = "/write.txt";
        final String content = "A content";
        final String absolute = this.temp.getRoot().getAbsolutePath();
        new MkStorage(absolute).put(
            path, IOUtils.toInputStream(content)
        );
        MatcherAssert.assertThat(
            IOUtils.toString(
                Files.newInputStream(
                    FileSystems.getDefault().getPath(absolute, path)
                )
            ),
            Matchers.is(content)
        );
    }

    /**
     * MkStorage can delete file.
     * @throws Exception If it fails.
     */
    @Test
    public void deletesFile() throws Exception {
        final String root = this.temp.getRoot().getAbsolutePath();
        final String path = "/delete.txt";
        final String content = "DELETE ME!";
        Files.write(
            FileSystems.getDefault().getPath(root, path),
            content.getBytes()
        );
        final File file = FileSystems.getDefault().getPath(root, path).toFile();
        MatcherAssert.assertThat(
            file.exists(),
            Matchers.equalTo(true)
        );
        new MkStorage(root).delete(path);
        MatcherAssert.assertThat(
            file.exists(),
            Matchers.equalTo(false)
        );
    }

}
