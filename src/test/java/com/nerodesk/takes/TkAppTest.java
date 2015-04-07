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

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.http.wire.VerboseWire;
import com.nerodesk.om.Base;
import com.nerodesk.om.mock.MkBase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.takes.http.FtRemote;

/**
 * Test case for {@code Launch}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MultipleStringLiterals (500 lines)
 * @todo #89:1h Doc should support partitioned read.
 *  If file is too big to fit in one read request it should be split
 *  by the Doc on parts and returned to the client one-by-one.
 *  It will use multipart/form-data requests with Content-Range header.
 *  See http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16
 *  for details about Content-Range.
 *  Client side should will receive first response, read Content-Range
 *  and request for the subsequent partitions.
 *  AWS S3 already supports partitioned read operation
 *  (see GetObjectRequest.setRange() for details). For the MkDoc partitioned
 *  read should be implemented as well.
 *  Let's start from proper tests. See example for partitioned write
 *  AppTest.uploadsBigFile()
 * @todo #160:30min Test readsFileWithSpecialCharactersInName and
 *  deletesFileWithSpecialCharactersInName are disabled because of a bug in
 *  takes 0.11.2, when yegor256/takes#126 is resolved and a new takes with this
 *  fix is released add it to the pom.xml and uncomment those tests.
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class TkAppTest {

    /**
     * Fake URN.
     */
    private static final String FAKE_URN = "urn:test:1";

    /**
     * File query param.
     */
    private static final String FILE = "file";

    /**
     * Launches web server on random port.
     * @throws Exception If fails
     */
    @Test
    public void launchesOnRandomPort() throws Exception {
        final TkApp app = new TkApp(new MkBase());
        new FtRemote(app).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .as(XmlResponse.class)
                        .assertXPath("/xhtml:html");
                    new JdkRequest(home)
                        .through(VerboseWire.class)
                        .header("Accept", "application/xml")
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .as(XmlResponse.class)
                        .assertXPath("/page/version");
                }
            }
        );
    }

    /**
     * TsApp can launch web server in non-latin locale.
     * @throws Exception If fails
     */
    @Test
    public void launchesWebServerInNonLatinLocale() throws Exception {
        final Locale def = Locale.getDefault();
        try {
            Locale.setDefault(Locale.CHINESE);
            final TkApp app = new TkApp(new MkBase());
            new FtRemote(app).exec(
                new FtRemote.Script() {
                    @Override
                    public void exec(final URI home) throws IOException {
                        new JdkRequest(home)
                            .fetch()
                            .as(RestResponse.class)
                            .assertStatus(HttpURLConnection.HTTP_OK)
                            .as(XmlResponse.class)
                            .assertXPath("/xhtml:html");
                    }
                }
            );
        } finally {
            Locale.setDefault(def);
        }
    }

    /**
     * Application can return file content.
     * @throws Exception If fails
     */
    @Test
    public void returnsFileContent() throws Exception {
        final Base base = new MkBase();
        final String name = "test.txt";
        base.user(TkAppTest.FAKE_URN).docs().doc(name).write(
            new ByteArrayInputStream("hello, world!".getBytes())
        );
        final TkApp app = new TkApp(base);
        new FtRemote(app).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .uri().path("/doc/read")
                        .queryParam(TkAppTest.FILE, name).back()
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .assertBody(Matchers.startsWith("hello, world"));
                }
            }
        );
    }

    /**
     * TsApp can read file with special characters in its name.
     * @throws Exception If fails
     */
    @Test
    @Ignore
    public void readsFileWithSpecialCharactersInName() throws Exception {
        final Base base = new MkBase();
        final String name = "[][].txt";
        final String data = "fake data";
        base.user(TkAppTest.FAKE_URN).docs().doc(name).write(
            new ByteArrayInputStream(data.getBytes())
        );
        new FtRemote(new TkApp(base)).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .uri().path("/doc/read")
                        .queryParam(TkAppTest.FILE, name).back()
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .assertBody(Matchers.startsWith(data));
                }
            }
        );
    }

    /**
     * TsApp can delete file with special characters in its name.
     * @throws Exception If fails
     */
    @Test
    @Ignore
    public void deletesFileWithSpecialCharactersInName() throws Exception {
        final Base base = new MkBase();
        final String name = "[][].txt";
        final String data = "fake data";
        base.user(TkAppTest.FAKE_URN).docs().doc(name).write(
            new ByteArrayInputStream(data.getBytes())
        );
        MatcherAssert.assertThat(
            base.user(TkAppTest.FAKE_URN).docs().names(),
            Matchers.not(Matchers.emptyIterable())
        );
        new FtRemote(new TkApp(base)).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .uri().path("/doc/delete")
                        .queryParam(TkAppTest.FILE, name).back()
                        .fetch().as(RestResponse.class).follow()
                        .fetch().as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK);
                }
            }
        );
        MatcherAssert.assertThat(
            base.user(TkAppTest.FAKE_URN).docs().names(),
            Matchers.emptyIterable()
        );
    }

    /**
     * Application can return file content in binary form.
     * @throws Exception If something goes wrong
     */
    @Test
    public void returnsBinaryContent() throws Exception {
        final Base base = new MkBase();
        final String name = "test.dat";
        final byte[] content = new byte[]{0x00, 0x0a, (byte) 0xff, (byte) 0xfe};
        base.user(TkAppTest.FAKE_URN).docs().doc(name).write(
            new ByteArrayInputStream(content)
        );
        new FtRemote(new TkApp(base)).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    MatcherAssert.assertThat(
                        new JdkRequest(home)
                            .uri().path("/doc/read")
                            .queryParam(TkAppTest.FILE, name).back()
                            .fetch()
                            .as(RestResponse.class)
                            .assertStatus(HttpURLConnection.HTTP_OK)
                            .binary(),
                        Matchers.is(content)
                    );
                }
            }
        );
    }

    /**
     * Application can return static image.
     * @throws Exception If something goes wrong
     */
    @Test
    public void returnsStaticImage() throws Exception {
        final String name = "/images/logo.png";
        new FtRemote(new TkApp(new MkBase())).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    MatcherAssert.assertThat(
                        new JdkRequest(home)
                            .uri().path(name)
                            .back()
                            .fetch()
                            .as(RestResponse.class)
                            .assertStatus(HttpURLConnection.HTTP_OK)
                            .assertHeader("Content-Type", "image/png")
                            .binary(),
                        Matchers.is(
                            IOUtils.toByteArray(
                                TkAppTest.class.getResource(name)
                            )
                        )
                    );
                }
            }
        );
    }

    /**
     * Application can upload file content.
     * @throws Exception If fails
     */
    @Test
    public void uploadsFileContent() throws Exception {
        final Base base = new MkBase();
        final String name = "small.txt";
        final String file = "uploaded by client";
        new FtRemote(new TkApp(base)).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    TkAppTest.write(home)
                        .fetch(
                            new ByteArrayInputStream(
                                TkAppTest.multipart(name, file).getBytes()
                            )
                        )
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_SEE_OTHER);
                }
            }
        );
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        base.user(TkAppTest.FAKE_URN).docs().doc(name).read(stream);
        MatcherAssert.assertThat(
            IOUtils.toString(stream.toByteArray(), Charsets.UTF_8.name()),
            Matchers.containsString(file)
        );
    }

    /**
     * Application can upload big file split on parts.
     * @todo #89:1h Doc should support partitioned write.
     *  If file is too big to fit in one write request it will be split
     *  by the client side on parts and sent in several requests.
     *  It will use multipart/form-data requests with Content-Range header.
     *  See http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16
     *  for details about Content-Range.
     *  Server side should receive these requests, retrieve and store
     *  file parts on-the-fly. We should not wait for the whole file.
     *  AWS S3 already supports partitioned operations
     *  (see AmazonS3.uploadPart() for details). For the MkDoc partitioned
     *  write should be implemented as well. Unignore the test after
     *  MkDoc partitioned write is implemented.
     * @throws Exception If fails
     */
    @Test
    @Ignore
    public void uploadsBigFile() throws Exception {
        final int psize = 5;
        final Base base = new MkBase();
        final String name = "large.txt";
        final String file = "123451234512345";
        new FtRemote(new TkApp(base)).exec(
            // @checkstyle AnonInnerLengthCheck (30 lines)
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    int pos = 0;
                    while (pos < file.length() - 1) {
                        TkAppTest.write(home)
                            .header(
                                HttpHeaders.CONTENT_RANGE,
                                String.format(
                                    "bytes %d-%d/%d",
                                    pos, pos + psize, file.length()
                                )
                            )
                            .fetch(
                                new ByteArrayInputStream(
                                    TkAppTest.multipart(name, file).getBytes()
                                )
                            )
                            .as(RestResponse.class)
                            .assertStatus(HttpURLConnection.HTTP_OK);
                        pos += psize;
                    }
                }
            }
        );
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        base.user(TkAppTest.FAKE_URN).docs().doc(name).read(stream);
        MatcherAssert.assertThat(
            IOUtils.toString(stream.toByteArray(), Charsets.UTF_8.name()),
            Matchers.containsString(file)
        );
    }

    /**
     * Application can show 'page not found' page.
     * @throws Exception If fails
     */
    @Test
    public void showsPageNotFound() throws Exception {
        final Base base = new MkBase();
        new FtRemote(new TkApp(base)).exec(
            new FtRemote.Script() {
                @Override
                public void exec(final URI home) throws IOException {
                    new JdkRequest(home)
                        .uri().path("/page-is-absent").back()
                        .fetch()
                        .as(RestResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .assertBody(Matchers.startsWith("Page not found!"));
                }
            }
        );
    }

    /**
     * Create request to add a file.
     * @param home URI home
     * @return Request
     */
    private static Request write(final URI home) {
        return new JdkRequest(home)
            .method("POST")
            .uri().path("/doc/write").back()
            .header(
                HttpHeaders.CONTENT_TYPE,
                "multipart/form-data; boundary=AaB03x"
            );
    }

    /**
     * Multipart request body.
     * @param name File name
     * @param content File content
     * @return Request body
     */
    private static String multipart(final String name, final String content) {
        return Joiner.on("\r\n").join(
            " --AaB03x",
            "Content-Disposition: form-data; name=\"name\"",
            "",
            name,
            "--AaB03x",
            Joiner.on("; ").join(
                "Content-Disposition: form-data",
                "name=\"file\"",
                String.format("filename=\"%s\"", name)
            ),
            "Content-Transfer-Encoding: utf-8",
            "",
            content,
            "--AaB03x--"
        );
    }

}
