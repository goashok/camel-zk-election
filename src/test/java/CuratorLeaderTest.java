import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by ashok on 3/30/17.
 */
public class CuratorLeaderTest {

    public static void main(String args[]) throws InterruptedException {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", 60000, 60000, retryPolicy);
        client.start();
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter()
        {
            public void takeLeadership(CuratorFramework client) throws Exception
            {
                System.out.println("I am leader now");
                try {
                    while (true)
                        Thread.sleep(10000);
                }catch(Exception e) {

                }
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
            }
        };

        LeaderSelector selector = new LeaderSelector(client, "/zookeeper/leader", listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();


        Thread t =new Thread() {
            public void run() {
                try {
                    while (true)
                    Thread.sleep(10000);
                }catch (Exception e) {

                }
            }
        };
        t.run();
    }
}
