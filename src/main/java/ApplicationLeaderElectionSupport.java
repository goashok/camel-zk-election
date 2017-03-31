import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by ashok on 3/31/17.
 * An interface to be implemented by Applications to do specific tasks
 * required by the application when it becomes a leader or loses its status.
 *
 */
public interface ApplicationLeaderElectionSupport {

    public void onElectionChange(CamelContext camelContext, ApplicationContext applicationContext, ElectionStatus status) throws Exception;

}
