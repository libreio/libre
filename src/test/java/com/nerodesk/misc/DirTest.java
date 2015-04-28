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
package com.nerodesk.misc;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple FileUtils.listFilesAndDirs tests.
 *
 * @author Dmitry Koudryavtsev (juliasoft@mail.ru)
 * @version $Id$
 */
public final class DirTest {

    /**
     * Default generated name length.
     */
    private static final int NAME_LEN = 8;

    /**
     * Fake root.
     */
    private static File fakeRoot;

    /**
     * Getter.
     * @return Return fake root.
     */
    public static File getFakeRoot() {
        return fakeRoot;
    }

    /**
     * Setter.
     * @param frr Fake root.
     */
    public static void setFakeRoot(final File frr) {
        DirTest.fakeRoot = frr;
    }

    /**
     * Make temporary directory.
     * @throws Exception If test fails.
     */
    @Before
    public void setUp() throws Exception {
        fakeRoot = new File(
                FileUtils.getTempDirectory(),
                RandomStringUtils.randomNumeric(NAME_LEN)
        );
        if (!fakeRoot.exists()) {
            FileUtils.forceMkdir(fakeRoot);
        }
    }

    /**
     * Drop temporary directory, if exists.
     * @throws Exception If test fails.
     */
    @After
    public void tearDown() throws Exception {
        if (fakeRoot.exists()) {
            FileUtils.forceDelete(fakeRoot);
        }
    }

    /**
     * Test recursive list files and dirs function and remove root.
     */
    @Test
    public void testDirList() {
        final Collection<File> files = FileUtils.listFilesAndDirs(
                fakeRoot,
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE
        );
        MatcherAssert.assertThat(files, Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(files.isEmpty(), Matchers.is(false));
        files.remove(fakeRoot);
        MatcherAssert.assertThat(files.isEmpty(), Matchers.is(true));
    }
}
