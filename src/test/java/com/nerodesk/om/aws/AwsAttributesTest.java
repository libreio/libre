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
package com.nerodesk.om.aws;

import com.jcabi.s3.mock.MkOcket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link AwsAttributes}.
 *
 * @author Dmitry Zaytsev (dmitry.zaytsev@gmail.comm)
 * @version $Id$
 * @since 0.3.30
 * @todo #210:30min when the issue jcabi-s3:25 see
 *  https://github.com/jcabi/jcabi-s3/issues/25 will be resolved
 *  please uncomment the tests below. And make sure that all test
 *  are working properly.
 */
public final class AwsAttributesTest {
    /**
     * AwsAttributes can return correct the doc size.
     * @throws IOException If unsuccessful.
     */
    @Test
    @Ignore
    public void returnsCorrectSize() throws IOException {
        final File file = AwsAttributesTest.createFile(".tmp");
        MatcherAssert.assertThat(
            new AwsAttributes(
                new MkOcket(
                    file,
                    AwsAttributesTest.class.getSimpleName(),
                    "size"
                )
            ).size(),
            Matchers.equalTo(file.length())
        );
    }

    /**
     * AwsAttributes can return correct the doc creation date.
     * @throws IOException If unsuccessful.
     */
    @Test
    @Ignore
    public void returnsCorrectDate() throws IOException {
        final File file = AwsAttributesTest.createFile(".json");
        MatcherAssert.assertThat(
            new AwsAttributes(
                new MkOcket(
                    file,
                    AwsAttributesTest.class.getSimpleName(),
                    "date"
                )
            ).created().getTime(),
            Matchers.equalTo(
                Files.readAttributes(
                    Paths.get(file.toURI()),
                    BasicFileAttributes.class
                ).creationTime().toMillis()
            )
        );
    }

    /**
     * AwsAttributes can return correct the doc type.
     * @throws IOException If unsuccessful.
     */
    @Test
    @Ignore
    public void returnsCorrectType() throws IOException {
        final File file = AwsAttributesTest.createFile(".xml");
        MatcherAssert.assertThat(
            new AwsAttributes(
                new MkOcket(
                    file,
                    AwsAttributesTest.class.getSimpleName(),
                    "type"
                )
            ).type(),
            Matchers.equalTo("application/xml")
        );
    }

    /**
     * Creates temporary file.
     * @param extension The file extension
     * @throws IOException In case of failure.
     * @return The temporary file.
     */
    private static File createFile(final String extension) throws IOException {
        final File file = File.createTempFile(
            AwsAttributesTest.class.getSimpleName(),
            extension
        );
        file.deleteOnExit();
        final FileOutputStream out = new FileOutputStream(file);
        out.write("test".getBytes());
        out.close();
        return file;
    }
}
