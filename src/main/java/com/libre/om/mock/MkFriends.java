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

import com.libre.om.Friends;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mocked version of friends.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.3
 * @todo #173:30min/DEV This class does not have any tests implemented yet.
 *  Let's implement tests for all existent methods (leader, names, add, eject).
 */
@EqualsAndHashCode(of = { "friends" })
@ToString
public final class MkFriends implements Friends {

    /**
     * Friends folder.
     */
    private final transient File friends;

    /**
     * Ctor.
     * @param file Directory
     * @param urn URN
     * @param name Document name
     */
    public MkFriends(@NotNull final File file, @NotNull final String urn,
    @NotNull final String name) {
        this.friends = Paths.get(
            file.getAbsolutePath(),
            urn.replaceAll("[^a-z0-9]", "/"),
            "friends",
            name
        ).toFile();
        if (!this.friends.exists()) {
            assert this.friends.mkdirs();
        }
    }

    @Override
    public boolean leader() {
        return true;
    }

    @Override
    public Iterable<String> names() {
        final String[] list = this.friends.list();
        final Iterable<String> names;
        if (list == null) {
            names = Collections.emptyList();
        } else {
            names = Arrays.asList(list);
        }
        return names;
    }

    @Override
    public void add(final String name) throws IOException {
        assert Paths.get(this.friends.getAbsolutePath(), name).toFile()
            .createNewFile();
    }

    @Override
    public void eject(final String name) {
        assert Paths.get(this.friends.getAbsolutePath(), name).toFile()
            .delete();
    }

}
