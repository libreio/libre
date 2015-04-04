package com.nerodesk.om.mock;

import com.nerodesk.om.Doc;
import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link MkBatch}.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.4
 */
public final class MkBatchTest {

    /**
     * MkBatch can accept multiple documents.
     */
    @Test
    public void acceptsMultipleDocs() {
        final Doc[] docs = {
            Mockito.mock(Doc.class),
            Mockito.mock(Doc.class),
            Mockito.mock(Doc.class),
        };
        MatcherAssert.assertThat(
            new MkBatch(docs).list(),
            Matchers.equalTo(Arrays.asList(docs))
        );
    }

}
