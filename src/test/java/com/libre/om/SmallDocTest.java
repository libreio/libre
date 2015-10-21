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
package com.libre.om;

import com.google.common.io.Files;
import com.jcabi.aspects.Tv;
import com.libre.om.mock.MkDoc;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link SmallDoc}.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class SmallDocTest {
    /**
     * Temporary folder.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @Rule
    public final transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * SmallDoc can write a doc.
     * @throws IOException In case of error
     */
    @Test
    public void writesTheDoc() throws IOException {
        final File file = new File(this.folder.newFolder(), "writable");
        final String content = "store";
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        new SmallDoc(new MkDoc(file, "", "")).write(
            new ByteArrayInputStream(bytes), bytes.length
        );
        MatcherAssert.assertThat(
            Files.toString(file, StandardCharsets.UTF_8),
            Matchers.equalTo(content)
        );
    }

    /**
     * SmallDoc can throw exception if the Doc is too big.
     * @throws IOException In case of error
     */
    @Test(expected = IllegalArgumentException.class)
    public void failsToWriteBigDoc() throws IOException {
        final File file = new File(
            this.folder.newFolder(), "big_file"
        );
        final String content = StringUtils.repeat('b', 101);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        new SmallDoc(new MkDoc(file, "", ""), Tv.HUNDRED).write(
            new ByteArrayInputStream(bytes), bytes.length
        );
        MatcherAssert.assertThat(
            Files.toString(file, StandardCharsets.UTF_8),
            Matchers.equalTo(content)
        );
    }

    /**
     * SmallDoc can shorten a URL.
     * @throws IOException In case of error.
     */
    @Test
    public void shortenUrl() throws IOException {
        final File file = new File(this.folder.newFolder(), "shorten");
        final String content = "shorten me";
        final Doc doc = new SmallDoc(
            new MkDoc(file, "", ""),
            Tv.HUNDRED
        );
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        doc.write(
            new ByteArrayInputStream(bytes), bytes.length
        );
        MatcherAssert.assertThat(
            doc.shortUrl(),
            Matchers.equalTo(file.toURI().toURL().toString())
        );
    }

    /**
     * SmallDoc conforms to equals and hashCode contract.
     */
    @Test
    public void conformsToEqualsHashCodeContract() {
        EqualsVerifier.forClass(SmallDoc.class)
            .suppress(Warning.TRANSIENT_FIELDS)
            .verify();
    }
}
