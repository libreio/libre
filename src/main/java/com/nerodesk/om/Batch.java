package com.nerodesk.om;

import java.util.List;

/**
 * Document batch.
 *
 * @author Felipe Pina (felipe.pina@protonmail.com)
 * @version $Id$
 * @since 0.4
 */
public interface Batch {

    /**
     * List of documents.
     * @return List of documents.
     */
    List<Doc> list();

}
