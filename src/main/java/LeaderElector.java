/**
 * Created by ashok on 3/30/17.
 */



import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Leader elector based on Curator recipes .
 * Note that {@link LeaderSelectorListenerAdapter} which
 * has the recommended handling for connection state issues
 */
public class LeaderElector extends LeaderSelectorListenerAdapter implements Closeable
{
    private final LeaderSelector leaderSelector;
    private ElectionHandler handler;
    private boolean isMaster = false;
    private Listenable<ConnectionStateListener> connListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderElector.class);

    public LeaderElector(CuratorFramework client, String path, ElectionHandler handler)
    {
        // create a leader selector using the given path for management
        // all participants in a given leader selection must use the same path
        // LeaderElector here is also a LeaderSelectorListener but this isn't required
        leaderSelector = new LeaderSelector(client, path, this);

        // for most cases you will want your instance to requeue when it relinquishes leadership
        leaderSelector.autoRequeue();

        this.handler = handler;

    }

    public void start() throws IOException
    {
        // the selection for this instance doesn't start until the leader selector is started
        // leader selection is done in the background so this call to leaderSelector.start() returns immediately
        leaderSelector.start();
    }

    @Override
    public void close() throws IOException
    {
        leaderSelector.close();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception
    {
        try {
            isMaster = true;
            // we are now the leader. This method should not return until we want to relinquish leadership
            handler.handleElection(client);
        }finally {
            isMaster = false;
        }
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState)
    {
        LOGGER.info("Status Changed. New Status: " + newState);
        super.stateChanged(client, newState);
    }
    public boolean isMaster() {
        return isMaster;
    }
}