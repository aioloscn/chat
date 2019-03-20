package org.n3r.idworker;

/**
 * @author Aiolos
 * @date 2019-03-19 22:53
 */
public interface WorkerIdStrategy {

    void initialize();

    long availableWorkerId();

    void release();
}
