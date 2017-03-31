import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ashok on 3/30/17.
 */
public class DefaultHandler implements ElectionHandler {

    private String name;
    private Logger LOGGER = LoggerFactory.getLogger(DefaultHandler.class);
    private final AtomicInteger leaderCount = new AtomicInteger();
    public DefaultHandler(String name) {
        this.name = name;
    }

    @Override
    public void handleElection(CuratorFramework client) throws Exception {
        LOGGER.info(name + "is now the leader");

         try {
             synchronized (this) {
                 this.wait();
             }
         }finally {
             LOGGER.warn(name + " - Relinquishing Master status");
         }
    }

    @Override
    public void shutdown() {
        //relinquish the master status
        this.notify();
    }
}
