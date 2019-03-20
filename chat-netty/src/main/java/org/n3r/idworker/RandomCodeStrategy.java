package org.n3r.idworker;

/**
 * @author Aiolos
 * @date 2019-03-19 22:51
 */
public interface RandomCodeStrategy {

    void init();

    int prefix();

    int next();

    void release();
}
