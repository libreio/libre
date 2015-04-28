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
package com.nerodesk.om.mock;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@code MkDocs}.
 * @author Yuriy Alevohin (alevohin@mail.ru)
 * @version $Id$
 * @since 0.2
 */
public final class MkDocsTest {

    /**
     * Temporary folder.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder temp = new TemporaryFolder();

    /**
     * MkDocs can return a list of docs.
     * @throws Exception If fails.
     * @todo 90:30min MkDocs creates files in directory
     *  `new File(this.dir, this.user)` (see MkDoc). Method `names`
     *  receives file's list from another directory (just dir). Don't
     *  forget remove @Ignore after fix.
     * @todo 90:30min Add test for MkDocs.names() to check that files
     *  from one user don't fall into the list of files of another user.
     * @todo 90:30min Add test for MkDocs.names() for several files
     *  in several folders. For example, urn/test/1/foo/1.txt and
     *  urn/test/1/bar/1.txt should both fall into the result. Username
     *  for test should be like "urn:test:1".
     */
    @Test
    @Ignore
    public void returnsNames() throws Exception {
        final String[] files = new String[] {"a.txt", "b.txt"};
        final MkDocs docs = new MkDocs(this.temp.newFolder(), "user1");
        for (final String file : files) {
            final String content = "content1";
            docs.doc(file).write(
                IOUtils.toInputStream(content), content.getBytes().length
            );
        }
        MatcherAssert.assertThat(
            docs.names(),
            Matchers.contains(files)
        );
    }

    /**
     * Test create directory.
     * @throws IOException If test fail.
     */
    @Test
    public void testMkDir() throws IOException {
        final String[] dirs = new String[] {"tmp1", "tmp2"};
        final MkDocs docs = new MkDocs(this.temp.newFolder(), "temp");
        for (final String dir : dirs) {
            docs.doc(dir).mkDir();
        }
        MatcherAssert.assertThat(
                docs.names(),
                Matchers.containsInAnyOrder(dirs)
        );
    }

    /**
     * Test non-force remove directory.
     * @throws IOException If test fail.
     */
    @Test
    public void testRmDir() throws IOException {
        final String[] dirs = new String[] {"tmp10", "tmp20"};
        final MkDocs docs = new MkDocs(this.temp.newFolder(), "temp10");
        for (final String dir : dirs) {
            docs.doc(dir).mkDir();
        }
        MatcherAssert.assertThat(
                docs.names(),
                Matchers.containsInAnyOrder(dirs)
        );
        for (final String dir : dirs) {
            docs.doc(dir).rmDir(false);
        }
        MatcherAssert.assertThat(
                docs.names(),
                Matchers.not(Matchers.contains(dirs))
        );
    }

    /**
     * Test force remove directory.
     * @throws IOException If test fail.
     */
    @Test
    public void testForceRmDir() throws IOException {
        final String[] dirs = new String[] {"tmp11", "tmp12"};
        final MkDocs docs = new MkDocs(this.temp.newFolder(), "temp111");
        for (final String dir : dirs) {
            docs.doc(dir).mkDir();
        }
        MatcherAssert.assertThat(
                docs.names(),
                Matchers.containsInAnyOrder(dirs)
        );
        for (final String dir : dirs) {
            docs.doc(dir).rmDir(true);
        }
        MatcherAssert.assertThat(
                docs.names(),
                Matchers.not(Matchers.contains(dirs))
        );
    }

    /**
     * MkDocs can return existing Doc.
     * @throws Exception If fails.
     */
    @Test
    public void returnsExistingDoc() throws Exception {
        final MkDocs docs = new MkDocs(this.temp.newFolder(), "user2");
        final String filename = "test2";
        final String content = "content2";
        docs.doc(filename).write(
            IOUtils.toInputStream(content), content.getBytes().length
        );
        MatcherAssert.assertThat(
            docs.doc(filename).exists(),
            Matchers.is(true)
        );
    }

    /**
     * MkDocs can calculate size of storage.
     * @throws Exception If it fails.
     */
    @Test
    public void calculatesSize() throws Exception {
        final MkDocs docs = new MkDocs(this.temp.getRoot(), "1");
        MatcherAssert.assertThat(
            docs.size(),
            Matchers.equalTo(0L)
        );
        FileUtils.writeByteArrayToFile(
            this.temp.newFile("test1"),
            new byte[]{0}
        );
        MatcherAssert.assertThat(
            docs.size(),
            Matchers.equalTo(1L)
        );
    }
}
