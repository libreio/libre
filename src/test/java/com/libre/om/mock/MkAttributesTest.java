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
package com.libre.om.mock;

import com.libre.om.Attributes;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link MkAttributes}.
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class MkAttributesTest {
    /**
     * Temporary folder.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * MkAttributes can guess content type.
     * @throws IOException In case of error
     */
    @Test
    public void guessesContentType() throws IOException {
        final File file = new File(this.folder.newFolder(), "findout");
        final String content = "Ordinary text content.";
        final Attributes attrs = new MkAttributes(file);
        Files.write(
            file.toPath(),
            content.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE
        );
        MatcherAssert.assertThat(
            attrs.type(),
            Matchers.notNullValue()
        );
    }

    /**
     * MkAttributes can count bytes in document.
     * @throws IOException In case of error
     */
    @Test
    public void countsBytesInDocument() throws IOException {
        final File file = new File(this.folder.newFolder(), "size");
        final String content = "count me";
        final Attributes attrs = new MkAttributes(file);
        Files.write(
            file.toPath(),
            content.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE
        );
        MatcherAssert.assertThat(
            attrs.size(),
            Matchers.equalTo((long) content.length())
        );
    }

    /**
     * MkAttributes can retrieve file creation time.
     * @throws IOException In case of error
     */
    @Test
    public void retrievesFileCreationTime() throws IOException {
        final File file = new File(this.folder.newFolder(), "time");
        final String content = "some content";
        final long before = System.currentTimeMillis();
        final Attributes attrs = new MkAttributes(file);
        Files.write(
            file.toPath(),
            content.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE
        );
        final long after = System.currentTimeMillis();
        MatcherAssert.assertThat(
            TimeUnit.MILLISECONDS.toSeconds(attrs.created().getTime()),
            Matchers.allOf(
                Matchers.greaterThanOrEqualTo(
                    TimeUnit.MILLISECONDS.toSeconds(before)
                ),
                Matchers.lessThanOrEqualTo(
                    TimeUnit.MILLISECONDS.toSeconds(after)
                )
            )
        );
    }

    /**
     * MkAttributes can set visibility.
     * @throws IOException In case of error
     */
    @Test
    public void setsVisibility() throws IOException {
        final File file = new File(this.folder.newFolder(), "visibility");
        final String content = "text content.";
        Files.write(
            file.toPath(),
            content.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE
        );
        final Attributes attrs = new MkAttributes(file);
        MatcherAssert.assertThat(
            attrs.visible(),
            Matchers.is(false)
        );
        attrs.show(true);
        MatcherAssert.assertThat(
            attrs.visible(),
            Matchers.is(true)
        );
    }

    /**
     * MkAttributes can throw an exception if visibility is set for a
     * nonexistent file.
     * @throws Exception In case of error
     */
    @Test(expected = IOException.class)
    public void throwsExceptionIfVisibilitySetForNonexistentFile()
        throws Exception {
        new MkAttributes(new File("nonexistent-set-visibility")).show(true);
    }

    /**
     * MkAttributes can throw an exception if visible is called for a
     * nonexistent file.
     * @throws Exception In case of error
     */
    @Test(expected = IOException.class)
    public void throwsExceptionIfVisibileCalledNonexistentFile()
        throws Exception {
        new MkAttributes(new File("nonexistent-call-visible")).visible();
    }
}
