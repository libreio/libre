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

import com.jcabi.log.Logger;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.takes.Response;
import org.takes.facets.fallback.Fallback;
import org.takes.facets.fallback.FbWrap;
import org.takes.facets.fallback.RqFallback;
import org.takes.rs.RsHTML;

/**
 * Fallback for error.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class FbError extends FbWrap {

    /**
     * Ctor.
     */
    public FbError() {
        super(
            new Fallback() {
                @Override
                public Iterator<Response> route(final RqFallback req) {
                    final String exc = ExceptionUtils.getStackTrace(
                        req.throwable()
                    );
                    Logger.info(this, "Exception thrown\n%s", exc);
                    return Collections.<Response>singleton(
                        new RsHTML(
                            String.format(
                                "oops, something went wrong!\n%s",
                                exc
                            )
                        )
                    ).iterator();
                }
            }
        );
    }

}
