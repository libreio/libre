package com.nerodesk.om.mock;

import com.nerodesk.om.Batch;
import com.nerodesk.om.Doc;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Mock Document batch.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.4
 */
public class MkBatch implements Batch {

    /**
     * Documents composing this batch.
     */
    private final transient List<Doc> docs;

    /**
     * Ctor.
     * @param list Documents composing this batch.
     */
    public MkBatch(final Doc... list) {
        this.docs = Arrays.asList(list);
    }

    @Override
    public final List<Doc> list() {
        return Collections.unmodifiableList(this.docs);
    }

}
