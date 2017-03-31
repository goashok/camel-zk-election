import org.apache.curator.framework.CuratorFramework;

/**
 * Created by ashok on 3/30/17.
 */
public interface ElectionHandler {

    void handleElection(CuratorFramework client) throws Exception ;

    void shutdown() throws Exception;
}
