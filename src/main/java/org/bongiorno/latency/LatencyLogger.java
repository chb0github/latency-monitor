package org.bongiorno.latency;


import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Stream;


public class LatencyLogger implements UpdateListener {

    private Logger log;

    public LatencyLogger(String loggerName) {
        log = LoggerFactory.getLogger(loggerName);
    }

    public LatencyLogger(Logger log) {
        this.log = log;
    }

    public void update(EventBean[] old, EventBean[] newStuff) {
        Arrays.stream(old).forEach(e -> log.info("%s",e.getUnderlying().toString()));
    }
}
