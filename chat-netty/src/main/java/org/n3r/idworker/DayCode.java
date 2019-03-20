package org.n3r.idworker;

import org.n3r.idworker.strategy.DayPrefixRandomCodeStrategy;

/**
 * @author Aiolos
 * @date 2019-03-19 22:49
 */
public class DayCode {

    static RandomCodeStrategy strategy;

    static {
        DayPrefixRandomCodeStrategy dayPrefixCodeStrategy = new DayPrefixRandomCodeStrategy("yyMM");
        dayPrefixCodeStrategy.setMinRandomSize(7);
        dayPrefixCodeStrategy.setMaxRandomSize(7);
        strategy = dayPrefixCodeStrategy;
        strategy.init();
    }

    public static synchronized String next() {
        return String.format("%d-%04d-%07d", Id.getWorkerId(), strategy.prefix(), strategy.next());
    }
}
