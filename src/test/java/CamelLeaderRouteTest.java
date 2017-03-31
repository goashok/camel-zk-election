import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ashok on 3/30/17.
 */
public class CamelLeaderRouteTest {

    public static void main(String args[]) throws Exception{
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                "test-leader-context.xml");
        appContext.start();
        Object lock = new Object();
        synchronized (lock) {
            lock.wait();
        }

    }
}
