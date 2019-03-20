package org.n3r.idworker;

/**
 * @author Aiolos
 * @date 2019-03-19 22:51
 */
public class InvalidSystemClock extends RuntimeException {

    public InvalidSystemClock(String message) {
        super(message);
    }
}
