import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by ashok on 3/30/17.
 */
public class RouteElectionHandler implements ElectionHandler , CamelContextAware, ApplicationContextAware{

    private Logger LOGGER = LoggerFactory.getLogger(RouteElectionHandler.class);
    private String masterRoute;
    private CamelContext camelContext;
    private ApplicationContext applicationContext;
    private AppElectionSupport appSupport;

    public RouteElectionHandler(String masterRoute, AppElectionSupport appSupport) {
        this.masterRoute = masterRoute;
        this.appSupport = appSupport;
    }

    @Override
    public void handleElection(CuratorFramework client) throws Exception {

        synchronized (this) {
            try {
                appSupport.onElectionChange(camelContext, applicationContext, ElectionStatus.MASTER);
                camelContext.startRoute(masterRoute);
                this.wait();
            }catch(Exception e) {
                LOGGER.error("Error handlig election", e);
                throw e; //this could be InterruptedException which we dont want to swallow
            }finally {
                LOGGER.warn("Stopping route " + masterRoute);
                try {
                }catch (Exception e) {
                    LOGGER.error("AppSupport did not handle new status change to SLAVE. Will shutdown route anyways",e);
                }finally {
                    camelContext.stopRoute(masterRoute);
                }

                LOGGER.warn("Relinquishing Master status");
            }
        }

    }

    @Override
    public void shutdown() throws Exception{
        this.notify();
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
