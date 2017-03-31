import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created by ashok on 3/31/17.
 */
public class ExampleApplicationLeaderElectionSupport implements ApplicationLeaderElectionSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleApplicationLeaderElectionSupport.class);

    @Override
    public void onElectionChange(CamelContext camelContext, ApplicationContext applicationContext, ElectionStatus status) throws Exception {
        LOGGER.info("App is " + status.name() + " now") ;
    }


}
