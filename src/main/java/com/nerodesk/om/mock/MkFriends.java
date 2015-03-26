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

import com.nerodesk.om.Friends;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mocked version of friends.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.3
 * @todo #103:30min/DEV This class is not implemented and doesn't work. Let's
 *  implement it and test in a unit test. Let's use some primitive mechanism,
 *  based on file names.
 */
@ToString
@EqualsAndHashCode
public final class MkFriends implements Friends {

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
     * Ctor.
     * @param file Directory
     * @param urn URN
     * @param name Document name
     */
    public MkFriends(final File file, final String urn, final String name) {
        this.dir = file;
        this.user = urn;
        this.label = name;
    }

    @Override
    public boolean leader() throws IOException {
        return true;
    }

    @Override
    public Iterable<String> names() throws IOException {
        assert this.dir != null;
        assert this.user != null;
        assert this.label != null;
        return Collections.emptyList();
    }

    @Override
    public void add(final String name) throws IOException {
        // nothing
    }

    @Override
    public void eject(final String name) throws IOException {
        // nothing
    }
}
