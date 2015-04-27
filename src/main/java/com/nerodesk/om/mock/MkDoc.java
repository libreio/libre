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

import com.jcabi.log.Logger;
import com.nerodesk.om.Attributes;
import com.nerodesk.om.Doc;
import com.nerodesk.om.Friends;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Mocked version of doc.
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.2
 */
public final class MkDoc implements Doc {

    /**
     * Directory.
     */
    private final transient File dir;

    /**
     * URN.
     */
    private final transient String user;

    /**
     * Doc name.
     */
    private final transient String label;

    /**
     * Document attributes.
     */
    private final transient Attributes attrs;

    /**
     * Ctor.
     * @param file Directory
     * @param urn URN
     * @param name Document name
     */
    public MkDoc(final File file, final String urn, final String name) {
        this.dir = file;
        this.user = urn;
        this.label = name;
        this.attrs = new MkAttributes(this.file());
    }

    @Override
    public boolean exists() {
        return this.file().exists();
    }

    @Override
    public void delete() throws IOException {
        final File file = this.file();
        if (!file.delete()) {
            throw new IOException(String.format("Failed to delete %s", file));
        }
    }

    @Override
    public Friends friends() {
        return new MkFriends(this.dir, this.user, this.label);
    }

    @Override
    public void read(final OutputStream output) throws IOException {
        final File file = this.file();
        IOUtils.copy(new FileInputStream(this.file()), output);
        Logger.info(this, "%s loaded", file);
    }

    @Override
    public void write(final InputStream input, final long size)
        throws IOException {
        final File file = this.file();
        FileUtils.touch(file);
        try (final FileOutputStream output = new FileOutputStream(file)) {
            IOUtils.copy(input, output);
        }
        Logger.info(this, "%s saved", file);
    }

    @Override
    public Attributes attributes() throws IOException {
        return this.attrs;
    }

    /**
     * File.
     * @return File
     */
    private File file() {
        return new File(new File(this.dir, this.user), this.label);
    }
}
